package com.ncardozo.msgeolocalizador.client

import com.ncardozo.msgeolocalizador.dto.external.IpLocationDto
import com.ncardozo.msgeolocalizador.exception.ExternalServiceException
import com.ncardozo.msgeolocalizador.utils.ErrorMessages
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.springframework.http.HttpStatus
import retrofit2.Call
import retrofit2.Response
import spock.lang.Specification

class RestClientGeolocationTest extends Specification{

    private RestClientGeolocation client = new RestClientGeolocation()

    private RestIntefaceGeolocation restIntefaceGeolocation = Mock(RestIntefaceGeolocation)

    private Call mockCall = Mock(Call)

    def setup(){
        client.restIntefaceGeolocation = restIntefaceGeolocation
        client.ACCESS_KEY = "asd1234"
    }

    def "Obtengo los datos de geolocalización de la ip"(){
        given: "Una dirección de ip y los datos que retornará ipapi"
        String ip = "192.168.0.1"

        IpLocationDto ipLocationDto = new IpLocationDto(countryCode: "AR", countryName: "ARGENTINA",latitude: 40.000, longitude: -30.450)

        when: "Voy a consultar ipapi"
        IpLocationDto response = client.getIpLocation(ip)

        then: "Se llama a la api externa"
        1 * restIntefaceGeolocation.getIpLocation(ip,_,_) >> mockCall
        1 * mockCall.execute() >> Response.success(ipLocationDto)

        and: "Se espera el resultado"
        response == ipLocationDto
    }

    def "Si ipapi responde con status code != 200, fallamos"(){
        given: "una ip a consultar"
        String ip = "192.168.0.1"

        when: "hago la consulta"
        client.getIpLocation(ip)

        then: "ipapi responde con status code != 200"
        1 * restIntefaceGeolocation.getIpLocation(ip,_,_) >> mockCall
        1 * mockCall.execute() >> Response.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ResponseBody.create(MediaType.get("application/json"), ""))

        and: "Se lanza excepción"
        ExternalServiceException ex = thrown()
        ex.getMessage() ==  ErrorMessages.IPAPI_ERROR.replace("{}", HttpStatus.INTERNAL_SERVER_ERROR.value().toString())
    }

    def "Si no nos podemos comunicar con ipapi, fallamos"(){
        given: "una ip a consultar"
        String ip = "192.168.0.1"

        when: "hago la consulta"
        client.getIpLocation(ip)

        then: "No podemos establecer conexión con ipapi"
        1 * restIntefaceGeolocation.getIpLocation(ip,_,_) >> mockCall
        1 * mockCall.execute() >> { throw new IOException("Error") }

        and: "Se lanza excepción"
        ExternalServiceException ex = thrown()
        ex.getMessage() ==  ErrorMessages.IPAPI_ERROR_COMUNICATION
    }
}

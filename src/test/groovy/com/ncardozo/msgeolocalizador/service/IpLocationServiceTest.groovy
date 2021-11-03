package com.ncardozo.msgeolocalizador.service

import com.ncardozo.msgeolocalizador.client.RestClientGeolocation
import com.ncardozo.msgeolocalizador.dto.ResponseDto
import com.ncardozo.msgeolocalizador.dto.external.IpLocationDto
import com.ncardozo.msgeolocalizador.dto.external.LanguageDto
import com.ncardozo.msgeolocalizador.dto.external.LocationDto
import com.ncardozo.msgeolocalizador.exception.ExternalServiceException
import spock.lang.Specification

class IpLocationServiceTest extends Specification{

    IpLocationService service = new IpLocationService()

    RestClientGeolocation restClientGeolocation = Mock(RestClientGeolocation)

    def setup(){
        service.restClientGeolocation = restClientGeolocation
        service.CABA_LATITUDE = -34.6157437
        service.CABA_LONGITUDE = -58.5733832
    }

    def "Se consulta la información de una ip"(){
        given: "Una ip a consultar y las respuestas provenientes de los demás servicios"
        String ip = "192.168.0.1"
        List<LanguageDto> languages = [new LanguageDto(code: "es", name: "español")]
        IpLocationDto ipLocationDto = new IpLocationDto(countryCode: "AR", countryName: "ARGENTINA",
                latitude: 30.1230, longitude: -25.1240,
                location: new LocationDto(languages: languages)
        )

        when: "Busco los datos correspondientes a la IP"
        ResponseDto responseDto = service.getIpLocation(ip)

        then: "Se llama a los servicios correspondientes"
        1 * restClientGeolocation.getIpLocation(ip) >> ipLocationDto

        and: "El resultado tiene todos los datos deseados"
        responseDto.ip == ip
        responseDto.countryCode == ipLocationDto.countryCode
        responseDto.countryName == ipLocationDto.countryName
        responseDto.distance != null
        responseDto.languages == languages
    }

    def "Si se recibe una excepción al consulta la geolocalización de la IP, la propagamos"(){
        given: "Una ip a consultar y las respuestas provenientes de los demás servicios"
        String ip = "192.168.0.1"

        when: "Busco los datos correspondientes a la IP"
        service.getIpLocation(ip)

        then: "el servicio para obtener la geolocalización falla"
        1 * restClientGeolocation.getIpLocation(ip) >> { throw new ExternalServiceException("error") }

        and: "Propagamos la excepción"
        thrown(ExternalServiceException)
    }
}

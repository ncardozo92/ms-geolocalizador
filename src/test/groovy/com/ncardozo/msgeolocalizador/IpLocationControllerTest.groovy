package com.ncardozo.msgeolocalizador

import com.ncardozo.msgeolocalizador.controller.IpLocationController
import com.ncardozo.msgeolocalizador.dto.ErrorResponseDto
import com.ncardozo.msgeolocalizador.dto.ResponseDto
import com.ncardozo.msgeolocalizador.exception.ExternalServiceException
import com.ncardozo.msgeolocalizador.service.IpLocationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class IpLocationControllerTest extends Specification {

    IpLocationController controller = new IpLocationController()

    IpLocationService ipLocationService = Mock(IpLocationService)

    def setup(){
        controller.ipLocationService = ipLocationService
    }

    def "Se realiza una solicitud y se obtiene una respuesta"(){
        when: "Realizo una consulta por una IP"
        ResponseEntity<ResponseDto> response = controller.findIpData("192.168.0.1")

        then: "Se llama al servicio"
        1 * ipLocationService.getIpLocation(_) >> new ResponseDto()

        and: "Se recibe la respuesta con un status 200"
        response.statusCode == HttpStatus.OK
    }

    def "Las excepciones producidas se elevan para ser manejadas"(){
        when: "Realizo una consulta por una IP"
        ResponseEntity<ResponseDto> response = controller.findIpData("192.168.0.1")

        then: "Se llama al servicio y falla"
        1 * ipLocationService.getIpLocation(_) >> { throw new ExternalServiceException("Error") }

        and: "La excepción recibida se eleva"
        thrown(ExternalServiceException)
    }

    def "Las excepciones se capturan y se retorna la respuesta correspondiente"(){
        given: "una excepción"
        ExternalServiceException exception = new ExternalServiceException("Error")

        when: "Manejamos la excepción"
        ResponseEntity<ErrorResponseDto> response = controller.handleErrors(exception)

        then: "La respuesta es la esperada"
        response.statusCode == HttpStatus.BAD_GATEWAY
        response.body.message == exception.message
    }
}

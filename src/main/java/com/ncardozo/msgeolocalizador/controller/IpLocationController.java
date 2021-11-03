package com.ncardozo.msgeolocalizador.controller;

import com.ncardozo.msgeolocalizador.dto.ErrorResponseDto;
import com.ncardozo.msgeolocalizador.dto.ResponseDto;
import com.ncardozo.msgeolocalizador.exception.ExternalServiceException;
import com.ncardozo.msgeolocalizador.service.IpLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpLocationController {

    @Autowired
    private IpLocationService ipLocationService;

    @GetMapping("/api/{ip}")
    public ResponseEntity<ResponseDto> findIpData(@PathVariable("ip") String ip) {
        return ResponseEntity.ok(ipLocationService.getIpLocation(ip));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponseDto> handleErrors(ExternalServiceException ex){
        return  new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.BAD_GATEWAY);
    }
}

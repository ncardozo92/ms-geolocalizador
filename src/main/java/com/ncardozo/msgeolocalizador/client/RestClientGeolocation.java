package com.ncardozo.msgeolocalizador.client;

import com.ncardozo.msgeolocalizador.dto.external.IpLocationDto;
import com.ncardozo.msgeolocalizador.exception.ExternalServiceException;
import com.ncardozo.msgeolocalizador.utils.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

@Component
@Slf4j
public class RestClientGeolocation {

    @Autowired
    private RestIntefaceGeolocation restIntefaceGeolocation;

    private String ACCESS_KEY;

    private final String LANGUAGE = "es";

    public RestClientGeolocation(){
        this.ACCESS_KEY  = System.getenv("IPAPI_ACCESS_KEY");
    }

    public IpLocationDto getIpLocation(String ip) {
        try {
            Response<IpLocationDto> response = restIntefaceGeolocation.getIpLocation(ip, LANGUAGE, ACCESS_KEY).execute();

            if (!response.isSuccessful()) {
                String errorMessage = ErrorMessages.IPAPI_ERROR.replace("{}", Integer.toString(response.code()));
                log.error(errorMessage);
                throw new ExternalServiceException(errorMessage);
            }

            return response.body();
        } catch(IOException e){
            log.error(ErrorMessages.IPAPI_ERROR_COMUNICATION);
            throw new ExternalServiceException(ErrorMessages.IPAPI_ERROR_COMUNICATION);
        }
    }
}

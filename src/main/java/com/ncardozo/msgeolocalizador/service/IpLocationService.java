package com.ncardozo.msgeolocalizador.service;

import com.ncardozo.msgeolocalizador.client.RestClientGeolocation;
import com.ncardozo.msgeolocalizador.dto.ResponseDto;
import com.ncardozo.msgeolocalizador.dto.TimeZoneDto;
import com.ncardozo.msgeolocalizador.dto.external.IpLocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class IpLocationService {

    private static final String ARGENTINA_CODE = "AR";

    @Autowired
    private RestClientGeolocation restClientGeolocation;

   @Value("${coordinates.latitude}")
   private Double CABA_LATITUDE;

    @Value("${coordinates.longitude}")
    private Double CABA_LONGITUDE;

    public ResponseDto getIpLocation(String ip){
        IpLocationDto ipLocationDto = restClientGeolocation.getIpLocation(ip);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setIp(ip);
        responseDto.setCountryCode(ipLocationDto.getCountryCode());
        responseDto.setCountryName(ipLocationDto.getCountryName());
        responseDto.setDistance(calculateDistance(ipLocationDto.getLatitude(), ipLocationDto.getLongitude()));
        responseDto.setLanguages(ipLocationDto.getLanguages());
        responseDto.setDate(LocalDateTime.now().toString());

        setCurrency(responseDto);
        setTimeZones(responseDto);

        return responseDto;
    }

    /**
     * debido a que la Rest Countries restringe el acceso a los usos horarios de un país, se mockean los mismos
     */
    private void setTimeZones(ResponseDto dto) {
        dto.setTimeZones(ARGENTINA_CODE.equals(dto.getCountryCode()) ?
                List.of(new TimeZoneDto("UTC" + OffsetDateTime.now().getOffset().toString())) :
                List.of(new TimeZoneDto("UTC-04:00"), new TimeZoneDto("UTC-05:00")));
    }

    private double calculateDistance(Double destinationLatitude, Double destinationLongitude) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(destinationLatitude - CABA_LATITUDE);
        double lonDistance = Math.toRadians(destinationLongitude - CABA_LONGITUDE);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(CABA_LATITUDE)) * Math.cos(Math.toRadians(destinationLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        distance = Math.pow(distance, 2);

        return Math.round(Math.sqrt(distance));
    }

    /**
     * debido a que la Rest Countries restringe el acceso a las monedas de un país, se mockean las mismas
     */
    private  void setCurrency(ResponseDto dto){
        dto.setCurrency(ARGENTINA_CODE.equals(dto.getCountryCode()) ? "ARS" : "USD");
    }
}

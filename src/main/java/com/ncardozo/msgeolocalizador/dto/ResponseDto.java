package com.ncardozo.msgeolocalizador.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncardozo.msgeolocalizador.dto.external.LanguageDto;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseDto {
    private String ip;
    private String countryCode;
    private String countryName;
    private Double distance;
    private List<LanguageDto> languages;
    private String date;
    private String currency;
    private List<TimeZoneDto> timeZones;
}

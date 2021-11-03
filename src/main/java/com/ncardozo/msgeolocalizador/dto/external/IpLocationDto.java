package com.ncardozo.msgeolocalizador.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpLocationDto {
    private String countryCode;
    private String countryName;
    private Double latitude;
    private Double longitude;
    private LocationDto location;

    public List<LanguageDto> getLanguages() {
        return this.location.getLanguages();
    }
}

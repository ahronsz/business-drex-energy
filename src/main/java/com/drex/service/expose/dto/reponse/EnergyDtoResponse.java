package com.drex.service.expose.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EnergyDtoResponse {
    @JsonProperty("device-code")
    private String deviceCode;
    @JsonProperty("energy-accumulated")
    private Double energyAccumulated;
    private LocalDateTime timestamp;
}

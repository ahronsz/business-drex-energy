package com.drex.service.expose.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EnergyDtoResponse {
    private String deviceCode;
    private Double energyMwh;
    private Double energyAccumulatedMwh;
    private LocalDateTime utcDateTime;
}

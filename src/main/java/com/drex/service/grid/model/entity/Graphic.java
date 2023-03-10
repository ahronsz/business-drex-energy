package com.drex.service.grid.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class Graphic {

    private Double energyAccumulated;
    private Double energy;
    private LocalDateTime utcDateTime;
}

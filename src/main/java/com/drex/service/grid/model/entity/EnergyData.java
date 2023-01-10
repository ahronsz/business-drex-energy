package com.drex.service.grid.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Table(name = "Energy_Data")
@Getter
@Setter
@Builder
public class EnergyData {
    @Id
    @Column
    private Long id;

    @Column
    private String deviceCode;

    @Column
    private Double energyAccumulated;

    @Column
    private LocalDateTime timestamp;
}

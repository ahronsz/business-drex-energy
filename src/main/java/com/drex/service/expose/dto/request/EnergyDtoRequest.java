package com.drex.service.expose.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EnergyDtoRequest {

    @NotNull
    @CsvBindByName(column = "deviceCode")
    private String deviceCode;

    @NotNull
    @CsvBindByName(column = "energy")
    private Double energy;

    @NotNull
    @CsvBindByName(column = "energyAccumulated")
    private Double energyAccumulated;

    @NotNull
    @CsvBindByName(column = "posixTimestamp")
    private Long posixTimestamp;
}

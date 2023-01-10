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
    @JsonProperty("device-code")
    @CsvBindByName(column = "device-code")
    private String deviceCode;

    @NotNull
    @JsonProperty("energy-accumulated")
    @CsvBindByName(column = "energy-accumulated")
    private Double energyAccumulated;

    @NotNull
    @JsonProperty("timestamp")
    @CsvBindByName(column = "timestamp")
    private Long posixTimestamp;
}

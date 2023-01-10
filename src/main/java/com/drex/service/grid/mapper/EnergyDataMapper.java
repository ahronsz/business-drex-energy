package com.drex.service.grid.mapper;

import com.drex.service.expose.dto.reponse.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.grid.model.entity.EnergyData;
import com.drex.service.grid.util.enums.CodeEnum;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EnergyDataMapper {

    @Mapping(source = "posixTimestamp", target = "timestamp", qualifiedByName = "timestamp")
    EnergyData toEntity(EnergyDtoRequest request);

    List<EnergyData> toEntityList(List<EnergyDtoRequest> requests);

    @Mapping(source = "energyAccumulated", target = "energyAccumulated", qualifiedByName = "energy")
    EnergyDtoResponse toResponse(EnergyData energyData);

    List<EnergyDtoResponse> toResponseList(List<EnergyData> energyDataList);

    @Named("timestamp")
    default LocalDateTime posixtoLocalDateTime(Long posix_timestamp) {
        return Instant.ofEpochMilli(posix_timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    @Named("energy")
    default String formatToDecimal(Double energy) {
        return String.format("%.2f", energy);
    }

    @BeforeMapping
    default void toKwh(EnergyData energyData) {
        energyData.setEnergyAccumulated(energyData.getEnergyAccumulated() / 1000);
    }
}

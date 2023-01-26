package com.drex.service.grid.mapper;

import com.drex.service.expose.dto.response.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.grid.model.entity.EnergyData;
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

    @Mapping(source = "energy", target = "energyMwh", qualifiedByName = "mwh")
    @Mapping(source = "energyAccumulated", target = "energyAccumulatedMwh", qualifiedByName = "mwh")
    @Mapping(source = "timestamp", target = "utcDateTime")
    EnergyDtoResponse toResponse(EnergyData energyData);

    List<EnergyDtoResponse> toResponseList(List<EnergyData> energyDataList);

    @Named("timestamp")
    default LocalDateTime posixtoLocalDateTime(Long posix_timestamp) {
        return Instant.ofEpochMilli(posix_timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    @Named("energy")
    default String formatDecimal(Double energy) {
        return String.format("%.4f", energy);
    }

    @Named("mwh")
    default Double toMwh(Double kwh) {
        return kwh != null ? kwh / 1000 : 0.00;
    }
}

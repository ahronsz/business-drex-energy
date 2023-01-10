package com.drex.service.grid.mapper;

import com.drex.service.expose.dto.reponse.EnergyDtoResponse;
import com.drex.service.expose.dto.request.EnergyDtoRequest;
import com.drex.service.grid.model.entity.EnergyData;
import com.drex.service.grid.util.enums.CodeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = {CodeEnum.class})
public interface EnergyMapper {

    @Mapping(source = "posix_timestamp", target = "timestamp", qualifiedByName = "timestamp")
    EnergyData toEntity(EnergyDtoRequest request);

    List<EnergyData> toEntityList(List<EnergyDtoRequest> requests);

    @Mapping(target = "energy_accumulated", expression = "java(grid.getEnergy_accumulated() / 1000)")
    EnergyDtoResponse toResponse(EnergyData grid);

    List<EnergyDtoResponse> toResponseList(List<EnergyData> energyDataList);

    @Named("timestamp")
    default LocalDateTime posixtoLocalDateTime(Long posix_timestamp) {
        return Instant.ofEpochMilli(posix_timestamp).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}

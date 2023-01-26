package com.drex.service.grid.mapper;

import com.drex.service.expose.dto.response.GraphicDtoResponse;
import com.drex.service.grid.model.entity.Graphic;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@Mapper(componentModel = "spring")
public interface GraphicMapper {

    @Mapping(source = "energy", target = "y.energyMwh", qualifiedByName = "mwh")
    @Mapping(source = "energyAccumulated", target = "y.energyAccumulatedMwh", qualifiedByName = "mwh")
    @Mapping(source = "utcDateTime", target = "x.utcDateTime")
    @Mapping(source = "utcDateTime", target = "x.localDateTime", qualifiedByName = "localDateTime")
    GraphicDtoResponse toResponse(Graphic Graphic);

    @Named("localDateTime")
    default OffsetDateTime toLocalDateTime(LocalDateTime utcDateTime) {
        final ZoneId zone = ZoneId.of("America/Guayaquil");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(utcDateTime);
        return utcDateTime.atOffset(zoneOffSet);
    }

    @Named("mwh")
    default Double toMwh(Double kwh) {
        return kwh != null ? kwh / 1000 : 0.00;
    }
}

package com.drex.service.grid.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Graphic {

    private String y;

    private String x;
}

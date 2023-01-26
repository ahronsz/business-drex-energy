package com.drex.service.expose.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class XDtoResponse {
    private LocalDateTime utcDateTime;
    private OffsetDateTime localDateTime;
}

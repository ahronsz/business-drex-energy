package com.drex.service.grid.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CodeEnum {

    LIMA(0, "LIM"),
    GUAYAQUIL(1, "GYE");

    private final Integer id;
    private final String code;

    public static String findById(Integer id) {
        for (CodeEnum value: values()) {
            if (Objects.equals(value.getId(), id)) {
                return value.getCode();
            }
        }
        return null;
    }

    public static Integer findByCode(String code) {
        for (CodeEnum value: values()) {
            if (Objects.equals(value.getCode().toLowerCase(), code.toLowerCase())) {
                return value.getId();
            }
        }
        return null;
    }

}

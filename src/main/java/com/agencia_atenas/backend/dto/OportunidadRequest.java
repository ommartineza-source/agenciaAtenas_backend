package com.agencia_atenas.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;

public record OportunidadRequest(Map<String, Object> campos) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public OportunidadRequest {}
}

package com.agencia_atenas.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;

public record OrganizacionRequest(Map<String, Object> campos) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public OrganizacionRequest {}
}

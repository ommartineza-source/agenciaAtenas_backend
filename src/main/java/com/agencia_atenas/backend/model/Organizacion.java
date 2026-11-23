package com.agencia_atenas.backend.model;

public record Organizacion(
    Long id_organizacion,
    String nombre,
    String tipo,
    String rubro,
    String comuna,
    String region,
    String sitio_web,
    String publico_asociado,
    String observaciones
) {}

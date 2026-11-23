package com.agencia_atenas.backend.model;

public record Contacto(
    Long id_contacto,
    Long id_organizacion,
    String nombre,
    String cargo,
    String area,
    String correo,
    String telefono,
    String medio_preferente
) {}

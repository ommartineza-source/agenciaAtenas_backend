package com.agencia_atenas.backend.model;

public record Usuario(
    Long id_usuario,
    String usuario,
    String nombre,
    String rol,
    Long activo
) {}

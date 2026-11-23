package com.agencia_atenas.backend.model;

public record Seguimiento(
    Long id_seguimiento,
    Long id_oportunidad,
    Long id_autor,
    Long id_contacto,
    String tipo_registro,
    String fecha_hora,
    String accion,
    String resultado,
    String observaciones,
    String proxima_accion,
    String fecha_proxima,
    Long resuelta_por_id,
    String fecha_realizacion,
    Long asistentes,
    Long contactos_generados,
    Long interesados,
    Long inscripciones,
    String aprendizajes
) {}

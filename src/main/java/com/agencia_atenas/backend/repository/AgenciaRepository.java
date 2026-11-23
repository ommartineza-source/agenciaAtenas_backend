package com.agencia_atenas.backend.repository;

import java.util.*;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class AgenciaRepository {
    private final JdbcTemplate db;
    private final JsonMapper mapper;
    public AgenciaRepository(JdbcTemplate db, JsonMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }
    public <T> List<T> consultar(Class<T> tipo, String sql, Object... args) {
        return db.queryForList(sql,args).stream().map(row -> mapper.convertValue(normalizarFechas(row),tipo)).toList();
    }
    public List<Map<String,Object>> filas(String sql,Object... args) {
        return db.queryForList(sql,args).stream().map(this::normalizarFechas).toList();
    }
    public long insertar(String tabla, Map<String,Object> campos) {
        String clave = switch (tabla) {
            case "usuarios" -> "id_usuario";
            case "organizaciones" -> "id_organizacion";
            case "contactos" -> "id_contacto";
            case "oportunidades" -> "id_oportunidad";
            case "seguimientos" -> "id_seguimiento";
            default -> throw new IllegalArgumentException("Tabla desconocida: " + tabla);
        };
        return new SimpleJdbcInsert(db).withTableName(tabla)
            .usingColumns(campos.keySet().toArray(String[]::new))
            .usingGeneratedKeyColumns(clave)
            .executeAndReturnKey(campos).longValue();
    }
    private Map<String, Object> normalizarFechas(Map<String, Object> fila) {
        var resultado = new LinkedHashMap<>(fila);
        var formato = java.time.format.DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        resultado.replaceAll((columna, valor) ->
            valor instanceof java.time.LocalDateTime fecha ? fecha.format(formato) :
            valor instanceof java.sql.Timestamp fecha ? fecha.toLocalDateTime().format(formato) : valor);
        return resultado;
    }
    public void ejecutar(String sql,Object... args) { db.update(sql,args); }
}
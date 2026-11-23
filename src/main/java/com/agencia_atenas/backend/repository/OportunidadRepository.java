package com.agencia_atenas.backend.repository;

import java.util.*;
import java.time.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.repository.*;
import com.agencia_atenas.backend.exception.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OportunidadRepository {
    private final AgenciaRepository jdbc;

    public OportunidadRepository(AgenciaRepository jdbc) {
        this.jdbc = jdbc;
    }

    public long insertar(Map<String, Object> datos) {
        return jdbc.insertar("oportunidades", datos);
    }

    public List<Oportunidad> listar(Usuario actor) {
        return jdbc.consultar(Oportunidad.class, "SELECT * FROM oportunidades WHERE "+ALCANCE, actor.rol(), actor.id_usuario());
    }

    public Optional<Oportunidad> obtener(long id, Usuario actor) {
        return jdbc.consultar(Oportunidad.class, "SELECT * FROM oportunidades WHERE id_oportunidad=? AND "+ALCANCE, id, actor.rol(), actor.id_usuario())
            .stream().findFirst();
    }

    public void editar(long id, Map<String, Object> datos) {
        var cambios = new ArrayList<String>();
        datos.keySet().forEach(campo -> cambios.add(campo + "=?"));
        cambios.add("fecha_ultima_actualizacion=CURRENT_TIMESTAMP");
        var parametros = new ArrayList<>(datos.values());
        parametros.add(id);
        jdbc.ejecutar("UPDATE oportunidades SET " + String.join(",", cambios) + " WHERE id_oportunidad=?", parametros.toArray());
    }
    static final String ALCANCE = "(?='SUPERVISOR' OR id_responsable=?)";
    public List<Oportunidad> sinSeguimiento(Usuario a) {
        return jdbc.consultar(Oportunidad.class,"SELECT * FROM oportunidades o WHERE "+ALCANCE+" AND NOT EXISTS (SELECT 1 FROM seguimientos s WHERE s.id_oportunidad=o.id_oportunidad)",a.rol(),a.id_usuario());
    }

    public void estado(long id, String destino) {
        jdbc.ejecutar("UPDATE oportunidades SET estado=?,fecha_ultima_actualizacion=CURRENT_TIMESTAMP WHERE id_oportunidad=?", destino, id);
    }

    public void asignar(long id, long responsable) {
        jdbc.ejecutar("UPDATE oportunidades SET id_responsable=?,fecha_ultima_actualizacion=CURRENT_TIMESTAMP WHERE id_oportunidad=?", responsable, id);
    }

}

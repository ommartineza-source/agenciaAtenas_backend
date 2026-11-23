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
public class OrganizacionRepository {
    private final AgenciaRepository jdbc;

    public OrganizacionRepository(AgenciaRepository jdbc) {
        this.jdbc = jdbc;
    }

    public long insertar(Map<String, Object> datos) {
        return jdbc.insertar("organizaciones", datos);
    }

    public List<Organizacion> listar() {
        return jdbc.consultar(Organizacion.class, "SELECT * FROM organizaciones");
    }

    public Optional<Organizacion> obtener(long id) {
        return jdbc.consultar(Organizacion.class, "SELECT * FROM organizaciones WHERE id_organizacion=?", id)
            .stream().findFirst();
    }

    public void editar(long id, Map<String, Object> datos) {
        var cambios = new ArrayList<String>();
        datos.keySet().forEach(campo -> cambios.add(campo + "=?"));

        var parametros = new ArrayList<>(datos.values());
        parametros.add(id);
        jdbc.ejecutar("UPDATE organizaciones SET " + String.join(",", cambios) + " WHERE id_organizacion=?", parametros.toArray());
    }

}

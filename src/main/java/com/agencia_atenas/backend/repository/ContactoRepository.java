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
public class ContactoRepository {
    private final AgenciaRepository jdbc;

    public ContactoRepository(AgenciaRepository jdbc) {
        this.jdbc = jdbc;
    }

    public long insertar(Map<String, Object> datos) {
        return jdbc.insertar("contactos", datos);
    }

    public List<Contacto> listar() {
        return jdbc.consultar(Contacto.class, "SELECT * FROM contactos");
    }

    public Optional<Contacto> obtener(long id) {
        return jdbc.consultar(Contacto.class, "SELECT * FROM contactos WHERE id_contacto=?", id)
            .stream().findFirst();
    }

    public void editar(long id, Map<String, Object> datos) {
        var cambios = new ArrayList<String>();
        datos.keySet().forEach(campo -> cambios.add(campo + "=?"));

        var parametros = new ArrayList<>(datos.values());
        parametros.add(id);
        jdbc.ejecutar("UPDATE contactos SET " + String.join(",", cambios) + " WHERE id_contacto=?", parametros.toArray());
    }

}

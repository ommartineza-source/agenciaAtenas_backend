package com.agencia_atenas.backend.service;

import java.util.*;
import java.time.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.repository.*;
import com.agencia_atenas.backend.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizacionService {
    private static final Set<String> CAMPOS = Set.of("nombre","tipo","rubro","comuna","region","sitio_web","publico_asociado","observaciones");
    private final OrganizacionRepository repo;
    private final SeguimientoRepository seguimientos;


    public OrganizacionService(OrganizacionRepository repo, SeguimientoRepository seguimientos) {
        this.repo = repo;
        this.seguimientos = seguimientos;

    }

    public List<Organizacion> listar(Usuario actor) {
        return repo.listar();
    }

    public Organizacion obtener(long id, Usuario actor) {
        return repo.obtener(id)
            .orElseThrow(() -> new ErrorApi(404, "Registro no disponible"));
    }

    public long crear(OrganizacionRequest solicitud, Usuario actor) {
        var datos = Formulario.validar(CAMPOS, solicitud.campos());

        if (datos.isEmpty()) throw new ErrorApi(422, "Faltan campos");
        return repo.insertar(datos);
    }

    public void editar(long id, OrganizacionRequest solicitud, Usuario actor) {
        obtener(id, actor);
        var datos = Formulario.validar(CAMPOS, solicitud.campos());
        if (datos.isEmpty()) {
            throw new ErrorApi(422, "Revisa los campos editables");
        }
        repo.editar(id, datos);
    }
    public List<Map<String, Object>> historial(long id, Usuario actor) {
        return seguimientos.historial(false, id, actor);
    }

}

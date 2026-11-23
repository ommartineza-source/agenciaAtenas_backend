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
public class ContactoService {
    private static final Set<String> CAMPOS = Set.of("id_organizacion","nombre","cargo","area","correo","telefono","medio_preferente");
    private final ContactoRepository repo;
    private final SeguimientoRepository seguimientos;


    public ContactoService(ContactoRepository repo, SeguimientoRepository seguimientos) {
        this.repo = repo;
        this.seguimientos = seguimientos;

    }

    public List<Contacto> listar(Usuario actor) {
        return repo.listar();
    }

    public Contacto obtener(long id, Usuario actor) {
        return repo.obtener(id)
            .orElseThrow(() -> new ErrorApi(404, "Registro no disponible"));
    }

    public long crear(ContactoRequest solicitud, Usuario actor) {
        var datos = Formulario.validar(CAMPOS, solicitud.campos());

        if (datos.isEmpty()) throw new ErrorApi(422, "Faltan campos");
        return repo.insertar(datos);
    }

    public void editar(long id, ContactoRequest solicitud, Usuario actor) {
        obtener(id, actor);
        var datos = Formulario.validar(CAMPOS, solicitud.campos());
        if (datos.isEmpty() || datos.containsKey("id_organizacion")) {
            throw new ErrorApi(422, "Revisa los campos editables");
        }
        repo.editar(id, datos);
    }
    public List<Map<String, Object>> historial(long id, Usuario actor) {
        return seguimientos.historial(true, id, actor);
    }

}

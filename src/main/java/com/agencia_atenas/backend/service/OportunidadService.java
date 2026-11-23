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
public class OportunidadService {
    private static final Set<String> CAMPOS = Set.of("id_organizacion","id_contacto_principal","nombre_oportunidad","tipo_oportunidad","descripcion","fuente_informacion","fecha_deteccion","prioridad","potencial_comercial","fecha_inicio","fecha_termino","horario","ubicacion_general","comuna","region","publico_esperado","publico_objetivo","cantidad_asistentes_estimada","condiciones_participacion","costos_referenciales","requisitos","espacios_disponibles","historial_observaciones");
    private final OportunidadRepository repo;
    private final SeguimientoRepository seguimientos;
    private final UsuarioRepository usuarios;

    public OportunidadService(OportunidadRepository repo, SeguimientoRepository seguimientos, UsuarioRepository usuarios) {
        this.repo = repo;
        this.seguimientos = seguimientos;
        this.usuarios = usuarios;
    }

    public List<Oportunidad> listar(Usuario actor) {
        return repo.listar(actor);
    }

    public Oportunidad obtener(long id, Usuario actor) {
        return repo.obtener(id, actor)
            .orElseThrow(() -> new ErrorApi(404, "Registro no disponible"));
    }

    public long crear(OportunidadRequest solicitud, Usuario actor) {
        var datos = Formulario.validar(CAMPOS, solicitud.campos());
        datos.put("id_responsable", actor.id_usuario());
        if (datos.isEmpty()) throw new ErrorApi(422, "Faltan campos");
        return repo.insertar(datos);
    }

    public void editar(long id, OportunidadRequest solicitud, Usuario actor) {
        obtener(id, actor);
        var datos = Formulario.validar(CAMPOS, solicitud.campos());
        if (datos.isEmpty()) {
            throw new ErrorApi(422, "Revisa los campos editables");
        }
        repo.editar(id, datos);
    }
    private static final Set<String> GESTION=Set.of("IDENTIFICADO","CONTACTO_PENDIENTE","CONTACTO_REALIZADO","PROPUESTA_ENVIADA","EN_ESPERA","REQUIERE_SEGUIMIENTO");
    private void auditar(long id,Usuario actor,String accion,String resultado) {
        seguimientos.insertar(Map.of("id_oportunidad",id,"id_autor",actor.id_usuario(),"tipo_registro","GESTION","accion",accion,"resultado",resultado));
    }
    public void estado(long id,String destino,Usuario actor) {
        String previo=obtener(id,actor).estado();
        boolean supervisor=actor.rol().equals("SUPERVISOR");
        boolean valido=destino!=null && !previo.equals(destino) && switch(previo) {
            case "CERRADO" -> false;
            case "DESCARTADO","ACTIVACION_REALIZADA" -> destino.equals("CERRADO");
            case "CONFIRMADO" -> destino.equals("ACTIVACION_REALIZADA") || supervisor && destino.equals("DESCARTADO");
            default -> GESTION.contains(destino) || supervisor && Set.of("CONFIRMADO","DESCARTADO").contains(destino);
        };
        if(!valido) throw new ErrorApi(403,"Cambio de estado no permitido");
        repo.estado(id,destino);
        auditar(id,actor,"Cambio de estado",previo+" → "+destino);
    }
    public void asignar(long id,long responsable,Usuario actor) {
        if(!actor.rol().equals("SUPERVISOR")) throw new ErrorApi(403,"Solo supervisor");
        var previa=obtener(id,actor);
        if(!usuarios.esEjecutivoActivo(responsable)) throw new ErrorApi(422,"Selecciona un ejecutivo activo");
        repo.asignar(id,responsable);
        auditar(id,actor,"Asignación",previa.id_responsable()+" → "+responsable);
    }
    public List<Oportunidad> sinSeguimiento(Usuario actor) { return repo.sinSeguimiento(actor); }

}

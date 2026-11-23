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
public class SeguimientoService {
    private static final Set<String> CAMPOS = Set.of("tipo_registro","accion","resultado","observaciones","proxima_accion","fecha_proxima","fecha_realizacion","asistentes","contactos_generados","interesados","inscripciones","aprendizajes");
    private final SeguimientoRepository repo;
    private final OportunidadService oportunidades;

    public SeguimientoService(SeguimientoRepository repo, OportunidadService oportunidades) {
        this.repo = repo;
        this.oportunidades = oportunidades;
    }

    public List<Seguimiento> seguimientos(long id, Usuario actor, boolean resultados) {
        oportunidades.obtener(id, actor);
        return repo.seguimientos(id, actor, resultados);
    }

    public List<Seguimiento> pendientes(Usuario actor) {
        return repo.pendientes(actor);
    }
    private static final Set<String> FINALES=Set.of("DESCARTADO","ACTIVACION_REALIZADA","CERRADO");
    public long registrar(long id,SeguimientoRequest solicitud,Usuario a) {
        var op=oportunidades.obtener(id,a);
        var entrada=new LinkedHashMap<>(solicitud.campos());
        Object pendiente=entrada.remove("pendiente_id");
        var datos=Formulario.validar(CAMPOS,entrada);
        datos.putIfAbsent("tipo_registro","GESTION");
        if(pendiente!=null) {
            var tarea=repo.buscarPendiente(pendiente,id);
            if(tarea.isEmpty() || !"GESTION".equals(datos.get("tipo_registro"))) throw new ErrorApi(409,"Tarea no disponible");
            datos.put("accion",tarea.get(0).get("proxima_accion"));
        }
        if(datos.get("proxima_accion")!=null && FINALES.contains(op.estado())) throw new ErrorApi(422,"Oportunidad finalizada");
        boolean activacion="ACTIVACION".equals(datos.get("tipo_registro"));
        if(activacion) {
            if(!Set.of("CONFIRMADO","ACTIVACION_REALIZADA","CERRADO").contains(op.estado())) throw new ErrorApi(422,"Se requiere confirmación");
            if(!(datos.get("fecha_realizacion") instanceof String fecha) || LocalDate.parse(fecha).isAfter(LocalDate.now(ZoneId.of("America/Santiago")))) throw new ErrorApi(422,"Revisa la fecha de realización");
            datos.put("accion","Resultado de activación");
        }
        datos.put("id_oportunidad",id); datos.put("id_autor",a.id_usuario()); datos.put("id_contacto",op.id_contacto_principal());
        long nuevo=repo.insertar(datos);
        if(pendiente!=null) repo.resolver(pendiente,nuevo);
        if(activacion && op.estado().equals("CONFIRMADO")) oportunidades.estado(id,"ACTIVACION_REALIZADA",a);
        return nuevo;
    }

}

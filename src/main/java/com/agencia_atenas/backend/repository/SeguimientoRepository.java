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
public class SeguimientoRepository {
    private final AgenciaRepository jdbc;

    public SeguimientoRepository(AgenciaRepository jdbc) {
        this.jdbc = jdbc;
    }

    public long insertar(Map<String, Object> datos) {
        return jdbc.insertar("seguimientos", datos);
    }
    public List<Seguimiento> seguimientos(long id,Usuario a,boolean resultados) {
        return jdbc.consultar(Seguimiento.class,"SELECT * FROM seguimientos WHERE id_oportunidad=? "+(resultados?"AND tipo_registro='ACTIVACION' ":"")+"ORDER BY fecha_hora,id_seguimiento",id);
    }
    public List<Map<String,Object>> historial(boolean contacto,long id,Usuario a) {
        return jdbc.filas("SELECT s.*,o.nombre_oportunidad FROM seguimientos s JOIN oportunidades o USING(id_oportunidad) WHERE "+(contacto?"s.id_contacto":"o.id_organizacion")+"=? AND "+OportunidadRepository.ALCANCE+" ORDER BY fecha_hora DESC,id_seguimiento DESC",id,a.rol(),a.id_usuario());
    }
    public List<Seguimiento> pendientes(Usuario a) {
        return jdbc.consultar(Seguimiento.class,"SELECT s.* FROM seguimientos s JOIN oportunidades USING(id_oportunidad) WHERE "+OportunidadRepository.ALCANCE+" AND proxima_accion IS NOT NULL AND resuelta_por_id IS NULL ORDER BY fecha_proxima,id_seguimiento",a.rol(),a.id_usuario());
    }

    public List<Map<String, Object>> buscarPendiente(Object pendiente, long id) {
        return jdbc.filas("SELECT proxima_accion FROM seguimientos WHERE id_seguimiento=? AND id_oportunidad=? AND proxima_accion IS NOT NULL AND resuelta_por_id IS NULL", pendiente, id);
    }

    public void resolver(Object pendiente, long nuevo) {
        jdbc.ejecutar("UPDATE seguimientos SET resuelta_por_id=? WHERE id_seguimiento=?", nuevo, pendiente);
    }

}

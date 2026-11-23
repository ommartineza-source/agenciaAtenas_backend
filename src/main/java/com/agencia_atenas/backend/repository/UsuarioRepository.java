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
public class UsuarioRepository {
    private final AgenciaRepository jdbc;

    public UsuarioRepository(AgenciaRepository jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> buscarActivo(String usuario) {
        return jdbc.filas("SELECT * FROM usuarios WHERE usuario=? AND activo=1", usuario);
    }

    public List<Usuario> listar() {
        return jdbc.consultar(Usuario.class, "SELECT id_usuario,usuario,nombre,rol,activo FROM usuarios");
    }

    public boolean esEjecutivoActivo(long id) {
        return !jdbc.filas("SELECT 1 FROM usuarios WHERE activo=1 AND rol='EJECUTIVO' AND id_usuario=?", id).isEmpty();
    }

    public long insertar(Map<String, Object> datos) {
        return jdbc.insertar("usuarios", datos);
    }
}

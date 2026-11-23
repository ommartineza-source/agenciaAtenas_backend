package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    private final UsuarioService usuarios;
    public UsuarioController(UsuarioService usuarios) {
        this.usuarios=usuarios;
    }

    @GetMapping("/usuarios")
    public List<Usuario> listar(@RequestHeader(value="Authorization",required=false) String auth) { return usuarios.listar(usuarios.autenticar(auth)); }
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Long> crear(@RequestBody NuevoUsuario datos, @RequestHeader(value="Authorization",required=false) String auth) {
        usuarios.supervisor(usuarios.autenticar(auth));
        return Map.of("id_usuario",usuarios.crear(datos));
    }
}

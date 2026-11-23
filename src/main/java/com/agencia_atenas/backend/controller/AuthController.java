package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UsuarioService usuarios;
    public AuthController(UsuarioService usuarios) {
        this.usuarios=usuarios;
    }

    @GetMapping("/perfil")
    public Usuario perfil(@RequestHeader(value="Authorization",required=false) String auth) { return usuarios.autenticar(auth); }
}

package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api")
public class SeguimientoController {
    private final UsuarioService usuarios;
    private final SeguimientoService gestion;
    public SeguimientoController(UsuarioService usuarios,SeguimientoService gestion) {
        this.usuarios=usuarios;
        this.gestion=gestion;
    }

    @GetMapping("/recordatorios")
    public List<Seguimiento> pendientes(@RequestHeader(value="Authorization",required=false) String auth) { return gestion.pendientes(usuarios.autenticar(auth)); }
    @GetMapping("/oportunidades/{id}/seguimientos")
    public List<Seguimiento> historial(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.seguimientos(id,usuarios.autenticar(auth),false); }
    @GetMapping("/oportunidades/{id}/resultados")
    public List<Seguimiento> resultados(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.seguimientos(id,usuarios.autenticar(auth),true); }
    @PostMapping("/oportunidades/{id}/seguimientos")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Long> registrar(@PathVariable long id,@RequestBody SeguimientoRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        return Map.of("id_seguimiento",gestion.registrar(id,datos,usuarios.autenticar(auth)));
    }
}

package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadController {
    private final UsuarioService usuarios;
    private final OportunidadService gestion;
    public OportunidadController(UsuarioService usuarios,OportunidadService gestion) {
        this.usuarios=usuarios;
        this.gestion=gestion;
    }

    @GetMapping
    public List<Oportunidad> listar(@RequestHeader(value="Authorization",required=false) String auth) { return gestion.listar(usuarios.autenticar(auth)); }
    @GetMapping("/{id}")
    public Oportunidad obtener(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.obtener(id,usuarios.autenticar(auth)); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Long> crear(@RequestBody OportunidadRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        return Map.of("id_oportunidad",gestion.crear(datos,usuarios.autenticar(auth)));
    }
    @PatchMapping("/{id}")
    public Map<String,Long> editar(@PathVariable long id,@RequestBody OportunidadRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        gestion.editar(id,datos,usuarios.autenticar(auth));
        return Map.of("id_oportunidad",id);
    }
    @GetMapping("/sin-seguimiento")
    public List<Oportunidad> sinSeguimiento(@RequestHeader(value="Authorization",required=false) String auth) { return gestion.sinSeguimiento(usuarios.autenticar(auth)); }
    @PatchMapping("/{id}/estado")
    public CambioEstado estado(@PathVariable long id,@RequestBody CambioEstado cambio, @RequestHeader(value="Authorization",required=false) String auth) {
        gestion.estado(id,cambio.estado(),usuarios.autenticar(auth));
        return cambio;
    }
    @PatchMapping("/{id}/responsable")
    public Asignacion responsable(@PathVariable long id,@RequestBody Asignacion cambio, @RequestHeader(value="Authorization",required=false) String auth) {
        gestion.asignar(id,cambio.id_responsable(),usuarios.autenticar(auth));
        return cambio;
    }
}

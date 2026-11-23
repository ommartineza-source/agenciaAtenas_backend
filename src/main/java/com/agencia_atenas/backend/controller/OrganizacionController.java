package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {
    private final UsuarioService usuarios;
    private final OrganizacionService gestion;
    public OrganizacionController(UsuarioService usuarios,OrganizacionService gestion) {
        this.usuarios=usuarios;
        this.gestion=gestion;
    }

    @GetMapping
    public List<Organizacion> listar(@RequestHeader(value="Authorization",required=false) String auth) { return gestion.listar(usuarios.autenticar(auth)); }
    @GetMapping("/{id}")
    public Organizacion obtener(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.obtener(id,usuarios.autenticar(auth)); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Long> crear(@RequestBody OrganizacionRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        return Map.of("id_organizacion",gestion.crear(datos,usuarios.autenticar(auth)));
    }
    @PatchMapping("/{id}")
    public Map<String,Long> editar(@PathVariable long id,@RequestBody OrganizacionRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        gestion.editar(id,datos,usuarios.autenticar(auth));
        return Map.of("id_organizacion",id);
    }
    @GetMapping("/{id}/historial")
    public List<Map<String,Object>> historial(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.historial(id,usuarios.autenticar(auth)); }
}

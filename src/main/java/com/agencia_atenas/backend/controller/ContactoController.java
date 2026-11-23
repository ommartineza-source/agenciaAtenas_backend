package com.agencia_atenas.backend.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.service.*;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {
    private final UsuarioService usuarios;
    private final ContactoService gestion;
    public ContactoController(UsuarioService usuarios,ContactoService gestion) {
        this.usuarios=usuarios;
        this.gestion=gestion;
    }

    @GetMapping
    public List<Contacto> listar(@RequestHeader(value="Authorization",required=false) String auth) { return gestion.listar(usuarios.autenticar(auth)); }
    @GetMapping("/{id}")
    public Contacto obtener(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.obtener(id,usuarios.autenticar(auth)); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Long> crear(@RequestBody ContactoRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        return Map.of("id_contacto",gestion.crear(datos,usuarios.autenticar(auth)));
    }
    @PatchMapping("/{id}")
    public Map<String,Long> editar(@PathVariable long id,@RequestBody ContactoRequest datos, @RequestHeader(value="Authorization",required=false) String auth) {
        gestion.editar(id,datos,usuarios.autenticar(auth));
        return Map.of("id_contacto",id);
    }
    @GetMapping("/{id}/historial")
    public List<Map<String,Object>> historial(@PathVariable long id, @RequestHeader(value="Authorization",required=false) String auth) { return gestion.historial(id,usuarios.autenticar(auth)); }
}

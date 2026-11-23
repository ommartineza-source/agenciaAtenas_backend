package com.agencia_atenas.backend.service;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import org.bouncycastle.crypto.generators.SCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agencia_atenas.backend.repository.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.exception.*;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    public UsuarioService(UsuarioRepository repo) { this.repo=repo; }
    private byte[] hash(String password,String sal) {
        return SCrypt.generate(password.getBytes(StandardCharsets.UTF_8),sal.getBytes(StandardCharsets.US_ASCII),16384,8,1,32);
    }
    public Usuario autenticar(String header) {
        if(header==null || !header.regionMatches(true,0,"Basic ",0,6)) throw new ErrorApi(401,"Faltan credenciales");
        String[] partes;
        try {
            partes=new String(Base64.getDecoder().decode(header.substring(6)),StandardCharsets.UTF_8).split(":",2);
        } catch(IllegalArgumentException ex) { throw new ErrorApi(401,"Credenciales inválidas"); }
        if(partes.length!=2) throw new ErrorApi(401,"Credenciales inválidas");
        var filas=repo.buscarActivo(partes[0]);
        if(filas.isEmpty()) throw new ErrorApi(401,"Credenciales inválidas");
        var fila=filas.get(0);
        var cred=fila.get("password_hash").toString().split(":");
        if(!MessageDigest.isEqual(hash(partes[1],cred[0]),HexFormat.of().parseHex(cred[1]))) throw new ErrorApi(401,"Credenciales inválidas");
        return new Usuario(((Number)fila.get("id_usuario")).longValue(),(String)fila.get("usuario"),(String)fila.get("nombre"),(String)fila.get("rol"),1L);
    }
    public void supervisor(Usuario actor) {
        if(!"SUPERVISOR".equals(actor.rol())) throw new ErrorApi(403,"Solo supervisor");
    }
    public List<Usuario> listar(Usuario actor) {
        supervisor(actor);
        return repo.listar();
    }
    @Transactional
    public long crear(NuevoUsuario datos) {
        if(datos.usuario()==null || datos.usuario().isBlank() || datos.nombre()==null || datos.nombre().isBlank() ||
           !("SUPERVISOR".equals(datos.rol()) || "EJECUTIVO".equals(datos.rol())) || datos.password()==null || datos.password().length()<8)
            throw new ErrorApi(422,"Revisa los datos del usuario");
        byte[] bytes=new byte[16];
        new SecureRandom().nextBytes(bytes);
        String sal=HexFormat.of().formatHex(bytes);
        return repo.insertar(Map.of("usuario",datos.usuario(),"nombre",datos.nombre(),"rol",datos.rol(),
            "password_hash",sal+":"+HexFormat.of().formatHex(hash(datos.password(),sal))));
    }
}

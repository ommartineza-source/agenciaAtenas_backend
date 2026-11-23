package com.agencia_atenas.backend.service;

import java.util.*;
import java.time.*;
import com.agencia_atenas.backend.model.*;
import com.agencia_atenas.backend.dto.*;
import com.agencia_atenas.backend.repository.*;
import com.agencia_atenas.backend.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class Formulario {
    static Map<String,Object> validar(Set<String> campos, Map<String,Object> cuerpo) {
        Map<String,Object> datos=new LinkedHashMap<>();
        cuerpo.forEach((k,v)->{
            if(!campos.contains(k)) throw new ErrorApi(422,"Campo no editable: "+k);
            if(v instanceof String texto) {
                v=texto.isBlank()?null:texto.trim();
                if(texto.length()>2000) throw new ErrorApi(422,"Texto demasiado largo: "+k);
                if(v!=null && k.startsWith("fecha_")) {
                    try { LocalDate.parse(v.toString()); } catch(DateTimeException ex) { throw new ErrorApi(422,"Fecha inválida: "+k); }
                }
            } else if(v!=null && !(v instanceof Integer || v instanceof Long)) throw new ErrorApi(422,"Valor inválido: "+k);
            datos.put(k,v);
        });
        return datos;
    }

}

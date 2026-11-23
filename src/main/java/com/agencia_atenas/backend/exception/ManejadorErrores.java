package com.agencia_atenas.backend.exception;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(ErrorApi.class)
    ResponseEntity<?> dominio(ErrorApi error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("error",error.getReason()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> restriccion() { return ResponseEntity.unprocessableEntity().body(Map.of("error","Revisa los campos y sus relaciones")); }
    @ExceptionHandler(UncategorizedSQLException.class)
    ResponseEntity<?> almacenamiento(UncategorizedSQLException error) {
        if (error.getSQLException().getErrorCode() == 3819) return restriccion();
        return ResponseEntity.internalServerError().body(Map.of("error","Error de almacenamiento"));
    }
}
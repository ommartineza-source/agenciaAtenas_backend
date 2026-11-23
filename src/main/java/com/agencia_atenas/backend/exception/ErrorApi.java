package com.agencia_atenas.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ErrorApi extends ResponseStatusException {
    public ErrorApi(int estado,String mensaje) { super(HttpStatus.valueOf(estado),mensaje); }
}
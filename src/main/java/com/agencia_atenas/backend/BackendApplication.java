package com.agencia_atenas.backend;

import com.agencia_atenas.backend.dto.NuevoUsuario;
import com.agencia_atenas.backend.service.UsuarioService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BackendApplication.class);
        if (args.length > 0 && "crear-supervisor".equals(args[0])) {
            if (args.length != 3) throw new IllegalArgumentException("Uso: crear-supervisor USUARIO NOMBRE");
            app.setWebApplicationType(WebApplicationType.NONE);
            try (var context = app.run(args)) {
                var cuenta = new NuevoUsuario(args[1], args[2], "SUPERVISOR", System.getenv("ATENAS_PASSWORD"));
                long id = context.getBean(UsuarioService.class).crear(cuenta);
                System.out.println("ID del supervisor: " + id);
            }
            return;
        }
        app.run(args);
    }
}
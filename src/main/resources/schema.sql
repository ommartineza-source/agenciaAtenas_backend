CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(191) COLLATE utf8mb4_0900_as_ci NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    password_hash TEXT NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK(rol IN ('SUPERVISOR','EJECUTIVO')),
    activo INTEGER NOT NULL DEFAULT 1 CHECK(activo IN (0,1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE IF NOT EXISTS organizaciones (
    id_organizacion BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre TEXT NOT NULL,
    tipo TEXT,
    rubro TEXT,
    comuna TEXT,
    region TEXT,
    sitio_web TEXT,
    publico_asociado TEXT,
    observaciones TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE IF NOT EXISTS contactos (
    id_contacto BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_organizacion BIGINT NOT NULL,
    nombre TEXT NOT NULL,
    cargo TEXT,
    area TEXT,
    correo TEXT,
    telefono TEXT,
    medio_preferente TEXT,
    UNIQUE(id_contacto,id_organizacion),
    FOREIGN KEY (id_organizacion) REFERENCES organizaciones(id_organizacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE IF NOT EXISTS oportunidades (
    id_oportunidad BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_responsable BIGINT NOT NULL,
    id_organizacion BIGINT,
    id_contacto_principal BIGINT,
    nombre_oportunidad TEXT NOT NULL,
    tipo_oportunidad TEXT NOT NULL,
    descripcion TEXT,
    fuente_informacion TEXT,
    fecha_deteccion VARCHAR(10) NOT NULL,
    prioridad VARCHAR(10) NOT NULL CHECK(prioridad IN ('Alta','Media','Baja')),
    potencial_comercial TEXT,
    estado VARCHAR(40) NOT NULL DEFAULT 'IDENTIFICADO',
    fecha_inicio VARCHAR(10) NOT NULL,
    fecha_termino VARCHAR(10),
    horario TEXT,
    ubicacion_general TEXT,
    comuna TEXT NOT NULL,
    region TEXT NOT NULL,
    publico_esperado TEXT NOT NULL,
    publico_objetivo TEXT,
    cantidad_asistentes_estimada INTEGER CHECK(cantidad_asistentes_estimada >= 0),
    condiciones_participacion TEXT,
    costos_referenciales TEXT,
    requisitos TEXT,
    espacios_disponibles TEXT,
    historial_observaciones TEXT,
    fecha_ultima_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK(id_contacto_principal IS NULL OR id_organizacion IS NOT NULL),
    FOREIGN KEY(id_contacto_principal,id_organizacion) REFERENCES contactos(id_contacto,id_organizacion),
    CHECK(fecha_termino IS NULL OR fecha_termino >= fecha_inicio),
    FOREIGN KEY (id_responsable) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_organizacion) REFERENCES organizaciones(id_organizacion),
    FOREIGN KEY (id_contacto_principal) REFERENCES contactos(id_contacto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE IF NOT EXISTS seguimientos (
    id_seguimiento BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_oportunidad BIGINT NOT NULL,
    id_autor BIGINT NOT NULL,
    id_contacto BIGINT,
    tipo_registro VARCHAR(20) NOT NULL CHECK(tipo_registro IN ('GESTION','ACTIVACION')),
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accion TEXT NOT NULL,
    resultado TEXT NOT NULL,
    observaciones TEXT,
    proxima_accion TEXT,
    fecha_proxima VARCHAR(10),
    resuelta_por_id BIGINT UNIQUE,
    fecha_realizacion VARCHAR(10),
    asistentes INTEGER CHECK(asistentes >= 0),
    contactos_generados INTEGER CHECK(contactos_generados >= 0),
    interesados INTEGER CHECK(interesados >= 0),
    inscripciones INTEGER CHECK(inscripciones >= 0),
    aprendizajes TEXT,
    CHECK((proxima_accion IS NULL) = (fecha_proxima IS NULL)),
    CHECK(tipo_registro = 'ACTIVACION' OR (fecha_realizacion IS NULL AND asistentes IS NULL AND contactos_generados IS NULL AND interesados IS NULL AND inscripciones IS NULL AND aprendizajes IS NULL)),
    FOREIGN KEY (id_oportunidad) REFERENCES oportunidades(id_oportunidad),
    FOREIGN KEY (id_autor) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_contacto) REFERENCES contactos(id_contacto),
    FOREIGN KEY (resuelta_por_id) REFERENCES seguimientos(id_seguimiento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
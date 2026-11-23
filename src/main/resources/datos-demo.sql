-- Registros ficticios para la demostración de Agencia Atenas.
SET NAMES utf8mb4;
START TRANSACTION;

INSERT INTO organizaciones (nombre,tipo,rubro,comuna,region)
SELECT 'Centro Cultural Horizonte', 'Centro cultural', 'Cultura', 'Puente Alto', 'Metropolitana'
WHERE NOT EXISTS (SELECT 1 FROM organizaciones WHERE nombre='Centro Cultural Horizonte');
SET @organizacion = (SELECT MIN(id_organizacion) FROM organizaciones WHERE nombre='Centro Cultural Horizonte');

INSERT INTO contactos (id_organizacion,nombre,cargo,correo,medio_preferente)
SELECT @organizacion, 'Camila Torres', 'Coordinadora de eventos', 'camila@example.com', 'Correo'
WHERE NOT EXISTS (SELECT 1 FROM contactos WHERE id_organizacion=@organizacion AND correo='camila@example.com');
SET @contacto = (SELECT MIN(id_contacto) FROM contactos WHERE id_organizacion=@organizacion AND correo='camila@example.com');
SET @responsable = (SELECT id_usuario FROM usuarios WHERE activo=1 AND usuario IN ('ejecutivo1', 'admin') ORDER BY (usuario='ejecutivo1') DESC LIMIT 1);

INSERT INTO oportunidades (id_responsable,id_organizacion,id_contacto_principal,nombre_oportunidad,tipo_oportunidad,descripcion,fuente_informacion,fecha_deteccion,prioridad,estado,fecha_inicio,ubicacion_general,comuna,region,publico_esperado,cantidad_asistentes_estimada)
SELECT @responsable, @organizacion, @contacto, 'Feria Cultural de Verano', 'Feria', 'Stand informativo durante una feria familiar.', 'Demostración', CURRENT_DATE, 'Alta', 'PROPUESTA_ENVIADA', CURRENT_DATE + INTERVAL 30 DAY, 'Centro Cultural Horizonte', 'Puente Alto', 'Metropolitana', 'Comunidad local', 350
WHERE NOT EXISTS (SELECT 1 FROM oportunidades WHERE id_organizacion=@organizacion AND nombre_oportunidad='Feria Cultural de Verano');

INSERT INTO organizaciones (nombre,tipo,rubro,comuna,region)
SELECT 'Corporación Barrio Vivo', 'Corporación', 'Desarrollo comunitario', 'La Florida', 'Metropolitana'
WHERE NOT EXISTS (SELECT 1 FROM organizaciones WHERE nombre='Corporación Barrio Vivo');
SET @organizacion = (SELECT MIN(id_organizacion) FROM organizaciones WHERE nombre='Corporación Barrio Vivo');

INSERT INTO contactos (id_organizacion,nombre,cargo,correo,medio_preferente)
SELECT @organizacion, 'Diego Rojas', 'Encargado de vinculación', 'diego@example.com', 'Correo'
WHERE NOT EXISTS (SELECT 1 FROM contactos WHERE id_organizacion=@organizacion AND correo='diego@example.com');
SET @contacto = (SELECT MIN(id_contacto) FROM contactos WHERE id_organizacion=@organizacion AND correo='diego@example.com');
SET @responsable = (SELECT id_usuario FROM usuarios WHERE activo=1 AND usuario IN ('ejecutivo2', 'admin') ORDER BY (usuario='ejecutivo2') DESC LIMIT 1);

INSERT INTO oportunidades (id_responsable,id_organizacion,id_contacto_principal,nombre_oportunidad,tipo_oportunidad,descripcion,fuente_informacion,fecha_deteccion,prioridad,estado,fecha_inicio,ubicacion_general,comuna,region,publico_esperado,cantidad_asistentes_estimada)
SELECT @responsable, @organizacion, @contacto, 'Encuentro de Emprendedores', 'Encuentro', 'Presentación de servicios para negocios locales.', 'Demostración', CURRENT_DATE, 'Media', 'CONTACTO_PENDIENTE', CURRENT_DATE + INTERVAL 16 DAY, 'Corporación Barrio Vivo', 'La Florida', 'Metropolitana', 'Comunidad local', 180
WHERE NOT EXISTS (SELECT 1 FROM oportunidades WHERE id_organizacion=@organizacion AND nombre_oportunidad='Encuentro de Emprendedores');

INSERT INTO organizaciones (nombre,tipo,rubro,comuna,region)
SELECT 'Club Deportivo Cordillera Sur', 'Club deportivo', 'Deporte', 'San José de Maipo', 'Metropolitana'
WHERE NOT EXISTS (SELECT 1 FROM organizaciones WHERE nombre='Club Deportivo Cordillera Sur');
SET @organizacion = (SELECT MIN(id_organizacion) FROM organizaciones WHERE nombre='Club Deportivo Cordillera Sur');

INSERT INTO contactos (id_organizacion,nombre,cargo,correo,medio_preferente)
SELECT @organizacion, 'Valentina Soto', 'Coordinadora deportiva', 'valentina@example.com', 'Correo'
WHERE NOT EXISTS (SELECT 1 FROM contactos WHERE id_organizacion=@organizacion AND correo='valentina@example.com');
SET @contacto = (SELECT MIN(id_contacto) FROM contactos WHERE id_organizacion=@organizacion AND correo='valentina@example.com');
SET @responsable = (SELECT id_usuario FROM usuarios WHERE activo=1 AND usuario IN ('ejecutivo3', 'admin') ORDER BY (usuario='ejecutivo3') DESC LIMIT 1);

INSERT INTO oportunidades (id_responsable,id_organizacion,id_contacto_principal,nombre_oportunidad,tipo_oportunidad,descripcion,fuente_informacion,fecha_deteccion,prioridad,estado,fecha_inicio,ubicacion_general,comuna,region,publico_esperado,cantidad_asistentes_estimada)
SELECT @responsable, @organizacion, @contacto, 'Jornada Deportiva Cordillera', 'Jornada', 'Activación con familias y deportistas del sector.', 'Demostración', CURRENT_DATE, 'Alta', 'CONFIRMADO', CURRENT_DATE + INTERVAL 42 DAY, 'Club Deportivo Cordillera Sur', 'San José de Maipo', 'Metropolitana', 'Comunidad local', 250
WHERE NOT EXISTS (SELECT 1 FROM oportunidades WHERE id_organizacion=@organizacion AND nombre_oportunidad='Jornada Deportiva Cordillera');

COMMIT;
SELECT nombre_oportunidad, estado FROM oportunidades WHERE fuente_informacion='Demostración';
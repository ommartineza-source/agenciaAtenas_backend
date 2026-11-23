-- Cuentas de prueba de la app Android. Ejecutar solo en desarrollo.
INSERT INTO usuarios (usuario, nombre, rol, password_hash) VALUES
('admin', 'Supervisor de prueba', 'SUPERVISOR', 'bfefe5c55e3a38215d5a4e753a14f389:570f12eafd476cee23a7470d2cc18b732675f9aac5c03b119a04d9696c9aa053'),
('ejecutivo1', 'Ejecutivo de prueba 1', 'EJECUTIVO', 'c15f4748a645c97b814ffe5877c18e6f:574b47189a35e5e4418ce377865df7f96ea723601eb5746ad82275476ca03663'),
('ejecutivo2', 'Ejecutivo de prueba 2', 'EJECUTIVO', '2fe2ea4aa4b81a14a7362a0efe4b91d3:2a5bd9973f922c9528a54880932015c25a05b7be01efe8520479598d8e42d77f'),
('ejecutivo3', 'Ejecutivo de prueba 3', 'EJECUTIVO', '9aa1a4c85543c897733f49e4006a484e:0bf4380d7dff519d1e45f44dfbbe318bdb82323bb7684264da0e69fffc0031d0')
ON DUPLICATE KEY UPDATE id_usuario = id_usuario;

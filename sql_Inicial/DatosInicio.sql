INSERT INTO Negocio (idnedocio, nombre, descripcion, fecha_inicio, fecha_fin)
VALUES
    (1, 'Tienda ABC', 'Venta de productos variados', '2024-01-01', '2024-12-31'),
    (2, 'Restaurante El Gourmet', 'Comida internacional y local', '2024-02-01', '2024-12-31'),
    (3, 'Librería El Saber', 'Libros y material didáctico', '2024-03-01', '2024-12-31'),
    (4, 'Electrodomésticos XYZ', 'Venta y reparación de electrodomésticos', '2024-04-01', '2024-12-31'),
    (5, 'Gimnasio FitLife', 'Centro de fitness y bienestar', '2024-05-01', '2024-12-31');

INSERT INTO Obligacion (idobligacion, descripcion, monto, fecha_vencimiento)
VALUES
    (1, 'Pago de alquiler oficina', 2000.00, '2024-08-31'),
    (2, 'Pago de proveedores', 1500.00, '2024-08-15'),
    (3, 'Pago de servicios públicos', 500.00, '2024-08-20'),
    (4, 'Pago de impuestos', 1200.00, '2024-09-10'),
    (5, 'Pago de nómina', 3000.00, '2024-08-25');



INSERT INTO Persona (persona_id, nombre, apellido, numerodocumento, tipo_documento)
VALUES
    (1, 'Juan', 'Pérez', 123456789, 'DNI'),
    (2, 'Ana', 'García', 987654321, 'DNI'),
    (3, 'Pedro', 'López', 123123123, 'DNI'),
    (4, 'María', 'Fernández', 456456456, 'DNI'),
    (5, 'Luis', 'Martínez', 789789789, 'DNI');
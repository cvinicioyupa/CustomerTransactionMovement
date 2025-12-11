-- Datos de prueba para la aplicación de transacciones

-- Insertar cuentas de prueba
INSERT INTO account (number, type, initial_balance, status, client_identification) VALUES
(100, 'Ahorros', 10000.0, true, 1),
(101, 'Corriente', 5000.0, true, 1),
(102, 'Ahorros', 15000.0, true, 2),
(103, 'Corriente', 8000.0, true, 2),
(104, 'Ahorros', 20000.0, true, 3);

-- Insertar movimientos de prueba
INSERT INTO movement (account_number, date, type, amount, balance, status) VALUES
(100, CURRENT_TIMESTAMP, 'credito', 2000.0, 12000.0, true),
(100, CURRENT_TIMESTAMP, 'debito', 500.0, 11500.0, true),
(101, CURRENT_TIMESTAMP, 'credito', 1000.0, 6000.0, true),
(102, CURRENT_TIMESTAMP, 'debito', 2000.0, 13000.0, true),
(103, CURRENT_TIMESTAMP, 'credito', 3000.0, 11000.0, true);

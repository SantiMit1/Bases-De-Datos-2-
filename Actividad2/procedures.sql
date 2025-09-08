USE SEGUNDA_EVALUACION11;
DELIMITER $$

CREATE PROCEDURE sp_cliente (
    IN op CHAR(1),
    IN p_id_cliente INT,
    IN p_nombre VARCHAR(100),
    IN p_dni VARCHAR(15)
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO cliente(nombre, dni) VALUES(p_nombre, p_dni);
    ELSEIF op = 'R' THEN
        SELECT * FROM cliente WHERE id_cliente = p_id_cliente;
    ELSEIF op = 'U' THEN
        UPDATE cliente SET nombre = p_nombre, dni = p_dni
        WHERE id_cliente = p_id_cliente;
    ELSEIF op = 'D' THEN
        DELETE FROM cliente WHERE id_cliente = p_id_cliente;
    END IF;
END$$

CREATE PROCEDURE sp_cta_corriente (
    IN op CHAR(1),
    IN p_nro INT,
    IN p_id_cliente INT,
    IN p_saldo DECIMAL(10,2)
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO cta_corriente(id_cliente, saldo)
        VALUES(p_id_cliente, p_saldo);
    ELSEIF op = 'R' THEN
        SELECT * FROM cta_corriente cta JOIN cliente c ON c.id_cliente = cta.id_cliente WHERE nro = p_nro;
    ELSEIF op = 'U' THEN
        UPDATE cta_corriente SET saldo = p_saldo
        WHERE nro = p_nro;
    ELSEIF op = 'D' THEN
        DELETE FROM cta_corriente WHERE nro = p_nro;
    END IF;
END$$

CREATE PROCEDURE sp_producto (
    IN op CHAR(1),
    IN p_id INT,
    IN p_nombre VARCHAR(100),
    IN p_precio DECIMAL(10,2),
    IN p_stock INT
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO producto(nombre, precio, stock)
        VALUES(p_nombre, p_precio, p_stock);
    ELSEIF op = 'R' THEN
        SELECT * FROM producto WHERE id = p_id;
    ELSEIF op = 'U' THEN
        UPDATE producto SET nombre = p_nombre, precio = p_precio, stock = p_stock
        WHERE id = p_id;
    ELSEIF op = 'D' THEN
        DELETE FROM producto WHERE id = p_id;
    END IF;
END$$

CREATE PROCEDURE sp_factura (
    IN op CHAR(1),
    IN p_nro INT,
    IN p_id_cliente INT,
    IN p_fecha DATE
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO factura(id_cliente, fecha)
        VALUES(p_id_cliente, p_fecha);
    ELSEIF op = 'R' THEN
        SELECT * FROM factura WHERE nro = p_nro;
    ELSEIF op = 'U' THEN
        UPDATE factura SET id_cliente = p_id_cliente, fecha = p_fecha
        WHERE nro = p_nro;
    ELSEIF op = 'D' THEN
        DELETE FROM factura WHERE nro = p_nro;
    END IF;
END$$

CREATE PROCEDURE sp_detalle (
    IN op CHAR(1),
    IN p_nro INT,
    IN p_id_producto INT,
    IN p_cantidad INT,
    IN p_precio DECIMAL(10,2)
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO detalle(nro, id_producto, cantidad, precio)
        VALUES(p_nro, p_id_producto, p_cantidad, p_precio);
    ELSEIF op = 'R' THEN
        SELECT * FROM detalle WHERE nro = p_nro AND id_producto = p_id_producto;
    ELSEIF op = 'U' THEN
        UPDATE detalle SET cantidad = p_cantidad, precio = p_precio
        WHERE nro = p_nro AND id_producto = p_id_producto;
    ELSEIF op = 'D' THEN
        DELETE FROM detalle WHERE nro = p_nro AND id_producto = p_id_producto;
    END IF;
END$$

CREATE PROCEDURE sp_pago (
    IN op CHAR(1),
    IN p_id INT,
    IN p_nro INT,
    IN p_monto DECIMAL(10,2)
)
BEGIN
    IF op = 'C' THEN
        INSERT INTO pago(nro, monto)
        VALUES(p_nro, p_monto);
    ELSEIF op = 'R' THEN
        SELECT * FROM pago WHERE id = p_id;
    ELSEIF op = 'U' THEN
        UPDATE pago SET nro = p_nro, monto = p_monto
        WHERE id = p_id;
    ELSEIF op = 'D' THEN
        DELETE FROM pago WHERE id = p_id;
    END IF;
END$$
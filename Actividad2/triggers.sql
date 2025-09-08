USE SEGUNDA_EVALUACION11;

DELIMITER $$

/* Validar stock antes de vender */
CREATE TRIGGER bi_detalle
BEFORE INSERT ON detalle
FOR EACH ROW
BEGIN
    DECLARE v_stock INT;

    SELECT stock INTO v_stock
    FROM producto
    WHERE id = NEW.id_producto;

    IF v_stock < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para realizar la venta';
    END IF;
END$$

/* Descontar stock y actualizar importe al insertar detalle */
CREATE TRIGGER ai_detalle
AFTER INSERT ON detalle
FOR EACH ROW
BEGIN
    UPDATE producto
    SET stock = stock - NEW.cantidad
    WHERE id = NEW.id_producto;

    UPDATE factura
    SET importe = importe + (NEW.cantidad * NEW.precio)
    WHERE nro = NEW.nro;
END$$

/* Revertir stock e importe al eliminar detalle */
CREATE TRIGGER ad_detalle
AFTER DELETE ON detalle
FOR EACH ROW
BEGIN
    UPDATE producto
    SET stock = stock + OLD.cantidad
    WHERE id = OLD.id_producto;

    UPDATE factura
    SET importe = importe - (OLD.cantidad * OLD.precio)
    WHERE nro = OLD.nro;
END$$

/* Ajustar stock e importe al modificar detalle */
CREATE TRIGGER au_detalle
AFTER UPDATE ON detalle
FOR EACH ROW
BEGIN
    -- ajustar stock
    UPDATE producto
    SET stock = stock + OLD.cantidad - NEW.cantidad
    WHERE id = NEW.id_producto;

    -- ajustar importe de factura
    UPDATE factura
    SET importe = importe - (OLD.cantidad * OLD.precio)
                  + (NEW.cantidad * NEW.precio)
    WHERE nro = NEW.nro;
END$$

/* Al crear un pago, el cliente es el mismo de la factura y debe tener saldo */
CREATE TRIGGER bi_pago
BEFORE INSERT ON pago
FOR EACH ROW
BEGIN    
    DECLARE v_id_cliente INT;
    DECLARE v_saldo NUMERIC(10,2);
    
    SELECT id_cliente INTO v_id_cliente
    FROM factura
    WHERE 
    NEW.nro = factura.nro;
    
    SET NEW.id_cliente = v_id_cliente;
    
    -- Verificar saldo en la cuenta corriente
    SELECT saldo INTO v_saldo
    FROM cta_corriente
    WHERE id_cliente = v_id_cliente;

    IF v_saldo IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no tiene cuenta corriente';
    END IF;

    IF v_saldo < NEW.monto THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo insuficiente en la cuenta corriente para realizar el pago';
    END IF;
END$$

/* Actualizar saldo de cuenta corriente al registrar pago */
CREATE TRIGGER ai_pago
AFTER INSERT ON pago
FOR EACH ROW
BEGIN
    UPDATE cta_corriente
    SET saldo = saldo - NEW.monto
    WHERE id_cliente = NEW.id_cliente;
END$$

DELIMITER ;

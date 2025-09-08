CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `dni` varchar(15) NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `dni` (`dni`)
);

CREATE TABLE `cta_corriente` (
  `nro` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `saldo` decimal(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`nro`),
  FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
);

CREATE TABLE `producto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL CHECK (`stock` >= 0),
  PRIMARY KEY (`id`)
);

CREATE TABLE `factura` (
  `nro` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `importe` decimal(10,2) NOT NULL DEFAULT 0.00,
  `fecha` date NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nro`),
  FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
);

CREATE TABLE `detalle` (
  `nro` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  PRIMARY KEY (`nro`,`id_producto`),
  FOREIGN KEY (`nro`) REFERENCES `factura` (`nro`),
  FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`)
);

CREATE TABLE `pago` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nro` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`nro`) REFERENCES `factura` (`nro`),
  FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`)
)
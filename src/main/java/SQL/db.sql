CREATE DATABASE tecnostore_db;
USE tecnostore_db;

CREATE TABLE marca(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL
);


CREATE TABLE celulares (
    id INT PRIMARY KEY AUTO_INCREMENT,
    marca INT NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    sistema_operativo VARCHAR(50),
    gama ENUM('baja', 'media', 'alta', 'premium') NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    FOREIGN KEY (marca) REFERENCES marcos(id)
);


CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(15)
);


CREATE TABLE ventas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);


CREATE TABLE detalle_venta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_celular INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id),
    FOREIGN KEY (id_celular) REFERENCES celulares(id)
);

CREATE INDEX idx_celulares_marca ON celulares(marca);
CREATE INDEX idx_celulares_modelo ON celulares(modelo);
CREATE INDEX idx_celulares_gama ON celulares(gama);

CREATE INDEX idx_clientes_identificacion ON clientes(identificacion);
CREATE INDEX idx_clientes_correo ON clientes(correo);

CREATE INDEX idx_ventas_cliente ON ventas(id_cliente);
CREATE INDEX idx_ventas_fecha ON ventas(fecha);

CREATE INDEX idx_detalle_venta_venta ON detalle_venta(id_venta);
CREATE INDEX idx_detalle_venta_celular ON detalle_venta(id_celular);
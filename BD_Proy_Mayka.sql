CREATE TABLE status_logistica (
    id_status_logistica INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    descripcion_logistica VARCHAR(12) NOT NULL,
    CONSTRAINT uix_logistica UNIQUE(descripcion_logistica)
);

CREATE TABLE status_pago (
    id_status_pago INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    descripcion_pago VARCHAR(12) NOT NULL,
    CONSTRAINT uix_pago UNIQUE(descripcion_pago)
);

CREATE TABLE tipo_envio (
    id_tipo_envio INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    descripcion_envio VARCHAR(30) NOT NULL,
    CONSTRAINT uix_envio UNIQUE(descripcion_envio)
);

CREATE TABLE status_pedido (
    id_status_pedido INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    descripcion_status_pedido VARCHAR(30) NOT NULL,
    CONSTRAINT uix_status_pedido UNIQUE(descripcion_status_pedido)
);

CREATE TABLE cliente (
    id_cliente INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    primer_apellido VARCHAR(30) NOT NULL,
    segundo_apellido VARCHAR(30),
    contacto VARCHAR(30) NOT NULL,
    CONSTRAINT uix_cliente UNIQUE(nombre, primer_apellido, segundo_apellido),
    CONSTRAINT uix_cliente2 UNIQUE(contacto)
);

CREATE TABLE producto (
    id_producto INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre_producto VARCHAR(50) NOT NULL,
    precio_unitario DECIMAL(7,2) NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT uix_producto UNIQUE(nombre_producto)
);

CREATE TABLE pedido (
    id_pedido INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_status_pedido INTEGER NOT NULL,
    fecha_pedido DATE NOT NULL,
    numero_guia VARCHAR(30),
    monto_total DECIMAL(7,2) NOT NULL,
    monto_envio DECIMAL(7,2),
    fk_cliente INTEGER NOT NULL,
    fk_status_pago INTEGER NOT NULL,
    fk_status_logistica INTEGER NOT NULL,
    fk_tipo_envio INTEGER NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (fk_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT fk_pedido_status_pago FOREIGN KEY (fk_status_pago) REFERENCES status_pago(id_status_pago),
    CONSTRAINT fk_pedido_status_log FOREIGN KEY (fk_status_logistica) REFERENCES status_logistica(id_status_logistica),
    CONSTRAINT fk_pedido_tipo_envio FOREIGN KEY (fk_tipo_envio) REFERENCES tipo_envio(id_tipo_envio),
    CONSTRAINT fk_pedido_status_pedido FOREIGN KEY (fk_status_pedido) REFERENCES status_pedido(id_status_pedido)
);

CREATE TABLE pago (
    id_pago INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    monto_pago DECIMAL(7,2) NOT NULL,
    fecha_pago DATE NOT NULL,
    numero_referencia VARCHAR(30),
    fk_pedido INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_pago_pedido FOREIGN KEY (fk_pedido) REFERENCES pedido(id_pedido)
);

CREATE TABLE pedidos_productos (
    id_pedido_producto INTEGER UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_producto INTEGER NOT NULL,
    fk_pedido INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_pp_producto FOREIGN KEY (fk_producto) REFERENCES producto(id_producto),
    CONSTRAINT fk_pp_pedido FOREIGN KEY (fk_pedido) REFERENCES pedido(id_pedido)
);

CREATE TABLE corte_mes_schedule (
    id_corte_mes_schedule INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    mes_envio INTEGER NOT NULL,
    anio_envio INTEGER NOT NULL,
    numero_intentos INTEGER NOT NULL,
    enviado BOOLEAN NOT NULL,
    fecha_hora_envio DATETIME,
    CONSTRAINT uix_corte UNIQUE(mes_envio, anio_envio)
);

CREATE TABLE pedido_detalle (
    id_pedido_detalle INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    telefono VARCHAR(15),
    direccion VARCHAR(120),
    notas VARCHAR(120),
    fk_pedido INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_pedido_detalle_pedido FOREIGN KEY (fk_pedido) REFERENCES pedido(id_pedido)
);


INSERT INTO status_logistica (descripcion_logistica) VALUES ('Pendiente');
INSERT INTO status_logistica (descripcion_logistica) VALUES ('En Ruta');
INSERT INTO status_logistica (descripcion_logistica) VALUES ('Entregado');

INSERT INTO status_pago (descripcion_pago) VALUES ('Pendiente');
INSERT INTO status_pago (descripcion_pago) VALUES ('Apartado');
INSERT INTO status_pago (descripcion_pago) VALUES ('Pagado');

INSERT INTO tipo_envio (descripcion_envio) VALUES ('Zona Metropolitana');
INSERT INTO tipo_envio (descripcion_envio) VALUES ('Foráneo');
INSERT INTO tipo_envio (descripcion_envio) VALUES ('Bosques');
INSERT INTO tipo_envio (descripcion_envio) VALUES ('Recolección en casa');

INSERT INTO status_pedido (descripcion_status_pedido) VALUES ('En Espera De Pago');
INSERT INTO status_pedido (descripcion_status_pedido) VALUES ('En Proceso');
INSERT INTO status_pedido (descripcion_status_pedido) VALUES ('Enviado');
INSERT INTO status_pedido (descripcion_status_pedido) VALUES ('Finalizado');


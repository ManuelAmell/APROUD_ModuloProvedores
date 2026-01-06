#!/bin/bash

# Script para configurar la base de datos en MariaDB/MySQL

echo "=========================================="
echo "Configuración de Base de Datos"
echo "=========================================="
echo ""

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}Configurando base de datos con sudo...${NC}"
echo ""

# Crear base de datos y usuario con sudo
sudo mysql << 'EOF'
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gestion_proveedores
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE gestion_proveedores;

-- Eliminar tablas si existen
DROP TABLE IF EXISTS compras;
DROP TABLE IF EXISTS categorias_compra;
DROP TABLE IF EXISTS proveedores;

-- Crear tabla de proveedores
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    nit VARCHAR(50) UNIQUE,
    tipo VARCHAR(100),
    direccion VARCHAR(300),
    telefono VARCHAR(50),
    email VARCHAR(100),
    persona_contacto VARCHAR(150),
    informacion_pago TEXT,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_nombre ON proveedores(nombre);
CREATE INDEX idx_activo ON proveedores(activo);

-- Crear tabla de categorías
CREATE TABLE categorias_compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    es_predefinida TINYINT(1) DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar categorías predefinidas
INSERT INTO categorias_compra (nombre, es_predefinida) VALUES
('zapatos', 1),
('pantalones', 1),
('ropa', 1),
('camisas', 1),
('accesorios', 1),
('otros', 1);

-- Crear tabla de compras
CREATE TABLE compras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    numero_factura VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    total DECIMAL(10, 2) NOT NULL,
    fecha_compra DATE NOT NULL,
    forma_pago ENUM('efectivo', 'transferencia', 'credito') NOT NULL,
    estado_credito ENUM('pendiente', 'pagado') DEFAULT NULL,
    fecha_pago DATE DEFAULT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id) ON DELETE RESTRICT,
    CHECK (
        (forma_pago != 'credito' AND estado_credito IS NULL)
        OR
        (forma_pago = 'credito' AND estado_credito IS NOT NULL)
    ),
    CHECK (
        (estado_credito = 'pendiente' AND fecha_pago IS NULL)
        OR
        (estado_credito = 'pagado' AND fecha_pago IS NOT NULL)
        OR
        (estado_credito IS NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_proveedor ON compras(id_proveedor);
CREATE INDEX idx_fecha_compra ON compras(fecha_compra);
CREATE INDEX idx_categoria ON compras(categoria);
CREATE INDEX idx_forma_pago ON compras(forma_pago);
CREATE INDEX idx_estado_credito ON compras(estado_credito);

-- Insertar datos de ejemplo
INSERT INTO proveedores (nombre, nit, tipo, direccion, telefono, email, persona_contacto, informacion_pago, activo) VALUES
('Distribuidora ABC', '900123456-1', 'ropa', 'Calle 50 #30-25, Bogotá', '601-555-0100', 'ventas@distribuidoraabc.com', 'María García', 'Bancolombia - Cta 123456789', 1),
('Suministros del Norte', '800987654-2', 'calzado', 'Av. Libertador 150, Barranquilla', '605-555-0200', 'contacto@suministrosnorte.com', 'Carlos Rodríguez', 'Davivienda - Cta 987654321', 1),
('Importadora XYZ', NULL, 'insumos', 'Zona Franca, Cartagena', '605-555-0300', 'info@importadoraxyz.com', 'Ana Martínez', NULL, 0);

INSERT INTO compras (id_proveedor, numero_factura, categoria, descripcion, cantidad, precio_unitario, total, fecha_compra, forma_pago, estado_credito, fecha_pago) VALUES
(1, 'FAC-001', 'zapatos', 'Zapatos deportivos Nike talla 42, color negro', 10, 150000, 1500000, '2025-01-02', 'efectivo', NULL, '2025-01-02'),
(1, 'FAC-002', 'camisas', 'Camisas polo manga corta, colores variados', 20, 45000, 900000, '2025-01-03', 'credito', 'pendiente', NULL),
(2, 'FAC-003', 'pantalones', 'Pantalones jean clásico, tallas 28-34', 15, 80000, 1200000, '2024-12-28', 'transferencia', NULL, '2024-12-28'),
(2, 'FAC-004', 'accesorios', 'Cinturones de cuero genuino', 30, 25000, 750000, '2024-12-20', 'credito', 'pagado', '2025-01-02');

-- Crear usuario para la aplicación (si no existe)
CREATE USER IF NOT EXISTS 'proveedor_app'@'localhost' IDENTIFIED BY 'Amell123';
GRANT ALL PRIVILEGES ON gestion_proveedores.* TO 'proveedor_app'@'localhost';
FLUSH PRIVILEGES;

SELECT 'Base de datos configurada exitosamente' AS Mensaje;
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}=========================================="
    echo "  BASE DE DATOS CONFIGURADA"
    echo "==========================================${NC}"
    echo ""
    echo -e "${YELLOW}Credenciales de la aplicación:${NC}"
    echo "  Base de datos: gestion_proveedores"
    echo "  Usuario: proveedor_app"
    echo "  Contraseña: proveedor123"
    echo ""
    echo -e "${GREEN}✓ Listo para usar${NC}"
    echo ""
else
    echo ""
    echo -e "${RED}Error al configurar la base de datos${NC}"
    echo "Verifica que MariaDB/MySQL esté corriendo:"
    echo "  sudo systemctl status mariadb"
    exit 1
fi

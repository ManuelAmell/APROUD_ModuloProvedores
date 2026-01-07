#!/bin/bash

# ============================================================
# SCRIPT INTELIGENTE DE EJECUCIÓN - LINUX/MAC
# Sistema de Gestión de Proveedores y Compras v2.0
# ============================================================
# Este script detecta automáticamente si es primera vez
# y realiza todas las configuraciones necesarias
# ============================================================

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

clear
echo -e "${CYAN}=========================================="
echo "  SISTEMA DE GESTIÓN v2.0"
echo "  Proveedores y Compras"
echo "==========================================${NC}"
echo ""

# ============================================================
# FUNCIÓN: Verificar si es primera vez
# ============================================================
es_primera_vez() {
    # Verificar si existe la base de datos usando las credenciales de la aplicación
    if command -v mysql &> /dev/null; then
        DB_EXISTS=$(mysql -u proveedor_app -pproveedor123 -e "SHOW DATABASES LIKE 'gestion_proveedores';" 2>/dev/null | grep -c "gestion_proveedores")
    elif command -v mariadb &> /dev/null; then
        DB_EXISTS=$(mariadb -u proveedor_app -pproveedor123 -e "SHOW DATABASES LIKE 'gestion_proveedores';" 2>/dev/null | grep -c "gestion_proveedores")
    else
        DB_EXISTS=0
    fi
    
    # Verificar si existe el directorio bin con clases compiladas
    BIN_EXISTS=0
    if [ -d "bin" ] && [ "$(ls -A bin 2>/dev/null)" ]; then
        BIN_EXISTS=1
    fi
    
    # Es primera vez si no existe la BD o no está compilado
    if [ $DB_EXISTS -eq 0 ] || [ $BIN_EXISTS -eq 0 ]; then
        return 0  # true - es primera vez
    else
        return 1  # false - no es primera vez
    fi
}

# ============================================================
# FUNCIÓN: Configurar base de datos
# ============================================================
configurar_base_datos() {
    echo -e "${BLUE}[1/4] Configurando base de datos...${NC}"
    
    # Detectar si es MySQL o MariaDB
    if command -v mariadb &> /dev/null; then
        DB_CMD="mariadb"
    elif command -v mysql &> /dev/null; then
        DB_CMD="mysql"
    else
        echo -e "${RED}✗ MySQL/MariaDB no está instalado${NC}"
        echo ""
        echo "Instala MySQL o MariaDB:"
        echo "  Ubuntu/Debian: sudo apt install mysql-server"
        echo "  Arch Linux: sudo pacman -S mariadb"
        echo "  Fedora: sudo dnf install mariadb-server"
        exit 1
    fi
    
    # Verificar si el servicio está corriendo
    if ! systemctl is-active --quiet mysql 2>/dev/null && \
       ! systemctl is-active --quiet mariadb 2>/dev/null; then
        echo -e "${YELLOW}⚠ Servicio de base de datos no está activo${NC}"
        echo "Inicia el servicio manualmente: sudo systemctl start mariadb"
    fi
    
    # Ejecutar script de configuración
    if [ -f "setup_database.sh" ]; then
        ./setup_database.sh > /dev/null 2>&1
    else
        # Configuración inline
        sudo $DB_CMD << 'EOSQL' > /dev/null 2>&1
CREATE DATABASE IF NOT EXISTS gestion_proveedores CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestion_proveedores;
DROP TABLE IF EXISTS compras;
DROP TABLE IF EXISTS categorias_compra;
DROP TABLE IF EXISTS proveedores;
CREATE TABLE proveedores (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(200) NOT NULL, nit VARCHAR(50) UNIQUE, tipo VARCHAR(100), direccion VARCHAR(300), telefono VARCHAR(50), email VARCHAR(100), persona_contacto VARCHAR(150), informacion_pago TEXT, activo TINYINT(1) DEFAULT 1, fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_nombre ON proveedores(nombre);
CREATE INDEX idx_activo ON proveedores(activo);
CREATE TABLE categorias_compra (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL UNIQUE, es_predefinida TINYINT(1) DEFAULT 0, fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO categorias_compra (nombre, es_predefinida) VALUES ('zapatos', 1), ('pantalones', 1), ('ropa', 1), ('camisas', 1), ('accesorios', 1), ('otros', 1);
CREATE TABLE compras (id INT AUTO_INCREMENT PRIMARY KEY, id_proveedor INT NOT NULL, numero_factura VARCHAR(100) NOT NULL, categoria VARCHAR(100) NOT NULL, descripcion TEXT NOT NULL, cantidad INT, precio_unitario DECIMAL(10, 2), total DECIMAL(10, 2) NOT NULL, fecha_compra DATE NOT NULL, forma_pago ENUM('efectivo', 'transferencia', 'credito') NOT NULL, estado_credito ENUM('pendiente', 'pagado') DEFAULT NULL, fecha_pago DATE DEFAULT NULL, fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, FOREIGN KEY (id_proveedor) REFERENCES proveedores(id) ON DELETE RESTRICT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_proveedor ON compras(id_proveedor);
CREATE INDEX idx_fecha_compra ON compras(fecha_compra);
CREATE INDEX idx_categoria ON compras(categoria);
INSERT INTO proveedores (nombre, nit, tipo, direccion, telefono, email, persona_contacto, informacion_pago, activo) VALUES ('Distribuidora ABC', '900123456-1', 'ropa', 'Calle 50 #30-25', '601-555-0100', 'ventas@abc.com', 'María García', 'Bancolombia - Cta 123456789', 1), ('Suministros del Norte', '800987654-2', 'calzado', 'Av. Libertador 150', '605-555-0200', 'contacto@norte.com', 'Carlos Rodríguez', 'Davivienda - Cta 987654321', 1);
INSERT INTO compras (id_proveedor, numero_factura, categoria, descripcion, cantidad, precio_unitario, total, fecha_compra, forma_pago, estado_credito, fecha_pago) VALUES (1, 'FAC-001', 'zapatos', 'Zapatos deportivos Nike talla 42', 10, 150000, 1500000, '2025-01-02', 'efectivo', NULL, '2025-01-02'), (1, 'FAC-002', 'camisas', 'Camisas polo manga corta', 20, 45000, 900000, '2025-01-03', 'credito', 'pendiente', NULL);
CREATE USER IF NOT EXISTS 'proveedor_app'@'localhost' IDENTIFIED BY 'proveedor123';
GRANT ALL PRIVILEGES ON gestion_proveedores.* TO 'proveedor_app'@'localhost';
FLUSH PRIVILEGES;
EOSQL
    fi
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Base de datos configurada${NC}"
    else
        echo -e "${RED}✗ Error al configurar base de datos${NC}"
        exit 1
    fi
}

# ============================================================
# FUNCIÓN: Compilar proyecto
# ============================================================
compilar_proyecto() {
    echo -e "${BLUE}[2/4] Compilando proyecto...${NC}"
    
    # Verificar Java
    if ! command -v javac &> /dev/null; then
        echo -e "${RED}✗ Java JDK no está instalado${NC}"
        echo "Instala Java JDK:"
        echo "  Ubuntu/Debian: sudo apt install default-jdk"
        echo "  Arch Linux: sudo pacman -S jdk-openjdk"
        exit 1
    fi
    
    # Limpiar y crear directorio bin
    rm -rf bin/*
    mkdir -p bin
    
    # Compilar clases base
    javac -d bin -cp "lib/*:src" src/modelo/*.java src/dao/*.java src/servicio/*.java src/util/*.java
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}✗ Error al compilar clases base${NC}"
        exit 1
    fi
    
    # Compilar interfaces
    javac -d bin -cp "lib/*:bin:src" src/vista/PantallaCarga.java src/vista/VentanaUnificada.java src/vista/FormularioProveedorDark.java src/vista/FormularioCompraDark.java
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}✗ Error al compilar interfaces${NC}"
        exit 1
    fi
    
    # Compilar Main
    javac -d bin -cp "lib/*:bin" src/Main.java
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Compilación exitosa${NC}"
    else
        echo -e "${RED}✗ Error al compilar Main${NC}"
        exit 1
    fi
}

# ============================================================
# FUNCIÓN: Verificar compilación
# ============================================================
verificar_compilacion() {
    if [ ! -d "bin" ] || [ ! "$(ls -A bin 2>/dev/null)" ]; then
        echo -e "${YELLOW}[2/4] Recompilando proyecto...${NC}"
        compilar_proyecto
    else
        echo -e "${GREEN}[2/4] ✓ Proyecto ya compilado${NC}"
    fi
}

# ============================================================
# FUNCIÓN: Ejecutar aplicación
# ============================================================
ejecutar_aplicacion() {
    echo -e "${BLUE}[3/4] Preparando ejecución...${NC}"
    sleep 1
    echo -e "${GREEN}✓ Listo${NC}"
    echo ""
    echo -e "${BLUE}[4/4] Iniciando aplicación...${NC}"
    echo ""
    echo -e "${GREEN}=========================================="
    echo "  SISTEMA INICIADO"
    echo "==========================================${NC}"
    echo ""
    echo -e "${CYAN}Características:${NC}"
    echo "  ✓ Interfaz oscura profesional"
    echo "  ✓ Búsqueda en tiempo real"
    echo "  ✓ Categorías dinámicas"
    echo "  ✓ Control de créditos"
    echo ""
    echo -e "${YELLOW}Abriendo interfaz gráfica...${NC}"
    echo ""
    
    # Ejecutar
    java -cp "bin:lib/*" Main
    
    echo ""
    echo -e "${GREEN}Aplicación cerrada correctamente${NC}"
}

# ============================================================
# PROGRAMA PRINCIPAL
# ============================================================

# Detectar si es primera vez
if es_primera_vez; then
    echo -e "${YELLOW}⚙️  Primera ejecución detectada${NC}"
    echo -e "${YELLOW}Configurando sistema...${NC}"
    echo ""
    
    # Configurar base de datos
    configurar_base_datos
    echo ""
    
    # Compilar proyecto
    compilar_proyecto
    echo ""
    
    # Ejecutar
    ejecutar_aplicacion
else
    echo -e "${GREEN}✓ Sistema ya configurado${NC}"
    echo ""
    
    # Solo verificar compilación
    verificar_compilacion
    echo ""
    
    # Ejecutar
    ejecutar_aplicacion
fi

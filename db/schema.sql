-- ============================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS Y TABLA
-- ============================================================
-- 
-- DESCRIPCIÓN:
-- Este script crea la base de datos para el sistema de gestión
-- de proveedores y la tabla principal 'proveedores'.
-- 
-- INSTRUCCIONES DE USO:
-- 1. Abre MySQL Workbench o conéctate a MySQL desde terminal
-- 2. Ejecuta este script completo
-- 3. La base de datos y tabla se crearán automáticamente
-- 
-- DESDE TERMINAL:
-- mysql -u root -p < db/schema.sql
-- 
-- ============================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS gestion_proveedores
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE gestion_proveedores;

-- Eliminar la tabla si existe (para poder recrearla)
DROP TABLE IF EXISTS proveedores;

-- Crear la tabla de proveedores
CREATE TABLE proveedores (
    -- ID: Clave primaria, autoincremental
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    -- Nombre de la empresa proveedora
    -- VARCHAR(200): Texto de hasta 200 caracteres
    -- NOT NULL: Campo obligatorio
    nombre VARCHAR(200) NOT NULL,
    
    -- Número de Identificación Tributaria
    -- UNIQUE: No puede haber dos proveedores con el mismo NIT
    nit VARCHAR(50) NOT NULL UNIQUE,
    
    -- Dirección física del proveedor
    direccion VARCHAR(300),
    
    -- Teléfono de contacto
    telefono VARCHAR(50),
    
    -- Correo electrónico
    email VARCHAR(100),
    
    -- Persona de contacto
    persona_contacto VARCHAR(150),
    
    -- Estado activo/inactivo
    -- TINYINT(1): Número pequeño (0 o 1)
    -- DEFAULT 1: Por defecto está activo
    activo TINYINT(1) DEFAULT 1,
    
    -- Fecha de creación del registro
    -- TIMESTAMP: Fecha y hora
    -- DEFAULT CURRENT_TIMESTAMP: Se asigna automáticamente
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Fecha de última actualización
    -- ON UPDATE CURRENT_TIMESTAMP: Se actualiza automáticamente
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear índices para mejorar el rendimiento de las búsquedas
-- Índice en el campo 'nombre' para búsquedas rápidas
CREATE INDEX idx_nombre ON proveedores(nombre);

-- Índice en el campo 'activo' para filtrar por estado
CREATE INDEX idx_activo ON proveedores(activo);

-- Insertar datos de ejemplo
INSERT INTO proveedores (nombre, nit, direccion, telefono, email, persona_contacto, activo) VALUES
('Distribuidora ABC', '900123456-1', 'Calle 50 #30-25, Bogotá', '601-555-0100', 'ventas@distribuidoraabc.com', 'María García', 1),
('Suministros del Norte', '800987654-2', 'Av. Libertador 150, Barranquilla', '605-555-0200', 'contacto@suministrosnorte.com', 'Carlos Rodríguez', 1),
('Importadora XYZ', '700555888-3', 'Zona Franca, Cartagena', '605-555-0300', 'info@importadoraxyz.com', 'Ana Martínez', 0);

-- Mostrar mensaje de confirmación
SELECT 'Base de datos y tabla creadas exitosamente' AS Mensaje;

-- Mostrar los datos insertados
SELECT * FROM proveedores;

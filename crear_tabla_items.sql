-- ==========================================
-- Crear tabla items_compra
-- ==========================================

USE gestion_proveedores;

-- Crear tabla de items de compra
CREATE TABLE IF NOT EXISTS items_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_compra INT NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    codigo VARCHAR(100),
    precio_unitario DECIMAL(15,2) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL,
    orden INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_compra) REFERENCES compras(id) ON DELETE CASCADE,
    INDEX idx_compra (id_compra),
    INDEX idx_orden (orden)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verificar que se creó correctamente
DESCRIBE items_compra;

SELECT 'Tabla items_compra creada exitosamente' AS Resultado;

-- ==========================================
-- Actualización: Sistema de Items por Compra
-- Permite agregar múltiples productos por factura
-- ==========================================

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

-- Agregar comentarios
ALTER TABLE items_compra COMMENT = 'Items/productos individuales de cada compra';

-- Nota: La tabla compras ya tiene los campos necesarios:
-- - descripcion: Se usará como descripción general de la compra
-- - cantidad: Opcional, para compatibilidad
-- - precio_unitario: Opcional, para compatibilidad
-- - total: Suma de todos los subtotales de items

-- ==========================================
-- Fin de actualización
-- ==========================================

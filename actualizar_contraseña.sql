-- ==========================================
-- Actualizar contraseña del usuario de la aplicación
-- ==========================================

-- Cambiar contraseña del usuario proveedor_app
ALTER USER 'proveedor_app'@'localhost' IDENTIFIED BY 'Amell123';

-- Asegurar que tiene todos los permisos
GRANT ALL PRIVILEGES ON gestion_proveedores.* TO 'proveedor_app'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;

-- Verificar usuario
SELECT User, Host FROM mysql.user WHERE User = 'proveedor_app';

SELECT '✓ Contraseña actualizada exitosamente a: Amell123' AS Resultado;

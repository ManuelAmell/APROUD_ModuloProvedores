#!/bin/bash

echo "=========================================="
echo "Actualizar Contraseña de Base de Datos"
echo "=========================================="
echo ""
echo "Esta operación cambiará la contraseña del usuario 'proveedor_app'"
echo "Nueva contraseña: Amell123"
echo ""
read -p "¿Deseas continuar? (s/n): " respuesta

if [ "$respuesta" != "s" ] && [ "$respuesta" != "S" ]; then
    echo "Operación cancelada."
    exit 0
fi

echo ""
echo "Actualizando contraseña en la base de datos..."
echo ""

# Intentar con diferentes comandos
if command -v mariadb &> /dev/null; then
    sudo mariadb -u root < actualizar_contraseña.sql
elif command -v mysql &> /dev/null; then
    sudo mysql -u root < actualizar_contraseña.sql
else
    echo "ERROR: No se encontró MySQL ni MariaDB"
    exit 1
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ Contraseña actualizada exitosamente"
    echo "=========================================="
    echo ""
    echo "Nueva contraseña: Amell123"
    echo ""
    echo "IMPORTANTE:"
    echo "- El código Java ya está actualizado"
    echo "- Recompila la aplicación: bash compilar.sh"
    echo "- Revisa el archivo: CREDENCIALES_BASE_DATOS.txt"
    echo ""
else
    echo ""
    echo "=========================================="
    echo "✗ Error al actualizar la contraseña"
    echo "=========================================="
    echo ""
    echo "Intenta manualmente:"
    echo "  sudo mysql -u root"
    echo "  ALTER USER 'proveedor_app'@'localhost' IDENTIFIED BY 'Amell123';"
    echo "  FLUSH PRIVILEGES;"
    echo ""
fi

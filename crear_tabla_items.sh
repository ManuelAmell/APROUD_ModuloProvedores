#!/bin/bash

echo "=========================================="
echo "Creando tabla items_compra"
echo "=========================================="
echo ""

# Intentar con diferentes comandos de MySQL/MariaDB
if command -v mariadb &> /dev/null; then
    echo "Usando MariaDB..."
    sudo mariadb -u root < crear_tabla_items.sql
elif command -v mysql &> /dev/null; then
    echo "Usando MySQL..."
    sudo mysql -u root < crear_tabla_items.sql
else
    echo "ERROR: No se encontró MySQL ni MariaDB"
    exit 1
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ Tabla creada exitosamente"
    echo "=========================================="
else
    echo ""
    echo "=========================================="
    echo "✗ Error al crear la tabla"
    echo "=========================================="
    echo ""
    echo "Intenta ejecutar manualmente:"
    echo "  sudo mysql -u root < crear_tabla_items.sql"
    echo ""
    echo "O desde MySQL:"
    echo "  sudo mysql -u root"
    echo "  USE gestion_proveedores;"
    echo "  SOURCE crear_tabla_items.sql;"
fi

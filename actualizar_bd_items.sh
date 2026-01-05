#!/bin/bash

# Script para actualizar la base de datos con la tabla items_compra

echo "=========================================="
echo "Actualización de Base de Datos"
echo "Sistema de Items por Compra"
echo "=========================================="
echo ""
echo "Este script creará la tabla 'items_compra' en la base de datos."
echo ""
read -p "Presione Enter para continuar..."

# Ejecutar script SQL
mysql -u root -p gestion_proveedores < db/update_items_compra.sql

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✓ Base de datos actualizada exitosamente"
    echo "=========================================="
    echo ""
    echo "La tabla 'items_compra' ha sido creada."
    echo "Ahora puede usar el sistema de múltiples items por compra."
else
    echo ""
    echo "=========================================="
    echo "  ✗ Error al actualizar la base de datos"
    echo "=========================================="
    echo ""
    echo "Verifique:"
    echo "  - MySQL está corriendo"
    echo "  - La contraseña de root es correcta"
    echo "  - La base de datos 'gestion_proveedores' existe"
fi

echo ""
read -p "Presione Enter para salir..."

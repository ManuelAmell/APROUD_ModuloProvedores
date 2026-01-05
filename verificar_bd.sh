#!/bin/bash

echo "=========================================="
echo "Verificando estructura de Base de Datos"
echo "=========================================="
echo ""

# Verificar si MySQL está corriendo
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL no está instalado o no está en el PATH"
    exit 1
fi

echo "1. Verificando tabla items_compra..."
mysql -u root -p -e "USE gestion_proveedores; DESCRIBE items_compra;" 2>/dev/null

echo ""
echo "2. Contando items existentes..."
mysql -u root -p -e "USE gestion_proveedores; SELECT COUNT(*) as total_items FROM items_compra;" 2>/dev/null

echo ""
echo "3. Mostrando últimos 5 items..."
mysql -u root -p -e "USE gestion_proveedores; SELECT id, id_compra, cantidad, LEFT(descripcion, 30) as desc_corta, precio_unitario, subtotal FROM items_compra ORDER BY id DESC LIMIT 5;" 2>/dev/null

echo ""
echo "=========================================="
echo "Verificación completada"
echo "=========================================="

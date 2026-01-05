#!/bin/bash

# ==========================================
# SCRIPT PARA SUBIR A GITHUB
# ==========================================
# Este script contiene todos los comandos necesarios
# para subir el proyecto a GitHub
# ==========================================

echo "=========================================="
echo "Preparando para subir a GitHub"
echo "=========================================="
echo ""

# Verificar que estamos en un repositorio git
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo "Inicializando repositorio Git..."
    git init
    echo ""
fi

# Mostrar estado actual
echo "Estado actual del repositorio:"
git status --short
echo ""

# Preguntar si continuar
read -p "¿Deseas continuar con la subida a GitHub? (s/n): " respuesta
if [ "$respuesta" != "s" ] && [ "$respuesta" != "S" ]; then
    echo "Operación cancelada."
    exit 0
fi

echo ""
echo "=========================================="
echo "Paso 1: Agregando archivos"
echo "=========================================="
git add .
echo "✓ Archivos agregados"
echo ""

echo "=========================================="
echo "Paso 2: Creando commit"
echo "=========================================="
git commit -m "Release v2.3.0: Sistema completo de items por compra

Características principales:
- Tabla editable estilo Excel con 8 columnas
- Modo visualización (solo lectura) con botón Ver
- Reloj en tiempo real en ventana principal
- Validaciones robustas y cálculo automático
- Nueva tabla items_compra en base de datos
- Documentación completa actualizada
- Scripts de utilidad y configuración
- Interfaz oscura profesional mejorada

Correcciones:
- Error al guardar items al editar factura
- Problema con IDs duplicados
- Navegación con clicks en tabla
- Actualización de tabla principal

Documentación:
- README.md completo
- CHANGELOG.md actualizado
- Guías de usuario
- Scripts de instalación"

echo "✓ Commit creado"
echo ""

echo "=========================================="
echo "Paso 3: Configurar repositorio remoto"
echo "=========================================="
echo ""
echo "IMPORTANTE: Primero crea el repositorio en GitHub:"
echo "1. Ve a: https://github.com/new"
echo "2. Nombre: ModuloProveedores"
echo "3. Descripción: Sistema de gestión de proveedores y compras con interfaz oscura profesional"
echo "4. Público o Privado (tu elección)"
echo "5. NO inicializar con README"
echo "6. Crear repositorio"
echo ""
read -p "¿Ya creaste el repositorio en GitHub? (s/n): " creado

if [ "$creado" != "s" ] && [ "$creado" != "S" ]; then
    echo ""
    echo "Crea el repositorio primero y luego ejecuta este script de nuevo."
    exit 0
fi

echo ""
read -p "Ingresa tu usuario de GitHub: " usuario

if [ -z "$usuario" ]; then
    echo "Error: Debes ingresar tu usuario de GitHub"
    exit 1
fi

echo ""
echo "Configurando remote..."
git remote remove origin 2>/dev/null
git remote add origin "https://github.com/$usuario/ModuloProveedores.git"
echo "✓ Remote configurado"
echo ""

echo "=========================================="
echo "Paso 4: Subiendo a GitHub"
echo "=========================================="
echo ""
echo "Subiendo archivos..."
git push -u origin main 2>/dev/null || git push -u origin master

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ ¡ÉXITO! Proyecto subido a GitHub"
    echo "=========================================="
    echo ""
    echo "Tu repositorio está en:"
    echo "https://github.com/$usuario/ModuloProveedores"
    echo ""
    echo "Próximos pasos:"
    echo "1. Ve a tu repositorio en GitHub"
    echo "2. Verifica que todo se vea bien"
    echo "3. Crea un Release v2.3.0"
    echo "4. Agrega topics: java, swing, mysql, inventory-management"
    echo "5. ¡Comparte tu proyecto!"
    echo ""
else
    echo ""
    echo "=========================================="
    echo "✗ Error al subir"
    echo "=========================================="
    echo ""
    echo "Posibles soluciones:"
    echo "1. Verifica tu usuario de GitHub"
    echo "2. Verifica que el repositorio exista"
    echo "3. Intenta manualmente:"
    echo "   git push -u origin main"
    echo ""
fi

#!/bin/bash
# ============================================================
# Script para compilar y ejecutar el Módulo de Proveedores
# Sistema: Linux / macOS
# ============================================================

echo "========================================"
echo "  MÓDULO DE PROVEEDORES - Compilación"
echo "========================================"
echo ""

# Crear carpeta bin si no existe
mkdir -p bin

# Compilar todos los archivos Java
echo "Compilando archivos Java..."
javac -d bin -encoding UTF-8 -cp "lib/*" \
    src/modelo/*.java \
    src/util/*.java \
    src/dao/*.java \
    src/servicio/*.java \
    src/vista/*.java \
    src/Main.java

# Verificar si la compilación fue exitosa
if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: La compilación falló."
    echo "Revisa los errores anteriores."
    exit 1
fi

echo ""
echo "¡Compilación exitosa!"
echo ""
echo "========================================"
echo "  Ejecutando aplicación..."
echo "========================================"
echo ""

# Ejecutar la aplicación con el driver MySQL en el classpath
java -cp "bin:lib/*" Main

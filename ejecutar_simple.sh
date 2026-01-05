#!/bin/bash

# Script simple de ejecución sin sudo
clear
echo "=========================================="
echo "  SISTEMA DE GESTIÓN v2.0"
echo "=========================================="
echo ""

# Ejecutar la aplicación
echo "Iniciando aplicación..."
java -cp "bin:lib/*" Main

echo ""
echo "Aplicación cerrada."

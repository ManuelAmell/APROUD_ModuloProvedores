#!/bin/bash
# ============================================================
# Script para crear distribuciones del Módulo de Proveedores
# ============================================================
# 
# USO:
#   ./build-dist.sh [opcion]
#
# OPCIONES:
#   jar      - Solo crear JAR ejecutable
#   native   - Intentar GraalVM native-image
#   linux    - Crear paquete Linux con jpackage
#   windows  - Crear paquete Windows con jpackage (requiere Windows)
#   all      - Ejecutar todo
#
# ============================================================

set -e  # Salir si hay error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # Sin color

# Directorio del proyecto
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

echo -e "${GREEN}========================================"
echo "  BUILD - Módulo de Proveedores"
echo -e "========================================${NC}"
echo ""

# Función: Crear directorios
crear_directorios() {
    echo -e "${YELLOW}➤ Creando directorios...${NC}"
    mkdir -p bin dist instaladores
}

# Función: Compilar código fuente
compilar() {
    echo -e "${YELLOW}➤ Compilando código fuente...${NC}"
    javac -d bin -encoding UTF-8 -cp "lib/*" \
        src/modelo/*.java \
        src/util/*.java \
        src/dao/*.java \
        src/servicio/*.java \
        src/vista/*.java \
        src/Main.java
    echo -e "${GREEN}✓ Compilación exitosa${NC}"
}

# Función: Crear JAR
crear_jar() {
    echo -e "${YELLOW}➤ Creando JAR ejecutable...${NC}"
    
    # Crear MANIFEST si no existe
    if [ ! -f MANIFEST.MF ]; then
        echo "Manifest-Version: 1.0" > MANIFEST.MF
        echo "Main-Class: Main" >> MANIFEST.MF
        echo "Class-Path: lib/mysql-connector-j-9.1.0.jar" >> MANIFEST.MF
        echo "" >> MANIFEST.MF
    fi
    
    jar cvfm dist/ModuloProveedores.jar MANIFEST.MF -C bin .
    
    # Copiar dependencias
    cp -r lib dist/
    
    echo -e "${GREEN}✓ JAR creado: dist/ModuloProveedores.jar${NC}"
}

# Función: Intentar GraalVM native-image
crear_native() {
    echo -e "${YELLOW}➤ Intentando GraalVM native-image...${NC}"
    
    if ! command -v native-image &> /dev/null; then
        echo -e "${RED}✗ native-image no encontrado.${NC}"
        echo "  Instálalo con: sudo pacman -S jdk21-graalvm-bin"
        echo "  O: yay -S graalvm-ce-bin"
        return 1
    fi
    
    echo -e "${YELLOW}  NOTA: Swing tiene soporte limitado en GraalVM.${NC}"
    echo "  Esto puede fallar o producir un binario incompleto."
    echo ""
    
    native-image \
        -cp "dist/ModuloProveedores.jar:lib/*" \
        --no-fallback \
        -H:+ReportExceptionStackTraces \
        Main \
        -o dist/modulo-proveedores-native \
        || {
            echo -e "${RED}✗ GraalVM native-image falló (esperado con Swing)${NC}"
            return 1
        }
    
    echo -e "${GREEN}✓ Binario nativo creado: dist/modulo-proveedores-native${NC}"
}

# Función: Crear paquete Linux con jpackage
crear_linux() {
    echo -e "${YELLOW}➤ Creando paquete Linux con jpackage...${NC}"
    
    if ! command -v jpackage &> /dev/null; then
        echo -e "${RED}✗ jpackage no encontrado. Requiere JDK 14+${NC}"
        return 1
    fi
    
    # Crear paquete app-image (carpeta portable)
    jpackage \
        --input dist \
        --dest instaladores \
        --name "ModuloProveedores" \
        --app-version "1.0.0" \
        --vendor "Tu Empresa" \
        --description "Sistema de Gestión de Proveedores" \
        --main-jar ModuloProveedores.jar \
        --main-class Main \
        --type app-image \
        --java-options "-Dfile.encoding=UTF-8"
    
    echo -e "${GREEN}✓ Paquete Linux creado: instaladores/ModuloProveedores/${NC}"
    
    # Opcional: Crear .tar.gz para distribución
    echo -e "${YELLOW}➤ Creando archivo .tar.gz...${NC}"
    cd instaladores
    tar -czvf ModuloProveedores-linux-x64.tar.gz ModuloProveedores/
    cd ..
    
    echo -e "${GREEN}✓ Archivo: instaladores/ModuloProveedores-linux-x64.tar.gz${NC}"
}

# Función: Crear paquete Windows (desde Linux - limitado)
crear_windows() {
    echo -e "${YELLOW}➤ Preparando distribución para Windows...${NC}"
    
    # Desde Linux solo podemos preparar la estructura
    # El exe real requiere ejecutar jpackage en Windows
    
    mkdir -p instaladores/windows
    cp dist/ModuloProveedores.jar instaladores/windows/
    cp -r dist/lib instaladores/windows/
    
    # Crear script .bat para Windows
    cat > instaladores/windows/ejecutar.bat << 'EOF'
@echo off
java -cp "ModuloProveedores.jar;lib\*" Main
pause
EOF

    # Crear README para Windows
    cat > instaladores/windows/README.txt << 'EOF'
MÓDULO DE PROVEEDORES - Versión Windows
========================================

REQUISITOS:
- Java 11 o superior instalado
- MySQL/MariaDB corriendo localmente

EJECUCIÓN:
1. Doble clic en ejecutar.bat
   O
2. Abrir CMD y ejecutar: java -jar ModuloProveedores.jar

NOTA: Para crear un .exe nativo, ejecute en Windows:
jpackage --input . --dest output --name ModuloProveedores ^
  --main-jar ModuloProveedores.jar --main-class Main --type exe
EOF

    # Crear zip para Windows
    cd instaladores
    zip -r ModuloProveedores-windows.zip windows/
    cd ..
    
    echo -e "${GREEN}✓ Distribución Windows: instaladores/ModuloProveedores-windows.zip${NC}"
    echo -e "${YELLOW}  NOTA: Para crear .exe nativo, ejecuta jpackage en Windows${NC}"
}

# Función: Mostrar ayuda
mostrar_ayuda() {
    echo "Uso: $0 [opcion]"
    echo ""
    echo "Opciones:"
    echo "  jar      - Solo crear JAR ejecutable"
    echo "  native   - Intentar GraalVM native-image"
    echo "  linux    - Crear paquete Linux con jpackage"
    echo "  windows  - Preparar distribución Windows"
    echo "  all      - Ejecutar todo"
    echo ""
    echo "Sin argumentos: muestra esta ayuda"
}

# ============================================================
# MAIN
# ============================================================

case "${1:-help}" in
    jar)
        crear_directorios
        compilar
        crear_jar
        ;;
    native)
        crear_directorios
        compilar
        crear_jar
        crear_native
        ;;
    linux)
        crear_directorios
        compilar
        crear_jar
        crear_linux
        ;;
    windows)
        crear_directorios
        compilar
        crear_jar
        crear_windows
        ;;
    all)
        crear_directorios
        compilar
        crear_jar
        crear_native || true  # Continuar si falla
        crear_linux
        crear_windows
        ;;
    *)
        mostrar_ayuda
        ;;
esac

echo ""
echo -e "${GREEN}========================================"
echo "  BUILD COMPLETADO"
echo -e "========================================${NC}"

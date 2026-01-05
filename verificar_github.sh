#!/bin/bash

echo "=========================================="
echo "Verificación Pre-GitHub"
echo "=========================================="
echo ""

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Contador de problemas
PROBLEMAS=0

# 1. Verificar que no haya archivos sensibles sin ignorar
echo "1. Verificando archivos sensibles..."
if git ls-files | grep -q "CREDENCIALES_BASE_DATOS.txt"; then
    echo -e "${RED}✗ CREDENCIALES_BASE_DATOS.txt está en el repositorio${NC}"
    PROBLEMAS=$((PROBLEMAS + 1))
else
    echo -e "${GREEN}✓ CREDENCIALES_BASE_DATOS.txt está ignorado${NC}"
fi

if git ls-files | grep -q "RESUMEN_CAMBIO_CONTRASEÑAS.txt"; then
    echo -e "${RED}✗ RESUMEN_CAMBIO_CONTRASEÑAS.txt está en el repositorio${NC}"
    PROBLEMAS=$((PROBLEMAS + 1))
else
    echo -e "${GREEN}✓ RESUMEN_CAMBIO_CONTRASEÑAS.txt está ignorado${NC}"
fi

echo ""

# 2. Verificar que existan archivos importantes
echo "2. Verificando archivos importantes..."
ARCHIVOS_IMPORTANTES=(
    "README.md"
    "CHANGELOG.md"
    "LICENSE"
    "CONTRIBUTING.md"
    "SECURITY.md"
    ".gitignore"
)

for archivo in "${ARCHIVOS_IMPORTANTES[@]}"; do
    if [ -f "$archivo" ]; then
        echo -e "${GREEN}✓ $archivo existe${NC}"
    else
        echo -e "${RED}✗ $archivo NO existe${NC}"
        PROBLEMAS=$((PROBLEMAS + 1))
    fi
done

echo ""

# 3. Verificar que el código compile
echo "3. Verificando compilación..."
if bash compilar.sh > /dev/null 2>&1; then
    echo -e "${GREEN}✓ El código compila correctamente${NC}"
else
    echo -e "${RED}✗ Error al compilar el código${NC}"
    PROBLEMAS=$((PROBLEMAS + 1))
fi

echo ""

# 4. Verificar estructura de directorios
echo "4. Verificando estructura..."
DIRECTORIOS=(
    "src"
    "db"
    "lib"
)

for dir in "${DIRECTORIOS[@]}"; do
    if [ -d "$dir" ]; then
        echo -e "${GREEN}✓ Directorio $dir/ existe${NC}"
    else
        echo -e "${RED}✗ Directorio $dir/ NO existe${NC}"
        PROBLEMAS=$((PROBLEMAS + 1))
    fi
done

echo ""

# 5. Verificar que bin/ esté ignorado
echo "5. Verificando .gitignore..."
if grep -q "^bin/" .gitignore; then
    echo -e "${GREEN}✓ bin/ está en .gitignore${NC}"
else
    echo -e "${YELLOW}⚠ bin/ NO está en .gitignore${NC}"
fi

if grep -q "CREDENCIALES" .gitignore; then
    echo -e "${GREEN}✓ Archivos sensibles están en .gitignore${NC}"
else
    echo -e "${RED}✗ Archivos sensibles NO están en .gitignore${NC}"
    PROBLEMAS=$((PROBLEMAS + 1))
fi

echo ""

# 6. Verificar git
echo "6. Verificando Git..."
if git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Repositorio Git inicializado${NC}"
    
    # Ver estado
    CAMBIOS=$(git status --porcelain | wc -l)
    if [ $CAMBIOS -gt 0 ]; then
        echo -e "${YELLOW}⚠ Hay $CAMBIOS archivos con cambios sin commit${NC}"
    else
        echo -e "${GREEN}✓ No hay cambios pendientes${NC}"
    fi
else
    echo -e "${RED}✗ No es un repositorio Git${NC}"
    echo "  Ejecuta: git init"
    PROBLEMAS=$((PROBLEMAS + 1))
fi

echo ""

# Resumen
echo "=========================================="
if [ $PROBLEMAS -eq 0 ]; then
    echo -e "${GREEN}✓ TODO LISTO PARA GITHUB${NC}"
    echo "=========================================="
    echo ""
    echo "Próximos pasos:"
    echo "1. git add ."
    echo "2. git commit -m 'Release v2.3.0'"
    echo "3. git remote add origin https://github.com/TU-USUARIO/ModuloProveedores.git"
    echo "4. git push -u origin main"
    echo ""
    echo "Ver PREPARAR_GITHUB.md para instrucciones detalladas"
else
    echo -e "${RED}✗ SE ENCONTRARON $PROBLEMAS PROBLEMAS${NC}"
    echo "=========================================="
    echo ""
    echo "Corrige los problemas antes de subir a GitHub"
fi
echo ""

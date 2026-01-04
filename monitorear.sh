#!/bin/bash
# ==========================================
# Script de Monitoreo de Recursos - Linux
# Aproud - Proveedores y Compras
# ==========================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Banner
echo ""
echo "=========================================="
echo "   MONITOR DE RECURSOS - APROUD"
echo "=========================================="
echo ""

# Buscar proceso Java
echo "Buscando proceso de Aproud..."
echo ""

PID=$(pgrep -f "java.*Main" | head -1)

if [ -z "$PID" ]; then
    echo -e "${RED}[ERROR]${NC} No se encontró el proceso de Aproud ejecutándose"
    echo ""
    echo "Asegúrate de que la aplicación esté ejecutándose:"
    echo "  bash ejecutar.sh"
    echo ""
    exit 1
fi

echo -e "${GREEN}[OK]${NC} Proceso encontrado - PID: $PID"
echo ""

# Crear archivo de log
LOGFILE="monitor_recursos_$(date +%Y%m%d_%H%M%S).txt"

echo "Iniciando monitoreo..."
echo -e "${YELLOW}Presiona Ctrl+C para detener${NC}"
echo ""
echo "Guardando datos en: $LOGFILE"
echo ""

# Encabezado del log
cat > "$LOGFILE" << EOF
==========================================
MONITOREO DE RECURSOS - APROUD
==========================================

Fecha: $(date)
PID: $PID
Sistema: $(uname -s) $(uname -r)
Hostname: $(hostname)

==========================================

EOF

# Función para limpiar al salir
cleanup() {
    echo ""
    echo ""
    echo "=========================================="
    echo "   MONITOREO FINALIZADO"
    echo "=========================================="
    echo ""
    
    # Calcular estadísticas
    SAMPLES=$(grep -c "Muestra #" "$LOGFILE")
    DURATION=$((SAMPLES * 2))
    
    cat >> "$LOGFILE" << EOF

==========================================
RESUMEN
==========================================
Total de muestras: $SAMPLES
Duración: ~$DURATION segundos
Finalizado: $(date)

EOF
    
    echo "Resumen:"
    echo "  Total de muestras: $SAMPLES"
    echo "  Duración: ~$DURATION segundos"
    echo "  Archivo de log: $LOGFILE"
    echo ""
    
    # Generar estadísticas
    echo "Generando estadísticas..."
    
    # Extraer valores de RAM
    RAM_VALUES=$(grep "RSS:" "$LOGFILE" | awk '{print $2}')
    
    if [ ! -z "$RAM_VALUES" ]; then
        RAM_MIN=$(echo "$RAM_VALUES" | sort -n | head -1)
        RAM_MAX=$(echo "$RAM_VALUES" | sort -n | tail -1)
        RAM_AVG=$(echo "$RAM_VALUES" | awk '{sum+=$1} END {printf "%.0f", sum/NR}')
        
        cat >> "$LOGFILE" << EOF
ESTADÍSTICAS DE RAM:
  Mínimo: $RAM_MIN MB
  Máximo: $RAM_MAX MB
  Promedio: $RAM_AVG MB

EOF
        
        echo ""
        echo "Estadísticas de RAM:"
        echo "  Mínimo: $RAM_MIN MB"
        echo "  Máximo: $RAM_MAX MB"
        echo "  Promedio: $RAM_AVG MB"
    fi
    
    echo ""
    echo -e "${GREEN}Resultados guardados en: $LOGFILE${NC}"
    echo ""
    
    exit 0
}

# Capturar Ctrl+C
trap cleanup SIGINT SIGTERM

# Contador
COUNT=0

# Loop de monitoreo
while true; do
    COUNT=$((COUNT + 1))
    TIMESTAMP=$(date +"%H:%M:%S")
    
    # Verificar si el proceso sigue ejecutándose
    if ! kill -0 $PID 2>/dev/null; then
        echo ""
        echo -e "${YELLOW}[INFO]${NC} El proceso ha finalizado"
        echo "" >> "$LOGFILE"
        echo "[INFO] El proceso ha finalizado" >> "$LOGFILE"
        cleanup
    fi
    
    # Obtener información del proceso
    echo "[$TIMESTAMP] Muestra #$COUNT"
    echo "[$TIMESTAMP] Muestra #$COUNT" >> "$LOGFILE"
    echo "------------------------------------------" >> "$LOGFILE"
    
    # Información básica con ps
    PS_INFO=$(ps -p $PID -o %cpu,%mem,vsz,rss,cmd --no-headers)
    
    if [ ! -z "$PS_INFO" ]; then
        CPU=$(echo "$PS_INFO" | awk '{print $1}')
        MEM=$(echo "$PS_INFO" | awk '{print $2}')
        VSZ=$(echo "$PS_INFO" | awk '{print $3}')
        RSS=$(echo "$PS_INFO" | awk '{print $4}')
        
        # Convertir a MB
        VSZ_MB=$((VSZ / 1024))
        RSS_MB=$((RSS / 1024))
        
        # Mostrar en pantalla
        echo "  CPU: ${CPU}%"
        echo "  RAM: ${MEM}% (${RSS_MB} MB)"
        
        # Guardar en log
        cat >> "$LOGFILE" << EOF
CPU: ${CPU}%
RAM: ${MEM}%
VSZ: ${VSZ_MB} MB
RSS: ${RSS_MB} MB

EOF
    fi
    
    # Información detallada con top
    TOP_INFO=$(top -b -n 1 -p $PID 2>/dev/null | tail -1)
    if [ ! -z "$TOP_INFO" ]; then
        echo "Detalle TOP:" >> "$LOGFILE"
        echo "$TOP_INFO" >> "$LOGFILE"
        echo "" >> "$LOGFILE"
    fi
    
    # Información de memoria con pmap (si está disponible)
    if command -v pmap &> /dev/null; then
        PMAP_TOTAL=$(pmap -x $PID 2>/dev/null | tail -1 | awk '{print $3}')
        if [ ! -z "$PMAP_TOTAL" ]; then
            PMAP_MB=$((PMAP_TOTAL / 1024))
            echo "Total mapeado: ${PMAP_MB} MB" >> "$LOGFILE"
            echo "" >> "$LOGFILE"
        fi
    fi
    
    # Esperar 2 segundos
    sleep 2
done

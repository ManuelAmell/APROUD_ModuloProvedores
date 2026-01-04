# 📊 Scripts de Monitoreo de Recursos

Scripts para monitorear el consumo de recursos de la aplicación Aproud en tiempo real.

---

## 🪟 Windows - monitorear.bat

### Uso

```cmd
REM 1. Ejecutar la aplicación
ejecutar.bat

REM 2. En otra ventana CMD, ejecutar el monitor
monitorear.bat
```

### Características

- ✅ Detecta automáticamente el proceso Java
- ✅ Monitorea RAM en tiempo real
- ✅ Muestra información cada 2 segundos
- ✅ Guarda log detallado con timestamp
- ✅ Genera archivo de log automáticamente
- ✅ Presiona Ctrl+C para detener

### Información Monitoreada

- **WorkingSetSize:** Memoria RAM usada
- **PageFileUsage:** Uso de archivo de paginación
- **ThreadCount:** Número de hilos
- **HandleCount:** Número de handles

### Archivo de Log

Formato: `monitor_recursos_YYYYMMDD_HHMMSS.txt`

Ejemplo: `monitor_recursos_20260104_083000.txt`

---

## 🐧 Linux - monitorear.sh

### Uso

```bash
# 1. Ejecutar la aplicación
bash ejecutar.sh

# 2. En otra terminal, ejecutar el monitor
bash monitorear.sh
```

### Características

- ✅ Detecta automáticamente el proceso Java
- ✅ Monitorea CPU y RAM en tiempo real
- ✅ Muestra información cada 2 segundos
- ✅ Guarda log detallado con timestamp
- ✅ Genera estadísticas al finalizar
- ✅ Colores en terminal
- ✅ Presiona Ctrl+C para detener

### Información Monitoreada

- **CPU:** Porcentaje de uso de CPU
- **RAM:** Porcentaje y MB de RAM usada
- **VSZ:** Tamaño virtual (MB)
- **RSS:** Memoria residente (MB)
- **TOP:** Información detallada del proceso
- **PMAP:** Mapa de memoria (si disponible)

### Archivo de Log

Formato: `monitor_recursos_YYYYMMDD_HHMMSS.txt`

Ejemplo: `monitor_recursos_20260104_083000.txt`

### Estadísticas Generadas

Al finalizar, el script calcula:
- **RAM Mínima:** Menor consumo registrado
- **RAM Máxima:** Mayor consumo registrado
- **RAM Promedio:** Consumo promedio
- **Total de muestras:** Número de mediciones
- **Duración:** Tiempo total de monitoreo

---

## 📋 Ejemplo de Uso

### Escenario 1: Monitoreo Básico

**Windows:**
```cmd
REM Terminal 1
ejecutar.bat

REM Terminal 2
monitorear.bat
REM Esperar 1-2 minutos
REM Presionar Ctrl+C
```

**Linux:**
```bash
# Terminal 1
bash ejecutar.sh

# Terminal 2
bash monitorear.sh
# Esperar 1-2 minutos
# Presionar Ctrl+C
```

### Escenario 2: Monitoreo Durante Uso Intensivo

1. Ejecutar la aplicación
2. Iniciar el monitor
3. Usar la aplicación intensivamente:
   - Agregar varios proveedores
   - Registrar múltiples compras
   - Aplicar filtros
   - Buscar datos
4. Detener el monitor
5. Revisar estadísticas

### Escenario 3: Monitoreo de Estabilidad

1. Ejecutar la aplicación
2. Iniciar el monitor
3. Dejar la aplicación en reposo por 10-30 minutos
4. Detener el monitor
5. Verificar que no haya memory leaks

---

## 📊 Interpretación de Resultados

### RAM (Memoria)

**Valores Normales:**
- **Inicio:** 100-150 MB
- **Uso normal:** 120-180 MB
- **Uso intensivo:** 150-250 MB

**Señales de Alerta:**
- ⚠️ Crecimiento constante sin estabilizarse
- ⚠️ Más de 500 MB en uso normal
- ⚠️ Incremento continuo sin liberar memoria

### CPU

**Valores Normales:**
- **Inicio:** 20-40%
- **Reposo:** 0-5%
- **Uso normal:** 5-15%
- **Operaciones intensivas:** 15-30%

**Señales de Alerta:**
- ⚠️ Más de 50% en reposo
- ⚠️ 100% constante
- ⚠️ No baja después de operaciones

---

## 🔍 Análisis de Logs

### Estructura del Log

```
==========================================
MONITOREO DE RECURSOS - APROUD
==========================================

Fecha: 2026-01-04 08:30:00
PID: 12345

==========================================

[08:30:00] Muestra #1
------------------------------------------
CPU: 15.2%
RAM: 1.8%
VSZ: 5726 MB
RSS: 125 MB

[08:30:02] Muestra #2
------------------------------------------
CPU: 3.5%
RAM: 1.8%
VSZ: 5726 MB
RSS: 126 MB

...

==========================================
RESUMEN
==========================================
Total de muestras: 30
Duración: ~60 segundos

ESTADÍSTICAS DE RAM:
  Mínimo: 125 MB
  Máximo: 135 MB
  Promedio: 128 MB
```

### Qué Buscar

1. **Tendencia de RAM:**
   - ✅ Estable: Bueno
   - ⚠️ Creciente: Posible memory leak
   - ✅ Fluctuante: Normal

2. **Uso de CPU:**
   - ✅ Bajo en reposo: Eficiente
   - ⚠️ Alto constante: Problema

3. **Estabilidad:**
   - ✅ Valores consistentes: Estable
   - ⚠️ Variaciones extremas: Investigar

---

## 🛠️ Solución de Problemas

### Windows

**Error: "No se encontró el proceso"**
```cmd
REM Verificar que la aplicación esté ejecutándose
tasklist | findstr java.exe

REM Si no aparece, ejecutar:
ejecutar.bat
```

**Error: "WMIC no disponible"**
```cmd
REM Usar Task Manager manualmente
REM O instalar herramientas de administración
```

### Linux

**Error: "No se encontró el proceso"**
```bash
# Verificar que la aplicación esté ejecutándose
ps aux | grep java

# Si no aparece, ejecutar:
bash ejecutar.sh
```

**Error: "pmap: command not found"**
```bash
# El script funciona sin pmap
# Para instalarlo (opcional):
sudo apt-get install procps  # Debian/Ubuntu
sudo yum install procps-ng   # CentOS/RHEL
```

---

## 📈 Casos de Uso

### 1. Verificar Consumo Inicial

**Objetivo:** Medir recursos al iniciar

**Pasos:**
1. Ejecutar aplicación
2. Iniciar monitor inmediatamente
3. Esperar 30 segundos
4. Detener monitor
5. Revisar primeras muestras

### 2. Detectar Memory Leaks

**Objetivo:** Verificar si hay fugas de memoria

**Pasos:**
1. Ejecutar aplicación
2. Iniciar monitor
3. Usar aplicación intensivamente por 10 minutos
4. Dejar en reposo 5 minutos
5. Detener monitor
6. Verificar si RAM se estabiliza

### 3. Benchmark de Operaciones

**Objetivo:** Medir impacto de operaciones

**Pasos:**
1. Ejecutar aplicación
2. Iniciar monitor
3. Realizar operación específica (ej: agregar 100 compras)
4. Observar picos de CPU/RAM
5. Detener monitor
6. Analizar resultados

### 4. Prueba de Estabilidad

**Objetivo:** Verificar estabilidad a largo plazo

**Pasos:**
1. Ejecutar aplicación
2. Iniciar monitor
3. Dejar ejecutándose 1-2 horas
4. Detener monitor
5. Verificar tendencias

---

## 📝 Recomendaciones

### Para Desarrollo

1. **Monitorear después de cambios importantes**
   - Nuevas características
   - Optimizaciones
   - Correcciones de bugs

2. **Comparar versiones**
   - Guardar logs de diferentes versiones
   - Comparar consumo de recursos
   - Identificar regresiones

3. **Pruebas de carga**
   - Agregar muchos datos
   - Monitorear comportamiento
   - Optimizar si es necesario

### Para Producción

1. **Monitoreo periódico**
   - Semanal o mensual
   - Detectar degradación
   - Planificar optimizaciones

2. **Documentar resultados**
   - Guardar logs históricos
   - Crear gráficas de tendencias
   - Reportar anomalías

3. **Establecer baselines**
   - Definir valores normales
   - Alertar sobre desviaciones
   - Actuar proactivamente

---

## 🎯 Métricas Objetivo

### Consumo de RAM

| Escenario | Objetivo | Aceptable | Crítico |
|-----------|----------|-----------|---------|
| Inicio | <150 MB | <200 MB | >300 MB |
| Reposo | <180 MB | <250 MB | >400 MB |
| Uso intensivo | <250 MB | <350 MB | >500 MB |

### Uso de CPU

| Escenario | Objetivo | Aceptable | Crítico |
|-----------|----------|-----------|---------|
| Inicio | <30% | <50% | >70% |
| Reposo | <5% | <10% | >20% |
| Uso normal | <15% | <25% | >40% |

---

## 🔗 Archivos Relacionados

- `CONSUMO_RECURSOS.md` - Análisis detallado de recursos
- `ejecutar.bat` / `ejecutar.sh` - Scripts de ejecución
- `compilar.bat` / `compilar.sh` - Scripts de compilación

---

## ✅ Checklist de Monitoreo

Antes de liberar una versión:

- [ ] Ejecutar monitoreo de inicio (30 segundos)
- [ ] Ejecutar monitoreo de uso normal (5 minutos)
- [ ] Ejecutar monitoreo de uso intensivo (10 minutos)
- [ ] Ejecutar prueba de estabilidad (30 minutos)
- [ ] Verificar que RAM se estabilice
- [ ] Verificar que CPU baje en reposo
- [ ] Comparar con versión anterior
- [ ] Documentar resultados
- [ ] Guardar logs para referencia

---

**Los scripts de monitoreo son herramientas esenciales para mantener la calidad y eficiencia de la aplicación.**

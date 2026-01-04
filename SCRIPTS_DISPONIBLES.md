# Scripts Disponibles

Listado de todos los scripts de automatización del proyecto.

---

## 🪟 Windows

### compilar.bat
**Propósito:** Compilar el proyecto completo

**Uso:**
```cmd
compilar.bat
```

**Qué hace:**
1. Limpia el directorio `bin/`
2. Compila clases base (modelo, util)
3. Compila DAO y servicios
4. Compila vistas y Main
5. Muestra resultado de compilación

**Cuándo usar:**
- Después de modificar código fuente
- Después de cambiar configuración
- Al clonar el proyecto por primera vez
- Antes de crear un instalador

---

### ejecutar.bat
**Propósito:** Ejecutar la aplicación

**Uso:**
```cmd
ejecutar.bat
```

**Qué hace:**
1. Verifica que exista el directorio `bin/`
2. Ejecuta la aplicación con las librerías necesarias
3. Muestra errores si los hay

**Cuándo usar:**
- Para iniciar la aplicación
- Después de compilar
- Para probar cambios

---

### monitorear.bat
**Propósito:** Monitorear consumo de recursos en tiempo real

**Uso:**
```cmd
REM Terminal 1: Ejecutar aplicación
ejecutar.bat

REM Terminal 2: Monitorear recursos
monitorear.bat
```

**Qué hace:**
1. Detecta automáticamente el proceso Java
2. Monitorea RAM cada 2 segundos
3. Guarda log detallado con timestamp
4. Genera archivo de resultados
5. Presiona Ctrl+C para detener

**Información monitoreada:**
- WorkingSetSize (RAM usada)
- PageFileUsage (Archivo de paginación)
- ThreadCount (Número de hilos)
- HandleCount (Número de handles)

**Cuándo usar:**
- Verificar consumo de recursos
- Detectar memory leaks
- Optimizar rendimiento
- Pruebas de estabilidad

---

## 🐧 Linux

### compilar.sh
**Propósito:** Compilar el proyecto completo

**Uso:**
```bash
bash compilar.sh
```

**Qué hace:**
1. Limpia el directorio `bin/`
2. Compila clases base (modelo, util)
3. Compila DAO y servicios
4. Compila vistas y Main
5. Muestra resultado con colores

**Cuándo usar:**
- Después de modificar código fuente
- Después de cambiar configuración
- Al clonar el proyecto por primera vez

---

### ejecutar.sh
**Propósito:** Ejecutar la aplicación

**Uso:**
```bash
bash ejecutar.sh
```

**Qué hace:**
1. Verifica que exista el directorio `bin/`
2. Ejecuta la aplicación con las librerías necesarias
3. Muestra errores si los hay

**Cuándo usar:**
- Para iniciar la aplicación
- Después de compilar
- Para probar cambios

---

### monitorear.sh
**Propósito:** Monitorear consumo de recursos en tiempo real

**Uso:**
```bash
# Terminal 1: Ejecutar aplicación
bash ejecutar.sh

# Terminal 2: Monitorear recursos
bash monitorear.sh
```

**Qué hace:**
1. Detecta automáticamente el proceso Java
2. Monitorea CPU y RAM cada 2 segundos
3. Guarda log detallado con timestamp
4. Genera estadísticas al finalizar
5. Muestra información con colores
6. Presiona Ctrl+C para detener

**Información monitoreada:**
- CPU (Porcentaje de uso)
- RAM (Porcentaje y MB)
- VSZ (Tamaño virtual)
- RSS (Memoria residente)
- TOP (Información detallada)
- PMAP (Mapa de memoria)

**Estadísticas generadas:**
- RAM Mínima
- RAM Máxima
- RAM Promedio
- Total de muestras
- Duración del monitoreo

**Cuándo usar:**
- Verificar consumo de recursos
- Detectar memory leaks
- Optimizar rendimiento
- Pruebas de estabilidad
- Benchmark de operaciones

---

### setup_database.sh
**Propósito:** Configurar la base de datos automáticamente

**Uso:**
```bash
bash setup_database.sh
```

**Qué hace:**
1. Solicita credenciales de MySQL
2. Crea la base de datos `gestion_proveedores`
3. Ejecuta el script `db/schema.sql`
4. Verifica la instalación

**Cuándo usar:**
- Primera instalación
- Reinstalar base de datos
- Después de errores de BD

---

## 📊 Comparación

| Característica | Windows | Linux |
|---------------|---------|-------|
| Compilar | `compilar.bat` | `compilar.sh` |
| Ejecutar | `ejecutar.bat` | `ejecutar.sh` |
| Monitorear | `monitorear.bat` | `monitorear.sh` |
| Setup BD | Manual | `setup_database.sh` |
| Colores | No | Sí |
| Pausas | Sí | No |
| Estadísticas | Básicas | Avanzadas |

---

## 🔧 Flujo de Trabajo Típico

### Primera Instalación (Windows)

```cmd
REM 1. Configurar base de datos
mysql -u root -p < db\schema.sql

REM 2. Compilar (si es necesario)
compilar.bat

REM 3. Ejecutar
ejecutar.bat
```

### Primera Instalación (Linux)

```bash
# 1. Configurar base de datos
bash setup_database.sh

# 2. Compilar
bash compilar.sh

# 3. Ejecutar
bash ejecutar.sh
```

### Desarrollo (Ambos)

```
1. Modificar código
2. Ejecutar script de compilación
3. Ejecutar script de ejecución
4. Probar cambios
5. Repetir
```

---

## ⚠️ Notas Importantes

### Windows
- Los scripts `.bat` deben ejecutarse desde CMD
- No funcionan en PowerShell (usar `cmd /c compilar.bat`)
- Requieren que Java esté en el PATH
- Pausan al final para ver resultados

### Linux
- Los scripts `.sh` requieren bash
- Usar `bash script.sh` no `sh script.sh`
- Requieren permisos de ejecución (opcional)
- No pausan, usar `; read` si se necesita

### Ambos
- Verificar que `lib/mysql-connector-j-9.1.0.jar` exista
- Verificar que MySQL esté ejecutándose
- Verificar configuración en `ConexionBD.java`

---

## 🐛 Solución de Problemas

### "javac no se reconoce como comando"
**Solución:** Agregar Java al PATH del sistema

### "Error al compilar"
**Solución:** 
1. Verificar versión de Java (JDK 11+)
2. Verificar que exista `lib/mysql-connector-j-9.1.0.jar`
3. Revisar errores específicos en la salida

### "No se puede conectar a la base de datos"
**Solución:**
1. Verificar que MySQL esté ejecutándose
2. Verificar credenciales en `ConexionBD.java`
3. Ejecutar script de base de datos

---

## 📝 Crear Nuevos Scripts

### Windows (.bat)
```batch
@echo off
echo Haciendo algo...
REM Tu código aquí
pause
```

### Linux (.sh)
```bash
#!/bin/bash
echo "Haciendo algo..."
# Tu código aquí
```

---

## ✅ Checklist de Scripts

- [x] compilar.bat (Windows)
- [x] ejecutar.bat (Windows)
- [x] monitorear.bat (Windows) - Nuevo
- [x] compilar.sh (Linux)
- [x] ejecutar.sh (Linux)
- [x] monitorear.sh (Linux) - Nuevo
- [x] setup_database.sh (Linux)
- [ ] setup_database.bat (Windows) - Pendiente

---

**Todos los scripts están listos para usar** ✅

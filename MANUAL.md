# 📘 Manual de Usuario - Sistema de Gestión de Proveedores y Compras

**Versión:** 2.2.5  
**Fecha:** Enero 2026

---

## 📋 Tabla de Contenidos

1. [Instalación](#instalación)
2. [Configuración Inicial](#configuración-inicial)
3. [Uso del Sistema](#uso-del-sistema)
4. [Características Principales](#características-principales)
5. [Solución de Problemas](#solución-de-problemas)

---

## 🚀 Instalación

### Requisitos Previos

- **Java JDK 11 o superior**
- **MySQL 8.0 o superior**
- **Sistema Operativo:** Windows, Linux o macOS

### Instalación en Windows

1. **Descargar el instalador:**
   - Ubicación: `instaladores/ModuloProveedores-windows.zip`
   - Extraer el contenido en una carpeta

2. **Configurar la base de datos:**
   ```cmd
   mysql -u root -p < db/schema.sql
   ```

3. **Ejecutar la aplicación:**
   - Doble clic en `ejecutar.bat`
   - O desde CMD: `ejecutar.bat`

### Instalación en Linux

1. **Configurar la base de datos:**
   ```bash
   bash setup_database.sh
   ```

2. **Ejecutar la aplicación:**
   ```bash
   bash ejecutar.sh
   ```

---

## ⚙️ Configuración Inicial

### Configuración de Base de Datos

El archivo `src/util/ConexionBD.java` contiene la configuración:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_proveedores";
private static final String USUARIO = "root";
private static final String PASSWORD = "tu_password";
```

**Modificar según tu configuración:**
1. Cambiar `PASSWORD` por tu contraseña de MySQL
2. Si usas otro puerto, cambiar `3306`
3. Recompilar:
   - **Windows:** `compilar.bat`
   - **Linux:** `bash compilar.sh`

---

## 💻 Uso del Sistema

### Pantalla Principal

La interfaz está dividida en 3 secciones:

```
┌─────────────┬──────────────────────────────────┐
│             │  Panel de Compras                │
│ Proveedores │  - Búsqueda y filtros            │
│             │  - Tabla de compras              │
│             │  - Botones de acción             │
├─────────────┴──────────────────────────────────┤
│  Estadísticas Generales                        │
└────────────────────────────────────────────────┘
```

---

## 🎯 Características Principales

### 1. Gestión de Proveedores

#### Agregar Proveedor
1. Clic en **"+ Nuevo Proveedor"**
2. Llenar los campos:
   - **Nombre** (obligatorio)
   - **Tipo** (opcional): Distribuidor, Mayorista, etc.
   - **Contacto** (opcional)
   - **Teléfono** (opcional)
   - **Email** (opcional)
   - **Dirección** (opcional)
3. Clic en **"Guardar"**

#### Buscar Proveedor
- Escribir en la barra de búsqueda superior
- Filtra en tiempo real por nombre

#### Editar Proveedor
1. Seleccionar proveedor de la lista
2. Clic en **"✎ Editar"**
3. Modificar campos
4. Clic en **"Guardar"**

---

### 2. Gestión de Compras

#### Agregar Nueva Compra
1. Seleccionar un proveedor
2. Clic en **"+ Nueva Compra"**
3. Llenar el formulario:

**Datos Generales:**
- **Nº Factura:** Número de la factura *
- **Fecha Compra:** Formato dd/mm/aa (ej: 03/01/26) *
- **Categoría:** Seleccionar o crear nueva (escribir "otros") *

**Opción 1: Compra Simple (sin items)**
1. Dejar desmarcado "☐ Inscribir productos"
2. Al guardar, se pedirá:
   - Descripción de la compra
   - Total de la compra

**Opción 2: Compra con Items (múltiples productos)**
1. Marcar **☑ Inscribir productos**
2. Se mostrará la sección de items

**Agregar Items:**
   - **Cant:** Cantidad de unidades
   - **Descripción:** Nombre del producto (ej: Arroz 50kg)
   - **Código:** Código del producto (opcional)
   - **Precio:** Precio unitario (formato: 10.000)
   - Clic en **"+ Agregar Item"**

**Gestionar Items:**
   - Los items aparecen en la tabla con líneas visibles
   - Para eliminar: seleccionar item y clic en **"- Eliminar Item"**
   - El **TOTAL** se calcula automáticamente sumando subtotales

**Ejemplo de Items:**
   ```
   Cant  Descripción      Código    P.Unit      Subtotal
   ────  ──────────────   ──────    ─────────   ─────────
   2     Arroz 50kg       A-001     $50.000     $100.000
   5     Aceite 1L        A-002     $12.000     $ 60.000
   10    Azúcar 1kg       A-003     $ 3.500     $ 35.000
                                    ─────────────────────
                                    TOTAL:      $195.000
   ```

**Forma de Pago:**
- **Forma de Pago:** Efectivo, Transferencia o Crédito *

3. **Para Efectivo/Transferencia:**
   - Marcar ☑ "Marcar como pagado" si ya está pagado
   - Ingresar fecha de pago

4. **Para Crédito:**
   - Seleccionar estado: Pendiente o Pagado
   - Si está pagado, ingresar fecha de pago

5. Clic en **"Guardar"**

**Nota:** Si activa "Inscribir productos", debe agregar al menos un item.

#### Formato de Números
Los campos numéricos se formatean automáticamente:
- **Escribes:** 1000000
- **Se muestra:** 1.000.000
- **Con decimales:** 1.500.000,50

#### Editar Compra
1. Seleccionar compra de la tabla
2. Clic en **"✎ Editar"**
3. Modificar items (agregar, eliminar o cambiar)
4. El total se recalcula automáticamente
4. Clic en **"Guardar"**

#### Marcar como Pagado
1. Seleccionar compra pendiente
2. Clic en **"✓ Marcar Pagado"**
3. Ingresar fecha de pago
4. Confirmar

---

### 3. Búsqueda y Filtros

#### Búsqueda de Compras
Barra superior: busca por factura, categoría o descripción

#### Filtros Avanzados

**Por Forma de Pago:**
- Todos
- Efectivo
- Transferencia
- Crédito

**Por Estado:**
- Todos
- Pendiente (rojo)
- Pagado (verde)

**Por Rango de Fechas:**
- **Desde:** dd/mm/aa
- **Hasta:** dd/mm/aa

**Limpiar Filtros:**
- Clic en **"✕ Limpiar"** para resetear todos los filtros

---

### 4. Categorías Personalizadas

#### Crear Nueva Categoría
1. En el formulario de compra
2. Seleccionar **"otros"** en Categoría
3. Aparece campo de texto
4. Escribir nombre (ej: "herramientas", "pocillos")
5. La categoría se guarda automáticamente

#### Categorías Predefinidas
- alimentos
- bebidas
- limpieza
- oficina
- mantenimiento
- servicios
- otros

---

### 5. Visualización de Datos

#### Panel de Información del Proveedor
Muestra al seleccionar un proveedor:
- **Nombre** (morado)
- **Total:** Suma de todas las compras
- **Pendiente:** Suma de compras sin pagar (rojo)

#### Tabla de Compras
Columnas:
- **Factura:** Número de factura
- **Categoría:** Tipo de compra
- **Descripción:** Detalle
- **Cant.:** Cantidad (opcional)
- **P.Unit:** Precio unitario (opcional)
- **Total:** Monto total
- **Fecha:** Fecha de compra
- **Pago:** Forma de pago
- **Estado:** Pendiente (rojo) / Pagado (verde)
- **F.Pago:** Fecha de pago

#### Estadísticas Generales
Panel inferior muestra:
- **Total General:** Suma de todas las compras
- **Pendientes:** Total de compras sin pagar (rojo)

---

## 🎨 Códigos de Color

### Estados de Pago
- 🔴 **Rojo:** Pendiente de pago
- 🟢 **Verde:** Pagado
- 🟣 **Morado:** Nombres de proveedores
- ⚪ **Blanco:** Texto normal
- 🔵 **Azul:** Botones de acción

### Tema Oscuro
- Fondo principal: Azul oscuro
- Paneles: Azul medio
- Campos de entrada: Azul gris
- Texto: Blanco/Gris claro

---

## 🔧 Solución de Problemas

### Error de Conexión a Base de Datos

**Problema:** "No se puede conectar a la base de datos"

**Solución:**
1. Verificar que MySQL esté ejecutándose
2. Revisar credenciales en `ConexionBD.java`
3. Verificar que la base de datos exista:
   ```sql
   SHOW DATABASES;
   ```
4. Si no existe, ejecutar: `mysql -u root -p < db/schema.sql`

### Error al Compilar

**Problema:** "Error de compilación"

**Solución:**
1. Verificar versión de Java: `java -version`
2. Debe ser JDK 11 o superior
3. Verificar que `lib/mysql-connector-j-9.1.0.jar` exista
4. Recompilar:
   - **Windows:** `compilar.bat`
   - **Linux:** `bash compilar.sh`

### Campos Numéricos No Formatean

**Problema:** Los números no se formatean automáticamente

**Solución:**
1. Verificar que `CampoNumericoFormateado.java` esté compilado
2. Recompilar el proyecto completo:
   - **Windows:** `compilar.bat`
   - **Linux:** `bash compilar.sh`
3. Reiniciar la aplicación

### Filtros No Funcionan

**Problema:** Los filtros no muestran resultados

**Solución:**
1. Clic en **"✕ Limpiar"** para resetear
2. Verificar formato de fechas: dd/mm/aa
3. Verificar que haya compras del proveedor seleccionado

### Placeholder No Desaparece

**Problema:** El placeholder "dd/mm/aa" no desaparece

**Solución:**
1. Hacer clic dentro del campo
2. El placeholder debe desaparecer automáticamente
3. Si persiste, reiniciar la aplicación

---

## 📊 Formatos de Entrada

### Fechas
- **Formato:** dd/mm/aa o dd/MM/yyyy
- **Ejemplos válidos:**
  - 03/01/26
  - 03/01/2026
  - 15/12/25

### Números
- **Formato automático:** 1.000.000,50
- **Escribir:** Solo números y coma para decimales
- **Ejemplos:**
  - 1000000 → 1.000.000
  - 1500000,50 → 1.500.000,50
  - 50000 → 50.000

### Texto
- **Categorías:** Minúsculas automáticas
- **Descripción:** Texto libre
- **Factura:** Alfanumérico

---

## 🔐 Seguridad

### Respaldo de Base de Datos

**Crear respaldo:**
```bash
mysqldump -u root -p gestion_proveedores > backup.sql
```

**Restaurar respaldo:**
```bash
mysql -u root -p gestion_proveedores < backup.sql
```

### Recomendaciones
- Cambiar contraseña por defecto de MySQL
- Realizar respaldos periódicos
- No compartir credenciales de base de datos

---

## 📞 Soporte

### Archivos de Configuración
- **Base de datos:** `db/schema.sql`
- **Conexión:** `src/util/ConexionBD.java`
- **Ejecutables:** `ejecutar.bat` (Windows), `ejecutar.sh` (Linux)

### Logs y Errores
Los errores se muestran en:
- Consola de la aplicación
- Mensajes emergentes en la interfaz

### Versión Actual
**2.2.5** - Incluye:
- ✓ Campos numéricos formateados
- ✓ Saldo pendiente por proveedor
- ✓ Filtros avanzados
- ✓ Búsqueda en tiempo real
- ✓ Categorías personalizadas
- ✓ Tema oscuro elegante

---

## 📝 Notas Adicionales

### Atajos de Teclado
- **Tab:** Navegar entre campos
- **Enter:** Confirmar en diálogos
- **Esc:** Cancelar en diálogos

### Límites del Sistema
- Sin límite de proveedores
- Sin límite de compras
- Números hasta 999.999.999.999,99

### Actualizaciones Futuras
Para actualizar el sistema:
1. Respaldar base de datos
2. Reemplazar archivos
3. Ejecutar scripts de actualización si existen
4. Recompilar:
   - **Windows:** `compilar.bat`
   - **Linux:** `bash compilar.sh`

---

**© 2026 - Sistema de Gestión de Proveedores y Compras**

# 📋 Changelog

Historial de cambios del sistema.

---

## [2.3.0] - 2026-01-04

### ✨ Nuevo - Sistema de Items por Compra
- **Tabla editable estilo Excel**: Edición con un solo clic, navegación con Tab/Enter/flechas
- **DialogoItems mejorado**: Formato de factura tradicional con 8 columnas
- **Modo visualización**: Botón "Ver" para consultar productos en modo solo lectura
- **Validaciones robustas**: Control de tipos de datos, rangos y valores negativos
- **Cálculo automático**: Subtotales y total general se calculan en tiempo real
- **Formato de moneda**: Separadores de miles ($1.234.567)
- **Placeholders visuales**: Indicadores en celdas vacías
- **Campos editables**: Fecha y número de factura editables en el diálogo
- **Resumen de productos**: Contador de items inscritos y con datos
- **Reloj en tiempo real**: Hora y fecha en ventana principal (esquina superior derecha)
- **Indicador de versión**: v2.3.0 en esquina inferior izquierda
- **FormularioCompraDarkConItems**: Formulario con integración de items
- **Múltiples productos por factura**: Agregar varios items con cantidad, descripción, código y precio
- **Métodos en CompraService**: 
  - `guardarCompraConItems()`
  - `actualizarCompraConItems()`
  - `obtenerItemsDeCompra()`
  - `contarItemsDeCompra()`

### 🗄️ Base de Datos
- Nueva tabla `items_compra` con campos:
  - id, id_compra, cantidad, descripcion, codigo
  - precio_unitario, subtotal, orden
- Scripts de actualización: `actualizar_bd_items.sh` y `.bat`

### 📦 Nuevos Archivos
- `src/vista/FormularioCompraDarkConItems.java`
- `src/modelo/ItemCompra.java`
- `src/dao/ItemCompraDAO.java`
- `src/dao/ItemCompraDAOMySQL.java`
- `db/update_items_compra.sql`
- `actualizar_bd_items.sh` y `.bat`

### 🔧 Mejorado
- VentanaUnificada usa nuevo formulario con items
- CompraService con soporte completo para items
- Interfaz más amplia (800x850px) para acomodar tabla

### 📝 Documentación
- `SISTEMA_ITEMS_COMPRA.md` actualizado con estado completo

---

## [2.2.5] - 2026-01-03

### ✨ Nuevo
- Campo numérico con formato automático (1.000.000,50)
- Clase `CampoNumericoFormateado.java` reutilizable
- Formato en tiempo real mientras se escribe
- Validación automática de entrada

### 🔧 Mejorado
- Botón "Limpiar" más grande (100px)
- Fuente del botón aumentada a 12px

### 📝 Archivos
- `src/util/CampoNumericoFormateado.java` (nuevo)
- `src/vista/FormularioCompraDark.java` (actualizado)
- `src/vista/VentanaUnificada.java` (actualizado)

---

## [2.2.4] - 2026-01-03

### ✨ Nuevo
- Saldo pendiente por proveedor
- Label `lblPendienteProveedor` en panel de información
- Método `calcularPendientesPorProveedor()` en servicio

### 🎨 Visual
- Pendiente mostrado en rojo junto al total
- Actualización automática al seleccionar proveedor

---

## [2.2.3] - 2026-01-03

### ✨ Nuevo
- Placeholders en campos de fecha ("dd/mm/aa")
- Método `agregarPlaceholder()` reutilizable
- Placeholders en filtros de fecha

### 🎨 Visual
- Placeholders en gris que desaparecen al hacer clic
- Reaparecen si el campo queda vacío

---

## [2.2.2] - 2026-01-03

### 🐛 Corregido
- Contador de pendientes ahora incluye TODOS los pendientes
- No solo créditos, también efectivo/transferencia sin fecha

### 🔧 Mejorado
- Query SQL actualizado en `calcularTotalCreditosPendientes()`
- Label renombrado: "Créditos Pendientes" → "Pendientes"

---

## [2.2.1] - 2026-01-03

### ✨ Nuevo
- Checkbox "Marcar como pagado" en formulario de edición
- Edición de estado de pago para efectivo/transferencia
- Validación de fecha de pago según checkbox

### 🎨 Visual
- Checkbox visible solo para efectivo y transferencia
- Campo de fecha se muestra/oculta automáticamente

---

## [2.2.0] - 2026-01-03

### 🎨 Visual
- Tema azul oscuro elegante (reemplaza gris)
- Proveedores en morado brillante
- Colores simplificados: rojo (pendiente), verde (pagado)
- Letras más grandes (14-17px)
- Mejor legibilidad general

### 🔧 Mejorado
- Compras nuevas salen pendientes por defecto
- 6 filtros implementados
- Búsqueda de proveedores
- Búsqueda de compras

---

## [2.1.0] - 2026-01-03

### ✨ Nuevo
- Búsqueda de proveedores en tiempo real
- Filtros avanzados de compras:
  - Por forma de pago
  - Por estado
  - Por rango de fechas
- Botón "Limpiar" para resetear filtros

### 🎨 Visual
- Centrado inteligente en tabla
- Números alineados a la derecha
- Texto alineado según tipo
- Encabezados centrados

### 📝 Documentación
- `FILTROS_AVANZADOS.md`
- `MEJORAS_V2.1.md`
- `CHANGELOG.md`

---

## [2.0.0] - 2026-01-02

### ✨ Inicial
- Sistema completo de gestión
- Proveedores y compras
- Múltiples formas de pago
- Control de créditos
- Categorías personalizadas
- Tema oscuro
- Base de datos MySQL

---

**Formato:** [Versión] - Fecha

**Tipos de cambios:**
- ✨ Nuevo: Nuevas características
- 🔧 Mejorado: Mejoras en funcionalidad existente
- 🐛 Corregido: Corrección de errores
- 🎨 Visual: Cambios visuales/UI
- 📝 Documentación: Cambios en documentación

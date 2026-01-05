# Sistema de Items por Compra

## 📋 Descripción

Sistema que permite agregar múltiples productos/items en una sola factura de compra, similar a una factura real donde se listan varios productos.

## 🎯 Objetivo

Cuando se hace clic en "Nueva Compra", el formulario debe permitir:
- Agregar múltiples productos/items
- Cada item con: cantidad, descripción, código, precio unitario
- Calcular subtotales automáticamente
- Calcular total general de la factura

## 📊 Estructura de Datos

### Tabla: items_compra

```sql
CREATE TABLE items_compra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_compra INT NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    codigo VARCHAR(100),
    precio_unitario DECIMAL(15,2) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL,
    orden INT DEFAULT 0,
    FOREIGN KEY (id_compra) REFERENCES compras(id) ON DELETE CASCADE
);
```

## 🏗️ Arquitectura

### Archivos Creados

1. **db/update_items_compra.sql**
   - Script SQL para crear tabla items_compra

2. **src/modelo/ItemCompra.java**
   - Clase modelo para items
   - Campos: id, idCompra, cantidad, descripcion, codigo, precioUnitario, subtotal, orden
   - Método: calcularSubtotal()

3. **src/dao/ItemCompraDAO.java**
   - Interfaz DAO

4. **src/dao/ItemCompraDAOMySQL.java**
   - Implementación MySQL
   - Métodos CRUD completos
   - obtenerPorCompra(idCompra)
   - eliminarPorCompra(idCompra)

## 🎨 Interfaz Propuesta

### FormularioCompraDark - Versión con Items

```
┌─────────────────────────────────────────────────┐
│  Nueva Compra - Distribuidora ABC               │
├─────────────────────────────────────────────────┤
│                                                  │
│  Nº Factura: [________]  Fecha: [__/__/__]     │
│  Categoría: [Alimentos ▼]                       │
│  Forma de Pago: [Efectivo ▼]                    │
│                                                  │
│  ┌─ Items de la Compra ────────────────────┐   │
│  │                                          │   │
│  │  Cant  Descripción      Código  P.Unit  │   │
│  │  ────  ─────────────    ──────  ──────  │   │
│  │  [ 2]  [Arroz 50kg  ]  [A-001] [50.000]│   │
│  │  [ 5]  [Aceite 1L   ]  [A-002] [12.000]│   │
│  │  [10]  [Azúcar 1kg  ]  [A-003] [ 3.500]│   │
│  │                                          │   │
│  │  [+ Agregar Item]  [- Eliminar]         │   │
│  │                                          │   │
│  │  Subtotales:                             │   │
│  │    Item 1: $100.000                      │   │
│  │    Item 2: $ 60.000                      │   │
│  │    Item 3: $ 35.000                      │   │
│  │                                          │   │
│  │  TOTAL: $195.000                         │   │
│  └──────────────────────────────────────────┘   │
│                                                  │
│  Observaciones:                                  │
│  [_____________________________________________] │
│                                                  │
│         [Cancelar]          [Guardar]           │
└─────────────────────────────────────────────────┘
```

## 🔧 Implementación Pendiente

### 1. Actualizar Base de Datos

```bash
mysql -u root -p gestion_proveedores < db/update_items_compra.sql
```

### 2. Compilar Nuevas Clases

```bash
# Linux
bash compilar.sh

# Windows
compilar.bat
```

### 3. Crear/Actualizar FormularioCompraDark

Necesita:
- JTable para mostrar items
- Botones: Agregar Item, Eliminar Item
- Campos para nuevo item: cantidad, descripción, código, precio
- Label para mostrar total calculado
- Lógica para agregar/eliminar items de la tabla
- Guardar todos los items al guardar la compra

### 4. Actualizar CompraService

Agregar métodos:
- guardarCompraConItems(Compra compra, List<ItemCompra> items)
- obtenerItemsDeCompra(int idCompra)
- actualizarCompraConItems(Compra compra, List<ItemCompra> items)

## 📝 Flujo de Uso

### Crear Compra con Items

1. Usuario hace clic en "+ Nueva Compra"
2. Se abre formulario con sección de items
3. Usuario llena datos generales (factura, fecha, etc.)
4. Usuario agrega items uno por uno:
   - Cantidad: 2
   - Descripción: Arroz 50kg
   - Código: A-001
   - Precio: 50.000
   - Clic en "+ Agregar Item"
5. Item se agrega a la tabla
6. Subtotal se calcula automáticamente
7. Total general se actualiza
8. Usuario repite para más items
9. Clic en "Guardar"
10. Se guarda la compra y todos sus items

### Editar Compra con Items

1. Usuario selecciona compra y hace clic en "Editar"
2. Se abre formulario con items existentes cargados
3. Usuario puede:
   - Modificar items existentes
   - Agregar nuevos items
   - Eliminar items
4. Total se recalcula automáticamente
5. Clic en "Guardar"
6. Se actualizan compra e items

### Ver Compra con Items

En la tabla principal, mostrar:
- Número de items: "3 items"
- Total de la compra
- Al hacer doble clic, ver detalle de items

## 🎯 Ventajas

✅ **Más realista:** Como una factura real
✅ **Más detallado:** Información completa de productos
✅ **Mejor control:** Saber exactamente qué se compró
✅ **Reportes:** Análisis por producto
✅ **Inventario:** Base para sistema de inventario futuro

## ⚠️ Consideraciones

### Compatibilidad

- Compras antiguas sin items seguirán funcionando
- Campo `descripcion` de compra se usa como descripción general
- Campos `cantidad` y `precio_unitario` de compra son opcionales

### Migración

Compras existentes pueden:
1. Mantenerse sin items (modo simple)
2. Convertirse a items (crear un item con los datos de la compra)

## 🚀 Estado Actual

✅ Base de datos diseñada
✅ Modelo ItemCompra creado
✅ DAO ItemCompra creado
✅ DAO ItemCompra implementado
✅ FormularioCompraDarkConItems creado
✅ CompraService actualizado con métodos para items
✅ VentanaUnificada actualizada para usar nuevo formulario
✅ Scripts de actualización de BD creados (actualizar_bd_items.sh y .bat)
⏳ Actualización de base de datos (pendiente - ejecutar script)
⏳ Pruebas (pendiente)

## 📋 Próximos Pasos

1. **Actualizar base de datos:**
   ```bash
   # Linux
   bash actualizar_bd_items.sh
   
   # Windows
   actualizar_bd_items.bat
   ```

2. **Ejecutar la aplicación:**
   ```bash
   # Linux
   bash ejecutar.sh
   
   # Windows
   ejecutar.bat
   ```

3. **Probar funcionalidad:**
   - Crear nueva compra con múltiples items
   - Editar compra existente
   - Eliminar items
   - Verificar cálculo automático de totales
   - Verificar guardado en base de datos

## 💡 Alternativa Simple

Si se prefiere una implementación más simple:
- Mantener formulario actual
- Agregar solo un campo de texto multilínea para listar items
- No crear tabla items_compra
- Guardar lista de items como texto en campo descripción

## 🎨 Mockup Detallado

```
Formulario con Items:

┌──────────────────────────────────────────────────────┐
│ Nueva Compra                                    [X]  │
├──────────────────────────────────────────────────────┤
│                                                       │
│ Proveedor: Distribuidora ABC                         │
│                                                       │
│ Nº Factura: [F-12345]    Fecha: [03/01/26]          │
│                                                       │
│ Categoría: [Alimentos ▼]                             │
│                                                       │
│ ┌─ Items ──────────────────────────────────────────┐│
│ │                                                   ││
│ │ Cant  Descripción          Código    P.Unit      ││
│ │ ────  ──────────────────   ──────    ─────────   ││
│ │  2    Arroz 50kg           A-001     $50.000     ││
│ │  5    Aceite 1L            A-002     $12.000     ││
│ │ 10    Azúcar 1kg           A-003     $ 3.500     ││
│ │                                                   ││
│ │ [+ Agregar]  [- Eliminar]  [Editar]              ││
│ │                                                   ││
│ │ ┌─ Nuevo Item ──────────────────────────────┐    ││
│ │ │ Cant: [__]  Desc: [___________]           │    ││
│ │ │ Código: [____]  Precio: [________]        │    ││
│ │ │              [Agregar a la lista]         │    ││
│ │ └───────────────────────────────────────────┘    ││
│ └───────────────────────────────────────────────────┘│
│                                                       │
│ Subtotales:                                          │
│   Item 1 (2 x $50.000):    $100.000                 │
│   Item 2 (5 x $12.000):    $ 60.000                 │
│   Item 3 (10 x $3.500):    $ 35.000                 │
│                            ─────────                 │
│ TOTAL:                     $195.000                  │
│                                                       │
│ Forma de Pago: [Efectivo ▼]                          │
│ ☐ Marcar como pagado                                 │
│                                                       │
│          [Cancelar]              [Guardar]           │
└──────────────────────────────────────────────────────┘
```

---

**Nota:** Esta es una funcionalidad compleja que requiere varios cambios. Se recomienda implementar por fases y probar cada fase antes de continuar.

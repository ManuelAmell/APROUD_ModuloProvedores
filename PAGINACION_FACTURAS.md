# Paginación de Facturas - Documentación

## Descripción General

Se ha implementado un sistema de paginación para las facturas en la ventana principal, mostrando solo 25 facturas por página con controles de navegación intuitivos.

## Características Principales

### 1. Límite de Facturas por Página
- **25 facturas por página** (constante configurable)
- Mejora el rendimiento al no cargar todas las facturas a la vez
- Reduce el tiempo de carga y renderizado de la tabla

### 2. Controles de Navegación
- **Botón "← Anterior"**: Navega a la página anterior
- **Botón "Siguiente →"**: Navega a la página siguiente
- **Label informativo**: Muestra información detallada de la paginación

### 3. Estados de Botones
- **Deshabilitados automáticamente** cuando no hay más páginas
- **Cambio de color** según el estado:
  - Habilitado: Azul brillante (ACENTO)
  - Deshabilitado: Gris oscuro (BG_INPUT)

### 4. Reinicio Automático
- La paginación se reinicia a la página 1 cuando:
  - Se cambia de proveedor
  - Se aplican filtros de búsqueda
  - Se limpian los filtros

## Implementación Técnica

### Variables de Instancia

```java
// Constante de configuración
private static final int FACTURAS_POR_PAGINA = 25;

// Variables de estado
private int paginaActual = 0;              // Página actual (0-indexed)
private int totalPaginas = 0;              // Total de páginas calculadas
private List<Compra> comprasCompletas;     // Lista completa sin paginar

// Componentes UI
private JButton btnPaginaAnterior;
private JButton btnPaginaSiguiente;
private JLabel lblPaginacion;
```

### Panel de Paginación

```java
private JPanel crearPanelPaginacion() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    panel.setBackground(BG_PANEL);
    panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE));
    
    // Botón Anterior
    btnPaginaAnterior = crearBoton("← Anterior", BG_INPUT);
    btnPaginaAnterior.addActionListener(e -> cambiarPagina(-1));
    
    // Label informativo
    lblPaginacion = new JLabel("Página 1 de 1");
    
    // Botón Siguiente
    btnPaginaSiguiente = crearBoton("Siguiente →", BG_INPUT);
    btnPaginaSiguiente.addActionListener(e -> cambiarPagina(1));
    
    return panel;
}
```

## Flujo de Funcionamiento

### 1. Carga Inicial
```
Usuario selecciona proveedor
         ↓
seleccionarProveedor()
         ↓
paginaActual = 0 (reiniciar)
         ↓
cargarComprasProveedor()
         ↓
Obtener todas las compras del proveedor
         ↓
Calcular totalPaginas
         ↓
cargarPaginaActual() (página 0)
         ↓
actualizarPaginacion()
```

### 2. Cambio de Página
```
Usuario hace click en "Siguiente" o "Anterior"
         ↓
cambiarPagina(direccion)
         ↓
Validar nueva página
         ↓
paginaActual += direccion
         ↓
cargarPaginaActual()
         ↓
actualizarPaginacion()
```

### 3. Aplicar Filtros
```
Usuario aplica filtros
         ↓
aplicarFiltros()
         ↓
Filtrar comprasCompletas
         ↓
paginaActual = 0 (reiniciar)
         ↓
Recalcular totalPaginas
         ↓
cargarPaginaActual()
         ↓
actualizarPaginacion()
```

## Métodos Principales

### cargarComprasProveedor()
```java
private void cargarComprasProveedor() {
    // Cargar todas las compras del proveedor
    comprasCompletas = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
    
    // Calcular total de páginas
    totalPaginas = (int) Math.ceil((double) comprasCompletas.size() / FACTURAS_POR_PAGINA);
    
    // Cargar página actual
    cargarPaginaActual();
    actualizarPaginacion();
}
```

### cargarPaginaActual()
```java
private void cargarPaginaActual() {
    modeloTablaCompras.setRowCount(0);
    
    // Calcular índices
    int inicio = paginaActual * FACTURAS_POR_PAGINA;
    int fin = Math.min(inicio + FACTURAS_POR_PAGINA, comprasCompletas.size());
    
    // Cargar solo las facturas de la página actual
    for (int i = inicio; i < fin; i++) {
        Compra c = comprasCompletas.get(i);
        // ... agregar fila a la tabla
    }
}
```

### cambiarPagina()
```java
private void cambiarPagina(int direccion) {
    int nuevaPagina = paginaActual + direccion;
    
    // Validar rango
    if (nuevaPagina >= 0 && nuevaPagina < totalPaginas) {
        paginaActual = nuevaPagina;
        cargarPaginaActual();
        actualizarPaginacion();
    }
}
```

### actualizarPaginacion()
```java
private void actualizarPaginacion() {
    // Calcular rangos
    int totalFacturas = comprasCompletas.size();
    int inicio = paginaActual * FACTURAS_POR_PAGINA + 1;
    int fin = Math.min((paginaActual + 1) * FACTURAS_POR_PAGINA, totalFacturas);
    
    // Actualizar label
    lblPaginacion.setText(String.format(
        "Mostrando %d-%d de %d facturas (Página %d de %d)", 
        inicio, fin, totalFacturas, paginaActual + 1, totalPaginas
    ));
    
    // Habilitar/deshabilitar botones
    btnPaginaAnterior.setEnabled(paginaActual > 0);
    btnPaginaSiguiente.setEnabled(paginaActual < totalPaginas - 1);
    
    // Cambiar colores
    btnPaginaAnterior.setBackground(btnPaginaAnterior.isEnabled() ? ACENTO : BG_INPUT);
    btnPaginaSiguiente.setBackground(btnPaginaSiguiente.isEnabled() ? ACENTO : BG_INPUT);
}
```

## Ejemplos de Uso

### Ejemplo 1: Proveedor con 50 Facturas
```
Total: 50 facturas
Páginas: 2 (25 + 25)

Página 1: Mostrando 1-25 de 50 facturas (Página 1 de 2)
  [← Anterior] (deshabilitado)  [Siguiente →] (habilitado)

Página 2: Mostrando 26-50 de 50 facturas (Página 2 de 2)
  [← Anterior] (habilitado)  [Siguiente →] (deshabilitado)
```

### Ejemplo 2: Proveedor con 63 Facturas
```
Total: 63 facturas
Páginas: 3 (25 + 25 + 13)

Página 1: Mostrando 1-25 de 63 facturas (Página 1 de 3)
Página 2: Mostrando 26-50 de 63 facturas (Página 2 de 3)
Página 3: Mostrando 51-63 de 63 facturas (Página 3 de 3)
```

### Ejemplo 3: Proveedor con 10 Facturas
```
Total: 10 facturas
Páginas: 1

Página 1: Mostrando 1-10 de 10 facturas (Página 1 de 1)
  [← Anterior] (deshabilitado)  [Siguiente →] (deshabilitado)
```

## Integración con Filtros

La paginación funciona perfectamente con los filtros existentes:

```java
private void aplicarFiltros() {
    // Filtrar compras según criterios
    comprasCompletas = new ArrayList<>();
    for (Compra c : todasCompras) {
        if (cumpleFiltros(c)) {
            comprasCompletas.add(c);
        }
    }
    
    // Reiniciar paginación
    paginaActual = 0;
    totalPaginas = (int) Math.ceil((double) comprasCompletas.size() / FACTURAS_POR_PAGINA);
    
    cargarPaginaActual();
    actualizarPaginacion();
}
```

## Ventajas del Sistema

### 1. Rendimiento Mejorado
- Solo se renderizan 25 filas a la vez
- Carga inicial más rápida
- Menos uso de memoria

### 2. Mejor Experiencia de Usuario
- Tabla más manejable y fácil de navegar
- Información clara de posición actual
- Controles intuitivos

### 3. Escalabilidad
- Funciona bien con proveedores que tienen cientos de facturas
- No hay degradación de rendimiento con grandes volúmenes

### 4. Flexibilidad
- Fácil cambiar el número de facturas por página (constante)
- Compatible con filtros y búsquedas
- Se adapta automáticamente al contenido

## Configuración

### Cambiar Facturas por Página
```java
// En VentanaUnificada.java
private static final int FACTURAS_POR_PAGINA = 50;  // Cambiar a 50
```

### Personalizar Botones
```java
// Cambiar texto
btnPaginaAnterior = crearBoton("◄ Atrás", BG_INPUT);
btnPaginaSiguiente = crearBoton("Adelante ►", BG_INPUT);

// Cambiar tamaño
btnPaginaAnterior.setPreferredSize(new Dimension(150, 35));
```

### Personalizar Label
```java
// Formato personalizado
lblPaginacion.setText(String.format("Pág. %d/%d (%d-%d de %d)", 
    paginaActual + 1, totalPaginas, inicio, fin, totalFacturas));
```

## Casos Especiales

### Sin Facturas
```
lblPaginacion.setText("Sin facturas");
btnPaginaAnterior.setEnabled(false);
btnPaginaSiguiente.setEnabled(false);
```

### Una Sola Página
```
Mostrando 1-15 de 15 facturas (Página 1 de 1)
Ambos botones deshabilitados
```

### Cambio de Proveedor
```
paginaActual = 0;  // Siempre volver a la primera página
cargarComprasProveedor();
```

## Ubicación Visual

```
┌────────────────────────────────────────────┐
│ Filtros y búsqueda                         │
├────────────────────────────────────────────┤
│                                            │
│ Tabla de facturas (25 filas máximo)       │
│                                            │
├────────────────────────────────────────────┤
│  [← Anterior]  Mostrando 1-25 de 100      │
│                facturas (Página 1 de 4)    │
│                              [Siguiente →] │
└────────────────────────────────────────────┘
```

## Notas Técnicas

1. **Índices 0-based**: `paginaActual` empieza en 0, pero se muestra como 1 al usuario
2. **Math.ceil**: Usado para calcular páginas correctamente (63/25 = 3 páginas)
3. **Math.min**: Asegura que no se exceda el tamaño de la lista
4. **Validación**: Siempre se valida que la página esté en rango válido
5. **Reinicio**: La paginación se reinicia en múltiples eventos para mejor UX

## Resultado Final

El sistema de paginación proporciona una navegación eficiente y clara de las facturas, mejorando significativamente el rendimiento y la usabilidad de la aplicación, especialmente para proveedores con grandes volúmenes de facturas.

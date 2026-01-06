# Toggle Switch para Estado de Proveedores

## Descripción General

Se ha implementado un **Toggle Switch** (interruptor deslizante) en el panel de proveedores que permite alternar entre ver proveedores **Activos** e **Inactivos**.

## Ubicación

El toggle switch se encuentra en la parte superior del panel lateral izquierdo de proveedores, justo al lado del título "PROVEEDORES".

```
┌─────────────────────────────┐
│ PROVEEDORES    [Activo] ⚪─│  ← Toggle Switch aquí
│ 🔍 [Buscar proveedor...]    │
│                             │
│ • Proveedor 1               │
│ • Proveedor 2               │
│ • Proveedor 3               │
└─────────────────────────────┘
```

## Características Visuales

### Estado ACTIVO (por defecto)
- **Color del switch**: Verde brillante (RGB: 80, 255, 120)
- **Texto mostrado**: "Activo"
- **Color del texto**: Verde brillante
- **Posición del círculo**: Derecha
- **Texto interno**: "ON"

### Estado INACTIVO
- **Color del switch**: Rojo brillante (RGB: 255, 80, 80)
- **Texto mostrado**: "Inactivo"
- **Color del texto**: Rojo brillante
- **Posición del círculo**: Izquierda
- **Texto interno**: "OFF"

## Comportamiento

### 1. Interacción del Usuario
- **Click**: Al hacer click en el toggle, cambia de estado
- **Cursor**: Se muestra como "mano" (cursor pointer) al pasar sobre él
- **Animación**: El círculo se desliza suavemente de un lado a otro (animación fluida)

### 2. Cambio de Estado
Cuando el usuario hace click en el toggle:

1. **Cambio visual inmediato**:
   - El color del switch cambia (verde ↔ rojo)
   - El círculo se anima hacia el otro lado
   - El texto cambia ("Activo" ↔ "Inactivo")
   - El color del texto cambia (verde ↔ rojo)

2. **Recarga de datos**:
   - Se llama al método `cargarProveedoresPorEstado(boolean activo)`
   - Si está en ACTIVO: carga solo proveedores activos
   - Si está en INACTIVO: carga solo proveedores inactivos
   - La lista de proveedores se actualiza automáticamente

### 3. Filtrado de Proveedores

**Modo ACTIVO (true)**:
```java
proveedores = proveedorService.obtenerProveedoresActivos();
```
- Muestra solo proveedores con `activo = true`
- Este es el estado por defecto al iniciar la aplicación

**Modo INACTIVO (false)**:
```java
List<Proveedor> todosProveedores = proveedorService.obtenerTodosProveedores();
proveedores = new ArrayList<>();
for (Proveedor p : todosProveedores) {
    if (!p.isActivo()) {
        proveedores.add(p);
    }
}
```
- Muestra solo proveedores con `activo = false`
- Útil para ver proveedores archivados o desactivados

## Implementación Técnica

### Componente Personalizado: `ToggleSwitch.java`

```java
public class ToggleSwitch extends JPanel {
    private boolean activo = true; // Estado por defecto
    
    // Colores
    private final Color COLOR_ACTIVO = new Color(80, 255, 120);   // Verde
    private final Color COLOR_INACTIVO = new Color(255, 80, 80);  // Rojo
    
    // Métodos principales
    public void toggle()                // Cambia el estado
    public void setActivo(boolean)      // Establece el estado
    public boolean isActivo()           // Obtiene el estado actual
}
```

### Integración en VentanaUnificada

```java
// Crear el toggle
ToggleSwitch toggleEstado = new ToggleSwitch();
toggleEstado.setActivo(true); // Por defecto: Activo

// Etiqueta del estado
JLabel lblEstado = new JLabel("Activo");
lblEstado.setForeground(CREDITO_PAGADO); // Verde

// Listener para cambios de estado
toggleEstado.addPropertyChangeListener("estado", evt -> {
    boolean activo = (Boolean) evt.getNewValue();
    lblEstado.setText(activo ? "Activo" : "Inactivo");
    lblEstado.setForeground(activo ? CREDITO_PAGADO : CREDITO_PENDIENTE);
    cargarProveedoresPorEstado(activo);
});
```

## Flujo de Eventos

```
Usuario hace click en toggle
         ↓
Toggle cambia estado interno (activo ↔ inactivo)
         ↓
Se dispara PropertyChangeEvent("estado")
         ↓
Listener actualiza:
  - Texto de la etiqueta
  - Color de la etiqueta
  - Llama a cargarProveedoresPorEstado()
         ↓
Se cargan proveedores según el nuevo estado
         ↓
Lista de proveedores se actualiza en la UI
```

## Animación

El toggle incluye una animación suave del círculo:

```java
private Timer animationTimer;
private float circleX;      // Posición actual
private float targetX;      // Posición objetivo

// Animación con interpolación
animationTimer = new Timer(10, e -> {
    float diff = targetX - circleX;
    if (Math.abs(diff) > 0.5f) {
        circleX += diff * 0.3f;  // Movimiento suave (30% de la distancia)
        repaint();
    } else {
        circleX = targetX;
        animationTimer.stop();
        repaint();
    }
});
```

## Ventajas del Diseño

1. **Visual intuitivo**: Los colores verde/rojo son universalmente reconocidos
2. **Feedback inmediato**: El usuario ve el cambio al instante
3. **Animación fluida**: Mejora la experiencia de usuario
4. **Integración perfecta**: Se adapta al tema oscuro de la aplicación
5. **Reutilizable**: El componente puede usarse en otras partes de la aplicación

## Casos de Uso

- **Ver proveedores activos**: Estado por defecto, muestra proveedores con los que se trabaja actualmente
- **Ver proveedores inactivos**: Útil para revisar proveedores archivados, históricos o temporalmente desactivados
- **Gestión de proveedores**: Facilita la administración separada de proveedores activos e inactivos

## Notas Técnicas

- El estado se maneja con una variable booleana: `true = Activo`, `false = Inactivo`
- Los colores se aplican mediante propiedades de Swing (`setForeground`, `setBackground`)
- El componente usa `PropertyChangeListener` para notificar cambios de estado
- La animación usa `javax.swing.Timer` para actualizaciones periódicas
- El renderizado personalizado se hace con `Graphics2D` y antialiasing para bordes suaves

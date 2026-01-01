# 📦 Módulo de Proveedores - Sistema de Gestión

> Un proyecto educativo en Java con interfaz gráfica (Swing) diseñado para aprender **Programación Orientada a Objetos (POO)**.

## 📚 Descripción

Este proyecto implementa un módulo completo de gestión de proveedores con:

- Interfaz gráfica moderna usando **Java Swing**
- Arquitectura de 3 capas (Modelo-Servicio-Vista)
- Patrón DAO (Data Access Object)
- Comentarios detallados en **absolutamente todo el código** para facilitar el aprendizaje

## 🎯 Conceptos de POO Demostrados

| Concepto             | Dónde se aplica                             |
| -------------------- | ------------------------------------------- |
| **Clases y Objetos** | `Proveedor.java` - representación de datos  |
| **Encapsulamiento**  | Atributos privados con getters/setters      |
| **Herencia**         | `VentanaPrincipal extends JFrame`           |
| **Interfaces**       | `ProveedorDAO` como contrato                |
| **Polimorfismo**     | `ProveedorDAO dao = new ProveedorDAOImpl()` |
| **Constructores**    | Sobrecarga de constructores en `Proveedor`  |
| **Métodos**          | Organización por responsabilidad            |

## 📁 Estructura del Proyecto

```
ModuloProveedores/
├── src/
│   ├── Main.java                    # Punto de entrada de la aplicación
│   │
│   ├── modelo/                      # Capa de Datos (Model)
│   │   └── Proveedor.java           # Entidad Proveedor
│   │
│   ├── dao/                         # Capa de Acceso a Datos
│   │   ├── ProveedorDAO.java        # Interfaz DAO
│   │   └── ProveedorDAOImpl.java    # Implementación en memoria
│   │
│   ├── servicio/                    # Capa de Lógica de Negocio
│   │   └── ProveedorService.java    # Servicio con validaciones
│   │
│   └── vista/                       # Capa de Interfaz Gráfica (GUI)
│       ├── VentanaPrincipal.java    # Ventana principal con tabla
│       └── FormularioProveedor.java # Diálogo para crear/editar
│
└── README.md                        # Este archivo
```

## 🏗️ Arquitectura de 3 Capas

```
┌─────────────────────────────────────────┐
│           VISTA (GUI)                   │
│  VentanaPrincipal, FormularioProveedor  │
└───────────────────┬─────────────────────┘
                    │ usa
                    ▼
┌─────────────────────────────────────────┐
│         SERVICIO (Lógica)               │
│         ProveedorService                │
│   - Validaciones                        │
│   - Reglas de negocio                   │
└───────────────────┬─────────────────────┘
                    │ usa
                    ▼
┌─────────────────────────────────────────┐
│           DAO (Datos)                   │
│   ProveedorDAO → ProveedorDAOImpl       │
│   - Operaciones CRUD                    │
│   - Acceso a datos                      │
└─────────────────────────────────────────┘
```

## 🚀 Cómo Ejecutar

### Requisitos

- Java JDK 8 o superior
- Terminal o IDE (VS Code, IntelliJ, Eclipse, NetBeans)

### Compilar y Ejecutar desde Terminal

```bash
# 1. Navegar al directorio del proyecto
cd ModuloProveedores

# 2. Compilar todos los archivos Java
javac -d bin src/modelo/*.java src/dao/*.java src/servicio/*.java src/vista/*.java src/Main.java

# 3. Ejecutar la aplicación
java -cp bin Main
```

### Ejecutar desde un IDE

1. Importar el proyecto
2. Marcar `src` como carpeta de fuentes
3. Ejecutar `Main.java`

## 🖥️ Funcionalidades

### Ventana Principal

- 📋 **Tabla de proveedores** - Muestra todos los proveedores con su información
- 🔍 **Búsqueda** - Filtra proveedores por nombre
- ✅ **Filtro de activos** - Muestra solo proveedores activos
- 🔄 **Refrescar** - Recarga los datos de la tabla

### Operaciones CRUD

- ➕ **Nuevo** - Abre formulario para crear proveedor
- ✏️ **Editar** - Modifica un proveedor existente
- 🗑️ **Eliminar** - Borra un proveedor (con confirmación)

### Validaciones

- Nombre y NIT son obligatorios
- NIT debe ser único (no duplicados)
- Formato de email válido (debe contener @)

## 📖 Archivos para Estudiar (en orden recomendado)

1. **`Proveedor.java`** - Aprende sobre clases, atributos, constructores y encapsulamiento
2. **`ProveedorDAO.java`** - Entiende qué son las interfaces y el patrón DAO
3. **`ProveedorDAOImpl.java`** - Ve cómo implementar una interfaz y usar ArrayList
4. **`ProveedorService.java`** - Comprende la capa de lógica y validaciones
5. **`VentanaPrincipal.java`** - Explora Swing: JFrame, JTable, eventos
6. **`FormularioProveedor.java`** - Conoce JDialog y layouts avanzados
7. **`Main.java`** - Descubre cómo iniciar una aplicación Swing correctamente

## 🧪 Datos de Ejemplo

La aplicación viene con 3 proveedores de ejemplo:

| ID  | Nombre                | NIT         | Activo |
| --- | --------------------- | ----------- | ------ |
| 1   | Distribuidora ABC     | 900123456-1 | ✓      |
| 2   | Suministros del Norte | 800987654-2 | ✓      |
| 3   | Importadora XYZ       | 700555888-3 | ✗      |

## 📝 Conceptos Clave en los Comentarios

Cada archivo contiene comentarios extensos que explican:

- ✨ **Qué**: Qué hace cada clase, método o atributo
- ❓ **Por qué**: Por qué se diseñó de esa manera
- 🔧 **Cómo**: Cómo funciona el código paso a paso
- 📌 **Ejemplos**: Ejemplos de uso cuando es útil

## 🔮 Posibles Mejoras Futuras

- [ ] Conectar a una base de datos real (MySQL, PostgreSQL)
- [ ] Agregar más entidades (Productos, Pedidos)
- [ ] Implementar exportación a Excel/PDF
- [ ] Agregar autenticación de usuarios
- [ ] Mejorar la interfaz con look and feel personalizado

## 👨‍💻 Autor

Proyecto creado con fines educativos para aprender Java y POO.

---

¡Happy Coding! 🎉

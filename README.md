# 📦 Sistema de Gestión de Proveedores y Compras

<div align="center">

![Version](https://img.shields.io/badge/version-2.3.0-blue.svg)
![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

**Sistema completo de gestión empresarial con interfaz oscura profesional**

[Características](#-características) • [Instalación](#-instalación-rápida) • [Uso](#-uso) • [Documentación](#-documentación)

</div>

---

## 📋 Descripción

Sistema de gestión empresarial desarrollado en Java con interfaz gráfica Swing y base de datos MySQL/MariaDB. Diseñado para gestionar proveedores, compras, inventario y control de créditos con una interfaz moderna y profesional.

## ✨ Características

### 🎨 Interfaz de Usuario
- ✅ **Tema oscuro profesional** - Interfaz moderna y elegante
- ✅ **Reloj en tiempo real** - Visualización de hora y fecha
- ✅ **Diseño responsivo** - Ventanas redimensionables
- ✅ **Búsqueda en tiempo real** - Filtrado instantáneo
- ✅ **Iconos intuitivos** - Navegación clara y sencilla

### 📊 Gestión de Proveedores
- ✅ Registro completo de proveedores
- ✅ Búsqueda y filtrado avanzado
- ✅ Información de contacto y pago
- ✅ Estado activo/inactivo
- ✅ Historial de compras por proveedor

### 🛒 Gestión de Compras
- ✅ **Sistema de items por compra** - Múltiples productos por factura
- ✅ **Tabla editable estilo Excel** - Edición con un solo clic
- ✅ **Cálculo automático** - Subtotales y totales
- ✅ **Validaciones robustas** - Control de datos
- ✅ Categorías personalizadas
- ✅ Control de formas de pago (Efectivo, Transferencia, Crédito)
- ✅ Gestión de créditos pendientes
- ✅ Filtros por fecha, categoría y estado
- ✅ Modo visualización (solo lectura)

### 💰 Control Financiero
- ✅ Total general de compras
- ✅ Créditos pendientes
- ✅ Estadísticas por proveedor
- ✅ Reportes por período
- ✅ Seguimiento de pagos

### 🔧 Características Técnicas
- ✅ Arquitectura MVC (Modelo-Vista-Controlador)
- ✅ Patrón DAO para acceso a datos
- ✅ Conexión singleton a base de datos
- ✅ Transacciones seguras
- ✅ Validación de datos en tiempo real
- ✅ Manejo de errores robusto

## 🚀 Instalación Rápida

### Requisitos Previos

- **Java JDK 11 o superior**
- **MySQL 8.0+ o MariaDB 10.5+**
- **Git** (para clonar el repositorio)

### Instalación por Sistema Operativo

#### 🐧 Linux (Ubuntu/Debian/Arch)

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/ModuloProveedores.git
cd ModuloProveedores

# 2. Instalar dependencias
# Ubuntu/Debian:
sudo apt update
sudo apt install default-jdk mysql-server

# Arch Linux:
sudo pacman -S jdk-openjdk mariadb

# 3. Configurar base de datos
sudo mysql -u root < db/schema.sql
sudo mysql -u root < db/update_items_compra.sql
sudo mysql -u root < actualizar_contraseña.sql

# 4. Compilar y ejecutar
bash compilar.sh
bash ejecutar_simple.sh
```

#### 🪟 Windows

```batch
REM 1. Clonar el repositorio
git clone https://github.com/tu-usuario/ModuloProveedores.git
cd ModuloProveedores

REM 2. Instalar MySQL desde: https://dev.mysql.com/downloads/installer/

REM 3. Configurar base de datos
mysql -u root -p < db\schema.sql
mysql -u root -p < db\update_items_compra.sql
mysql -u root -p < actualizar_contraseña.sql

REM 4. Compilar y ejecutar
compilar.bat
ejecutar.bat
```

#### 🍎 macOS

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/ModuloProveedores.git
cd ModuloProveedores

# 2. Instalar dependencias con Homebrew
brew install openjdk mysql

# 3. Configurar base de datos
mysql -u root < db/schema.sql
mysql -u root < db/update_items_compra.sql
mysql -u root < actualizar_contraseña.sql

# 4. Compilar y ejecutar
bash compilar.sh
bash ejecutar_simple.sh
```

## 🔐 Configuración de Base de Datos

### Credenciales por Defecto

```
Base de Datos: gestion_proveedores
Usuario:       proveedor_app
Contraseña:    Amell123
Servidor:      localhost:3306
```

### Cambiar Credenciales

1. Editar `src/util/ConexionBD.java`:
```java
private static final String USUARIO = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

2. Actualizar en MySQL:
```sql
ALTER USER 'proveedor_app'@'localhost' IDENTIFIED BY 'nueva_contraseña';
FLUSH PRIVILEGES;
```

3. Recompilar:
```bash
bash compilar.sh
```

## 📖 Uso

### Inicio Rápido

1. **Ejecutar la aplicación**:
   ```bash
   bash ejecutar_simple.sh
   ```

2. **Agregar un proveedor**:
   - Clic en "+ Nuevo Proveedor"
   - Llenar formulario
   - Guardar

3. **Registrar una compra**:
   - Seleccionar proveedor
   - Clic en "+ Nueva Compra"
   - Clic en "Inscribir productos"
   - Agregar items en la tabla
   - Guardar

4. **Ver productos de una factura**:
   - Seleccionar compra
   - Clic en "👁 Ver"

5. **Editar una compra**:
   - Seleccionar compra
   - Clic en "✏ Editar"
   - Modificar datos
   - Guardar

### Sistema de Items por Compra

La tabla de items permite:
- ✅ Edición con un solo clic
- ✅ Navegación con Tab/Enter
- ✅ Cálculo automático de totales
- ✅ Validación de datos
- ✅ Formato de moneda automático
- ✅ Agregar/eliminar filas dinámicamente

**Columnas:**
- **#**: Numeración automática
- **CANTIDAD**: Unidades del producto
- **DESCRIPCIÓN**: Nombre del producto
- **REF**: Referencia (opcional)
- **CÓDIGO**: Código del producto (opcional)
- **COSTO**: Precio unitario
- **TOTAL**: Cálculo automático (cantidad × costo)
- **MÍNIMO**: Stock mínimo (opcional)

## 📚 Documentación

### Documentos Disponibles

- 📘 [**MANUAL.md**](MANUAL.md) - Manual completo del usuario
- 📗 [**GUIA_USO_RAPIDO.md**](GUIA_USO_RAPIDO.md) - Guía de inicio rápido
- 📙 [**INSTALACION_WINDOWS.md**](INSTALACION_WINDOWS.md) - Instalación en Windows
- 📕 [**SISTEMA_ITEMS_COMPRA.md**](SISTEMA_ITEMS_COMPRA.md) - Sistema de items
- 📓 [**CATEGORIAS_PERSONALIZADAS.md**](CATEGORIAS_PERSONALIZADAS.md) - Categorías
- 📔 [**FILTROS_AVANZADOS.md**](FILTROS_AVANZADOS.md) - Filtros y búsquedas
- 📖 [**SCRIPTS_DISPONIBLES.md**](SCRIPTS_DISPONIBLES.md) - Scripts de utilidad
- 📄 [**CHANGELOG.md**](CHANGELOG.md) - Historial de cambios

### Índice de Documentación

Ver [**INDICE_DOCUMENTACION.md**](INDICE_DOCUMENTACION.md) para el índice completo.

## 🛠️ Scripts Disponibles

### Compilación y Ejecución

```bash
bash compilar.sh          # Compilar el proyecto
bash ejecutar_simple.sh   # Ejecutar la aplicación
bash ejecutar.sh          # Ejecutar con configuración automática
```

### Base de Datos

```bash
bash setup_database.sh           # Configurar BD desde cero
bash actualizar_bd_items.sh      # Crear tabla items_compra
bash crear_tabla_items.sh        # Crear tabla items (alternativo)
bash actualizar_contraseña.sh    # Cambiar contraseña de BD
```

### Utilidades

```bash
bash monitorear.sh        # Monitorear recursos del sistema
bash verificar_bd.sh      # Verificar estado de la BD
```

## 🏗️ Estructura del Proyecto

```
ModuloProveedores/
├── src/                          # Código fuente Java
│   ├── modelo/                   # Clases de modelo
│   ├── vista/                    # Interfaces gráficas
│   ├── dao/                      # Acceso a datos
│   ├── servicio/                 # Lógica de negocio
│   └── util/                     # Utilidades
├── db/                           # Scripts de base de datos
│   ├── schema.sql               # Esquema principal
│   └── update_items_compra.sql  # Tabla de items
├── lib/                          # Librerías externas
├── bin/                          # Archivos compilados
├── docs/                         # Documentación
└── scripts/                      # Scripts de utilidad
```

## 🔄 Changelog

### v2.3.0 (Actual)
- ✨ Sistema completo de items por compra
- ✨ Tabla editable estilo Excel
- ✨ Modo visualización (solo lectura)
- ✨ Reloj en tiempo real en ventana principal
- ✨ Indicador de versión
- ✨ Validaciones robustas
- ✨ Cálculo automático de totales
- 🐛 Corrección de errores de guardado
- 📝 Documentación actualizada

Ver [CHANGELOG.md](CHANGELOG.md) para historial completo.

## 🤝 Contribuir

Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

Ver [CONTRIBUTING.md](CONTRIBUTING.md) para más detalles.

## 🐛 Reportar Problemas

Usa la plantilla en [ISSUE_TEMPLATE.md](ISSUE_TEMPLATE.md) para reportar bugs.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

## 👥 Autores

- **Desarrollador Principal** - [Tu Nombre](https://github.com/tu-usuario)

## 🙏 Agradecimientos

- Comunidad Java
- Documentación de MySQL
- Contribuidores del proyecto

## 📞 Soporte

- 📧 Email: tu-email@ejemplo.com
- 🐛 Issues: [GitHub Issues](https://github.com/tu-usuario/ModuloProveedores/issues)
- 📖 Wiki: [GitHub Wiki](https://github.com/tu-usuario/ModuloProveedores/wiki)

---

<div align="center">

**⭐ Si te gusta este proyecto, dale una estrella en GitHub! ⭐**

Hecho con ❤️ y ☕

</div>

# 🏢 Sistema de Gestión de Proveedores y Compras

<div align="center">

![Version](https://img.shields.io/badge/version-2.2.5-blue.svg)
![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Platform](https://img.shields.io/badge/platform-Windows%20%7C%20Linux-lightgrey.svg)

Sistema completo para gestionar proveedores, compras y pagos con interfaz moderna de tema oscuro.

[Características](#-características) •
[Instalación](#-inicio-rápido) •
[Documentación](#-documentación) •
[Contribuir](CONTRIBUTING.md)

</div>

---

## 🚀 Inicio Rápido

### Windows (Instalador Precompilado)

1. **Descargar:** `instaladores/ModuloProveedores-windows.zip`
2. **Extraer** en una carpeta
3. **Configurar base de datos:**
   ```cmd
   mysql -u root -p < db\schema.sql
   ```
4. **Ejecutar:** Doble clic en `ejecutar.bat`

### Windows (Desde Código Fuente)

```cmd
mysql -u root -p < db\schema.sql
compilar.bat
ejecutar.bat
```

### Linux

```bash
bash setup_database.sh
bash compilar.sh
bash ejecutar.sh
```

---

## 📋 Requisitos

- Java JDK 11+
- MySQL 8.0+
- Windows / Linux / macOS

---

## ✨ Características

### 🎯 Gestión Completa
- ✅ Proveedores con información detallada
- ✅ Compras con múltiples formas de pago
- ✅ Categorías personalizables
- ✅ Control de pagos y créditos

### 🔍 Búsqueda y Filtros
- ✅ Búsqueda de proveedores en tiempo real
- ✅ Búsqueda de compras por múltiples criterios
- ✅ Filtros por forma de pago, estado y fechas
- ✅ Botón para limpiar todos los filtros

### 💰 Gestión Financiera
- ✅ Campos numéricos con formato automático (1.000.000,50)
- ✅ Cálculo automático de totales
- ✅ Saldo pendiente por proveedor
- ✅ Estadísticas generales en tiempo real

### 🎨 Interfaz Moderna
- ✅ Tema oscuro elegante (azul)
- ✅ Colores semánticos (rojo=pendiente, verde=pagado)
- ✅ Proveedores destacados en morado
- ✅ Letras grandes y legibles
- ✅ Icono personalizado de la aplicación

---

## 📖 Documentación

- **[MANUAL.md](MANUAL.md)** - Manual completo de usuario
- **[CHANGELOG.md](CHANGELOG.md)** - Historial de cambios

---

## 🎯 Uso Básico

### Agregar Proveedor
1. Clic en **"+ Nuevo Proveedor"**
2. Llenar nombre y datos
3. Guardar

### Registrar Compra
1. Seleccionar proveedor
2. Clic en **"+ Nueva Compra"**
3. Llenar formulario:
   - Factura, categoría, descripción
   - Total (formato automático: 1.000.000)
   - Fecha (dd/mm/aa)
   - Forma de pago
4. Guardar

### Marcar como Pagado
1. Seleccionar compra pendiente (roja)
2. Clic en **"✓ Marcar Pagado"**
3. Ingresar fecha de pago

---

## 🔧 Configuración

### Base de Datos

Editar `src/util/ConexionBD.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_proveedores";
private static final String USUARIO = "root";
private static final String PASSWORD = "tu_password";
```

Recompilar después de cambios.

---

## 📊 Estructura del Proyecto

```
├── src/
│   ├── dao/           # Acceso a datos
│   ├── modelo/        # Clases de dominio
│   ├── servicio/      # Lógica de negocio
│   ├── util/          # Utilidades
│   └── vista/         # Interfaz gráfica
├── db/
│   └── schema.sql     # Estructura de BD
├── lib/
│   └── mysql-connector-j-9.1.0.jar
├── instaladores/      # Versiones empaquetadas
├── ejecutar.bat       # Ejecutar en Windows
├── ejecutar.sh        # Ejecutar en Linux
└── MANUAL.md          # Manual completo
```

---

## 🎨 Capturas

### Panel Principal
- Lista de proveedores con búsqueda
- Tabla de compras con filtros avanzados
- Estadísticas en tiempo real

### Formulario de Compra
- Campos numéricos con formato automático
- Categorías personalizables
- Validación en tiempo real

### Colores
- 🔴 Rojo: Pendientes
- 🟢 Verde: Pagados
- 🟣 Morado: Proveedores
- 🔵 Azul: Tema principal

---

## 🐛 Solución de Problemas

### Error de Conexión
```bash
# Verificar MySQL
mysql -u root -p

# Crear base de datos
mysql -u root -p < db/schema.sql
```

### Error de Compilación
```bash
# Verificar Java
java -version

# Recompilar
bash compilar.sh  # Linux
# O compilar manualmente en Windows
```

---

## 📝 Changelog

### v2.2.5 (Actual)
- ✨ Campos numéricos con formato automático
- ✨ Saldo pendiente por proveedor
- 🐛 Botón limpiar más grande

### v2.2.4
- ✨ Saldo pendiente por proveedor

### v2.2.3
- ✨ Placeholders en campos de fecha

### v2.2.2
- 🐛 Corrección contador de pendientes

### v2.2.1
- ✨ Edición de estado de pago con checkbox

### v2.2.0
- ✨ Tema azul oscuro elegante
- ✨ Colores simplificados
- ✨ Letras más grandes

### v2.1.0
- ✨ Búsqueda de proveedores
- ✨ Filtros avanzados de compras
- ✨ Mejoras visuales

Ver [CHANGELOG.md](CHANGELOG.md) para más detalles.

---

## 📄 Licencia

Ver archivo [LICENSE](LICENSE)

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Por favor lee [CONTRIBUTING.md](CONTRIBUTING.md) para detalles sobre nuestro código de conducta y el proceso para enviar pull requests.

### Pasos Rápidos

1. Fork del proyecto
2. Crear rama (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'feat: Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

---

## 🔒 Seguridad

Para reportar vulnerabilidades de seguridad, consulta [SECURITY.md](SECURITY.md).

---

## 📄 Licencia

Ver archivo [LICENSE](LICENSE) para más detalles.

---

## 👥 Autores

- **Desarrollador Principal** - *Trabajo inicial* - [Tu GitHub](https://github.com/tu-usuario)

---

## 🙏 Agradecimientos

- Comunidad Java
- Contribuidores del proyecto
- Usuarios que reportan issues y sugerencias

---

<div align="center">

**⭐ Si este proyecto te fue útil, considera darle una estrella ⭐**

Hecho con ☕ y Java

</div>

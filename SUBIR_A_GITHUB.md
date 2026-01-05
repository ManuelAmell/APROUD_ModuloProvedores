# 🚀 Subir Proyecto a GitHub

## Pasos Rápidos

### 1. Crear Repositorio en GitHub
1. Ve a https://github.com/new
2. Nombre: `ModuloProveedores`
3. Descripción: `Sistema de gestión de proveedores y compras con interfaz oscura profesional`
4. Público o Privado (tu elección)
5. **NO** marcar "Initialize with README"
6. Click en "Create repository"

### 2. Subir el Proyecto

**Opción A - Script Automático:**
```bash
bash COMANDOS_GITHUB.sh
```

**Opción B - Manual:**
```bash
git add .
git commit -m "Release v2.3.0: Sistema completo de items por compra"
git remote add origin https://github.com/TU-USUARIO/ModuloProveedores.git
git push -u origin main
```

### 3. Verificar
- ✅ README.md se ve correctamente
- ✅ NO se subieron archivos sensibles (CREDENCIALES_BASE_DATOS.txt)
- ✅ Todos los archivos importantes están presentes

## Después de Subir

1. **Crear Release v2.3.0**
   - Releases → New release
   - Tag: `v2.3.0`
   - Copiar descripción del CHANGELOG.md

2. **Agregar Topics**
   - Settings → Topics
   - Agregar: `java`, `swing`, `mysql`, `inventory-management`, `business-management`

3. **Compartir**
   - Copiar URL del repositorio
   - Compartir con colaboradores

## Archivos Importantes

- `README.md` - Documentación principal
- `MANUAL.md` - Manual de usuario
- `CHANGELOG.md` - Historial de cambios
- `PREPARAR_GITHUB.md` - Guía detallada

## Ayuda

Si tienes problemas, consulta `PREPARAR_GITHUB.md` para instrucciones detalladas.

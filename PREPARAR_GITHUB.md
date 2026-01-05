# 🚀 Preparar Proyecto para GitHub

Este documento contiene los pasos para subir el proyecto a GitHub.

## ✅ Checklist Pre-Subida

### 1. Archivos Sensibles Protegidos

El `.gitignore` ya está configurado para excluir:
- ✅ `CREDENCIALES_BASE_DATOS.txt`
- ✅ `RESUMEN_CAMBIO_CONTRASEÑAS.txt`
- ✅ `INSTRUCCIONES_CAMBIO_CONTRASEÑA.txt`
- ✅ Archivos temporales de desarrollo
- ✅ Logs y archivos compilados

### 2. Documentación Actualizada

- ✅ README.md - Completo y actualizado
- ✅ CHANGELOG.md - Versión 2.3.0 documentada
- ✅ MANUAL.md - Guía de usuario
- ✅ CONTRIBUTING.md - Guía de contribución
- ✅ LICENSE - Licencia MIT
- ✅ SECURITY.md - Política de seguridad

### 3. Código Limpio

- ✅ Código compilado y probado
- ✅ Sin contraseñas hardcodeadas (usar variables de entorno)
- ✅ Comentarios en español
- ✅ Estructura MVC clara

## 📋 Pasos para Subir a GitHub

### Paso 1: Verificar Estado del Repositorio

```bash
# Ver archivos que se subirán
git status

# Ver archivos ignorados
git status --ignored
```

### Paso 2: Agregar Archivos

```bash
# Agregar todos los archivos (respetando .gitignore)
git add .

# Verificar qué se agregó
git status
```

### Paso 3: Hacer Commit

```bash
# Commit con mensaje descriptivo
git commit -m "Release v2.3.0: Sistema completo de items por compra

- Tabla editable estilo Excel
- Modo visualización (solo lectura)
- Reloj en tiempo real
- Validaciones robustas
- Cálculo automático de totales
- Documentación completa actualizada"
```

### Paso 4: Crear Repositorio en GitHub

1. Ve a https://github.com/new
2. Nombre: `ModuloProveedores` o `SistemaGestionProveedores`
3. Descripción: "Sistema de gestión de proveedores y compras con interfaz oscura profesional"
4. Público o Privado (tu elección)
5. **NO** inicializar con README (ya lo tienes)
6. Crear repositorio

### Paso 5: Conectar y Subir

```bash
# Agregar remote (reemplaza TU-USUARIO con tu usuario de GitHub)
git remote add origin https://github.com/TU-USUARIO/ModuloProveedores.git

# Verificar remote
git remote -v

# Subir a GitHub
git push -u origin main

# Si tu rama se llama 'master' en lugar de 'main':
# git push -u origin master
```

### Paso 6: Verificar en GitHub

1. Ve a tu repositorio en GitHub
2. Verifica que todos los archivos estén presentes
3. Verifica que el README.md se vea correctamente
4. Verifica que NO estén los archivos sensibles

## 🔐 Seguridad

### Archivos que NO deben estar en GitHub:

- ❌ CREDENCIALES_BASE_DATOS.txt
- ❌ Contraseñas reales
- ❌ Archivos de configuración local
- ❌ Archivos compilados (bin/)
- ❌ Logs personales

### Verificar que no se subieron:

```bash
# Buscar en el repositorio remoto
git ls-files | grep -i credencial
git ls-files | grep -i contraseña
git ls-files | grep -i password

# No debe mostrar nada
```

## 📝 Después de Subir

### 1. Crear Release en GitHub

1. Ve a tu repositorio
2. Click en "Releases" → "Create a new release"
3. Tag: `v2.3.0`
4. Título: `Release v2.3.0 - Sistema de Items por Compra`
5. Descripción: Copiar del CHANGELOG.md
6. Publicar release

### 2. Actualizar README con Badges

Reemplaza en README.md:
```markdown
![Version](https://img.shields.io/badge/version-2.3.0-blue.svg)
```

Con tu URL real de GitHub.

### 3. Configurar GitHub Pages (Opcional)

Para documentación:
1. Settings → Pages
2. Source: Deploy from a branch
3. Branch: main → /docs
4. Save

### 4. Agregar Topics

En GitHub, agregar topics:
- `java`
- `swing`
- `mysql`
- `inventory-management`
- `business-management`
- `dark-theme`
- `spanish`

## 🔄 Actualizaciones Futuras

Para subir cambios futuros:

```bash
# 1. Hacer cambios en el código

# 2. Ver cambios
git status
git diff

# 3. Agregar cambios
git add .

# 4. Commit
git commit -m "Descripción del cambio"

# 5. Subir
git push
```

## 🆘 Solución de Problemas

### Error: "remote origin already exists"

```bash
git remote remove origin
git remote add origin https://github.com/TU-USUARIO/ModuloProveedores.git
```

### Error: "failed to push some refs"

```bash
# Traer cambios primero
git pull origin main --rebase

# Luego subir
git push origin main
```

### Subí un archivo sensible por error

```bash
# Eliminar del historial
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch ARCHIVO_SENSIBLE.txt" \
  --prune-empty --tag-name-filter cat -- --all

# Forzar push
git push origin --force --all
```

## ✨ Recomendaciones

1. **Usa .gitignore**: Ya está configurado, no lo modifiques sin revisar
2. **Commits descriptivos**: Explica qué cambios hiciste
3. **Branches para features**: Usa ramas para nuevas características
4. **Pull Requests**: Para cambios importantes
5. **Issues**: Documenta bugs y mejoras
6. **Wiki**: Considera usar GitHub Wiki para documentación extensa

## 📞 Ayuda

Si tienes problemas:
1. Revisa la documentación de Git: https://git-scm.com/doc
2. Revisa GitHub Docs: https://docs.github.com
3. Busca en Stack Overflow
4. Pregunta en la comunidad

---

**¡Listo para subir! 🚀**

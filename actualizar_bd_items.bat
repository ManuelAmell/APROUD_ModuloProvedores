@echo off
chcp 65001 > nul
color 0B

echo ==========================================
echo Actualización de Base de Datos
echo Sistema de Items por Compra
echo ==========================================
echo.
echo Este script creará la tabla 'items_compra' en la base de datos.
echo.
pause

REM Ejecutar script SQL
mysql -u root -p gestion_proveedores < db\update_items_compra.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ==========================================
    echo   ✓ Base de datos actualizada exitosamente
    echo ==========================================
    echo.
    echo La tabla 'items_compra' ha sido creada.
    echo Ahora puede usar el sistema de múltiples items por compra.
) else (
    echo.
    echo ==========================================
    echo   ✗ Error al actualizar la base de datos
    echo ==========================================
    echo.
    echo Verifique:
    echo   - MySQL está corriendo
    echo   - La contraseña de root es correcta
    echo   - La base de datos 'gestion_proveedores' existe
)

echo.
pause

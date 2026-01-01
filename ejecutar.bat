@echo off
REM ============================================================
REM Script para compilar y ejecutar el Modulo de Proveedores
REM Sistema: Windows
REM ============================================================

echo ========================================
echo   MODULO DE PROVEEDORES - Compilacion
echo ========================================
echo.

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

REM Compilar todos los archivos Java
echo Compilando archivos Java...
javac -d bin -encoding UTF-8 -cp "lib\*" src\modelo\*.java src\util\*.java src\dao\*.java src\servicio\*.java src\vista\*.java src\Main.java

REM Verificar si la compilacion fue exitosa
if %errorlevel% neq 0 (
    echo.
    echo ERROR: La compilacion fallo.
    echo Revisa los errores anteriores.
    pause
    exit /b 1
)

echo.
echo Compilacion exitosa!
echo.
echo ========================================
echo   Ejecutando aplicacion...
echo ========================================
echo.

REM Ejecutar la aplicacion con el driver MySQL en el classpath
java -cp "bin;lib\*" Main

echo.
pause

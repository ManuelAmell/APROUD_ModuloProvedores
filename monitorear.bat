@echo off
REM ==========================================
REM Script de Monitoreo de Recursos - Windows
REM Aproud - Proveedores y Compras
REM ==========================================

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo   MONITOR DE RECURSOS - APROUD
echo ==========================================
echo.

REM Buscar proceso Java
echo Buscando proceso de Aproud...
echo.

REM Obtener PID del proceso Java
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST ^| findstr /C:"PID:"') do (
    set PID=%%a
    goto :found
)

echo [ERROR] No se encontro el proceso de Aproud ejecutandose
echo.
echo Asegurate de que la aplicacion este ejecutandose:
echo   ejecutar.bat
echo.
pause
exit /b 1

:found
echo [OK] Proceso encontrado - PID: %PID%
echo.

REM Crear archivo de log
set LOGFILE=monitor_recursos_%date:~-4,4%%date:~-7,2%%date:~-10,2%_%time:~0,2%%time:~3,2%%time:~6,2%.txt
set LOGFILE=%LOGFILE: =0%

echo Iniciando monitoreo...
echo Presiona Ctrl+C para detener
echo.
echo Guardando datos en: %LOGFILE%
echo.

REM Encabezado del log
echo ========================================== > %LOGFILE%
echo MONITOREO DE RECURSOS - APROUD >> %LOGFILE%
echo ========================================== >> %LOGFILE%
echo. >> %LOGFILE%
echo Fecha: %date% %time% >> %LOGFILE%
echo PID: %PID% >> %LOGFILE%
echo. >> %LOGFILE%
echo ========================================== >> %LOGFILE%
echo. >> %LOGFILE%

REM Contador
set COUNT=0

:loop
set /a COUNT+=1

REM Obtener timestamp
set TIMESTAMP=%time%

REM Obtener información del proceso
echo [%TIMESTAMP%] Muestra #%COUNT%
echo [%TIMESTAMP%] Muestra #%COUNT% >> %LOGFILE%
echo ------------------------------------------ >> %LOGFILE%

REM Usar WMIC para obtener información detallada
wmic process where ProcessId=%PID% get WorkingSetSize,PageFileUsage,ThreadCount,HandleCount /format:list >> %LOGFILE% 2>nul

REM Mostrar en pantalla (simplificado)
for /f "tokens=*" %%i in ('wmic process where ProcessId^=%PID% get WorkingSetSize /format:value ^| findstr "="') do (
    set "%%i"
    set /a RAM_MB=!WorkingSetSize! / 1024 / 1024
    echo   RAM: !RAM_MB! MB
)

echo. >> %LOGFILE%

REM Esperar 2 segundos
timeout /t 2 /nobreak >nul

REM Verificar si el proceso sigue ejecutándose
tasklist /FI "PID eq %PID%" 2>nul | find "%PID%" >nul
if errorlevel 1 (
    echo.
    echo [INFO] El proceso ha finalizado
    echo. >> %LOGFILE%
    echo [INFO] El proceso ha finalizado >> %LOGFILE%
    goto :end
)

REM Continuar el loop
goto :loop

:end
echo.
echo ==========================================
echo   MONITOREO FINALIZADO
echo ==========================================
echo.
echo Resultados guardados en: %LOGFILE%
echo.

REM Calcular estadísticas básicas
echo ========================================== >> %LOGFILE%
echo RESUMEN >> %LOGFILE%
echo ========================================== >> %LOGFILE%
echo Total de muestras: %COUNT% >> %LOGFILE%
echo Duracion: Aproximadamente %COUNT% x 2 segundos >> %LOGFILE%
echo. >> %LOGFILE%

echo Resumen:
echo   Total de muestras: %COUNT%
echo   Archivo de log: %LOGFILE%
echo.

REM Abrir el archivo de log
echo Deseas abrir el archivo de log? (S/N)
set /p ABRIR=
if /i "%ABRIR%"=="S" (
    start notepad %LOGFILE%
)

pause

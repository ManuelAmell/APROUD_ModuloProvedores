MÓDULO DE PROVEEDORES - Versión Windows
========================================

REQUISITOS:
- Java 11 o superior instalado
- MySQL/MariaDB corriendo localmente

EJECUCIÓN:
1. Doble clic en ejecutar.bat
   O
2. Abrir CMD y ejecutar: java -jar ModuloProveedores.jar

NOTA: Para crear un .exe nativo, ejecute en Windows:
jpackage --input . --dest output --name ModuloProveedores ^
  --main-jar ModuloProveedores.jar --main-class Main --type exe

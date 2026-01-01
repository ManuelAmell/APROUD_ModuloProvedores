/*
 * ============================================================
 * CLASE: ConexionBD.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Clase utilitaria que maneja la conexión a la base de datos MySQL.
 * Implementa el patrón SINGLETON para asegurar que solo exista
 * una instancia de conexión en toda la aplicación.
 * 
 * ¿QUÉ ES EL PATRÓN SINGLETON?
 * Es un patrón de diseño que garantiza que una clase tenga solo
 * una instancia y proporciona un punto de acceso global a ella.
 * 
 * ¿POR QUÉ USARLO PARA LA CONEXIÓN?
 * - Evita abrir múltiples conexiones innecesarias
 * - Centraliza la configuración de la base de datos
 * - Facilita el manejo de recursos
 * 
 * CONFIGURACIÓN:
 * Antes de usar, asegúrate de:
 * 1. Tener MySQL instalado y corriendo
 * 2. Haber ejecutado el script db/schema.sql
 * 3. Actualizar las credenciales si es necesario
 * 
 * ============================================================
 */

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para manejar la conexión a MySQL.
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class ConexionBD {

    // ========================================================
    // CONFIGURACIÓN DE LA BASE DE DATOS
    // ========================================================

    /*
     * Estos valores definen cómo conectarse a MySQL.
     * Modifica según tu configuración local.
     */

    // URL de conexión JDBC
    // Formato: jdbc:mysql://servidor:puerto/base_de_datos
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_proveedores";

    // Usuario de la base de datos
    private static final String USUARIO = "proveedor_app";

    // Contraseña del usuario
    private static final String PASSWORD = "proveedor123";

    // ========================================================
    // IMPLEMENTACIÓN DEL SINGLETON
    // ========================================================

    /*
     * 'instance' guarda la única instancia de la clase.
     * 'volatile' asegura visibilidad entre hilos.
     */
    private static volatile ConexionBD instance;

    // La conexión a la base de datos
    private Connection conexion;

    /**
     * Constructor privado - nadie fuera puede crear instancias.
     * Esto es clave para el patrón Singleton.
     */
    private ConexionBD() {
        // El constructor está vacío porque la conexión
        // se crea bajo demanda en getConexion()
    }

    /**
     * Obtiene la única instancia de ConexionBD.
     * 
     * Usa "double-checked locking" para seguridad en hilos.
     * 
     * @return La instancia única de ConexionBD
     */
    public static ConexionBD getInstance() {
        // Primera verificación (sin sincronización)
        if (instance == null) {
            // Sincronizamos solo si es necesario crear
            synchronized (ConexionBD.class) {
                // Segunda verificación (con sincronización)
                if (instance == null) {
                    instance = new ConexionBD();
                }
            }
        }
        return instance;
    }

    /**
     * Obtiene la conexión a la base de datos.
     * 
     * Si la conexión no existe o está cerrada, la crea.
     * 
     * @return Objeto Connection para interactuar con MySQL
     * @throws SQLException Si hay error al conectar
     */
    public Connection getConexion() throws SQLException {
        // Verificamos si necesitamos crear una nueva conexión
        if (conexion == null || conexion.isClosed()) {
            try {
                /*
                 * Class.forName() carga el driver JDBC de MySQL.
                 * Esto es necesario en versiones antiguas de Java.
                 * En Java 6+ con JDBC 4.0, es opcional pero no hace daño.
                 */
                Class.forName("com.mysql.cj.jdbc.Driver");

                /*
                 * DriverManager.getConnection() establece la conexión.
                 * Parámetros:
                 * - URL: dónde está la base de datos
                 * - USUARIO: nombre de usuario
                 * - PASSWORD: contraseña
                 */
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);

                System.out.println("✓ Conexión a MySQL establecida correctamente");

            } catch (ClassNotFoundException e) {
                /*
                 * Este error ocurre si el driver JDBC no está
                 * en el classpath.
                 * 
                 * Solución: Agregar mysql-connector-j-X.X.X.jar
                 * al classpath del proyecto.
                 */
                System.err.println("ERROR: Driver MySQL no encontrado.");
                System.err.println("Asegúrate de agregar mysql-connector-j al classpath.");
                throw new SQLException("Driver no encontrado: " + e.getMessage());
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos.
     * 
     * Debe llamarse cuando la aplicación termine
     * para liberar recursos.
     */
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("✓ Conexión a MySQL cerrada");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica si la conexión está activa.
     * 
     * @return true si hay conexión activa, false si no
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

/*
 * ============================================================
 * CLASE: ProveedorDAOMySQL.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Implementación del DAO de Proveedores usando MySQL.
 * Esta clase realiza las operaciones CRUD directamente
 * contra la base de datos MySQL.
 * 
 * ¿QUÉ ES JDBC?
 * Java Database Connectivity (JDBC) es la API estándar
 * de Java para conectarse a bases de datos relacionales.
 * 
 * CLASES PRINCIPALES DE JDBC:
 * - Connection: Representa la conexión a la BD
 * - PreparedStatement: Ejecuta consultas SQL parametrizadas
 * - ResultSet: Contiene los resultados de una consulta
 * 
 * ¿POR QUÉ PreparedStatement EN VEZ DE Statement?
 * - Previene SQL Injection (ataques de seguridad)
 * - Mejor rendimiento con consultas repetidas
 * - Manejo automático de tipos de datos
 * 
 * ============================================================
 */

package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modelo.Proveedor;
import util.ConexionBD;

/**
 * Implementación de ProveedorDAO usando MySQL.
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class ProveedorDAOMySQL implements ProveedorDAO {

    // ========================================================
    // ATRIBUTOS
    // ========================================================

    /**
     * Referencia al singleton de conexión.
     */
    private final ConexionBD conexionBD;

    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    /**
     * Constructor que obtiene la instancia del singleton.
     */
    public ProveedorDAOMySQL() {
        this.conexionBD = ConexionBD.getInstance();
    }

    // ========================================================
    // IMPLEMENTACIÓN DE MÉTODOS CRUD
    // ========================================================

    /**
     * {@inheritDoc}
     * 
     * Inserta un nuevo proveedor en la base de datos.
     */
    @Override
    public boolean insertar(Proveedor proveedor) {
        /*
         * SQL INSERT: Agrega un nuevo registro a la tabla.
         * 
         * Los '?' son PLACEHOLDERS (marcadores de posición)
         * que serán reemplazados por valores reales de forma
         * segura usando setXxx() del PreparedStatement.
         */
        String sql = "INSERT INTO proveedores (nombre, nit, direccion, telefono, email, persona_contacto, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        /*
         * TRY-WITH-RESOURCES:
         * Esta sintaxis asegura que los recursos (Connection,
         * PreparedStatement) se cierren automáticamente al
         * terminar, incluso si hay excepciones.
         * 
         * Statement.RETURN_GENERATED_KEYS indica que queremos
         * obtener el ID autogenerado después del INSERT.
         */
        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            /*
             * setXxx(posición, valor): Asigna valores a los placeholders.
             * Las posiciones empiezan en 1 (no en 0).
             */
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getEmail());
            stmt.setString(6, proveedor.getPersonaContacto());
            stmt.setBoolean(7, proveedor.isActivo());

            /*
             * executeUpdate(): Ejecuta INSERT, UPDATE o DELETE.
             * Retorna el número de filas afectadas.
             */
            int filasAfectadas = stmt.executeUpdate();

            // Obtener el ID generado
            if (filasAfectadas > 0) {
                /*
                 * getGeneratedKeys(): Obtiene las claves generadas
                 * (en este caso, el ID autoincremental).
                 */
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        proveedor.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * Obtiene un proveedor por su ID.
     */
    @Override
    public Proveedor obtenerPorId(int id) {
        String sql = "SELECT * FROM proveedores WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            /*
             * executeQuery(): Ejecuta SELECT y retorna ResultSet.
             * ResultSet es como un cursor que recorre los resultados.
             */
            try (ResultSet rs = stmt.executeQuery()) {
                /*
                 * rs.next(): Avanza al siguiente registro.
                 * Retorna true si hay más registros, false si no.
                 */
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * Obtiene todos los proveedores de la base de datos.
     */
    @Override
    public List<Proveedor> obtenerTodos() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            /*
             * Recorremos todos los resultados con un while.
             * Cada iteración procesa una fila de la tabla.
             */
            while (rs.next()) {
                proveedores.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los proveedores: " + e.getMessage());
            e.printStackTrace();
        }

        return proveedores;
    }

    /**
     * {@inheritDoc}
     * 
     * Actualiza los datos de un proveedor existente.
     */
    @Override
    public boolean actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre = ?, nit = ?, direccion = ?, " +
                "telefono = ?, email = ?, persona_contacto = ?, activo = ? " +
                "WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getEmail());
            stmt.setString(6, proveedor.getPersonaContacto());
            stmt.setBoolean(7, proveedor.isActivo());
            stmt.setInt(8, proveedor.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * Elimina un proveedor de la base de datos.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * Busca proveedores cuyo nombre contenga el texto dado.
     */
    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        List<Proveedor> proveedores = new ArrayList<>();

        /*
         * LIKE con %: Busca coincidencias parciales.
         * %valor% encuentra el valor en cualquier posición.
         */
        String sql = "SELECT * FROM proveedores WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Agregamos % antes y después para buscar en cualquier posición
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    proveedores.add(mapearResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar proveedores por nombre: " + e.getMessage());
            e.printStackTrace();
        }

        return proveedores;
    }

    /**
     * {@inheritDoc}
     * 
     * Obtiene solo los proveedores activos.
     */
    @Override
    public List<Proveedor> obtenerActivos() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores WHERE activo = 1 ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                proveedores.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proveedores activos: " + e.getMessage());
            e.printStackTrace();
        }

        return proveedores;
    }

    /**
     * {@inheritDoc}
     * 
     * Busca un proveedor por su NIT exacto.
     */
    @Override
    public Proveedor buscarPorNit(String nit) {
        String sql = "SELECT * FROM proveedores WHERE nit = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nit);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar proveedor por NIT: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // ========================================================
    // MÉTODOS AUXILIARES
    // ========================================================

    /**
     * Convierte una fila del ResultSet a un objeto Proveedor.
     * 
     * Este método es privado porque solo se usa internamente.
     * Evita duplicar código en cada método que lee proveedores.
     * 
     * @param rs El ResultSet posicionado en una fila
     * @return Un objeto Proveedor con los datos de la fila
     * @throws SQLException Si hay error al leer los datos
     */
    private Proveedor mapearResultSet(ResultSet rs) throws SQLException {
        /*
         * Creamos un proveedor usando el constructor vacío
         * y luego asignamos cada campo con los setters.
         * 
         * rs.getInt("columna"): Obtiene un entero
         * rs.getString("columna"): Obtiene un String
         * rs.getBoolean("columna"): Obtiene un boolean
         */
        Proveedor proveedor = new Proveedor();
        proveedor.setId(rs.getInt("id"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setNit(rs.getString("nit"));
        proveedor.setDireccion(rs.getString("direccion"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setEmail(rs.getString("email"));
        proveedor.setPersonaContacto(rs.getString("persona_contacto"));
        proveedor.setActivo(rs.getBoolean("activo"));

        return proveedor;
    }
}

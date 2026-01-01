/*
 * ============================================================
 * CLASE: ProveedorService.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Esta es la capa de SERVICIO o LÓGICA DE NEGOCIO.
 * Actúa como intermediario entre la interfaz gráfica (vista)
 * y el acceso a datos (DAO).
 * 
 * ¿POR QUÉ UNA CAPA DE SERVICIO?
 * 
 * Arquitectura de 3 capas:
 * 
 *   [VISTA/GUI] --> [SERVICIO] --> [DAO] --> [Base de Datos]
 * 
 * 1. VISTA (GUI): Lo que el usuario ve y con lo que interactúa
 * 2. SERVICIO: Lógica de negocio, validaciones, reglas
 * 3. DAO: Acceso a datos, operaciones con base de datos
 * 
 * BENEFICIOS:
 * - Separación de responsabilidades
 * - Código más organizado y mantenible
 * - Fácil de modificar una capa sin afectar las otras
 * - Las validaciones están centralizadas
 * 
 * ============================================================
 */

package servicio;

// Importación de clases necesarias
import java.util.List;

// Importamos el DAO y su implementación MySQL
import dao.ProveedorDAO;
import dao.ProveedorDAOMySQL;

// Importamos el modelo
import modelo.Proveedor;

/**
 * Servicio que maneja la lógica de negocio para Proveedores.
 * 
 * Esta clase:
 * - Valida los datos antes de enviarlos al DAO
 * - Aplica reglas de negocio
 * - Proporciona una interfaz simplificada a la capa de vista
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class ProveedorService {

    // ========================================================
    // ATRIBUTOS
    // ========================================================

    /**
     * Referencia al DAO para acceder a los datos.
     * 
     * Usamos la INTERFAZ como tipo (ProveedorDAO) en lugar
     * de la implementación concreta (ProveedorDAOMySQL).
     * 
     * ¿Por qué?
     * Esto se llama "programar hacia interfaces" y permite:
     * - Cambiar la implementación fácilmente
     * - Crear implementaciones de prueba (mocks)
     * - Mayor flexibilidad
     */
    private final ProveedorDAO proveedorDAO;

    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    /**
     * Constructor que inicializa el servicio con un DAO MySQL.
     * 
     * Creamos una instancia de ProveedorDAOMySQL pero la
     * guardamos en una variable de tipo ProveedorDAO.
     * 
     * Esto es posible gracias al POLIMORFISMO: una variable
     * de tipo interfaz puede referenciar cualquier objeto
     * que implemente esa interfaz.
     */
    public ProveedorService() {
        // Usamos la implementación MySQL del DAO
        this.proveedorDAO = new ProveedorDAOMySQL();
    }

    // ========================================================
    // MÉTODOS DE SERVICIO
    // ========================================================

    /**
     * Registra un nuevo proveedor en el sistema.
     * 
     * Este método valida los datos antes de guardarlos.
     * 
     * @param proveedor El proveedor a registrar
     * @return Mensaje indicando el resultado de la operación
     */
    public String registrarProveedor(Proveedor proveedor) {
        /*
         * VALIDACIONES DE NEGOCIO:
         * 
         * Antes de guardar, verificamos que los datos sean válidos.
         * Si hay un error, retornamos un mensaje explicativo.
         */

        // Validar que el nombre no esté vacío
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            /*
             * trim() elimina espacios al inicio y final del texto
             * "  hola  ".trim() -> "hola"
             * "   ".trim() -> "" (vacío)
             * 
             * isEmpty() verifica si el String está vacío
             */
            return "Error: El nombre del proveedor es obligatorio";
        }

        // Validar que el NIT no esté vacío
        if (proveedor.getNit() == null || proveedor.getNit().trim().isEmpty()) {
            return "Error: El NIT es obligatorio";
        }

        // Validar que el NIT no esté repetido
        // Usamos el DAO para verificar si ya existe
        if (proveedorDAO.buscarPorNit(proveedor.getNit()) != null) {
            return "Error: Ya existe un proveedor con el NIT " + proveedor.getNit();
        }

        // Validar formato de email si se proporcionó
        if (proveedor.getEmail() != null && !proveedor.getEmail().isEmpty()) {
            /*
             * Expresión regular para validar email:
             * ^[\w.-]+ : Inicio con letras, números, punto o guion
             * 
             * @ : Debe contener arroba
             * [\w.-]+ : Dominio con letras, números, punto o guion
             * \. : Un punto
             * [a-zA-Z]{2,} : Extensión de al menos 2 letras
             */
            String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
            if (!proveedor.getEmail().matches(emailRegex)) {
                return "Error: El formato del email no es válido";
            }
        }

        // Si pasa todas las validaciones, insertamos
        boolean exito = proveedorDAO.insertar(proveedor);

        // Retornamos mensaje según el resultado
        if (exito) {
            return "Proveedor registrado exitosamente con ID: " + proveedor.getId();
        } else {
            return "Error: No se pudo registrar el proveedor";
        }
    }

    /**
     * Obtiene un proveedor por su ID.
     * 
     * @param id El ID del proveedor
     * @return El proveedor encontrado o null
     */
    public Proveedor obtenerProveedor(int id) {
        // Delegamos al DAO sin lógica adicional
        return proveedorDAO.obtenerPorId(id);
    }

    /**
     * Obtiene todos los proveedores.
     * 
     * @return Lista de todos los proveedores
     */
    public List<Proveedor> obtenerTodosProveedores() {
        return proveedorDAO.obtenerTodos();
    }

    /**
     * Obtiene solo los proveedores activos.
     * 
     * @return Lista de proveedores activos
     */
    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorDAO.obtenerActivos();
    }

    /**
     * Actualiza los datos de un proveedor.
     * 
     * @param proveedor El proveedor con los datos actualizados
     * @return Mensaje indicando el resultado
     */
    public String actualizarProveedor(Proveedor proveedor) {
        // Validar que el proveedor exista
        if (proveedorDAO.obtenerPorId(proveedor.getId()) == null) {
            return "Error: No existe un proveedor con ID " + proveedor.getId();
        }

        // Validar nombre
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            return "Error: El nombre del proveedor es obligatorio";
        }

        // Validar NIT
        if (proveedor.getNit() == null || proveedor.getNit().trim().isEmpty()) {
            return "Error: El NIT es obligatorio";
        }

        // Verificar que el NIT no esté usado por otro proveedor
        Proveedor existente = proveedorDAO.buscarPorNit(proveedor.getNit());
        if (existente != null && existente.getId() != proveedor.getId()) {
            return "Error: El NIT " + proveedor.getNit() + " ya está en uso por otro proveedor";
        }

        // Actualizar
        boolean exito = proveedorDAO.actualizar(proveedor);

        if (exito) {
            return "Proveedor actualizado exitosamente";
        } else {
            return "Error: No se pudo actualizar el proveedor";
        }
    }

    /**
     * Elimina un proveedor del sistema.
     * 
     * @param id El ID del proveedor a eliminar
     * @return Mensaje indicando el resultado
     */
    public String eliminarProveedor(int id) {
        // Verificar que existe
        Proveedor proveedor = proveedorDAO.obtenerPorId(id);
        if (proveedor == null) {
            return "Error: No existe un proveedor con ID " + id;
        }

        // Intentar eliminar
        boolean exito = proveedorDAO.eliminar(id);

        if (exito) {
            return "Proveedor '" + proveedor.getNombre() + "' eliminado exitosamente";
        } else {
            return "Error: No se pudo eliminar el proveedor";
        }
    }

    /**
     * Busca proveedores por nombre.
     * 
     * @param nombre El texto a buscar en el nombre
     * @return Lista de proveedores que coinciden
     */
    public List<Proveedor> buscarPorNombre(String nombre) {
        // Validar que se proporcione un término de búsqueda
        if (nombre == null || nombre.trim().isEmpty()) {
            // Si no hay búsqueda, devolvemos todos
            return proveedorDAO.obtenerTodos();
        }

        return proveedorDAO.buscarPorNombre(nombre.trim());
    }

    /**
     * Activa o desactiva un proveedor.
     * 
     * Este es un método de conveniencia para cambiar solo
     * el estado activo sin modificar otros datos.
     * 
     * @param id     El ID del proveedor
     * @param activo El nuevo estado
     * @return Mensaje indicando el resultado
     */
    public String cambiarEstado(int id, boolean activo) {
        // Obtener el proveedor
        Proveedor proveedor = proveedorDAO.obtenerPorId(id);

        if (proveedor == null) {
            return "Error: No existe un proveedor con ID " + id;
        }

        // Cambiar estado
        proveedor.setActivo(activo);

        // Guardar cambios
        boolean exito = proveedorDAO.actualizar(proveedor);

        if (exito) {
            String estado = activo ? "activado" : "desactivado";
            return "Proveedor " + estado + " exitosamente";
        } else {
            return "Error: No se pudo cambiar el estado del proveedor";
        }
    }
}

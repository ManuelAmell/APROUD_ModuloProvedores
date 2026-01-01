/*
 * ============================================================
 * INTERFAZ: ProveedorDAO.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Esta es una INTERFAZ que define las operaciones que podemos
 * realizar con los proveedores en la base de datos.
 * 
 * ¿QUÉ ES UNA INTERFAZ?
 * Una interfaz es como un "contrato" o "plantilla" que define
 * QUÉ métodos debe tener una clase, pero NO CÓMO implementarlos.
 * 
 * Es como un plano arquitectónico: te dice qué habitaciones
 * debe tener la casa, pero no cómo construirlas.
 * 
 * ¿QUÉ ES DAO?
 * DAO significa "Data Access Object" (Objeto de Acceso a Datos).
 * Es un PATRÓN DE DISEÑO que separa la lógica de acceso a datos
 * de la lógica de negocio.
 * 
 * En lugar de mezclar código SQL con la lógica de la aplicación,
 * ponemos todo el acceso a base de datos en una capa separada.
 * 
 * ¿POR QUÉ USAR UNA INTERFAZ PARA DAO?
 * 1. FLEXIBILIDAD: Podemos cambiar cómo guardamos los datos
 *    (MySQL, PostgreSQL, archivos, memoria) sin cambiar el
 *    resto de la aplicación.
 * 
 * 2. PRUEBAS: Podemos crear versiones "falsas" del DAO para
 *    probar nuestra aplicación sin necesitar una base de datos.
 * 
 * 3. ABSTRACCIÓN: Ocultamos los detalles de implementación.
 * 
 * OPERACIONES CRUD:
 * Las interfaces DAO típicamente definen operaciones CRUD:
 * - Create (Crear) -> insertar()
 * - Read (Leer) -> obtenerPorId(), obtenerTodos()
 * - Update (Actualizar) -> actualizar()
 * - Delete (Eliminar) -> eliminar()
 * 
 * ============================================================
 */

// Paquete donde se encuentra esta interfaz
package dao;

// Importamos las clases que necesitamos usar

// List es una interfaz de Java que representa una lista ordenada de elementos
// Está en el paquete java.util (utilidades de Java)
import java.util.List;

// Importamos nuestra clase Proveedor del paquete modelo
// Esto nos permite usar Proveedor como tipo de dato
import modelo.Proveedor;

/**
 * Interfaz que define las operaciones de acceso a datos para Proveedor.
 * 
 * PALABRA CLAVE "interface":
 * - Define un tipo que solo contiene declaraciones de métodos
 * - Los métodos son implícitamente "public abstract"
 * - No se puede instanciar directamente
 * - Una clase puede implementar múltiples interfaces
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public interface ProveedorDAO {

    /*
     * NOTA IMPORTANTE:
     * 
     * Los métodos en una interfaz son automáticamente:
     * - public (accesibles desde cualquier lugar)
     * - abstract (no tienen cuerpo/implementación)
     * 
     * Por eso, escribir:
     * void insertar(Proveedor proveedor);
     * 
     * Es lo mismo que escribir:
     * public abstract void insertar(Proveedor proveedor);
     * 
     * La forma corta es la preferida por convención.
     */

    // ========================================================
    // OPERACIONES CRUD (Create, Read, Update, Delete)
    // ========================================================

    /**
     * CREAR - Inserta un nuevo proveedor en la base de datos.
     * 
     * Este método toma un objeto Proveedor y lo guarda de forma
     * persistente (permanente) en la base de datos.
     * 
     * @param proveedor El proveedor a insertar
     * @return true si se insertó correctamente, false si hubo error
     * 
     *         Ejemplo de uso (cuando tengamos la implementación):
     *         Proveedor nuevo = new Proveedor(0, "Mi Empresa", "900123456");
     *         boolean exito = proveedorDAO.insertar(nuevo);
     */
    boolean insertar(Proveedor proveedor);

    /**
     * LEER UNO - Obtiene un proveedor por su ID.
     * 
     * Busca en la base de datos el proveedor que tenga el ID
     * especificado y lo devuelve como objeto.
     * 
     * @param id El identificador único del proveedor
     * @return El proveedor encontrado, o null si no existe
     * 
     *         Ejemplo de uso:
     *         Proveedor p = proveedorDAO.obtenerPorId(5);
     *         if (p != null) {
     *         System.out.println("Encontrado: " + p.getNombre());
     *         }
     */
    Proveedor obtenerPorId(int id);

    /**
     * LEER TODOS - Obtiene todos los proveedores.
     * 
     * @return Lista con todos los proveedores de la base de datos
     * 
     *         ¿POR QUÉ DEVOLVEMOS List<Proveedor>?
     *         List es una interfaz de Java Collections que representa
     *         una colección ordenada de elementos.
     * 
     *         El <Proveedor> es un GENÉRICO que indica que la lista
     *         contendrá objetos de tipo Proveedor.
     * 
     *         Ejemplo de uso:
     *         List<Proveedor> todos = proveedorDAO.obtenerTodos();
     *         for (Proveedor p : todos) {
     *         System.out.println(p.getNombre());
     *         }
     */
    List<Proveedor> obtenerTodos();

    /**
     * ACTUALIZAR - Modifica un proveedor existente.
     * 
     * Toma un objeto Proveedor con datos actualizados y los
     * guarda en la base de datos. El ID determina cuál
     * registro se actualiza.
     * 
     * @param proveedor El proveedor con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     * 
     *         Ejemplo de uso:
     *         Proveedor p = proveedorDAO.obtenerPorId(5);
     *         p.setNombre("Nuevo Nombre");
     *         p.setTelefono("3001234567");
     *         boolean exito = proveedorDAO.actualizar(p);
     */
    boolean actualizar(Proveedor proveedor);

    /**
     * ELIMINAR - Borra un proveedor de la base de datos.
     * 
     * Elimina permanentemente el proveedor con el ID especificado.
     * 
     * @param id El identificador del proveedor a eliminar
     * @return true si se eliminó correctamente, false si hubo error
     * 
     *         Ejemplo de uso:
     *         boolean eliminado = proveedorDAO.eliminar(5);
     *         if (eliminado) {
     *         System.out.println("Proveedor eliminado con éxito");
     *         }
     */
    boolean eliminar(int id);

    // ========================================================
    // OPERACIONES ADICIONALES (No son CRUD básico)
    // ========================================================

    /**
     * Busca proveedores por nombre.
     * 
     * Útil para implementar una barra de búsqueda donde el
     * usuario escribe parte del nombre.
     * 
     * @param nombre Texto a buscar en el nombre (búsqueda parcial)
     * @return Lista de proveedores que coinciden con la búsqueda
     * 
     *         Ejemplo de uso:
     *         List<Proveedor> resultados = proveedorDAO.buscarPorNombre("distri");
     *         // Encontrará: "Distribuidora ABC", "Distri Norte", etc.
     */
    List<Proveedor> buscarPorNombre(String nombre);

    /**
     * Obtiene solo los proveedores activos.
     * 
     * Muchas veces no queremos mostrar proveedores que ya
     * no trabajamos con ellos (inactivos).
     * 
     * @return Lista de proveedores donde activo = true
     */
    List<Proveedor> obtenerActivos();

    /**
     * Busca un proveedor por su NIT.
     * 
     * Como el NIT es único para cada empresa, solo puede
     * devolver un proveedor o null si no existe.
     * 
     * @param nit El NIT a buscar
     * @return El proveedor con ese NIT, o null si no existe
     */
    Proveedor buscarPorNit(String nit);
}

/*
 * ============================================================
 * CLASE: ProveedorDAOImpl.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Esta clase IMPLEMENTA la interfaz ProveedorDAO.
 * Aquí escribimos el código real que hace las operaciones
 * con la base de datos.
 * 
 * ¿QUÉ SIGNIFICA "IMPLEMENTS"?
 * Cuando una clase "implementa" una interfaz, se compromete
 * a proporcionar código real para todos los métodos que
 * la interfaz define.
 * 
 * Es como firmar un contrato: la interfaz dice "debes tener
 * estos métodos" y la clase implementadora dice "aquí están".
 * 
 * SOBRE ESTA IMPLEMENTACIÓN:
 * Esta es una implementación EN MEMORIA usando ArrayList.
 * Esto significa que los datos se pierden cuando el programa
 * termina. Es útil para:
 * - Aprender los conceptos sin preocuparse por bases de datos
 * - Probar el sistema antes de conectar a una base de datos real
 * - Prototipos rápidos
 * 
 * Más adelante puedes crear otra implementación que use MySQL,
 * PostgreSQL, o cualquier otra base de datos real.
 * 
 * ============================================================
 */

package dao;

// Importamos las clases de Java que necesitamos

// ArrayList: Una implementación de List que usa un array dinámico
// Es como una lista que crece automáticamente
import java.util.ArrayList;

// List: Interfaz que representa una lista ordenada
import java.util.List;

// Importamos nuestras clases
import modelo.Proveedor;

/**
 * Implementación de ProveedorDAO que almacena datos en memoria.
 * 
 * PALABRA CLAVE "implements":
 * - Indica que esta clase va a proporcionar código para
 * todos los métodos de la interfaz ProveedorDAO.
 * - Si no implementamos algún método, el código no compila.
 * 
 * Esta clase demuestra:
 * - Cómo implementar una interfaz
 * - Uso de ArrayList para almacenar objetos
 * - Búsqueda y manipulación de listas
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class ProveedorDAOImpl implements ProveedorDAO {

    // ========================================================
    // ATRIBUTOS DE LA CLASE
    // ========================================================

    /**
     * Lista donde almacenamos los proveedores.
     * 
     * ArrayList<Proveedor> significa:
     * - ArrayList: tipo de lista que usamos (array dinámico)
     * - <Proveedor>: solo puede contener objetos Proveedor
     * 
     * "private" porque nadie fuera de esta clase debe
     * manipular directamente esta lista.
     * 
     * "final" porque la referencia a la lista no cambiará
     * (aunque su contenido sí puede cambiar).
     */
    private final List<Proveedor> proveedores;

    /**
     * Contador para generar IDs únicos.
     * 
     * Cada vez que creamos un proveedor, incrementamos este
     * contador y usamos su valor como ID.
     * 
     * En una base de datos real, esto se haría automáticamente
     * con AUTO_INCREMENT.
     */
    private int contadorId;

    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    /**
     * Constructor que inicializa la lista de proveedores.
     * 
     * Creamos un nuevo ArrayList vacío donde guardaremos
     * los proveedores y establecemos el contador en 0.
     */
    public ProveedorDAOImpl() {
        // Creamos la lista vacía
        // "new ArrayList<>()" crea una nueva instancia de ArrayList
        // Los <> vacíos (diamond operator) toman el tipo de la variable
        this.proveedores = new ArrayList<>();

        // Iniciamos el contador en 0, el primer proveedor será 1
        this.contadorId = 0;

        // Agregamos algunos proveedores de ejemplo para probar
        agregarProveedoresDeEjemplo();
    }

    /**
     * Método privado que agrega datos de ejemplo.
     * 
     * Es "private" porque solo lo usamos internamente para
     * inicializar la lista con datos de prueba.
     */
    private void agregarProveedoresDeEjemplo() {
        /*
         * Usamos el método insertar() que definimos abajo
         * para agregar proveedores de ejemplo.
         * 
         * Esto nos ayuda a probar el sistema sin tener
         * que agregar proveedores manualmente cada vez.
         */

        // Proveedor 1
        Proveedor p1 = new Proveedor();
        p1.setNombre("Distribuidora ABC");
        p1.setNit("900123456-1");
        p1.setDireccion("Calle 50 #30-25, Bogotá");
        p1.setTelefono("601-555-0100");
        p1.setEmail("ventas@distribuidoraabc.com");
        p1.setPersonaContacto("María García");
        p1.setActivo(true);
        insertar(p1);

        // Proveedor 2
        Proveedor p2 = new Proveedor();
        p2.setNombre("Suministros del Norte");
        p2.setNit("800987654-2");
        p2.setDireccion("Av. Libertador 150, Barranquilla");
        p2.setTelefono("605-555-0200");
        p2.setEmail("contacto@suministrosnorte.com");
        p2.setPersonaContacto("Carlos Rodríguez");
        p2.setActivo(true);
        insertar(p2);

        // Proveedor 3 (inactivo para probar el filtro)
        Proveedor p3 = new Proveedor();
        p3.setNombre("Importadora XYZ");
        p3.setNit("700555888-3");
        p3.setDireccion("Zona Franca, Cartagena");
        p3.setTelefono("605-555-0300");
        p3.setEmail("info@importadoraxyz.com");
        p3.setPersonaContacto("Ana Martínez");
        p3.setActivo(false); // Este está inactivo
        insertar(p3);
    }

    // ========================================================
    // IMPLEMENTACIÓN DE MÉTODOS DE LA INTERFAZ
    // ========================================================

    /*
     * La anotación @Override indica que estamos implementando
     * un método definido en la interfaz (o clase padre).
     * 
     * Si escribimos mal el nombre del método, el compilador
     * nos avisará gracias a esta anotación.
     */

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * 1. Generamos un nuevo ID único
     * 2. Lo asignamos al proveedor
     * 3. Agregamos el proveedor a la lista
     */
    @Override
    public boolean insertar(Proveedor proveedor) {
        /*
         * "try-catch" nos permite manejar errores que puedan
         * ocurrir durante la ejecución del código.
         * 
         * Si algo falla dentro del "try", el código salta
         * al bloque "catch" en lugar de detener el programa.
         */
        try {
            // Incrementamos el contador antes de usarlo
            // ++contadorId primero incrementa, luego usa el valor
            contadorId++;

            // Asignamos el nuevo ID al proveedor
            proveedor.setId(contadorId);

            // Agregamos el proveedor a nuestra lista
            // add() es un método de ArrayList que agrega un elemento al final
            proveedores.add(proveedor);

            // Retornamos true para indicar éxito
            return true;

        } catch (Exception e) {
            /*
             * Si algo falla, capturamos la excepción.
             * 'e' es el objeto Exception que contiene información del error.
             * 
             * printStackTrace() imprime el error en la consola
             * para que podamos ver qué salió mal.
             */
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            e.printStackTrace();

            // Retornamos false para indicar que hubo un error
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Recorremos la lista buscando el proveedor con el ID dado.
     * Usamos un bucle for-each para iterar sobre cada elemento.
     */
    @Override
    public Proveedor obtenerPorId(int id) {
        /*
         * FOR-EACH LOOP (bucle para cada):
         * 
         * for (Proveedor p : proveedores)
         * 
         * Esto se lee como: "para cada Proveedor p en proveedores"
         * 
         * En cada iteración, 'p' toma el valor del siguiente
         * proveedor en la lista, hasta recorrer todos.
         */
        for (Proveedor proveedor : proveedores) {
            // Comparamos el ID del proveedor actual con el buscado
            if (proveedor.getId() == id) {
                // ¡Lo encontramos! Lo retornamos
                return proveedor;
            }
        }

        // Si llegamos aquí, no encontramos ningún proveedor con ese ID
        // Retornamos null para indicar "no encontrado"
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Creamos una copia de la lista para no exponer la original.
     * Esto es una buena práctica de encapsulamiento.
     */
    @Override
    public List<Proveedor> obtenerTodos() {
        /*
         * ¿Por qué creamos una nueva lista?
         * 
         * Si devolviéramos directamente 'proveedores', quien
         * llame a este método podría modificar nuestra lista
         * interna, rompiendo el encapsulamiento.
         * 
         * Al devolver una COPIA, protegemos nuestros datos.
         * 
         * new ArrayList<>(proveedores) crea una nueva lista
         * que contiene todos los elementos de 'proveedores'.
         */
        return new ArrayList<>(proveedores);
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Buscamos el proveedor existente por ID y actualizamos sus datos.
     */
    @Override
    public boolean actualizar(Proveedor proveedor) {
        try {
            /*
             * ÍNDICE EN UNA LISTA:
             * 
             * Cada elemento en una lista tiene un índice (posición).
             * Los índices empiezan en 0:
             * - Índice 0: primer elemento
             * - Índice 1: segundo elemento
             * - etc.
             * 
             * Usamos un bucle for tradicional para poder usar el índice.
             */
            for (int i = 0; i < proveedores.size(); i++) {
                /*
                 * proveedores.get(i) obtiene el elemento en la posición i
                 * 
                 * size() devuelve el número de elementos en la lista
                 */
                Proveedor existente = proveedores.get(i);

                // Comparamos IDs para encontrar el proveedor a actualizar
                if (existente.getId() == proveedor.getId()) {
                    /*
                     * set(i, proveedor) reemplaza el elemento en la
                     * posición i con el nuevo objeto proveedor.
                     */
                    proveedores.set(i, proveedor);
                    return true; // Actualización exitosa
                }
            }

            // No encontramos el proveedor
            return false;

        } catch (Exception e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Buscamos y eliminamos el proveedor con el ID dado.
     */
    @Override
    public boolean eliminar(int id) {
        try {
            /*
             * USANDO removeIf() CON EXPRESIÓN LAMBDA:
             * 
             * removeIf() es un método que elimina elementos que
             * cumplan una condición.
             * 
             * La expresión lambda (p -> p.getId() == id) es una
             * forma corta de escribir:
             * 
             * new Predicate<Proveedor>() {
             * 
             * @Override
             * public boolean test(Proveedor p) {
             * return p.getId() == id;
             * }
             * }
             * 
             * La flecha (->) separa el parámetro de la expresión.
             * - Lado izquierdo: el parámetro 'p' (cada proveedor)
             * - Lado derecho: la condición a evaluar
             * 
             * Si la condición es true, el elemento se elimina.
             */
            return proveedores.removeIf(p -> p.getId() == id);

        } catch (Exception e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Filtramos proveedores cuyo nombre contenga el texto buscado.
     * La búsqueda no distingue mayúsculas de minúsculas.
     */
    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        // Creamos una nueva lista para los resultados
        List<Proveedor> resultados = new ArrayList<>();

        // Convertimos a minúsculas para búsqueda sin distinción
        // toLowerCase() convierte "HOLA" a "hola"
        String nombreBusqueda = nombre.toLowerCase();

        // Recorremos todos los proveedores
        for (Proveedor proveedor : proveedores) {
            /*
             * contains() verifica si un texto contiene otro.
             * 
             * "Distribuidora ABC".contains("distri") -> true
             * "Suministros".contains("distri") -> false
             * 
             * Usamos toLowerCase() en ambos lados para que
             * la búsqueda no distinga mayúsculas/minúsculas.
             */
            if (proveedor.getNombre().toLowerCase().contains(nombreBusqueda)) {
                // Agregamos el proveedor a los resultados
                resultados.add(proveedor);
            }
        }

        return resultados;
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Filtramos solo los proveedores donde activo == true.
     */
    @Override
    public List<Proveedor> obtenerActivos() {
        // Lista para almacenar proveedores activos
        List<Proveedor> activos = new ArrayList<>();

        // Recorremos y filtramos
        for (Proveedor proveedor : proveedores) {
            // isActivo() devuelve el valor del atributo 'activo'
            if (proveedor.isActivo()) {
                activos.add(proveedor);
            }
        }

        return activos;
    }

    /**
     * {@inheritDoc}
     * 
     * IMPLEMENTACIÓN:
     * Buscamos el proveedor que tenga exactamente el NIT dado.
     */
    @Override
    public Proveedor buscarPorNit(String nit) {
        // Recorremos todos los proveedores
        for (Proveedor proveedor : proveedores) {
            /*
             * Para comparar Strings, usamos equals() en lugar de ==
             * 
             * ¿Por qué?
             * - == compara si son el MISMO objeto en memoria
             * - equals() compara si tienen el MISMO contenido
             * 
             * String a = new String("hola");
             * String b = new String("hola");
             * a == b -> false (son objetos diferentes)
             * a.equals(b) -> true (tienen el mismo contenido)
             */
            if (proveedor.getNit().equals(nit)) {
                return proveedor;
            }
        }

        // No encontrado
        return null;
    }

    // ========================================================
    // MÉTODOS ADICIONALES DE UTILIDAD
    // ========================================================

    /**
     * Obtiene el total de proveedores registrados.
     * 
     * Este método no está en la interfaz, pero es útil
     * para estadísticas y validaciones.
     * 
     * @return Número total de proveedores
     */
    public int contarProveedores() {
        // size() devuelve el número de elementos en la lista
        return proveedores.size();
    }

    /**
     * Verifica si existe un proveedor con el NIT dado.
     * 
     * Útil para validar antes de insertar un nuevo proveedor.
     * 
     * @param nit El NIT a verificar
     * @return true si ya existe, false si no
     */
    public boolean existeNit(String nit) {
        // Reutilizamos buscarPorNit() y verificamos si encontró algo
        return buscarPorNit(nit) != null;
    }
}

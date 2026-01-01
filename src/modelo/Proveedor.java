/*
 * ============================================================
 * CLASE: Proveedor.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Esta clase representa a un Proveedor en nuestro sistema.
 * Es una clase MODELO (también conocida como "Entity" o "POJO").
 * 
 * ¿QUÉ ES UN MODELO?
 * Un modelo es una clase que representa los datos de nuestro
 * sistema. En este caso, representa toda la información que
 * necesitamos guardar sobre un proveedor.
 * 
 * CONCEPTOS DE POO APLICADOS:
 * 1. ENCAPSULAMIENTO: Los atributos son privados (private) y
 *    se acceden a través de métodos públicos (getters/setters).
 * 2. CONSTRUCTOR: Método especial para crear objetos.
 * 3. SOBRECARGA DE CONSTRUCTORES: Múltiples constructores
 *    con diferentes parámetros.
 * 
 * ============================================================
 */

// El "package" indica en qué carpeta/paquete está esta clase
// Esto nos ayuda a organizar el código en módulos lógicos
package modelo;

/**
 * Clase que representa a un Proveedor en el sistema.
 * 
 * Esta clase sigue el patrón JavaBean, que significa:
 * - Atributos privados
 * - Constructor sin parámetros
 * - Métodos getter y setter para cada atributo
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class Proveedor {
    
    // ========================================================
    // ATRIBUTOS (también llamados "campos" o "propiedades")
    // ========================================================
    
    /*
     * Los atributos son las características que definen a un
     * proveedor. Los declaramos como "private" para aplicar
     * el principio de ENCAPSULAMIENTO.
     * 
     * ENCAPSULAMIENTO significa que los datos internos de la
     * clase están protegidos y solo se pueden acceder/modificar
     * a través de métodos públicos (getters y setters).
     * 
     * ¿Por qué usamos private?
     * - Protege los datos de modificaciones incorrectas
     * - Permite validar datos antes de asignarlos
     * - Oculta la implementación interna de la clase
     */
    
    // Identificador único del proveedor (clave primaria)
    // Usamos "int" para números enteros
    private int id;
    
    // Nombre de la empresa proveedora
    // Usamos "String" para texto/cadenas de caracteres
    private String nombre;
    
    // Número de Identificación Tributaria
    // Lo guardamos como String porque puede tener guiones o letras
    private String nit;
    
    // Dirección física del proveedor
    private String direccion;
    
    // Número de teléfono de contacto
    // Lo guardamos como String para permitir formatos como "+57-300-123-4567"
    private String telefono;
    
    // Correo electrónico del proveedor
    private String email;
    
    // Nombre de la persona de contacto en la empresa proveedora
    private String personaContacto;
    
    // Indica si el proveedor está activo en el sistema
    // "boolean" solo puede ser true (verdadero) o false (falso)
    private boolean activo;
    
    // ========================================================
    // CONSTRUCTORES
    // ========================================================
    
    /*
     * Un CONSTRUCTOR es un método especial que se llama
     * automáticamente cuando creamos un nuevo objeto con "new".
     * 
     * Características de los constructores:
     * - Tienen el MISMO nombre que la clase
     * - NO tienen tipo de retorno (ni siquiera void)
     * - Se usan para inicializar el objeto
     * 
     * SOBRECARGA DE CONSTRUCTORES:
     * Podemos tener múltiples constructores con diferentes
     * parámetros. Java sabe cuál usar según los argumentos
     * que pasemos al crear el objeto.
     */
    
    /**
     * Constructor vacío (sin parámetros).
     * 
     * Este constructor es necesario para:
     * - Frameworks que crean objetos automáticamente
     * - Cuando queremos crear un objeto y luego asignar valores
     * 
     * Ejemplo de uso:
     * Proveedor p = new Proveedor();
     * p.setNombre("Mi Empresa");
     */
    public Proveedor() {
        // Por defecto, el proveedor está activo
        // "this" se refiere al objeto actual que estamos creando
        this.activo = true;
    }
    
    /**
     * Constructor con parámetros principales.
     * 
     * Usamos este constructor cuando ya conocemos los datos
     * básicos del proveedor al momento de crearlo.
     * 
     * Ejemplo de uso:
     * Proveedor p = new Proveedor(1, "Distribuidora XYZ", "900123456-7");
     * 
     * @param id Identificador único del proveedor
     * @param nombre Nombre de la empresa proveedora
     * @param nit Número de identificación tributaria
     */
    public Proveedor(int id, String nombre, String nit) {
        /*
         * "this" se usa para diferenciar entre el atributo
         * de la clase y el parámetro del constructor cuando
         * tienen el mismo nombre.
         * 
         * this.id = el atributo de la clase
         * id = el parámetro del constructor
         */
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.activo = true; // Por defecto está activo
    }
    
    /**
     * Constructor completo con todos los parámetros.
     * 
     * Usamos este constructor cuando tenemos TODA la
     * información del proveedor disponible.
     * 
     * @param id Identificador único
     * @param nombre Nombre de la empresa
     * @param nit Número de identificación tributaria
     * @param direccion Dirección física
     * @param telefono Teléfono de contacto
     * @param email Correo electrónico
     * @param personaContacto Nombre del contacto
     * @param activo Estado del proveedor
     */
    public Proveedor(int id, String nombre, String nit, String direccion,
                     String telefono, String email, String personaContacto,
                     boolean activo) {
        // Asignamos cada parámetro a su atributo correspondiente
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.personaContacto = personaContacto;
        this.activo = activo;
    }
    
    // ========================================================
    // GETTERS Y SETTERS
    // ========================================================
    
    /*
     * Los GETTERS y SETTERS son métodos que nos permiten
     * acceder y modificar los atributos privados de la clase.
     * 
     * GETTER: Método que OBTIENE (get) el valor de un atributo
     *         Convención: getNombreAtributo()
     *         Excepción: Para boolean se usa isNombreAtributo()
     * 
     * SETTER: Método que ESTABLECE (set) el valor de un atributo
     *         Convención: setNombreAtributo(valor)
     * 
     * ¿Por qué usamos getters y setters en lugar de hacer
     * los atributos públicos?
     * 
     * 1. VALIDACIÓN: Podemos validar los datos antes de asignarlos
     *    Por ejemplo: verificar que el email tenga formato válido
     * 
     * 2. CONTROL: Podemos hacer atributos de solo lectura
     *    (solo getter) o solo escritura (solo setter)
     * 
     * 3. FLEXIBILIDAD: Podemos cambiar la implementación
     *    interna sin afectar a quien use la clase
     */
    
    /**
     * Obtiene el ID del proveedor.
     * 
     * @return El identificador único del proveedor
     */
    public int getId() {
        return id; // También podríamos escribir: return this.id;
    }
    
    /**
     * Establece el ID del proveedor.
     * 
     * @param id El nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene el nombre del proveedor.
     * 
     * @return El nombre de la empresa proveedora
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del proveedor.
     * 
     * @param nombre El nuevo nombre de la empresa
     */
    public void setNombre(String nombre) {
        // Aquí podríamos agregar validación, por ejemplo:
        // if (nombre != null && !nombre.isEmpty()) {
        //     this.nombre = nombre;
        // }
        this.nombre = nombre;
    }
    
    /**
     * Obtiene el NIT del proveedor.
     * 
     * @return El número de identificación tributaria
     */
    public String getNit() {
        return nit;
    }
    
    /**
     * Establece el NIT del proveedor.
     * 
     * @param nit El nuevo NIT
     */
    public void setNit(String nit) {
        this.nit = nit;
    }
    
    /**
     * Obtiene la dirección del proveedor.
     * 
     * @return La dirección física
     */
    public String getDireccion() {
        return direccion;
    }
    
    /**
     * Establece la dirección del proveedor.
     * 
     * @param direccion La nueva dirección
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    /**
     * Obtiene el teléfono del proveedor.
     * 
     * @return El número de teléfono
     */
    public String getTelefono() {
        return telefono;
    }
    
    /**
     * Establece el teléfono del proveedor.
     * 
     * @param telefono El nuevo teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    /**
     * Obtiene el email del proveedor.
     * 
     * @return El correo electrónico
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email del proveedor.
     * 
     * @param email El nuevo correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtiene la persona de contacto.
     * 
     * @return El nombre del contacto
     */
    public String getPersonaContacto() {
        return personaContacto;
    }
    
    /**
     * Establece la persona de contacto.
     * 
     * @param personaContacto El nuevo contacto
     */
    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }
    
    /**
     * Verifica si el proveedor está activo.
     * 
     * NOTA: Para atributos boolean, usamos "is" en lugar de "get"
     * Esto es una convención de Java para hacer el código más legible.
     * 
     * @return true si está activo, false si no
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Establece el estado activo del proveedor.
     * 
     * @param activo El nuevo estado
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    // ========================================================
    // MÉTODO toString()
    // ========================================================
    
    /**
     * Devuelve una representación en texto del objeto.
     * 
     * IMPORTANTE: Este método está SOBRESCRIBIENDO (Override)
     * el método toString() de la clase Object.
     * 
     * Todas las clases en Java heredan de Object, y Object
     * tiene un método toString() que por defecto muestra
     * algo como "Proveedor@1a2b3c4d" (nombre de clase + hashcode).
     * 
     * Al sobrescribirlo, podemos mostrar información útil.
     * 
     * La anotación @Override le indica al compilador que
     * estamos sobrescribiendo un método de la clase padre.
     * Es opcional pero recomendada.
     * 
     * @return String con la información del proveedor
     */
    @Override
    public String toString() {
        /*
         * StringBuilder es más eficiente que concatenar strings
         * con + cuando hay muchas concatenaciones.
         * 
         * Pero para fines educativos, usaremos la forma simple:
         */
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", nit='" + nit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", personaContacto='" + personaContacto + '\'' +
                ", activo=" + activo +
                '}';
    }
}

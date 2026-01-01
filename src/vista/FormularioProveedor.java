/*
 * ============================================================
 * CLASE: FormularioProveedor.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Este es un DIÁLOGO (ventana secundaria) para crear o editar
 * proveedores. Se abre desde la ventana principal.
 * 
 * ¿QUÉ ES UN JDialog?
 * JDialog es una ventana secundaria que pertenece a otra ventana
 * (la ventana "padre" o "propietaria").
 * 
 * MODO MODAL:
 * Un diálogo puede ser "modal" o "no modal":
 * - Modal: Bloquea la ventana padre hasta que se cierre
 * - No modal: La ventana padre sigue accesible
 * 
 * Usamos modal para formularios porque queremos que el usuario
 * complete o cancele antes de hacer otra cosa.
 * 
 * ============================================================
 */

package vista;

// Importaciones de Swing y AWT
import javax.swing.*;
import java.awt.*;

// Nuestras clases
import modelo.Proveedor;
import servicio.ProveedorService;

/**
 * Diálogo para crear o editar proveedores.
 * 
 * HERENCIA:
 * "extends JDialog" significa que esta clase hereda de JDialog.
 * JDialog es similar a JFrame pero está diseñado para
 * ventanas secundarias/temporales.
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class FormularioProveedor extends JDialog {

    // ========================================================
    // ATRIBUTOS
    // ========================================================

    // Servicio para operaciones de negocio
    private final ProveedorService servicio;

    // Proveedor que estamos editando (null si es nuevo)
    private Proveedor proveedorActual;

    // Campos del formulario
    private JTextField txtNombre;
    private JTextField txtNit;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtContacto;
    private JCheckBox chkActivo;

    // Botones
    private JButton btnGuardar;
    private JButton btnCancelar;

    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    /**
     * Constructor del formulario.
     * 
     * @param padre     La ventana principal (propietaria del diálogo)
     * @param proveedor El proveedor a editar, o null para crear nuevo
     */
    public FormularioProveedor(VentanaPrincipal padre, Proveedor proveedor) {
        /*
         * super() llama al constructor de la clase padre (JDialog).
         * 
         * Parámetros:
         * - padre: la ventana propietaria
         * - título: aparece en la barra del diálogo
         * - true: indica que es modal (bloquea la ventana padre)
         */
        super(padre, proveedor == null ? "Nuevo Proveedor" : "Editar Proveedor", true);

        /*
         * El operador ternario determina el título:
         * - Si proveedor es null -> "Nuevo Proveedor"
         * - Si proveedor existe -> "Editar Proveedor"
         */

        // Guardamos referencias
        this.servicio = padre.getServicio();
        this.proveedorActual = proveedor;

        // Configuramos el diálogo
        configurarDialogo();

        // Creamos los componentes
        inicializarComponentes();

        // Si estamos editando, cargamos los datos del proveedor
        if (proveedor != null) {
            cargarDatosProveedor();
        }
    }

    // ========================================================
    // MÉTODOS DE CONFIGURACIÓN
    // ========================================================

    /**
     * Configura las propiedades del diálogo.
     */
    private void configurarDialogo() {
        // Tamaño del diálogo
        setSize(450, 400);

        // Centramos respecto a la ventana padre
        setLocationRelativeTo(getParent());

        /*
         * setResizable(): Indica si el usuario puede redimensionar.
         * false = tamaño fijo
         */
        setResizable(false);

        /*
         * setDefaultCloseOperation(): Qué pasa al cerrar.
         * DISPOSE_ON_CLOSE: Libera recursos y cierra el diálogo.
         */
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa los componentes del formulario.
     */
    private void inicializarComponentes() {
        // Usamos BorderLayout para el diálogo
        setLayout(new BorderLayout(10, 10));

        // Panel principal para los campos
        JPanel panelCampos = crearPanelCampos();

        // Panel para los botones
        JPanel panelBotones = crearPanelBotones();

        // Agregamos los paneles
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel con los campos del formulario.
     * 
     * @return JPanel con los campos
     */
    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel();

        /*
         * GridBagLayout: Es el layout más flexible de Swing.
         * Permite crear cuadrículas donde las celdas pueden
         * tener diferentes tamaños.
         * 
         * Es más complejo pero ofrece mayor control.
         */
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        /*
         * GridBagConstraints: Define cómo se posiciona cada
         * componente en el GridBagLayout.
         */
        GridBagConstraints gbc = new GridBagConstraints();

        /*
         * Propiedades de GridBagConstraints:
         * - gridx, gridy: Posición en la cuadrícula (columna, fila)
         * - insets: Espacio alrededor del componente
         * - anchor: Alineación dentro de la celda
         * - fill: Cómo se expande el componente
         */
        gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre componentes
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda

        // -------- Fila 0: Nombre (obligatorio) --------

        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        panel.add(new JLabel("Nombre: *"), gbc);

        gbc.gridx = 1; // Columna 1
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expandir horizontalmente
        /*
         * weightx: Proporción de espacio extra que toma este componente.
         * 1.0 significa que toma todo el espacio extra disponible.
         */
        gbc.weightx = 1.0;
        txtNombre = new JTextField(25);
        panel.add(txtNombre, gbc);

        // -------- Fila 1: NIT (obligatorio) --------

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("NIT: *"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNit = new JTextField(25);
        panel.add(txtNit, gbc);

        // -------- Fila 2: Dirección --------

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Dirección:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtDireccion = new JTextField(25);
        panel.add(txtDireccion, gbc);

        // -------- Fila 3: Teléfono --------

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(25);
        panel.add(txtTelefono, gbc);

        // -------- Fila 4: Email --------

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(25);
        panel.add(txtEmail, gbc);

        // -------- Fila 5: Persona de contacto --------

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Contacto:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtContacto = new JTextField(25);
        panel.add(txtContacto, gbc);

        // -------- Fila 6: Activo --------

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Activo:"), gbc);

        gbc.gridx = 1;
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true); // Por defecto, nuevo proveedor está activo
        panel.add(chkActivo, gbc);

        // -------- Nota de campos obligatorios --------

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Ocupa 2 columnas
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Arial", Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);
        panel.add(lblNota, gbc);

        return panel;
    }

    /**
     * Crea el panel con los botones Guardar y Cancelar.
     * 
     * @return JPanel con los botones
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Botón Guardar
        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setPreferredSize(new Dimension(120, 35));

        /*
         * setBackground(): Color de fondo del botón.
         * setForeground(): Color del texto del botón.
         * 
         * new Color(r, g, b) crea un color usando valores RGB
         * (rojo, verde, azul) de 0 a 255.
         */
        btnGuardar.setBackground(new Color(76, 175, 80)); // Verde
        btnGuardar.setForeground(Color.WHITE);

        // Botón Cancelar
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 35));

        // Agregamos los eventos
        btnGuardar.addActionListener(e -> guardarProveedor());
        btnCancelar.addActionListener(e -> cerrarFormulario());

        // Agregamos al panel
        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    // ========================================================
    // MÉTODOS DE DATOS
    // ========================================================

    /**
     * Carga los datos del proveedor en los campos del formulario.
     * 
     * Este método se llama cuando estamos EDITANDO un proveedor
     * existente.
     */
    private void cargarDatosProveedor() {
        /*
         * setText(): Establece el texto de un JTextField.
         * 
         * Usamos el operador ternario para manejar valores null:
         * Si el valor es null, ponemos "" (vacío) en su lugar.
         */
        txtNombre.setText(proveedorActual.getNombre() != null ? proveedorActual.getNombre() : "");

        txtNit.setText(proveedorActual.getNit() != null ? proveedorActual.getNit() : "");

        txtDireccion.setText(proveedorActual.getDireccion() != null ? proveedorActual.getDireccion() : "");

        txtTelefono.setText(proveedorActual.getTelefono() != null ? proveedorActual.getTelefono() : "");

        txtEmail.setText(proveedorActual.getEmail() != null ? proveedorActual.getEmail() : "");

        txtContacto.setText(proveedorActual.getPersonaContacto() != null ? proveedorActual.getPersonaContacto() : "");

        /*
         * setSelected(): Marca o desmarca un JCheckBox.
         * true = marcado, false = desmarcado
         */
        chkActivo.setSelected(proveedorActual.isActivo());

        /*
         * Deshabilitamos el campo NIT si estamos editando,
         * porque normalmente el NIT no debería cambiar.
         * 
         * setEditable(false): El campo se puede ver pero no editar.
         * setBackground(): Cambiamos el color para indicar visualmente.
         */
        txtNit.setEditable(false);
        txtNit.setBackground(new Color(240, 240, 240)); // Gris claro
    }

    /**
     * Valida y guarda el proveedor.
     */
    private void guardarProveedor() {
        // Obtenemos los valores de los campos
        /*
         * getText(): Obtiene el texto del campo.
         * trim(): Elimina espacios al inicio y final.
         */
        String nombre = txtNombre.getText().trim();
        String nit = txtNit.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String contacto = txtContacto.getText().trim();
        boolean activo = chkActivo.isSelected();

        // Creamos o actualizamos el proveedor
        Proveedor proveedor;
        String resultado;

        if (proveedorActual == null) {
            // Estamos CREANDO un nuevo proveedor
            proveedor = new Proveedor();
            proveedor.setNombre(nombre);
            proveedor.setNit(nit);
            proveedor.setDireccion(direccion);
            proveedor.setTelefono(telefono);
            proveedor.setEmail(email);
            proveedor.setPersonaContacto(contacto);
            proveedor.setActivo(activo);

            // Llamamos al servicio para registrar
            resultado = servicio.registrarProveedor(proveedor);
        } else {
            // Estamos EDITANDO un proveedor existente
            proveedorActual.setNombre(nombre);
            // No cambiamos el NIT porque está deshabilitado
            proveedorActual.setDireccion(direccion);
            proveedorActual.setTelefono(telefono);
            proveedorActual.setEmail(email);
            proveedorActual.setPersonaContacto(contacto);
            proveedorActual.setActivo(activo);

            // Llamamos al servicio para actualizar
            resultado = servicio.actualizarProveedor(proveedorActual);
        }

        // Mostramos el resultado
        /*
         * startsWith(): Verifica si un String comienza con cierto texto.
         * Lo usamos para determinar si hubo error o éxito.
         */
        if (resultado.startsWith("Error")) {
            // Hubo un error - mostramos mensaje de error
            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            // Éxito - mostramos mensaje y cerramos
            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Cerramos el formulario
            cerrarFormulario();
        }
    }

    /**
     * Cierra el formulario.
     */
    private void cerrarFormulario() {
        /*
         * dispose(): Libera los recursos del diálogo y lo cierra.
         * 
         * Es más limpio que setVisible(false) porque libera memoria.
         */
        dispose();
    }
}

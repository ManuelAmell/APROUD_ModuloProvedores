/*
 * ============================================================
 * CLASE: VentanaPrincipal.java
 * ============================================================
 * 
 * DESCRIPCIÓN:
 * Esta es la VENTANA PRINCIPAL de nuestra aplicación.
 * Usa Java Swing para crear la interfaz gráfica.
 * 
 * ¿QUÉ ES JAVA SWING?
 * Swing es la biblioteca estándar de Java para crear
 * interfaces gráficas (GUI - Graphical User Interface).
 * 
 * COMPONENTES PRINCIPALES DE SWING:
 * - JFrame: Ventana principal de la aplicación
 * - JPanel: Contenedor para organizar otros componentes
 * - JButton: Botón que el usuario puede clickear
 * - JTable: Tabla para mostrar datos en filas y columnas
 * - JTextField: Campo de texto para entrada de datos
 * - JLabel: Etiqueta para mostrar texto
 * 
 * HERENCIA EN SWING:
 * Esta clase HEREDA de JFrame usando "extends".
 * Esto significa que VentanaPrincipal ES UN JFrame y tiene
 * todas sus características (tamaño, título, botones de
 * cerrar, minimizar, maximizar, etc.)
 * 
 * ============================================================
 */

package vista;

// ============================================================
// IMPORTACIONES
// ============================================================

/*
 * Las importaciones nos permiten usar clases de otros paquetes.
 * Organizamos las importaciones por categoría para claridad.
 */

// Clases de Swing para la interfaz gráfica
import javax.swing.*; // JFrame, JPanel, JButton, etc.
import javax.swing.table.DefaultTableModel; // Modelo de datos para JTable

// Clases de AWT para eventos y diseño
import java.awt.*; // BorderLayout, FlowLayout, Color, Font

// Clases de utilidad
import java.util.List;

// Nuestras clases
import modelo.Proveedor;
import servicio.ProveedorService;

/**
 * Ventana principal de la aplicación de gestión de proveedores.
 * 
 * HERENCIA:
 * "extends JFrame" significa que esta clase hereda de JFrame.
 * - JFrame es la clase padre (superclase)
 * - VentanaPrincipal es la clase hija (subclase)
 * 
 * Al heredar, obtenemos todos los métodos y atributos de JFrame:
 * - setTitle(), setSize(), setVisible(), etc.
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

    // ========================================================
    // COLORES (Paleta Azul)
    // ========================================================
    private final Color COLOR_PRIMARIO = new Color(25, 118, 210); // Azul oscuro
    private final Color COLOR_SECUNDARIO = new Color(33, 150, 243); // Azul medio
    private final Color COLOR_FONDO = new Color(227, 242, 253); // Azul muy claro
    private final Color COLOR_TEXTO = Color.WHITE;

    // ========================================================
    // ATRIBUTOS - Componentes de la interfaz
    // ========================================================

    /*
     * Declaramos los componentes como atributos de la clase
     * para poder acceder a ellos desde cualquier método.
     */

    // Servicio para manejar la lógica de negocio
    private final ProveedorService servicio;

    // Tabla para mostrar los proveedores
    private JTable tablaProveedores;

    // Modelo de datos de la tabla
    // El modelo contiene los datos, la tabla solo los muestra
    private DefaultTableModel modeloTabla;

    // Campo de texto para búsqueda
    private JTextField txtBuscar;

    // Checkbox para filtrar solo activos
    private JCheckBox chkSoloActivos;

    // Botones de acción
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;

    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    /**
     * Constructor que inicializa la ventana principal.
     * 
     * El constructor:
     * 1. Crea el servicio
     * 2. Configura la ventana
     * 3. Crea los componentes
     * 4. Carga los datos iniciales
     */
    public VentanaPrincipal() {
        // Inicializamos el servicio
        this.servicio = new ProveedorService();

        // Configuramos la ventana
        configurarVentana();

        // Inicializamos los componentes de la interfaz
        inicializarComponentes();

        // Cargamos los datos en la tabla
        cargarDatos();
    }

    // ========================================================
    // MÉTODOS DE CONFIGURACIÓN
    // ========================================================

    /**
     * Configura las propiedades básicas de la ventana.
     * 
     * Estos métodos son HEREDADOS de JFrame.
     */
    private void configurarVentana() {
        /*
         * setTitle(): Establece el título que aparece en la
         * barra superior de la ventana.
         */
        setTitle("Módulo de Proveedores - Sistema de Gestión");

        /*
         * setSize(ancho, alto): Establece el tamaño de la ventana
         * en píxeles.
         */
        setSize(1000, 600);

        /*
         * setDefaultCloseOperation(): Define qué pasa cuando el
         * usuario cierra la ventana (click en la X).
         * 
         * EXIT_ON_CLOSE: Termina la aplicación completamente.
         * Otras opciones:
         * - HIDE_ON_CLOSE: Solo oculta la ventana
         * - DO_NOTHING_ON_CLOSE: No hace nada
         * - DISPOSE_ON_CLOSE: Libera los recursos de la ventana
         */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * setLocationRelativeTo(null): Centra la ventana en la
         * pantalla. Pasamos null para que use toda la pantalla
         * como referencia.
         */
        setLocationRelativeTo(null);

        /*
         * setMinimumSize(): Establece un tamaño mínimo para que
         * el usuario no pueda hacer la ventana muy pequeña.
         */
        setMinimumSize(new Dimension(800, 400));
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz.
     * 
     * Usamos el patrón de organización por paneles:
     * - Panel superior: Título y búsqueda
     * - Panel central: Tabla de proveedores
     * - Panel inferior: Botones de acción
     */
    private void inicializarComponentes() {
        /*
         * LAYOUT MANAGERS (Administradores de Diseño):
         * 
         * Los layouts controlan cómo se organizan los componentes
         * dentro de un contenedor.
         * 
         * BorderLayout: Divide el contenedor en 5 regiones:
         * 
         * NORTH (Norte)
         * WEST CENTER EAST
         * SOUTH (Sur)
         * 
         * Es el layout por defecto de JFrame.
         */
        setLayout(new BorderLayout(10, 10)); // 10 px de espacio

        // Creamos los paneles
        JPanel panelSuperior = crearPanelSuperior();
        JPanel panelCentral = crearPanelTabla();
        JPanel panelInferior = crearPanelBotones();

        /*
         * add(componente, posición): Agrega un componente al
         * contenedor en la posición especificada.
         * 
         * BorderLayout.NORTH, CENTER, SOUTH son constantes
         * que indican la posición.
         */
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel superior con el título y la barra de búsqueda.
     * 
     * @return JPanel configurado
     */
    private JPanel crearPanelSuperior() {
        /*
         * JPanel: Contenedor genérico para organizar componentes.
         * Podemos poner componentes dentro de paneles, y paneles
         * dentro de otros paneles para crear diseños complejos.
         */
        JPanel panel = new JPanel();

        /*
         * BoxLayout: Organiza componentes en una línea, ya sea
         * horizontal (X_AXIS) o vertical (Y_AXIS).
         * 
         * BoxLayout.Y_AXIS: Los componentes se apilan verticalmente.
         */
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        /*
         * setBorder(): Agrega un borde/margen alrededor del panel.
         * 
         * BorderFactory.createEmptyBorder(top, left, bottom, right)
         * crea un borde invisible que actúa como margen.
         */
        // Configuramos el color de fondo
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        // -------- Título --------

        /*
         * JLabel: Componente para mostrar texto o imágenes.
         * Solo muestra, el usuario no puede interactuar.
         */
        JLabel lblTitulo = new JLabel("Gestión de Proveedores");

        /*
         * setFont(): Establece la fuente del texto.
         * 
         * new Font(nombre, estilo, tamaño):
         * - nombre: "Arial", "Times New Roman", etc.
         * - estilo: Font.PLAIN, Font.BOLD, Font.ITALIC
         * - tamaño: tamaño en puntos (14, 16, 24, etc.)
         */
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        /*
         * setAlignmentX(): Alinea el componente horizontalmente.
         * Component.CENTER_ALIGNMENT: Centra el componente.
         */
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // -------- Panel de búsqueda --------

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(COLOR_FONDO);

        /*
         * FlowLayout: Organiza componentes en una línea, pasando
         * a la siguiente línea si no caben.
         * 
         * FlowLayout.LEFT: Alinea los componentes a la izquierda.
         */

        JLabel lblBuscar = new JLabel("Buscar:");

        /*
         * JTextField: Campo de texto para que el usuario escriba.
         * 
         * new JTextField(columnas): El parámetro indica el ancho
         * aproximado en caracteres.
         */
        txtBuscar = new JTextField(20);

        /*
         * JButton: Botón que el usuario puede clickear.
         * 
         * El texto del constructor aparece en el botón.
         */
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(COLOR_SECUNDARIO);
        btnBuscar.setForeground(COLOR_TEXTO);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));

        /*
         * JCheckBox: Casilla de verificación (check/uncheck).
         * 
         * Útil para opciones de sí/no, activar/desactivar.
         */
        chkSoloActivos = new JCheckBox("Solo activos");
        chkSoloActivos.setBackground(COLOR_FONDO);
        chkSoloActivos.setSelected(false); // Por defecto no está marcado

        // Agregamos los componentes al panel de búsqueda
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(Box.createHorizontalStrut(20)); // Espacio horizontal
        panelBusqueda.add(chkSoloActivos);

        // -------- Eventos (Listeners) --------

        /*
         * EVENTOS EN SWING:
         * 
         * Cuando el usuario interactúa (click, tecla, etc.),
         * se genera un "evento". Usamos "listeners" para
         * responder a esos eventos.
         * 
         * addActionListener(): Escucha cuando el usuario hace
         * click en un botón o presiona Enter en un campo.
         * 
         * La expresión lambda (e -> ...) es una forma corta de
         * crear un ActionListener:
         * 
         * Forma larga equivalente:
         * btnBuscar.addActionListener(new ActionListener() {
         * 
         * @Override
         * public void actionPerformed(ActionEvent e) {
         * buscarProveedores();
         * }
         * });
         */
        btnBuscar.addActionListener(e -> buscarProveedores());

        /*
         * También buscamos cuando el usuario presiona Enter
         * en el campo de texto.
         */
        txtBuscar.addActionListener(e -> buscarProveedores());

        /*
         * ItemListener: Escucha cuando cambia el estado del checkbox.
         */
        chkSoloActivos.addItemListener(e -> buscarProveedores());

        // Agregamos todo al panel principal
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10)); // Espacio vertical
        panel.add(panelBusqueda);

        return panel;
    }

    /**
     * Crea el panel central con la tabla de proveedores.
     * 
     * @return JPanel con la tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // -------- Definir columnas de la tabla --------

        /*
         * Las columnas definen qué datos muestra la tabla.
         * Cada String es el encabezado de una columna.
         */
        String[] columnas = {
                "ID", // Columna 0
                "Nombre", // Columna 1
                "NIT", // Columna 2
                "Teléfono", // Columna 3
                "Email", // Columna 4
                "Contacto", // Columna 5
                "Activo" // Columna 6
        };

        // -------- Crear el modelo de la tabla --------

        /*
         * DefaultTableModel: Controla los datos de la tabla.
         * 
         * Separamos los DATOS (modelo) de la PRESENTACIÓN (tabla).
         * Esto sigue el patrón MVC (Modelo-Vista-Controlador).
         * 
         * Parámetros:
         * - null: sin datos iniciales (los cargaremos después)
         * - columnas: los encabezados de las columnas
         * 
         * CLASE ANÓNIMA:
         * Creamos una subclase de DefaultTableModel que
         * sobrescribe isCellEditable() para hacer la tabla
         * de solo lectura.
         */
        modeloTabla = new DefaultTableModel(null, columnas) {
            /*
             * @Override indica que estamos sobrescribiendo un
             * método de la clase padre.
             * 
             * isCellEditable() determina si una celda se puede
             * editar. Retornamos false para todas las celdas.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda es editable
            }
        };

        // -------- Crear la tabla --------

        /*
         * JTable: Componente que muestra datos en filas y columnas.
         * 
         * Recibe el modelo que contiene los datos.
         */
        tablaProveedores = new JTable(modeloTabla);

        /*
         * setSelectionMode(): Define cómo puede seleccionar el usuario.
         * 
         * SINGLE_SELECTION: Solo puede seleccionar una fila a la vez.
         * Otras opciones:
         * - MULTIPLE_INTERVAL_SELECTION: Múltiples filas no contiguas
         * - SINGLE_INTERVAL_SELECTION: Múltiples filas contiguas
         */
        tablaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /*
         * setRowHeight(): Altura de cada fila en píxeles.
         * Una altura mayor hace la tabla más fácil de leer.
         */
        tablaProveedores.setRowHeight(25);

        /*
         * getTableHeader(): Obtiene el encabezado de la tabla.
         * setFont(): Cambia la fuente del encabezado.
         */
        tablaProveedores.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaProveedores.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaProveedores.getTableHeader().setForeground(COLOR_TEXTO);
        tablaProveedores.setSelectionBackground(COLOR_SECUNDARIO);
        tablaProveedores.setSelectionForeground(COLOR_TEXTO);

        // Configurar ancho de columnas
        configurarAnchoColumnas();

        // -------- Scroll Pane --------

        /*
         * JScrollPane: Agrega barras de desplazamiento a un componente.
         * 
         * Si la tabla tiene más filas de las que caben en la pantalla,
         * aparecerá una barra de desplazamiento vertical.
         */
        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        scrollPane.getViewport().setBackground(Color.WHITE);

        /*
         * Agregamos el scroll pane al centro del panel.
         * En BorderLayout, el centro ocupa todo el espacio disponible.
         */
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Configura el ancho preferido de cada columna.
     */
    private void configurarAnchoColumnas() {
        /*
         * getColumnModel(): Obtiene el modelo de columnas.
         * getColumn(índice): Obtiene una columna específica.
         * setPreferredWidth(ancho): Establece el ancho en píxeles.
         */
        tablaProveedores.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tablaProveedores.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaProveedores.getColumnModel().getColumn(2).setPreferredWidth(100); // NIT
        tablaProveedores.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
        tablaProveedores.getColumnModel().getColumn(4).setPreferredWidth(180); // Email
        tablaProveedores.getColumnModel().getColumn(5).setPreferredWidth(120); // Contacto
        tablaProveedores.getColumnModel().getColumn(6).setPreferredWidth(60); // Activo
    }

    /**
     * Crea el panel inferior con los botones de acción.
     * 
     * @return JPanel con los botones
     */
    private JPanel crearPanelBotones() {
        /*
         * FlowLayout.CENTER: Los botones se centran horizontalmente.
         * Los números (20, 10) son los espacios horizontal y vertical
         * entre componentes.
         */
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        // Creamos los botones
        btnNuevo = new JButton("➕ Nuevo Proveedor");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnRefrescar = new JButton("🔄 Refrescar");

        /*
         * setPreferredSize(): Establece un tamaño preferido para
         * que todos los botones tengan el mismo tamaño.
         */
        Dimension tamBoton = new Dimension(160, 35);
        btnNuevo.setPreferredSize(tamBoton);
        btnEditar.setPreferredSize(tamBoton);
        btnEliminar.setPreferredSize(tamBoton);
        btnRefrescar.setPreferredSize(tamBoton);

        // Estilizar botones
        estilizarBoton(btnNuevo);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);
        estilizarBoton(btnRefrescar);

        // Agregamos los eventos
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        btnEditar.addActionListener(e -> abrirFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarProveedorSeleccionado());
        btnRefrescar.addActionListener(e -> cargarDatos());

        // Agregamos los botones al panel
        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnRefrescar);

        return panel;
    }

    // ========================================================
    // MÉTODOS DE DATOS
    // ========================================================

    /**
     * Carga todos los proveedores en la tabla.
     */
    private void cargarDatos() {
        // Limpiamos la tabla actual
        /*
         * setRowCount(0): Elimina todas las filas de la tabla.
         * Es más eficiente que eliminar fila por fila.
         */
        modeloTabla.setRowCount(0);

        // Obtenemos los proveedores del servicio
        List<Proveedor> proveedores = servicio.obtenerTodosProveedores();

        // Agregamos cada proveedor a la tabla
        for (Proveedor p : proveedores) {
            agregarFilaTabla(p);
        }

        // Limpiamos el campo de búsqueda
        txtBuscar.setText("");
        chkSoloActivos.setSelected(false);
    }

    /**
     * Busca proveedores según los filtros actuales.
     */
    private void buscarProveedores() {
        // Limpiamos la tabla
        modeloTabla.setRowCount(0);

        // Obtenemos el texto de búsqueda
        String textoBusqueda = txtBuscar.getText().trim();

        // Obtenemos los proveedores según los filtros
        List<Proveedor> proveedores;

        if (textoBusqueda.isEmpty()) {
            // Sin texto de búsqueda
            if (chkSoloActivos.isSelected()) {
                proveedores = servicio.obtenerProveedoresActivos();
            } else {
                proveedores = servicio.obtenerTodosProveedores();
            }
        } else {
            // Con texto de búsqueda
            proveedores = servicio.buscarPorNombre(textoBusqueda);

            // Si además está marcado "solo activos", filtramos
            if (chkSoloActivos.isSelected()) {
                /*
                 * removeIf() con lambda: Elimina elementos que
                 * NO están activos (donde isActivo() es false).
                 * 
                 * El ! (negación) invierte la condición.
                 */
                proveedores.removeIf(p -> !p.isActivo());
            }
        }

        // Agregamos los resultados a la tabla
        for (Proveedor p : proveedores) {
            agregarFilaTabla(p);
        }
    }

    /**
     * Agrega una fila a la tabla con los datos de un proveedor.
     * 
     * @param proveedor El proveedor a agregar
     */
    private void agregarFilaTabla(Proveedor proveedor) {
        /*
         * Creamos un array de objetos con los valores de cada columna.
         * El orden debe coincidir con las columnas definidas.
         */
        Object[] fila = {
                proveedor.getId(), // Columna 0: ID
                proveedor.getNombre(), // Columna 1: Nombre
                proveedor.getNit(), // Columna 2: NIT
                proveedor.getTelefono(), // Columna 3: Teléfono
                proveedor.getEmail(), // Columna 4: Email
                proveedor.getPersonaContacto(), // Columna 5: Contacto
                proveedor.isActivo() ? "Sí" : "No" // Columna 6: Activo
        };

        /*
         * El operador ternario (condición ? valorSi : valorNo)
         * es una forma corta de escribir un if-else.
         * 
         * proveedor.isActivo() ? "Sí" : "No"
         * equivale a:
         * if (proveedor.isActivo()) {
         * return "Sí";
         * } else {
         * return "No";
         * }
         */

        // Agregamos la fila al modelo
        modeloTabla.addRow(fila);
    }

    // ========================================================
    // MÉTODOS DE ACCIONES
    // ========================================================

    /**
     * Abre el formulario para crear un nuevo proveedor.
     */
    private void abrirFormularioNuevo() {
        /*
         * Creamos una nueva instancia del formulario.
         * 'this' se refiere a esta ventana (la ventana padre).
         * 'null' indica que no hay proveedor existente (es nuevo).
         */
        FormularioProveedor formulario = new FormularioProveedor(this, null);

        /*
         * setVisible(true) muestra el formulario.
         * Como es un JDialog modal, bloquea esta ventana hasta
         * que el formulario se cierre.
         */
        formulario.setVisible(true);

        /*
         * Después de cerrar el formulario, recargamos los datos
         * por si se agregó un nuevo proveedor.
         */
        cargarDatos();
    }

    /**
     * Abre el formulario para editar el proveedor seleccionado.
     */
    private void abrirFormularioEditar() {
        // Obtenemos el proveedor seleccionado
        Proveedor seleccionado = obtenerProveedorSeleccionado();

        if (seleccionado == null) {
            /*
             * JOptionPane: Clase para mostrar diálogos simples.
             * 
             * showMessageDialog(): Muestra un mensaje al usuario.
             * Parámetros:
             * - this: la ventana padre
             * - mensaje: el texto a mostrar
             * - título: el título del diálogo
             * - tipo: WARNING_MESSAGE, ERROR_MESSAGE, etc.
             */
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un proveedor de la tabla",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Abrimos el formulario con el proveedor seleccionado
        FormularioProveedor formulario = new FormularioProveedor(this, seleccionado);
        formulario.setVisible(true);

        // Recargamos los datos
        cargarDatos();
    }

    /**
     * Elimina el proveedor seleccionado después de confirmar.
     */
    private void eliminarProveedorSeleccionado() {
        // Obtenemos el proveedor seleccionado
        Proveedor seleccionado = obtenerProveedorSeleccionado();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un proveedor de la tabla",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*
         * showConfirmDialog(): Muestra un diálogo de confirmación.
         * 
         * Retorna:
         * - JOptionPane.YES_OPTION si el usuario clickea "Sí"
         * - JOptionPane.NO_OPTION si clickea "No"
         * - JOptionPane.CANCEL_OPTION si cancela
         */
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar al proveedor '" + seleccionado.getNombre() + "'?\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // Solo eliminamos si el usuario confirmó
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Llamamos al servicio para eliminar
            String resultado = servicio.eliminarProveedor(seleccionado.getId());

            // Mostramos el resultado
            JOptionPane.showMessageDialog(this, resultado);

            // Recargamos la tabla
            cargarDatos();
        }
    }

    /**
     * Obtiene el proveedor seleccionado en la tabla.
     * 
     * @return El proveedor seleccionado o null si no hay selección
     */
    private Proveedor obtenerProveedorSeleccionado() {
        /*
         * getSelectedRow(): Obtiene el índice de la fila seleccionada.
         * Retorna -1 si no hay ninguna fila seleccionada.
         */
        int filaSeleccionada = tablaProveedores.getSelectedRow();

        if (filaSeleccionada == -1) {
            return null; // No hay selección
        }

        /*
         * getValueAt(fila, columna): Obtiene el valor de una celda.
         * 
         * La columna 0 contiene el ID del proveedor.
         * Hacemos casting a Integer porque getValueAt() devuelve Object.
         */
        int id = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);

        // Obtenemos el proveedor completo del servicio
        return servicio.obtenerProveedor(id);
    }

    /**
     * Proporciona acceso al servicio para otras clases.
     * 
     * @return El servicio de proveedores
     */
    public ProveedorService getServicio() {
        return servicio;
    }

    /**
     * Aplica el estilo visual a un botón.
     */
    private void estilizarBoton(JButton btn) {
        btn.setBackground(COLOR_PRIMARIO);
        btn.setForeground(COLOR_TEXTO);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Efecto hover simple (opcional, requiere MouseListener más complejo,
        // pero por defecto Swing ya maneja algo de feedback)
    }
}

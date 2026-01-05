package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import modelo.*;
import servicio.CompraService;
import servicio.ProveedorService;
import util.GeneradorFacturaMarkdown;

/**
 * Ventana unificada con tema oscuro estilo VS Code.
 * TODO en una sola pantalla: proveedores + compras + estadísticas.
 */
public class VentanaUnificada extends JFrame {
    
    // Tema oscuro con fondo azul
    private final Color BG_PRINCIPAL = new Color(25, 35, 55);  // Azul oscuro
    private final Color BG_PANEL = new Color(30, 42, 65);
    private final Color BG_SIDEBAR = new Color(35, 48, 72);
    private final Color BG_INPUT = new Color(45, 58, 82);
    private final Color TEXTO_PRINCIPAL = new Color(220, 220, 220);
    private final Color TEXTO_SECUNDARIO = new Color(160, 160, 160);
    private final Color BORDE = new Color(70, 85, 110);
    
    // Colores semánticos mejorados
    private final Color CREDITO_PENDIENTE = new Color(255, 80, 80);      // Rojo brillante
    private final Color CREDITO_PAGADO = new Color(80, 255, 120);        // Verde brillante
    private final Color TRANSFERENCIA = new Color(100, 180, 255);        // Azul brillante
    private final Color EFECTIVO = new Color(180, 180, 180);             // Gris claro
    private final Color ADVERTENCIA = new Color(255, 193, 7, 180);
    private final Color ACENTO = new Color(0, 150, 255);                 // Azul brillante
    private final Color MORADO_PROVEEDOR = new Color(200, 120, 255);     // Morado para proveedores
    
    private final ProveedorService proveedorService;
    private final CompraService compraService;
    
    // Componentes principales
    private JList<String> listaProveedores;
    private DefaultListModel<String> modeloListaProveedores;
    private JTable tablaCompras;
    private DefaultTableModel modeloTablaCompras;
    
    private JLabel lblProveedorSeleccionado;
    private JLabel lblTotalProveedor;
    private JLabel lblPendienteProveedor;
    private JLabel lblTotalGeneral;
    private JLabel lblCreditosPendientes;
    
    private List<Proveedor> proveedores;
    private Proveedor proveedorActual;
    
    // Campos de filtro
    private JTextField txtBuscarProveedor;
    private JTextField txtBuscarCompra;
    private JComboBox<String> cmbFiltroFormaPago;
    private JComboBox<String> cmbFiltroEstado;
    private JTextField txtFiltroFechaDesde;
    private JTextField txtFiltroFechaHasta;
    
    // Componentes de UI adicionales
    private JLabel lblReloj;
    private JLabel lblVersion;
    private javax.swing.Timer timerReloj;
    
    public VentanaUnificada() {
        this.proveedorService = new ProveedorService();
        this.compraService = new CompraService();
        
        configurarVentana();
        inicializarComponentes();
        cargarProveedores();
        actualizarEstadisticasGenerales();
    }

    
    private void configurarVentana() {
        setTitle("Aproud - Proveedores y Compras");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));
        
        // Detener el timer al cerrar la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (timerReloj != null) {
                    timerReloj.stop();
                }
            }
        });
        
        // Cargar icono de la aplicación
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("lib/ModuloProveedores.png");
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        // Aplicar tema oscuro
        getContentPane().setBackground(BG_PRINCIPAL);
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 0));
        
        // Panel superior (reloj)
        add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel lateral izquierdo (proveedores)
        add(crearPanelProveedores(), BorderLayout.WEST);
        
        // Panel central (compras y detalles)
        add(crearPanelCentral(), BorderLayout.CENTER);
        
        // Panel inferior (estadísticas y versión)
        add(crearPanelEstadisticas(), BorderLayout.SOUTH);
        
        // Iniciar reloj
        iniciarReloj();
    }

    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PRINCIPAL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Título a la izquierda
        JLabel lblTitulo = new JLabel("Sistema de Gestión - Proveedores y Compras");
        lblTitulo.setForeground(ACENTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Reloj a la derecha
        lblReloj = new JLabel();
        lblReloj.setForeground(ACENTO);
        lblReloj.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblReloj.setHorizontalAlignment(JLabel.RIGHT);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(lblReloj, BorderLayout.EAST);
        
        return panel;
    }
    
    private void iniciarReloj() {
        // Actualizar hora inmediatamente
        actualizarHora();
        
        // Actualizar cada segundo
        timerReloj = new javax.swing.Timer(1000, e -> actualizarHora());
        timerReloj.start();
    }
    
    private void actualizarHora() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
        lblReloj.setText(ahora.format(formatter));
    }

    
    private JPanel crearPanelProveedores() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDE));
        
        // Panel superior con título y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBackground(BG_SIDEBAR);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        
        // Título
        JLabel lblTitulo = new JLabel("PROVEEDORES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(TEXTO_PRINCIPAL);
        
        // Barra de búsqueda de proveedores
        JPanel panelBusquedaProveedor = new JPanel(new BorderLayout(5, 0));
        panelBusquedaProveedor.setBackground(BG_SIDEBAR);
        panelBusquedaProveedor.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        JLabel lblBuscarIcon = new JLabel("🔍");
        lblBuscarIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBuscarIcon.setForeground(TEXTO_SECUNDARIO);
        
        txtBuscarProveedor = new JTextField();
        txtBuscarProveedor.setBackground(BG_INPUT);
        txtBuscarProveedor.setForeground(TEXTO_PRINCIPAL);
        txtBuscarProveedor.setCaretColor(TEXTO_PRINCIPAL);
        txtBuscarProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscarProveedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        txtBuscarProveedor.setToolTipText("Buscar proveedor por nombre");
        
        // Búsqueda en tiempo real
        txtBuscarProveedor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarProveedores(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarProveedores(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarProveedores(); }
        });
        
        panelBusquedaProveedor.add(lblBuscarIcon, BorderLayout.WEST);
        panelBusquedaProveedor.add(txtBuscarProveedor, BorderLayout.CENTER);
        
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusquedaProveedor, BorderLayout.CENTER);
        
        // Lista de proveedores
        modeloListaProveedores = new DefaultListModel<>();
        listaProveedores = new JList<>(modeloListaProveedores);
        listaProveedores.setBackground(BG_SIDEBAR);
        listaProveedores.setForeground(MORADO_PROVEEDOR);  // Morado para proveedores
        listaProveedores.setSelectionBackground(ACENTO);
        listaProveedores.setSelectionForeground(Color.WHITE);
        listaProveedores.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Más grande y negrita
        listaProveedores.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        listaProveedores.setFixedCellHeight(45);  // Más alto
        
        // Centrar texto en la lista
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        listaProveedores.setCellRenderer(renderer);
        
        listaProveedores.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProveedor();
            }
        });
        
        JScrollPane scrollProveedores = new JScrollPane(listaProveedores);
        scrollProveedores.setBackground(BG_SIDEBAR);
        scrollProveedores.setBorder(null);
        scrollProveedores.getViewport().setBackground(BG_SIDEBAR);
        
        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setBackground(BG_SIDEBAR);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JButton btnNuevo = crearBoton("+ Nuevo Proveedor", ACENTO);
        JButton btnEditar = crearBoton("✎ Editar", BG_INPUT);
        JButton btnRefrescar = crearBoton("⟳ Refrescar", BG_INPUT);
        
        btnNuevo.addActionListener(e -> nuevoProveedor());
        btnEditar.addActionListener(e -> editarProveedor());
        btnRefrescar.addActionListener(e -> {
            cargarProveedores();
            actualizarEstadisticasGenerales();
        });
        
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnRefrescar);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollProveedores, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }

    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_PRINCIPAL);
        
        // Panel de búsqueda en la parte superior
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel superior con info del proveedor y filtros
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(BG_PANEL);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Info del proveedor
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelInfo.setBackground(BG_PANEL);
        
        lblProveedorSeleccionado = new JLabel("Seleccione un proveedor");
        lblProveedorSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 17));  // Más grande
        lblProveedorSeleccionado.setForeground(MORADO_PROVEEDOR);  // Morado para proveedor
        
        lblTotalProveedor = new JLabel("");
        lblTotalProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));  // Más grande
        lblTotalProveedor.setForeground(TEXTO_SECUNDARIO);
        
        lblPendienteProveedor = new JLabel("");
        lblPendienteProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));  // Más grande
        lblPendienteProveedor.setForeground(CREDITO_PENDIENTE);  // Rojo para pendientes
        
        panelInfo.add(lblProveedorSeleccionado);
        panelInfo.add(lblTotalProveedor);
        panelInfo.add(lblPendienteProveedor);
        
        // Botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setBackground(BG_PANEL);
        
        JButton btnNuevaCompra = crearBoton("+ Nueva Compra", CREDITO_PAGADO);
        JButton btnEditarCompra = crearBoton("✎ Editar", BG_INPUT);
        JButton btnMarcarPagado = crearBoton("✓ Marcar Pagado", ADVERTENCIA);
        JButton btnVerCompra = crearBoton("👁 Ver", ACENTO);
        
        btnNuevaCompra.addActionListener(e -> nuevaCompra());
        btnEditarCompra.addActionListener(e -> editarCompra());
        btnMarcarPagado.addActionListener(e -> marcarComoPagado());
        btnVerCompra.addActionListener(e -> verCompraSeleccionada());
        
        panelAcciones.add(btnNuevaCompra);
        panelAcciones.add(btnEditarCompra);
        panelAcciones.add(btnMarcarPagado);
        panelAcciones.add(btnVerCompra);
        
        panelSuperior.add(panelInfo, BorderLayout.WEST);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);
        
        // Tabla de compras
        String[] columnas = {"Factura", "Categoría", "Descripción", "Cant.", "P.Unit", 
                             "Total", "Fecha", "Pago", "Estado", "F.Pago"};
        
        modeloTablaCompras = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCompras = new JTable(modeloTablaCompras);
        tablaCompras.setBackground(BG_PRINCIPAL);
        tablaCompras.setForeground(TEXTO_PRINCIPAL);
        tablaCompras.setGridColor(BORDE);
        tablaCompras.setSelectionBackground(ACENTO);
        tablaCompras.setSelectionForeground(Color.WHITE);
        tablaCompras.setFont(new Font("Consolas", Font.PLAIN, 13));  // Más grande
        tablaCompras.setRowHeight(36);  // Más alto
        tablaCompras.setShowGrid(true);
        tablaCompras.setIntercellSpacing(new Dimension(1, 1));
        
        // Header de la tabla
        tablaCompras.getTableHeader().setBackground(BG_PANEL);
        tablaCompras.getTableHeader().setForeground(TEXTO_PRINCIPAL);
        tablaCompras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));  // Más grande
        tablaCompras.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE));
        
        // Centrar encabezados
        ((DefaultTableCellRenderer)tablaCompras.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.CENTER);
        
        // Renderer personalizado para colores y centrado
        aplicarRenderizadorColores();
        
        JScrollPane scrollTabla = new JScrollPane(tablaCompras);
        scrollTabla.setBackground(BG_PRINCIPAL);
        scrollTabla.setBorder(null);
        scrollTabla.getViewport().setBackground(BG_PRINCIPAL);
        
        // Contenedor para búsqueda y panel superior
        JPanel panelTop = new JPanel(new BorderLayout(0, 0));
        panelTop.setBackground(BG_PRINCIPAL);
        panelTop.add(panelBusqueda, BorderLayout.NORTH);
        panelTop.add(panelSuperior, BorderLayout.CENTER);
        
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Panel de búsqueda de texto
        JPanel panelBusquedaTexto = new JPanel(new BorderLayout(10, 0));
        panelBusquedaTexto.setBackground(BG_PANEL);
        
        JLabel lblBuscar = new JLabel("🔍");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblBuscar.setForeground(TEXTO_SECUNDARIO);
        
        txtBuscarCompra = new JTextField();
        txtBuscarCompra.setBackground(BG_INPUT);
        txtBuscarCompra.setForeground(TEXTO_PRINCIPAL);
        txtBuscarCompra.setCaretColor(TEXTO_PRINCIPAL);
        txtBuscarCompra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscarCompra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtBuscarCompra.setToolTipText("Buscar por categoría, descripción o factura");
        
        // Búsqueda en tiempo real
        txtBuscarCompra.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
        });
        
        panelBusquedaTexto.add(lblBuscar, BorderLayout.WEST);
        panelBusquedaTexto.add(txtBuscarCompra, BorderLayout.CENTER);
        
        // Panel de filtros avanzados
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFiltros.setBackground(BG_PANEL);
        
        // Filtro por forma de pago
        JLabel lblFormaPago = new JLabel("Pago:");
        lblFormaPago.setForeground(TEXTO_SECUNDARIO);
        lblFormaPago.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        cmbFiltroFormaPago = new JComboBox<>(new String[]{"Todos", "Efectivo", "Transferencia", "Crédito"});
        cmbFiltroFormaPago.setBackground(BG_INPUT);
        cmbFiltroFormaPago.setForeground(TEXTO_PRINCIPAL);
        cmbFiltroFormaPago.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFiltroFormaPago.setPreferredSize(new Dimension(130, 30));
        cmbFiltroFormaPago.addActionListener(e -> aplicarFiltros());
        
        // Filtro por estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(TEXTO_SECUNDARIO);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        cmbFiltroEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Pagado"});
        cmbFiltroEstado.setBackground(BG_INPUT);
        cmbFiltroEstado.setForeground(TEXTO_PRINCIPAL);
        cmbFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFiltroEstado.setPreferredSize(new Dimension(110, 30));
        cmbFiltroEstado.addActionListener(e -> aplicarFiltros());
        
        // Filtro por fecha desde
        JLabel lblFechaDesde = new JLabel("Desde:");
        lblFechaDesde.setForeground(TEXTO_SECUNDARIO);
        lblFechaDesde.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtFiltroFechaDesde = new JTextField(8);
        txtFiltroFechaDesde.setBackground(BG_INPUT);
        txtFiltroFechaDesde.setForeground(TEXTO_PRINCIPAL);
        txtFiltroFechaDesde.setCaretColor(TEXTO_PRINCIPAL);
        txtFiltroFechaDesde.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFiltroFechaDesde.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtFiltroFechaDesde.setToolTipText("dd/mm/aa");
        agregarPlaceholder(txtFiltroFechaDesde, "dd/mm/aa");
        txtFiltroFechaDesde.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
        });
        
        // Filtro por fecha hasta
        JLabel lblFechaHasta = new JLabel("Hasta:");
        lblFechaHasta.setForeground(TEXTO_SECUNDARIO);
        lblFechaHasta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtFiltroFechaHasta = new JTextField(8);
        txtFiltroFechaHasta.setBackground(BG_INPUT);
        txtFiltroFechaHasta.setForeground(TEXTO_PRINCIPAL);
        txtFiltroFechaHasta.setCaretColor(TEXTO_PRINCIPAL);
        txtFiltroFechaHasta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFiltroFechaHasta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtFiltroFechaHasta.setToolTipText("dd/mm/aa");
        agregarPlaceholder(txtFiltroFechaHasta, "dd/mm/aa");
        txtFiltroFechaHasta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
        });
        
        // Botón limpiar filtros
        JButton btnLimpiar = new JButton("✕ Limpiar");
        btnLimpiar.setBackground(BG_INPUT);
        btnLimpiar.setForeground(TEXTO_SECUNDARIO);
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setPreferredSize(new Dimension(100, 30));
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        
        panelFiltros.add(lblFormaPago);
        panelFiltros.add(cmbFiltroFormaPago);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(lblEstado);
        panelFiltros.add(cmbFiltroEstado);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(lblFechaDesde);
        panelFiltros.add(txtFiltroFechaDesde);
        panelFiltros.add(lblFechaHasta);
        panelFiltros.add(txtFiltroFechaHasta);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(btnLimpiar);
        
        panel.add(panelBusquedaTexto, BorderLayout.NORTH);
        panel.add(panelFiltros, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void filtrarCompras(String textoBusqueda) {
        if (proveedorActual == null) return;
        
        modeloTablaCompras.setRowCount(0);
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String busqueda = textoBusqueda.toLowerCase().trim();
        
        for (Compra c : compras) {
            // Filtrar por múltiples campos
            boolean coincide = busqueda.isEmpty() ||
                c.getNumeroFactura().toLowerCase().contains(busqueda) ||
                c.getCategoria().toLowerCase().contains(busqueda) ||
                c.getDescripcion().toLowerCase().contains(busqueda) ||
                proveedorActual.getNombre().toLowerCase().contains(busqueda);
            
            if (coincide) {
                // Capitalizar primera letra de categoría
                String categoriaDisplay = c.getCategoria();
                if (categoriaDisplay != null && !categoriaDisplay.isEmpty()) {
                    categoriaDisplay = categoriaDisplay.substring(0, 1).toUpperCase() + categoriaDisplay.substring(1);
                }
                
                // Determinar estado de pago
                String estadoDisplay = "";
                if (c.getFormaPago() == FormaPago.CREDITO) {
                    estadoDisplay = c.getEstadoCredito() != null ? c.getEstadoCredito().getEtiqueta() : "";
                } else {
                    estadoDisplay = c.getFechaPago() != null ? "Pagado" : "Pendiente";
                }
                
                Object[] fila = {
                    c.getNumeroFactura(),
                    categoriaDisplay,
                    c.getDescripcion().length() > 40 ? c.getDescripcion().substring(0, 37) + "..." : c.getDescripcion(),
                    c.getCantidad() != null ? c.getCantidad() : "",
                    c.getPrecioUnitario() != null ? formatoMoneda.format(c.getPrecioUnitario()) : "",
                    formatoMoneda.format(c.getTotal()),
                    c.getFechaCompra().format(formatoFecha),
                    c.getFormaPago().getEtiqueta(),
                    estadoDisplay,
                    c.getFechaPago() != null ? c.getFechaPago().format(formatoFecha) : ""
                };
                modeloTablaCompras.addRow(fila);
            }
        }
    }
    
    private void filtrarProveedores() {
        String busqueda = txtBuscarProveedor.getText().toLowerCase().trim();
        modeloListaProveedores.clear();
        
        for (Proveedor p : proveedores) {
            if (busqueda.isEmpty() || p.getNombre().toLowerCase().contains(busqueda)) {
                String tipo = p.getTipo() != null ? " [" + p.getTipo() + "]" : "";
                modeloListaProveedores.addElement(p.getNombre() + tipo);
            }
        }
        
        if (modeloListaProveedores.getSize() > 0) {
            listaProveedores.setSelectedIndex(0);
        }
    }
    
    private void aplicarFiltros() {
        if (proveedorActual == null) return;
        
        modeloTablaCompras.setRowCount(0);
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoFechaInput = DateTimeFormatter.ofPattern("[dd/MM/yy][dd/MM/yyyy][dd-MM-yy][dd-MM-yyyy]");
        
        String busqueda = txtBuscarCompra.getText().toLowerCase().trim();
        String filtroFormaPago = (String) cmbFiltroFormaPago.getSelectedItem();
        String filtroEstado = (String) cmbFiltroEstado.getSelectedItem();
        String fechaDesdeStr = txtFiltroFechaDesde.getText().trim();
        String fechaHastaStr = txtFiltroFechaHasta.getText().trim();
        
        // Ignorar placeholders
        if (fechaDesdeStr.equals("dd/mm/aa")) fechaDesdeStr = "";
        if (fechaHastaStr.equals("dd/mm/aa")) fechaHastaStr = "";
        
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        
        // Parsear fechas si están presentes
        try {
            if (!fechaDesdeStr.isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeStr, formatoFechaInput);
            }
            if (!fechaHastaStr.isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaStr, formatoFechaInput);
            }
        } catch (Exception e) {
            // Si hay error en las fechas, ignorar el filtro de fecha
        }
        
        for (Compra c : compras) {
            // Filtro de texto
            boolean coincideTexto = busqueda.isEmpty() ||
                c.getNumeroFactura().toLowerCase().contains(busqueda) ||
                c.getCategoria().toLowerCase().contains(busqueda) ||
                c.getDescripcion().toLowerCase().contains(busqueda);
            
            // Filtro de forma de pago
            boolean coincideFormaPago = filtroFormaPago.equals("Todos") ||
                c.getFormaPago().getEtiqueta().equals(filtroFormaPago);
            
            // Filtro de estado
            String estadoCompra = "";
            if (c.getFormaPago() == FormaPago.CREDITO) {
                estadoCompra = c.getEstadoCredito() != null ? c.getEstadoCredito().getEtiqueta() : "";
            } else {
                estadoCompra = c.getFechaPago() != null ? "Pagado" : "Pendiente";
            }
            boolean coincideEstado = filtroEstado.equals("Todos") || estadoCompra.equals(filtroEstado);
            
            // Filtro de fecha
            boolean coincideFecha = true;
            if (fechaDesde != null && c.getFechaCompra().isBefore(fechaDesde)) {
                coincideFecha = false;
            }
            if (fechaHasta != null && c.getFechaCompra().isAfter(fechaHasta)) {
                coincideFecha = false;
            }
            
            if (coincideTexto && coincideFormaPago && coincideEstado && coincideFecha) {
                // Capitalizar primera letra de categoría
                String categoriaDisplay = c.getCategoria();
                if (categoriaDisplay != null && !categoriaDisplay.isEmpty()) {
                    categoriaDisplay = categoriaDisplay.substring(0, 1).toUpperCase() + categoriaDisplay.substring(1);
                }
                
                // Determinar estado de pago
                String estadoDisplay = "";
                if (c.getFormaPago() == FormaPago.CREDITO) {
                    estadoDisplay = c.getEstadoCredito() != null ? c.getEstadoCredito().getEtiqueta() : "";
                } else {
                    estadoDisplay = c.getFechaPago() != null ? "Pagado" : "Pendiente";
                }
                
                Object[] fila = {
                    c.getNumeroFactura(),
                    categoriaDisplay,
                    c.getDescripcion().length() > 40 ? c.getDescripcion().substring(0, 37) + "..." : c.getDescripcion(),
                    c.getCantidad() != null ? c.getCantidad() : "",
                    c.getPrecioUnitario() != null ? formatoMoneda.format(c.getPrecioUnitario()) : "",
                    formatoMoneda.format(c.getTotal()),
                    c.getFechaCompra().format(formatoFecha),
                    c.getFormaPago().getEtiqueta(),
                    estadoDisplay,
                    c.getFechaPago() != null ? c.getFechaPago().format(formatoFecha) : ""
                };
                modeloTablaCompras.addRow(fila);
            }
        }
    }
    
    private void limpiarFiltros() {
        txtBuscarCompra.setText("");
        cmbFiltroFormaPago.setSelectedIndex(0);
        cmbFiltroEstado.setSelectedIndex(0);
        txtFiltroFechaDesde.setText("dd/mm/aa");
        txtFiltroFechaDesde.setForeground(TEXTO_SECUNDARIO);
        txtFiltroFechaHasta.setText("dd/mm/aa");
        txtFiltroFechaHasta.setForeground(TEXTO_SECUNDARIO);
        aplicarFiltros();
    }

    
    private JPanel crearPanelEstadisticas() {
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(BG_PANEL);
        panelContenedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        
        // Panel central con estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panelEstadisticas.setBackground(BG_PANEL);
        
        lblTotalGeneral = crearLabelEstadistica("Total General: $0", TEXTO_PRINCIPAL);
        lblCreditosPendientes = crearLabelEstadistica("Pendientes: $0", CREDITO_PENDIENTE);
        
        panelEstadisticas.add(lblTotalGeneral);
        panelEstadisticas.add(lblCreditosPendientes);
        
        // Label de versión a la izquierda
        lblVersion = new JLabel("v2.3.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(TEXTO_SECUNDARIO);
        
        panelContenedor.add(lblVersion, BorderLayout.WEST);
        panelContenedor.add(panelEstadisticas, BorderLayout.CENTER);
        
        return panelContenedor;
    }
    
    private JLabel crearLabelEstadistica(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));  // Más grande
        label.setForeground(color);
        return label;
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }

    
    private void aplicarRenderizadorColores() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(BG_PRINCIPAL);
                    
                    // Colorear según forma de pago y estado
                    String formaPago = (String) table.getValueAt(row, 7); // Columna "Pago"
                    String estado = (String) table.getValueAt(row, 8); // Columna "Estado"
                    
                    if (formaPago != null && estado != null) {
                        if (estado.equals("Pendiente")) {
                            // ROJO para pendientes
                            c.setForeground(CREDITO_PENDIENTE);
                        } else if (estado.equals("Pagado")) {
                            // VERDE para pagados
                            c.setForeground(CREDITO_PAGADO);
                        } else {
                            c.setForeground(TEXTO_PRINCIPAL);
                        }
                    } else {
                        c.setForeground(TEXTO_PRINCIPAL);
                    }
                }
                
                // Centrado inteligente por columna
                if (column == 4 || column == 5) {
                    // P.Unit, Total - Derecha (números)
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (column == 2) {
                    // Descripción - Izquierda (texto largo)
                    setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    // Todo lo demás centrado: Factura, Categoría, Cantidad, Fecha, Pago, Estado, F.Pago
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
                
                return c;
            }
        };
        
        for (int i = 0; i < tablaCompras.getColumnCount(); i++) {
            tablaCompras.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
    
    private void cargarProveedores() {
        modeloListaProveedores.clear();
        proveedores = proveedorService.obtenerProveedoresActivos();
        
        for (Proveedor p : proveedores) {
            String tipo = p.getTipo() != null ? " [" + p.getTipo() + "]" : "";
            modeloListaProveedores.addElement(p.getNombre() + tipo);
        }
        
        if (!proveedores.isEmpty()) {
            listaProveedores.setSelectedIndex(0);
        }
    }
    
    private void seleccionarProveedor() {
        int index = listaProveedores.getSelectedIndex();
        if (index >= 0 && index < proveedores.size()) {
            proveedorActual = proveedores.get(index);
            lblProveedorSeleccionado.setText(proveedorActual.getNombre());
            cargarComprasProveedor();
            actualizarTotalProveedor();
        }
    }
    
    private void cargarComprasProveedor() {
        modeloTablaCompras.setRowCount(0);
        
        if (proveedorActual == null) return;
        
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Compra c : compras) {
            // Capitalizar primera letra de categoría para mostrar
            String categoriaDisplay = c.getCategoria();
            if (categoriaDisplay != null && !categoriaDisplay.isEmpty()) {
                categoriaDisplay = categoriaDisplay.substring(0, 1).toUpperCase() + categoriaDisplay.substring(1);
            }
            
            // Determinar estado de pago
            String estadoDisplay = "";
            if (c.getFormaPago() == FormaPago.CREDITO) {
                estadoDisplay = c.getEstadoCredito() != null ? c.getEstadoCredito().getEtiqueta() : "";
            } else {
                // Para efectivo y transferencia, mostrar "Pagado" si tiene fecha de pago
                estadoDisplay = c.getFechaPago() != null ? "Pagado" : "Pendiente";
            }
            
            Object[] fila = {
                c.getNumeroFactura(),
                categoriaDisplay,
                c.getDescripcion().length() > 40 ? c.getDescripcion().substring(0, 37) + "..." : c.getDescripcion(),
                c.getCantidad() != null ? c.getCantidad() : "",
                c.getPrecioUnitario() != null ? formatoMoneda.format(c.getPrecioUnitario()) : "",
                formatoMoneda.format(c.getTotal()),
                c.getFechaCompra().format(formatoFecha),
                c.getFormaPago().getEtiqueta(),
                estadoDisplay,
                c.getFechaPago() != null ? c.getFechaPago().format(formatoFecha) : ""
            };
            modeloTablaCompras.addRow(fila);
        }
    }

    
    private void actualizarTotalProveedor() {
        if (proveedorActual == null) {
            lblTotalProveedor.setText("");
            lblPendienteProveedor.setText("");
            return;
        }
        
        BigDecimal total = compraService.calcularTotalPorProveedor(proveedorActual.getId());
        BigDecimal pendiente = compraService.calcularPendientesPorProveedor(proveedorActual.getId());
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        lblTotalProveedor.setText("Total: " + formatoMoneda.format(total));
        lblPendienteProveedor.setText("Pendiente: " + formatoMoneda.format(pendiente));
    }
    
    private void actualizarEstadisticasGenerales() {
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        
        BigDecimal totalGeneral = BigDecimal.ZERO;
        List<Compra> todasCompras = compraService.obtenerTodasCompras();
        for (Compra c : todasCompras) {
            totalGeneral = totalGeneral.add(c.getTotal());
        }
        
        BigDecimal totalCreditosPendientes = compraService.calcularTotalCreditosPendientes();
        
        lblTotalGeneral.setText("Total General: " + formatoMoneda.format(totalGeneral));
        lblCreditosPendientes.setText("Pendientes: " + formatoMoneda.format(totalCreditosPendientes));
    }
    
    private void nuevoProveedor() {
        FormularioProveedorDark formulario = new FormularioProveedorDark(this, null);
        formulario.setVisible(true);
        cargarProveedores();
    }
    
    private void editarProveedor() {
        if (proveedorActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FormularioProveedorDark formulario = new FormularioProveedorDark(this, proveedorActual);
        formulario.setVisible(true);
        cargarProveedores();
    }
    
    private void nuevaCompra() {
        if (proveedorActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor primero", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FormularioCompraDarkConItems formulario = new FormularioCompraDarkConItems(this, null, proveedorActual);
        formulario.setVisible(true);
        cargarComprasProveedor();
        actualizarTotalProveedor();
        actualizarEstadisticasGenerales();
    }
    
    private void editarCompra() {
        int filaSeleccionada = tablaCompras.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una compra", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String numeroFactura = (String) modeloTablaCompras.getValueAt(filaSeleccionada, 0);
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        Compra compraSeleccionada = compras.stream()
            .filter(c -> c.getNumeroFactura().equals(numeroFactura))
            .findFirst()
            .orElse(null);
        
        if (compraSeleccionada != null) {
            FormularioCompraDarkConItems formulario = new FormularioCompraDarkConItems(this, compraSeleccionada, proveedorActual);
            formulario.setVisible(true);
            
            // Actualizar todas las vistas después de editar
            cargarComprasProveedor();
            actualizarTotalProveedor();
            actualizarEstadisticasGenerales();
            
            System.out.println("Tabla de compras actualizada después de editar");
        }
    }
    
    private void marcarComoPagado() {
        int filaSeleccionada = tablaCompras.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una compra de la tabla", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String numeroFactura = (String) modeloTablaCompras.getValueAt(filaSeleccionada, 0);
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        Compra compraSeleccionada = null;
        
        for (Compra c : compras) {
            if (c.getNumeroFactura().equals(numeroFactura)) {
                compraSeleccionada = c;
                break;
            }
        }
        
        if (compraSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la compra seleccionada", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar si ya está pagado
        boolean yaPagado = false;
        if (compraSeleccionada.getFormaPago() == FormaPago.CREDITO) {
            yaPagado = compraSeleccionada.getEstadoCredito() == EstadoCredito.PAGADO;
        } else {
            yaPagado = compraSeleccionada.getFechaPago() != null;
        }
        
        if (yaPagado) {
            JOptionPane.showMessageDialog(this, "Esta compra ya está marcada como pagada", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Solicitar fecha de pago
        String fechaStr = JOptionPane.showInputDialog(this, 
            "Ingrese la fecha de pago:\n(Formato: dd/mm/aa o dd/MM/yyyy)\nEjemplo: 03/01/26",
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return; // Usuario canceló
        }
        
        try {
            // Parsear fecha con múltiples formatos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yy][dd/MM/yyyy][dd-MM-yy][dd-MM-yyyy]");
            LocalDate fechaPago = LocalDate.parse(fechaStr.trim(), formatter);
            
            // Actualizar según el tipo de pago
            if (compraSeleccionada.getFormaPago() == FormaPago.CREDITO) {
                // Para crédito, usar el método específico
                String resultado = compraService.marcarCreditoComoPagado(compraSeleccionada.getId(), fechaPago);
                if (resultado.startsWith("Error")) {
                    JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Crédito marcado como pagado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Para efectivo y transferencia, actualizar directamente
                compraSeleccionada.setFechaPago(fechaPago);
                String resultado = compraService.actualizarCompra(compraSeleccionada);
                if (resultado.startsWith("Error")) {
                    JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Compra marcada como pagada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            // Recargar datos
            cargarComprasProveedor();
            actualizarTotalProveedor();
            actualizarEstadisticasGenerales();
            
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inválido.\nUse: dd/mm/aa o dd/MM/yyyy\nEjemplo: 03/01/26", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al marcar como pagado: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public ProveedorService getProveedorService() {
        return proveedorService;
    }
    
    public CompraService getCompraService() {
        return compraService;
    }
    
    private void verCompraSeleccionada() {
        int filaSeleccionada = tablaCompras.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una compra para ver", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el número de factura de la fila seleccionada (columna 0 es "Factura")
        String numeroFactura = (String) modeloTablaCompras.getValueAt(filaSeleccionada, 0);
        
        // Buscar la compra por número de factura y proveedor
        List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorActual.getId());
        Compra compraSeleccionada = null;
        
        for (Compra c : compras) {
            if (c.getNumeroFactura().equals(numeroFactura)) {
                compraSeleccionada = c;
                break;
            }
        }
        
        if (compraSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró la compra seleccionada",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener los items de la compra
        List<modelo.ItemCompra> items = compraService.obtenerItemsDeCompra(compraSeleccionada.getId());
        
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠ COMPRA VACÍA\n\n" +
                "Esta compra no tiene productos registrados.\n\n" +
                "Detalles de la compra:\n" +
                "• Factura: " + numeroFactura + "\n" +
                "• Proveedor: " + proveedorActual.getNombre() + "\n" +
                "• Descripción: " + compraSeleccionada.getDescripcion() + "\n" +
                "• Total: " + String.format("$%,d", compraSeleccionada.getTotal().longValue()).replace(",", "."),
                "Compra sin productos",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Mostrar diálogo con los items (solo lectura)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String fecha = compraSeleccionada.getFechaCompra().format(formatter);
        
        DialogoItems dialogo = new DialogoItems(
            null,  // parent
            items,
            proveedorActual.getNombre(),
            numeroFactura,
            fecha,
            true  // soloLectura = true
        );
        
        dialogo.setVisible(true);
    }
    
    private void agregarPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(TEXTO_SECUNDARIO);
        
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXTO_PRINCIPAL);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(TEXTO_SECUNDARIO);
                    textField.setText(placeholder);
                }
            }
        });
    }
}

package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import modelo.*;
import servicio.CompraService;
import servicio.CategoriaService;
import util.CampoNumericoFormateado;

public class FormularioCompraDarkConItems extends JDialog {
    
    // Tema oscuro
    private final Color BG_PRINCIPAL = new Color(30, 30, 30);
    private final Color BG_INPUT = new Color(60, 60, 60);
    private final Color TEXTO_PRINCIPAL = new Color(212, 212, 212);
    private final Color TEXTO_SECUNDARIO = new Color(150, 150, 150);
    private final Color BORDE = new Color(80, 80, 80);
    private final Color ACENTO = new Color(0, 122, 204);
    private final Color ADVERTENCIA = new Color(255, 193, 7);
    private final Color MORADO = new Color(156, 39, 176);
    private final Color VERDE = new Color(76, 175, 80);
    private final Color ROJO = new Color(244, 67, 54);
    
    private final CompraService compraService;
    private final CategoriaService categoriaService;
    private Compra compraActual;
    private Proveedor proveedor;
    
    // Campos del formulario
    private JTextField txtFactura, txtFechaCompra, txtFechaPago, txtInfoPago, txtCategoriaPersonalizada;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbCategoria;
    private JComboBox<FormaPago> cmbFormaPago;
    private JComboBox<EstadoCredito> cmbEstadoCredito;
    private JCheckBox chkPagado;
    private JButton btnInscribirProductos;
    private JLabel lblEstadoCredito, lblFechaPago, lblInfoPago, lblCategoriaPersonalizada, lblTotalCalculado;
    
    // Items
    private List<ItemCompra> listaItems;
    
    public FormularioCompraDarkConItems(VentanaUnificada padre, Compra compra, Proveedor proveedor) {
        super(padre, compra == null ? "Nueva Compra" : "Editar Compra", true);
        
        this.compraService = padre.getCompraService();
        this.categoriaService = new CategoriaService();
        this.compraActual = compra;
        this.proveedor = proveedor;
        this.listaItems = new ArrayList<>();
        
        configurarDialogo();
        inicializarComponentes();
        
        if (compra != null) {
            cargarDatos();
        }
    }
    
    private void configurarDialogo() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(true); // Permitir maximizar
        getContentPane().setBackground(BG_PRINCIPAL);
        
        // Cargar icono de la aplicación
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("lib/ModuloProveedores.png");
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }

    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(BG_PRINCIPAL);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // ===== SECCIÓN 1: Datos Generales =====
        agregarSeccionDatosGenerales(panelPrincipal);
        
        panelPrincipal.add(Box.createVerticalStrut(15));
        
        // ===== SECCIÓN 2: Items de la Compra =====
        agregarSeccionItems(panelPrincipal);
        
        panelPrincipal.add(Box.createVerticalStrut(15));
        
        // ===== SECCIÓN 3: Forma de Pago =====
        agregarSeccionFormaPago(panelPrincipal);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        
        JButton btnGuardar = crearBoton("Guardar", VERDE);
        JButton btnCancelar = crearBoton("Cancelar", BG_INPUT);
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        JScrollPane scroll = new JScrollPane(panelPrincipal);
        scroll.getViewport().setBackground(BG_PRINCIPAL);
        ModernScrollBarUI.aplicarScrollModerno(scroll);
        
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void agregarSeccionDatosGenerales(JPanel panel) {
        // Proveedor (solo lectura)
        JLabel lblProveedor = crearLabel("Proveedor", true);
        JLabel lblProveedorNombre = new JLabel(proveedor.getNombre());
        lblProveedorNombre.setForeground(MORADO);
        lblProveedorNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblProveedorNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblProveedor);
        panel.add(lblProveedorNombre);
        panel.add(Box.createVerticalStrut(10));
        
        txtFactura = agregarCampo(panel, "Nº Factura *", true);
        txtFechaCompra = agregarCampo(panel, "Fecha Compra (dd/mm/aa) *", true);
        txtFechaCompra.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        txtFechaCompra.setToolTipText("Formato: dd/mm/aa (ej: 03/01/26)");
        
        // Categoría con opción "otros"
        panel.add(crearLabel("Categoría *", true));
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBackground(BG_INPUT);
        cmbCategoria.setForeground(TEXTO_PRINCIPAL);
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbCategoria.addActionListener(e -> actualizarCampoCategoria());
        panel.add(cmbCategoria);
        panel.add(Box.createVerticalStrut(5));
        
        // Campo para categoría personalizada (oculto por defecto)
        lblCategoriaPersonalizada = crearLabel("Escriba la nueva categoría:", true);
        lblCategoriaPersonalizada.setVisible(false);
        txtCategoriaPersonalizada = crearTextField();
        txtCategoriaPersonalizada.setVisible(false);
        txtCategoriaPersonalizada.setToolTipText("Ejemplo: herramientas, materiales, pocillos, etc.");
        panel.add(lblCategoriaPersonalizada);
        panel.add(txtCategoriaPersonalizada);
        panel.add(Box.createVerticalStrut(10));
        
        // Cargar categorías DESPUÉS de crear los componentes
        cargarCategorias();
        
        // Descripción general
        txtDescripcion = agregarTextArea(panel, "Descripción *");
    }
    
    private void agregarSeccionItems(JPanel panel) {
        // Botón para abrir diálogo de items
        btnInscribirProductos = crearBoton("Inscribir productos", MORADO);
        btnInscribirProductos.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInscribirProductos.setPreferredSize(new Dimension(200, 40));
        btnInscribirProductos.setMaximumSize(new Dimension(200, 40));
        btnInscribirProductos.addActionListener(e -> abrirDialogoItems());
        panel.add(btnInscribirProductos);
        panel.add(Box.createVerticalStrut(10));
        
        // Label para mostrar total
        lblTotalCalculado = new JLabel("");
        lblTotalCalculado.setForeground(VERDE);
        lblTotalCalculado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalCalculado.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTotalCalculado.setVisible(false);
        panel.add(lblTotalCalculado);
    }
    
    private void abrirDialogoItems() {
        // Obtener datos actuales del formulario
        String proveedorNombre = proveedor.getNombre();
        String facturaNumero = txtFactura.getText().trim();
        String fechaTexto = txtFechaCompra.getText().trim();
        
        // Si no hay número de factura aún, dejar vacío para que el usuario lo ingrese
        if (facturaNumero.isEmpty()) {
            facturaNumero = "";
        }
        
        // Si no hay fecha, usar la actual
        if (fechaTexto.isEmpty()) {
            fechaTexto = txtFechaCompra.getText(); // Ya tiene la fecha por defecto
        }
        
        DialogoItems dialogo = new DialogoItems(this, listaItems, proveedorNombre, facturaNumero, fechaTexto);
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            listaItems = dialogo.getItems();
            BigDecimal total = dialogo.getTotal();
            
            // Actualizar campos de fecha y factura con los valores editados en el diálogo
            String facturaEditada = dialogo.getNumeroFactura();
            String fechaEditada = dialogo.getFechaCompra();
            
            if (!facturaEditada.isEmpty()) {
                txtFactura.setText(facturaEditada);
            }
            
            if (!fechaEditada.isEmpty()) {
                txtFechaCompra.setText(fechaEditada);
            }
            
            if (!listaItems.isEmpty()) {
                lblTotalCalculado.setText(String.format("Items: %d | TOTAL: $%,.0f", 
                    listaItems.size(), total));
                lblTotalCalculado.setVisible(true);
            } else {
                lblTotalCalculado.setVisible(false);
            }
        }
    }

    
    private void agregarSeccionFormaPago(JPanel panel) {
        // Título de sección
        JLabel lblTitulo = new JLabel("Forma de Pago");
        lblTitulo.setForeground(ACENTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        
        cmbFormaPago = agregarCombo(panel, "Forma de Pago *", FormaPago.values());
        cmbFormaPago.addActionListener(e -> actualizarCamposCredito());
        
        lblInfoPago = crearLabel("Info de Pago (banco, cuenta, comprobante)", false);
        txtInfoPago = crearTextField();
        panel.add(lblInfoPago);
        panel.add(txtInfoPago);
        panel.add(Box.createVerticalStrut(5));
        
        lblEstadoCredito = crearLabel("Estado Crédito", false);
        cmbEstadoCredito = crearComboBox(EstadoCredito.values());
        cmbEstadoCredito.addActionListener(e -> actualizarCampoFechaPago());
        panel.add(lblEstadoCredito);
        panel.add(cmbEstadoCredito);
        panel.add(Box.createVerticalStrut(5));
        
        // Checkbox para marcar como pagado (para efectivo y transferencia)
        chkPagado = new JCheckBox("Marcar como pagado");
        chkPagado.setBackground(BG_PRINCIPAL);
        chkPagado.setForeground(TEXTO_PRINCIPAL);
        chkPagado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkPagado.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkPagado.addActionListener(e -> actualizarCampoFechaPagoPorCheckbox());
        panel.add(chkPagado);
        panel.add(Box.createVerticalStrut(5));
        
        lblFechaPago = crearLabel("Fecha de Pago (dd/mm/aa)", false);
        txtFechaPago = crearTextField();
        txtFechaPago.setToolTipText("Formato: dd/mm/aa (ej: 03/01/26)");
        panel.add(lblFechaPago);
        panel.add(txtFechaPago);
        
        actualizarCamposCredito();
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private void actualizarCamposCredito() {
        FormaPago formaPago = (FormaPago) cmbFormaPago.getSelectedItem();
        boolean esCredito = formaPago == FormaPago.CREDITO;
        boolean esTransferencia = formaPago == FormaPago.TRANSFERENCIA;
        boolean esEfectivo = formaPago == FormaPago.EFECTIVO;
        
        lblEstadoCredito.setVisible(esCredito);
        cmbEstadoCredito.setVisible(esCredito);
        
        lblInfoPago.setVisible(esCredito || esTransferencia);
        txtInfoPago.setVisible(esCredito || esTransferencia);
        
        chkPagado.setVisible(esEfectivo || esTransferencia);
        
        if (esEfectivo || esTransferencia) {
            actualizarCampoFechaPagoPorCheckbox();
        } else if (!esCredito) {
            lblFechaPago.setVisible(false);
            txtFechaPago.setVisible(false);
            txtFechaPago.setText("");
        } else {
            actualizarCampoFechaPago();
        }
    }
    
    private void actualizarCampoFechaPagoPorCheckbox() {
        FormaPago formaPago = (FormaPago) cmbFormaPago.getSelectedItem();
        if (formaPago != FormaPago.EFECTIVO && formaPago != FormaPago.TRANSFERENCIA) {
            return;
        }
        
        boolean estaPagado = chkPagado.isSelected();
        
        lblFechaPago.setVisible(estaPagado);
        txtFechaPago.setVisible(estaPagado);
        lblFechaPago.setText("Fecha de Pago (dd/mm/aa) *");
        
        if (estaPagado && txtFechaPago.getText().isEmpty()) {
            txtFechaPago.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        } else if (!estaPagado) {
            txtFechaPago.setText("");
        }
    }
    
    private void actualizarCampoFechaPago() {
        FormaPago formaPago = (FormaPago) cmbFormaPago.getSelectedItem();
        if (formaPago != FormaPago.CREDITO) {
            return;
        }
        
        EstadoCredito estado = (EstadoCredito) cmbEstadoCredito.getSelectedItem();
        boolean esPagado = estado == EstadoCredito.PAGADO;
        
        lblFechaPago.setVisible(esPagado);
        txtFechaPago.setVisible(esPagado);
        lblFechaPago.setText("Fecha de Pago (dd/mm/aa) *");
        
        if (!esPagado) {
            txtFechaPago.setText("");
        }
    }
    
    private void actualizarCampoCategoria() {
        String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
        boolean esOtros = categoriaSeleccionada != null && categoriaSeleccionada.equals("otros");
        
        lblCategoriaPersonalizada.setVisible(esOtros);
        txtCategoriaPersonalizada.setVisible(esOtros);
        
        if (esOtros) {
            txtCategoriaPersonalizada.requestFocus();
        } else {
            txtCategoriaPersonalizada.setText("");
        }
    }
    
    private void cargarCategorias() {
        try {
            cmbCategoria.removeAllItems();
            List<String> categorias = categoriaService.obtenerTodasCategorias();
            
            if (categorias == null || categorias.isEmpty()) {
                cmbCategoria.addItem("zapatos");
                cmbCategoria.addItem("pantalones");
                cmbCategoria.addItem("ropa");
                cmbCategoria.addItem("camisas");
                cmbCategoria.addItem("accesorios");
                cmbCategoria.addItem("otros");
            } else {
                for (String cat : categorias) {
                    cmbCategoria.addItem(cat);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            cmbCategoria.addItem("zapatos");
            cmbCategoria.addItem("pantalones");
            cmbCategoria.addItem("ropa");
            cmbCategoria.addItem("camisas");
            cmbCategoria.addItem("accesorios");
            cmbCategoria.addItem("otros");
        }
    }

    
    private void cargarDatos() {
        txtFactura.setText(compraActual.getNumeroFactura());
        txtFechaCompra.setText(compraActual.getFechaCompra().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        txtDescripcion.setText(compraActual.getDescripcion());
        
        // Cargar categoría
        String categoriaActual = compraActual.getCategoria();
        boolean categoriaExiste = false;
        
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).equals(categoriaActual)) {
                cmbCategoria.setSelectedIndex(i);
                categoriaExiste = true;
                break;
            }
        }
        
        if (!categoriaExiste) {
            cmbCategoria.setSelectedItem("otros");
            txtCategoriaPersonalizada.setText(categoriaActual);
            lblCategoriaPersonalizada.setVisible(true);
            txtCategoriaPersonalizada.setVisible(true);
        }
        
        cmbFormaPago.setSelectedItem(compraActual.getFormaPago());
        
        if (compraActual.getEstadoCredito() != null) {
            cmbEstadoCredito.setSelectedItem(compraActual.getEstadoCredito());
        }
        
        if (compraActual.getFechaPago() != null) {
            txtFechaPago.setText(compraActual.getFechaPago().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            if (compraActual.getFormaPago() == FormaPago.EFECTIVO || 
                compraActual.getFormaPago() == FormaPago.TRANSFERENCIA) {
                chkPagado.setSelected(true);
            }
        }
        
        // Cargar items si existen
        List<ItemCompra> items = compraService.obtenerItemsDeCompra(compraActual.getId());
        if (items != null && !items.isEmpty()) {
            listaItems = items;
            BigDecimal total = BigDecimal.ZERO;
            for (ItemCompra item : listaItems) {
                total = total.add(item.getSubtotal());
            }
            lblTotalCalculado.setText(String.format("Items: %d | TOTAL: $%,.0f", 
                listaItems.size(), total));
            lblTotalCalculado.setVisible(true);
        }
        
        actualizarCamposCredito();
    }
    
    private void guardar() {
        try {
            Compra compra = compraActual != null ? compraActual : new Compra();
            
            compra.setIdProveedor(proveedor.getId());
            compra.setNumeroFactura(txtFactura.getText().trim());
            
            if (compra.getNumeroFactura().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El número de factura es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Obtener categoría
            String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
            String categoriaFinal = "";
            
            if (categoriaSeleccionada == null || categoriaSeleccionada.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (categoriaSeleccionada.equals("otros")) {
                String categoriaPersonalizada = txtCategoriaPersonalizada.getText().trim();
                if (categoriaPersonalizada.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Debe escribir el nombre de la categoría personalizada", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    txtCategoriaPersonalizada.requestFocus();
                    return;
                }
                categoriaFinal = categoriaPersonalizada.toLowerCase();
            } else {
                categoriaFinal = categoriaSeleccionada.toLowerCase();
            }
            
            // Guardar nueva categoría si no existe
            if (!categoriaService.existeCategoria(categoriaFinal)) {
                String resultado = categoriaService.agregarCategoria(categoriaFinal);
                if (resultado.startsWith("Error")) {
                    JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            compra.setCategoria(categoriaFinal);
            
            // Descripción general
            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            compra.setDescripcion(descripcion);
            
            // Fecha de compra
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yy][dd/MM/yyyy]");
            String fechaCompraStr = txtFechaCompra.getText().trim();
            if (fechaCompraStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la fecha de compra", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            compra.setFechaCompra(LocalDate.parse(fechaCompraStr, formatter));
            
            compra.setFormaPago((FormaPago) cmbFormaPago.getSelectedItem());
            
            if (compra.getFormaPago() == FormaPago.CREDITO) {
                compra.setEstadoCredito((EstadoCredito) cmbEstadoCredito.getSelectedItem());
                
                if (compra.getEstadoCredito() == EstadoCredito.PAGADO) {
                    String fechaPagoStr = txtFechaPago.getText().trim();
                    if (!fechaPagoStr.isEmpty()) {
                        compra.setFechaPago(LocalDate.parse(fechaPagoStr, formatter));
                    }
                } else {
                    compra.setFechaPago(null);
                }
            } else if (compra.getFormaPago() == FormaPago.EFECTIVO || compra.getFormaPago() == FormaPago.TRANSFERENCIA) {
                compra.setEstadoCredito(null);
                
                if (chkPagado.isSelected()) {
                    String fechaPagoStr = txtFechaPago.getText().trim();
                    if (fechaPagoStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Debe ingresar la fecha de pago si marca como pagado", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    compra.setFechaPago(LocalDate.parse(fechaPagoStr, formatter));
                } else {
                    compra.setFechaPago(null);
                }
            } else {
                compra.setEstadoCredito(null);
                compra.setFechaPago(null);
            }
            
            // Guardar compra con o sin items
            String resultado;
            if (!listaItems.isEmpty()) {
                // Guardar con items
                if (compraActual == null) {
                    resultado = compraService.guardarCompraConItems(compra, listaItems);
                } else {
                    resultado = compraService.actualizarCompraConItems(compra, listaItems);
                }
            } else {
                // Guardar sin items (modo simple)
                // Pedir total
                String totalStr = JOptionPane.showInputDialog(this, 
                    "Ingrese el total de la compra:", 
                    "Total", 
                    JOptionPane.QUESTION_MESSAGE);
                if (totalStr == null || totalStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El total es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    // Limpiar formato y convertir
                    totalStr = totalStr.replaceAll("[^0-9,]", "").replace(",", ".");
                    compra.setTotal(new BigDecimal(totalStr));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Total inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (compraActual == null) {
                    resultado = compraService.registrarCompra(compra);
                } else {
                    resultado = compraService.actualizarCompra(compra);
                }
            }
            
            if (resultado.startsWith("Error")) {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // ===== MÉTODOS DE CREACIÓN DE COMPONENTES =====
    
    private JLabel crearLabel(String texto, boolean obligatorio) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(obligatorio ? ACENTO : TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return lbl;
    }
    
    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXTO_PRINCIPAL);
        txt.setCaretColor(TEXTO_PRINCIPAL);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }
    
    private JTextField agregarCampo(JPanel panel, String label, boolean obligatorio) {
        panel.add(crearLabel(label, obligatorio));
        JTextField txt = crearTextField();
        panel.add(txt);
        return txt;
    }
    
    private <T> JComboBox<T> crearComboBox(T[] items) {
        JComboBox<T> cmb = new JComboBox<>(items);
        cmb.setBackground(BG_INPUT);
        cmb.setForeground(TEXTO_PRINCIPAL);
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cmb;
    }
    
    private <T> JComboBox<T> agregarCombo(JPanel panel, String label, T[] items) {
        panel.add(crearLabel(label, true));
        JComboBox<T> cmb = crearComboBox(items);
        panel.add(cmb);
        return cmb;
    }
    
    private JTextArea agregarTextArea(JPanel panel, String label) {
        panel.add(crearLabel(label, true));
        
        JTextArea txt = new JTextArea(3, 20);
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXTO_PRINCIPAL);
        txt.setCaretColor(TEXTO_PRINCIPAL);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scroll = new JScrollPane(txt);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        ModernScrollBarUI.aplicarScrollModerno(scroll);
        
        panel.add(scroll);
        return txt;
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
        return btn;
    }
}

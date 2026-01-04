package vista;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import modelo.*;
import servicio.CompraService;
import servicio.CategoriaService;
import util.CampoNumericoFormateado;

public class FormularioCompraDark extends JDialog {
    
    // Tema oscuro
    private final Color BG_PRINCIPAL = new Color(30, 30, 30);
    private final Color BG_INPUT = new Color(60, 60, 60);
    private final Color TEXTO_PRINCIPAL = new Color(212, 212, 212);
    private final Color TEXTO_SECUNDARIO = new Color(150, 150, 150);
    private final Color BORDE = new Color(80, 80, 80);
    private final Color ACENTO = new Color(0, 122, 204);
    private final Color ADVERTENCIA = new Color(255, 193, 7);
    private final Color MORADO = new Color(156, 39, 176);
    
    private final CompraService compraService;
    private final CategoriaService categoriaService;
    private Compra compraActual;
    private Proveedor proveedor;
    
    private JTextField txtFactura, txtCantidad, txtFechaCompra, txtFechaPago, txtInfoPago, txtCategoriaPersonalizada;
    private CampoNumericoFormateado txtPrecioUnit, txtTotal;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbCategoria;
    private JComboBox<FormaPago> cmbFormaPago;
    private JComboBox<EstadoCredito> cmbEstadoCredito;
    private JCheckBox chkPagado;  // Nuevo checkbox para marcar como pagado
    private JLabel lblEstadoCredito, lblFechaPago, lblInfoPago, lblCategoriaPersonalizada;
    
    public FormularioCompraDark(VentanaUnificada padre, Compra compra, Proveedor proveedor) {
        super(padre, compra == null ? "Nueva Compra" : "Editar Compra", true);
        
        this.compraService = padre.getCompraService();
        this.categoriaService = new CategoriaService();
        this.compraActual = compra;
        this.proveedor = proveedor;
        
        configurarDialogo();
        inicializarComponentes();
        
        if (compra != null) {
            cargarDatos();
        }
    }
    
    private void configurarDialogo() {
        setSize(600, 750);
        setLocationRelativeTo(getParent());
        setResizable(false);
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
        
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(BG_PRINCIPAL);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Proveedor (solo lectura)
        JLabel lblProveedor = crearLabel("Proveedor", true);
        JLabel lblProveedorNombre = new JLabel(proveedor.getNombre());
        lblProveedorNombre.setForeground(ACENTO);
        lblProveedorNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblProveedorNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCampos.add(lblProveedor);
        panelCampos.add(lblProveedorNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        
        txtFactura = agregarCampo(panelCampos, "Nº Factura *", true);
        
        // Categoría con opción "otros"
        panelCampos.add(crearLabel("Categoría *", true));
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBackground(BG_INPUT);
        cmbCategoria.setForeground(TEXTO_PRINCIPAL);
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbCategoria.addActionListener(e -> actualizarCampoCategoria());
        cargarCategorias();
        panelCampos.add(cmbCategoria);
        panelCampos.add(Box.createVerticalStrut(5));
        
        // Campo para categoría personalizada (oculto por defecto)
        lblCategoriaPersonalizada = crearLabel("Escriba la nueva categoría:", true);
        lblCategoriaPersonalizada.setVisible(false);
        txtCategoriaPersonalizada = crearTextField();
        txtCategoriaPersonalizada.setVisible(false);
        txtCategoriaPersonalizada.setToolTipText("Ejemplo: herramientas, materiales, pocillos, etc.");
        panelCampos.add(lblCategoriaPersonalizada);
        panelCampos.add(txtCategoriaPersonalizada);
        panelCampos.add(Box.createVerticalStrut(5));
        
        txtDescripcion = agregarTextArea(panelCampos, "Descripción *");
        
        txtCantidad = agregarCampo(panelCampos, "Cantidad (opcional)", false);
        
        // Precio Unitario con formato numérico
        panelCampos.add(crearLabel("Precio Unitario (opcional)", false));
        txtPrecioUnit = new CampoNumericoFormateado("100.000");
        txtPrecioUnit.setBackground(BG_INPUT);
        txtPrecioUnit.setColorTexto(TEXTO_PRINCIPAL);
        txtPrecioUnit.setColorPlaceholder(TEXTO_SECUNDARIO);
        txtPrecioUnit.setCaretColor(TEXTO_PRINCIPAL);
        txtPrecioUnit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPrecioUnit.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtPrecioUnit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPrecioUnit.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCampos.add(txtPrecioUnit);
        panelCampos.add(Box.createVerticalStrut(10));
        
        // Total con formato numérico
        panelCampos.add(crearLabel("Total *", true));
        txtTotal = new CampoNumericoFormateado("1.000.000");
        txtTotal.setBackground(BG_INPUT);
        txtTotal.setColorTexto(TEXTO_PRINCIPAL);
        txtTotal.setColorPlaceholder(TEXTO_SECUNDARIO);
        txtTotal.setCaretColor(TEXTO_PRINCIPAL);
        txtTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTotal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCampos.add(txtTotal);
        panelCampos.add(Box.createVerticalStrut(10));
        
        txtFechaCompra = agregarCampo(panelCampos, "Fecha Compra (dd/mm/aa) *", true);
        txtFechaCompra.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        txtFechaCompra.setToolTipText("Formato: dd/mm/aa (ej: 03/01/26)");
        agregarPlaceholder(txtFechaCompra, "dd/mm/aa");
        
        cmbFormaPago = agregarCombo(panelCampos, "Forma de Pago *", FormaPago.values());
        cmbFormaPago.addActionListener(e -> actualizarCamposCredito());
        
        lblInfoPago = crearLabel("Info de Pago (banco, cuenta, comprobante)", false);
        txtInfoPago = crearTextField();
        panelCampos.add(lblInfoPago);
        panelCampos.add(txtInfoPago);
        panelCampos.add(Box.createVerticalStrut(5));
        
        lblEstadoCredito = crearLabel("Estado Crédito", false);
        cmbEstadoCredito = crearComboBox(EstadoCredito.values());
        cmbEstadoCredito.addActionListener(e -> actualizarCampoFechaPago());
        panelCampos.add(lblEstadoCredito);
        panelCampos.add(cmbEstadoCredito);
        panelCampos.add(Box.createVerticalStrut(5));
        
        // Checkbox para marcar como pagado (para efectivo y transferencia)
        chkPagado = new JCheckBox("Marcar como pagado");
        chkPagado.setBackground(BG_PRINCIPAL);
        chkPagado.setForeground(TEXTO_PRINCIPAL);
        chkPagado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkPagado.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkPagado.addActionListener(e -> actualizarCampoFechaPagoPorCheckbox());
        panelCampos.add(chkPagado);
        panelCampos.add(Box.createVerticalStrut(5));
        
        lblFechaPago = crearLabel("Fecha de Pago (dd/mm/aa)", false);
        txtFechaPago = crearTextField();
        txtFechaPago.setToolTipText("Formato: dd/mm/aa (ej: 03/01/26)");
        agregarPlaceholder(txtFechaPago, "dd/mm/aa");
        panelCampos.add(lblFechaPago);
        panelCampos.add(txtFechaPago);
        
        actualizarCamposCredito();
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        
        JButton btnGuardar = crearBoton("Guardar", ACENTO);
        JButton btnCancelar = crearBoton("Cancelar", BG_INPUT);
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        JScrollPane scroll = new JScrollPane(panelCampos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PRINCIPAL);
        
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    
    private JLabel crearLabel(String texto, boolean obligatorio) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(obligatorio ? ACENTO : TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return lbl;
    }
    
    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXTO_PRINCIPAL);
        txt.setCaretColor(TEXTO_PRINCIPAL);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        
        JTextArea txt = new JTextArea(4, 20);
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXTO_PRINCIPAL);
        txt.setCaretColor(TEXTO_PRINCIPAL);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scroll = new JScrollPane(txt);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(scroll);
        return txt;
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }
    
    private void actualizarCamposCredito() {
        FormaPago formaPago = (FormaPago) cmbFormaPago.getSelectedItem();
        boolean esCredito = formaPago == FormaPago.CREDITO;
        boolean esTransferencia = formaPago == FormaPago.TRANSFERENCIA;
        boolean esEfectivo = formaPago == FormaPago.EFECTIVO;
        
        lblEstadoCredito.setVisible(esCredito);
        cmbEstadoCredito.setVisible(esCredito);
        
        lblInfoPago.setVisible(esCredito || esTransferencia);
        txtInfoPago.setVisible(esCredito || esTransferencia);
        
        // Mostrar checkbox para efectivo y transferencia
        chkPagado.setVisible(esEfectivo || esTransferencia);
        
        // Para efectivo y transferencia, mostrar fecha de pago según checkbox
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
            // Sugerir fecha actual solo si está marcado como pagado
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
                // Si no hay categorías, agregar las predefinidas
                cmbCategoria.addItem("zapatos");
                cmbCategoria.addItem("pantalones");
                cmbCategoria.addItem("ropa");
                cmbCategoria.addItem("camisas");
                cmbCategoria.addItem("accesorios");
                cmbCategoria.addItem("otros");
                System.out.println("Categorías cargadas por defecto");
            } else {
                for (String cat : categorias) {
                    cmbCategoria.addItem(cat);
                }
                System.out.println("Categorías cargadas desde BD: " + categorias.size());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
            // Cargar categorías por defecto en caso de error
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
        
        // Cargar categoría
        String categoriaActual = compraActual.getCategoria();
        boolean categoriaExiste = false;
        
        // Verificar si la categoría existe en el combo
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).equals(categoriaActual)) {
                cmbCategoria.setSelectedIndex(i);
                categoriaExiste = true;
                break;
            }
        }
        
        // Si no existe, es una categoría personalizada
        if (!categoriaExiste) {
            cmbCategoria.setSelectedItem("otros");
            txtCategoriaPersonalizada.setText(categoriaActual);
            lblCategoriaPersonalizada.setVisible(true);
            txtCategoriaPersonalizada.setVisible(true);
        }
        
        txtDescripcion.setText(compraActual.getDescripcion());
        
        if (compraActual.getCantidad() != null) {
            txtCantidad.setText(compraActual.getCantidad().toString());
        }
        
        if (compraActual.getPrecioUnitario() != null) {
            txtPrecioUnit.setValorNumerico(compraActual.getPrecioUnitario());
        }
        
        txtTotal.setValorNumerico(compraActual.getTotal());
        txtFechaCompra.setText(compraActual.getFechaCompra().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        cmbFormaPago.setSelectedItem(compraActual.getFormaPago());
        
        if (compraActual.getEstadoCredito() != null) {
            cmbEstadoCredito.setSelectedItem(compraActual.getEstadoCredito());
        }
        
        if (compraActual.getFechaPago() != null) {
            txtFechaPago.setText(compraActual.getFechaPago().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            // Si tiene fecha de pago, marcar el checkbox
            if (compraActual.getFormaPago() == FormaPago.EFECTIVO || 
                compraActual.getFormaPago() == FormaPago.TRANSFERENCIA) {
                chkPagado.setSelected(true);
            }
        }
        
        actualizarCamposCredito();
    }
    
    private void guardar() {
        try {
            Compra compra = compraActual != null ? compraActual : new Compra();
            
            compra.setIdProveedor(proveedor.getId());
            compra.setNumeroFactura(txtFactura.getText().trim());
            
            // Obtener categoría
            String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
            String categoriaFinal = "";
            
            if (categoriaSeleccionada == null || categoriaSeleccionada.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Si seleccionó "otros", usar el campo de texto personalizado
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
            compra.setDescripcion(txtDescripcion.getText().trim());
            
            String cantidadStr = txtCantidad.getText().trim();
            if (!cantidadStr.isEmpty()) {
                compra.setCantidad(Integer.parseInt(cantidadStr));
            } else {
                compra.setCantidad(null);
            }
            
            // Obtener precio unitario del campo formateado
            if (!txtPrecioUnit.estaVacio()) {
                compra.setPrecioUnitario(txtPrecioUnit.getValorNumerico());
            } else {
                compra.setPrecioUnitario(null);
            }
            
            // Obtener total del campo formateado
            if (txtTotal.estaVacio()) {
                JOptionPane.showMessageDialog(this, "El total es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            compra.setTotal(txtTotal.getValorNumerico());
            
            // Soportar formato dd/mm/aa y dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yy][dd/MM/yyyy]");
            String fechaCompraStr = txtFechaCompra.getText().trim();
            // Ignorar placeholder
            if (fechaCompraStr.equals("dd/mm/aa")) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la fecha de compra", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            compra.setFechaCompra(LocalDate.parse(fechaCompraStr, formatter));
            
            compra.setFormaPago((FormaPago) cmbFormaPago.getSelectedItem());
            
            if (compra.getFormaPago() == FormaPago.CREDITO) {
                compra.setEstadoCredito((EstadoCredito) cmbEstadoCredito.getSelectedItem());
                
                if (compra.getEstadoCredito() == EstadoCredito.PAGADO) {
                    String fechaPagoStr = txtFechaPago.getText().trim();
                    // Ignorar placeholder
                    if (!fechaPagoStr.isEmpty() && !fechaPagoStr.equals("dd/mm/aa")) {
                        compra.setFechaPago(LocalDate.parse(fechaPagoStr, formatter));
                    }
                } else {
                    compra.setFechaPago(null);
                }
            } else if (compra.getFormaPago() == FormaPago.EFECTIVO || compra.getFormaPago() == FormaPago.TRANSFERENCIA) {
                // Para efectivo y transferencia, guardar fecha de pago según checkbox
                compra.setEstadoCredito(null);
                
                if (chkPagado.isSelected()) {
                    // Si está marcado como pagado, debe tener fecha
                    String fechaPagoStr = txtFechaPago.getText().trim();
                    if (fechaPagoStr.isEmpty() || fechaPagoStr.equals("dd/mm/aa")) {
                        JOptionPane.showMessageDialog(this, 
                            "Debe ingresar la fecha de pago si marca como pagado", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    compra.setFechaPago(LocalDate.parse(fechaPagoStr, formatter));
                } else {
                    // Si no está marcado, dejar como pendiente (sin fecha)
                    compra.setFechaPago(null);
                }
            } else {
                compra.setEstadoCredito(null);
                compra.setFechaPago(null);
            }
            
            String resultado;
            if (compraActual == null) {
                resultado = compraService.registrarCompra(compra);
            } else {
                resultado = compraService.actualizarCompra(compra);
            }
            
            if (resultado.startsWith("Error")) {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarPlaceholder(JTextField textField, String placeholder) {
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

package vista;

import javax.swing.*;
import java.awt.*;
import modelo.Proveedor;
import servicio.ProveedorService;

public class FormularioProveedorDark extends JDialog {
    
    // Tema oscuro azul (igual que VentanaUnificada)
    private final Color BG_PRINCIPAL = new Color(25, 35, 55);  // Azul oscuro
    private final Color BG_PANEL = new Color(30, 42, 65);
    private final Color BG_INPUT = new Color(45, 58, 82);
    private final Color TEXTO_PRINCIPAL = new Color(220, 220, 220);
    private final Color TEXTO_SECUNDARIO = new Color(160, 160, 160);
    private final Color BORDE = new Color(70, 85, 110);
    private final Color ACENTO = new Color(0, 150, 255);  // Azul brillante
    private final Color CREDITO_PAGADO = new Color(80, 255, 120);   // Verde brillante
    private final Color CREDITO_PENDIENTE = new Color(255, 80, 80); // Rojo brillante
    
    private final ProveedorService proveedorService;
    private Proveedor proveedorActual;
    
    private JTextField txtNombre, txtNit, txtTipo, txtDireccion, txtTelefono, txtEmail, txtContacto, txtInfoPago;
    private ToggleSwitch toggleActivo;
    private JLabel lblEstadoActivo;
    
    public FormularioProveedorDark(VentanaUnificada padre, Proveedor proveedor) {
        super(padre, proveedor == null ? "Nuevo Proveedor" : "Editar Proveedor", true);
        
        this.proveedorService = padre.getProveedorService();
        this.proveedorActual = proveedor;
        
        configurarDialogo();
        inicializarComponentes();
        
        if (proveedor != null) {
            cargarDatos();
        }
    }
    
    private void configurarDialogo() {
        setSize(600, 680);
        setLocationRelativeTo(getParent());
        setResizable(true);
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
        
        // Panel principal con scroll
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(BG_PRINCIPAL);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        // ===== TOGGLE SWITCH AL PRINCIPIO =====
        JPanel panelToggle = new JPanel(new BorderLayout(15, 0));
        panelToggle.setBackground(BG_PRINCIPAL);
        panelToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panelToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelToggle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblEstadoTitulo = new JLabel("Estado del Proveedor:");
        lblEstadoTitulo.setForeground(TEXTO_PRINCIPAL);
        lblEstadoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel panelToggleDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelToggleDerecha.setBackground(BG_PRINCIPAL);
        
        lblEstadoActivo = new JLabel("Activo");
        lblEstadoActivo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstadoActivo.setForeground(CREDITO_PAGADO);
        
        toggleActivo = new ToggleSwitch();
        toggleActivo.setActivo(true); // Por defecto activo
        
        // Listener para cambiar el texto y color
        toggleActivo.addPropertyChangeListener("estado", evt -> {
            boolean activo = (Boolean) evt.getNewValue();
            lblEstadoActivo.setText(activo ? "Activo" : "Inactivo");
            lblEstadoActivo.setForeground(activo ? CREDITO_PAGADO : CREDITO_PENDIENTE);
        });
        
        panelToggleDerecha.add(lblEstadoActivo);
        panelToggleDerecha.add(toggleActivo);
        
        panelToggle.add(lblEstadoTitulo, BorderLayout.WEST);
        panelToggle.add(panelToggleDerecha, BorderLayout.EAST);
        
        panelCampos.add(panelToggle);
        panelCampos.add(Box.createVerticalStrut(20)); // Espacio
        
        // ===== CAMPOS DEL FORMULARIO =====
        txtNombre = agregarCampo(panelCampos, "Nombre del Proveedor *", true);
        txtNit = agregarCampo(panelCampos, "NIT / Identificación", false);
        txtTipo = agregarCampo(panelCampos, "Tipo (ropa, calzado, insumos, varios)", false);
        txtDireccion = agregarCampo(panelCampos, "Dirección", false);
        txtTelefono = agregarCampo(panelCampos, "Teléfono", false);
        txtEmail = agregarCampo(panelCampos, "Email", false);
        txtContacto = agregarCampo(panelCampos, "Persona de Contacto", false);
        txtInfoPago = agregarCampo(panelCampos, "Info de Pago (banco, cuenta)", false);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(BG_PANEL);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE));
        
        JButton btnGuardar = crearBoton("💾 Guardar", ACENTO);
        JButton btnCancelar = crearBoton("✕ Cancelar", new Color(100, 100, 100));
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        // Scroll pane con estilo moderno
        JScrollPane scrollPane = new JScrollPane(panelCampos);
        scrollPane.setBackground(BG_PRINCIPAL);
        scrollPane.getViewport().setBackground(BG_PRINCIPAL);
        ModernScrollBarUI.aplicarScrollModerno(scrollPane);
        
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    
    private JTextField agregarCampo(JPanel panel, String label, boolean obligatorio) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(obligatorio ? ACENTO : TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));
        
        JTextField txt = new JTextField();
        txt.setBackground(BG_INPUT);
        txt.setForeground(TEXTO_PRINCIPAL);
        txt.setCaretColor(ACENTO);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Efecto focus
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACENTO, 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE, 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
        
        panel.add(lbl);
        panel.add(txt);
        
        return txt;
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 42));
        
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
    
    private void cargarDatos() {
        txtNombre.setText(proveedorActual.getNombre() != null ? proveedorActual.getNombre() : "");
        txtNit.setText(proveedorActual.getNit() != null ? proveedorActual.getNit() : "");
        txtTipo.setText(proveedorActual.getTipo() != null ? proveedorActual.getTipo() : "");
        txtDireccion.setText(proveedorActual.getDireccion() != null ? proveedorActual.getDireccion() : "");
        txtTelefono.setText(proveedorActual.getTelefono() != null ? proveedorActual.getTelefono() : "");
        txtEmail.setText(proveedorActual.getEmail() != null ? proveedorActual.getEmail() : "");
        txtContacto.setText(proveedorActual.getPersonaContacto() != null ? proveedorActual.getPersonaContacto() : "");
        txtInfoPago.setText(proveedorActual.getInformacionPago() != null ? proveedorActual.getInformacionPago() : "");
        
        // Cargar estado del toggle
        boolean activo = proveedorActual.isActivo();
        toggleActivo.setActivo(activo);
        lblEstadoActivo.setText(activo ? "Activo" : "Inactivo");
        lblEstadoActivo.setForeground(activo ? CREDITO_PAGADO : CREDITO_PENDIENTE);
    }
    
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        
        if (nombre.isEmpty()) {
            mostrarError("El nombre del proveedor es obligatorio");
            txtNombre.requestFocus();
            return;
        }
        
        Proveedor proveedor = proveedorActual != null ? proveedorActual : new Proveedor();
        proveedor.setNombre(nombre);
        proveedor.setNit(txtNit.getText().trim());
        proveedor.setTipo(txtTipo.getText().trim());
        proveedor.setDireccion(txtDireccion.getText().trim());
        proveedor.setTelefono(txtTelefono.getText().trim());
        proveedor.setEmail(txtEmail.getText().trim());
        proveedor.setPersonaContacto(txtContacto.getText().trim());
        proveedor.setInformacionPago(txtInfoPago.getText().trim());
        proveedor.setActivo(toggleActivo.isActivo()); // Usar el estado del toggle
        
        String resultado;
        if (proveedorActual == null) {
            resultado = proveedorService.registrarProveedor(proveedor);
        } else {
            resultado = proveedorService.actualizarProveedor(proveedor);
        }
        
        if (resultado.startsWith("Error")) {
            mostrarError(resultado);
        } else {
            mostrarExito(resultado);
            dispose();
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}

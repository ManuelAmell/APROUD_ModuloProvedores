package vista;

import javax.swing.*;
import java.awt.*;
import modelo.Proveedor;
import servicio.ProveedorService;

public class FormularioProveedorDark extends JDialog {
    
    // Tema oscuro
    private final Color BG_PRINCIPAL = new Color(30, 30, 30);
    private final Color BG_PANEL = new Color(37, 37, 38);
    private final Color BG_INPUT = new Color(60, 60, 60);
    private final Color TEXTO_PRINCIPAL = new Color(212, 212, 212);
    private final Color TEXTO_SECUNDARIO = new Color(150, 150, 150);
    private final Color BORDE = new Color(80, 80, 80);
    private final Color ACENTO = new Color(0, 122, 204);
    
    private final ProveedorService proveedorService;
    private Proveedor proveedorActual;
    
    private JTextField txtNombre, txtNit, txtTipo, txtDireccion, txtTelefono, txtEmail, txtContacto, txtInfoPago;
    private JCheckBox chkActivo;
    
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
        setSize(550, 600);
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
        
        txtNombre = agregarCampo(panelCampos, "Nombre *", true);
        txtNit = agregarCampo(panelCampos, "NIT (opcional)", false);
        txtTipo = agregarCampo(panelCampos, "Tipo (ropa, calzado, insumos, varios)", false);
        txtDireccion = agregarCampo(panelCampos, "Dirección", false);
        txtTelefono = agregarCampo(panelCampos, "Teléfono", false);
        txtEmail = agregarCampo(panelCampos, "Email", false);
        txtContacto = agregarCampo(panelCampos, "Persona de Contacto", false);
        txtInfoPago = agregarCampo(panelCampos, "Info de Pago (banco, cuenta)", false);
        
        JPanel panelActivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelActivo.setBackground(BG_PRINCIPAL);
        chkActivo = new JCheckBox("Activo");
        chkActivo.setSelected(true);
        chkActivo.setBackground(BG_PRINCIPAL);
        chkActivo.setForeground(TEXTO_PRINCIPAL);
        chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelActivo.add(chkActivo);
        panelCampos.add(panelActivo);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        
        JButton btnGuardar = crearBoton("Guardar", ACENTO);
        JButton btnCancelar = crearBoton("Cancelar", BG_INPUT);
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(new JScrollPane(panelCampos), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    
    private JTextField agregarCampo(JPanel panel, String label, boolean obligatorio) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(obligatorio ? ACENTO : TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
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
        
        panel.add(lbl);
        panel.add(txt);
        
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
    
    private void cargarDatos() {
        txtNombre.setText(proveedorActual.getNombre() != null ? proveedorActual.getNombre() : "");
        txtNit.setText(proveedorActual.getNit() != null ? proveedorActual.getNit() : "");
        txtTipo.setText(proveedorActual.getTipo() != null ? proveedorActual.getTipo() : "");
        txtDireccion.setText(proveedorActual.getDireccion() != null ? proveedorActual.getDireccion() : "");
        txtTelefono.setText(proveedorActual.getTelefono() != null ? proveedorActual.getTelefono() : "");
        txtEmail.setText(proveedorActual.getEmail() != null ? proveedorActual.getEmail() : "");
        txtContacto.setText(proveedorActual.getPersonaContacto() != null ? proveedorActual.getPersonaContacto() : "");
        txtInfoPago.setText(proveedorActual.getInformacionPago() != null ? proveedorActual.getInformacionPago() : "");
        chkActivo.setSelected(proveedorActual.isActivo());
    }
    
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
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
        proveedor.setActivo(chkActivo.isSelected());
        
        String resultado;
        if (proveedorActual == null) {
            resultado = proveedorService.registrarProveedor(proveedor);
        } else {
            resultado = proveedorService.actualizarProveedor(proveedor);
        }
        
        if (resultado.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}

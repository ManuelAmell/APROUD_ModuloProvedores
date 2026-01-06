package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import modelo.ItemCompra;

public class DialogoItems extends JDialog {
    
    // Tema oscuro
    private final Color BG_PRINCIPAL = new Color(30, 30, 30);
    private final Color BG_INPUT = new Color(60, 60, 60);
    private final Color TEXTO_PRINCIPAL = new Color(212, 212, 212);
    private final Color TEXTO_SECUNDARIO = new Color(150, 150, 150);
    private final Color BORDE = new Color(80, 80, 80);
    private final Color ACENTO = new Color(0, 122, 204);
    private final Color VERDE = new Color(76, 175, 80);
    private final Color ROJO = new Color(244, 67, 54);
    
    // Componentes
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalCalculado;
    private JLabel lblHoraActual;
    private JLabel lblResumenItems;
    private List<ItemCompra> listaItems;
    private boolean aceptado = false;
    private boolean actualizandoCelda = false;
    private javax.swing.Timer timerReloj;
    private boolean soloLectura = false;
    
    // Datos del contexto
    private String nombreProveedor;
    private JTextField txtNumeroFactura;
    private JTextField txtFechaCompra;
    
    public DialogoItems(JDialog parent, List<ItemCompra> itemsExistentes, String proveedor, String factura, String fecha) {
        this(parent, itemsExistentes, proveedor, factura, fecha, false);
    }
    
    public DialogoItems(JDialog parent, List<ItemCompra> itemsExistentes, String proveedor, String factura, String fecha, boolean soloLectura) {
        super(parent, soloLectura ? "Ver Productos" : "Inscribir Productos", true);
        
        this.soloLectura = soloLectura;
        
        this.listaItems = new ArrayList<>();
        if (itemsExistentes != null) {
            this.listaItems.addAll(itemsExistentes);
        }
        
        this.nombreProveedor = proveedor != null ? proveedor : "Sin especificar";
        
        configurarDialogo();
        inicializarComponentes();
        
        if (factura != null && !factura.trim().isEmpty()) {
            txtNumeroFactura.setForeground(TEXTO_PRINCIPAL);
            txtNumeroFactura.setText(factura);
        }
        if (fecha != null && !fecha.trim().isEmpty()) {
            txtFechaCompra.setForeground(TEXTO_PRINCIPAL);
            txtFechaCompra.setText(fecha);
        }
        
        cargarItemsExistentes();
    }
    
    private void configurarDialogo() {
        // Tamaño fijo razonable para ver todo el contenido
        setSize(1200, 750);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setMinimumSize(new Dimension(1000, 600));
        getContentPane().setBackground(BG_PRINCIPAL);
        
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("lib/ModuloProveedores.png");
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 0));
        
        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(BG_PRINCIPAL);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("FACTURA / REGISTRO DE COMPRAS");
        lblTitulo.setForeground(ACENTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel para título y reloj
        JPanel panelTituloReloj = new JPanel(new BorderLayout());
        panelTituloReloj.setBackground(BG_PRINCIPAL);
        panelTituloReloj.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panelTituloReloj.add(lblTitulo, BorderLayout.CENTER);
        
        // Reloj en la esquina superior derecha
        lblHoraActual = new JLabel();
        lblHoraActual.setForeground(ACENTO);
        lblHoraActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHoraActual.setHorizontalAlignment(JLabel.RIGHT);
        panelTituloReloj.add(lblHoraActual, BorderLayout.EAST);
        
        // Iniciar reloj
        actualizarHora();
        timerReloj = new javax.swing.Timer(1000, e -> actualizarHora());
        timerReloj.start();
        
        panelSuperior.add(panelTituloReloj);
        panelSuperior.add(Box.createVerticalStrut(15));
        
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(BORDE);
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        panelSuperior.add(sep1);
        panelSuperior.add(Box.createVerticalStrut(10));
        
        // PROVEEDOR
        JPanel panelProveedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelProveedor.setBackground(BG_PRINCIPAL);
        panelProveedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel lblProvTitulo = new JLabel("PROVEEDOR:");
        lblProvTitulo.setForeground(TEXTO_PRINCIPAL);
        lblProvTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblProvNombre = new JLabel(nombreProveedor);
        lblProvNombre.setForeground(new Color(156, 39, 176));
        lblProvNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelProveedor.add(lblProvTitulo);
        panelProveedor.add(lblProvNombre);
        panelSuperior.add(panelProveedor);
        panelSuperior.add(Box.createVerticalStrut(5));
        
        // FECHA Y FACTURA
        JPanel panelFechaFactura = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelFechaFactura.setBackground(BG_PRINCIPAL);
        panelFechaFactura.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblFechaTitulo = new JLabel("FECHA:");
        lblFechaTitulo.setForeground(TEXTO_PRINCIPAL);
        lblFechaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        txtFechaCompra = crearCampoConPlaceholder("dd/mm/aa", 12);
        if (soloLectura) txtFechaCompra.setEditable(false);
        
        panelFechaFactura.add(lblFechaTitulo);
        panelFechaFactura.add(txtFechaCompra);
        
        JLabel lblFactTitulo = new JLabel("No. FACTURA:");
        lblFactTitulo.setForeground(TEXTO_PRINCIPAL);
        lblFactTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        txtNumeroFactura = crearCampoConPlaceholder("Ingrese número de factura", 25);
        if (soloLectura) txtNumeroFactura.setEditable(false);
        
        panelFechaFactura.add(lblFactTitulo);
        panelFechaFactura.add(txtNumeroFactura);
        
        panelSuperior.add(panelFechaFactura);
        panelSuperior.add(Box.createVerticalStrut(10));
        
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(BORDE);
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        panelSuperior.add(sep2);
        panelSuperior.add(Box.createVerticalStrut(10));
        
        JLabel lblSubtitulo = new JLabel("Detalle de Productos");
        lblSubtitulo.setForeground(ACENTO);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSuperior.add(lblSubtitulo);
        panelSuperior.add(Box.createVerticalStrut(5));
        
        // Resumen de productos inscritos
        lblResumenItems = new JLabel("Productos inscritos: 0 | Items con datos: 0");
        lblResumenItems.setForeground(VERDE);
        lblResumenItems.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResumenItems.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSuperior.add(lblResumenItems);
        panelSuperior.add(Box.createVerticalStrut(3));
        
        JLabel lblInstrucciones = new JLabel("Haz clic en cualquier celda para editar. Usa Tab, Enter o flechas para moverte.");
        lblInstrucciones.setForeground(TEXTO_SECUNDARIO);
        lblInstrucciones.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInstrucciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSuperior.add(lblInstrucciones);
        
        // TABLA
        String[] columnas = {"#", "CANTIDAD", "DESCRIPCIÓN", "REF", "CÓDIGO", "COSTO", "TOTAL", "MÍNIMO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (soloLectura) return false; // En modo solo lectura, nada es editable
                return column != 0 && column != 6; // # y TOTAL no editables
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        
        tablaItems = new JTable(modeloTabla);
        configurarTablaEstiloExcel();
        
        JScrollPane scrollTabla = new JScrollPane(tablaItems);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        scrollTabla.getViewport().setBackground(BG_INPUT);
        ModernScrollBarUI.aplicarScrollModerno(scrollTabla);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(BG_PRINCIPAL);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        
        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(BG_PRINCIPAL);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonesTabla.setBackground(BG_PRINCIPAL);
        
        if (!soloLectura) {
            // Solo mostrar botones de edición si NO es modo solo lectura
            JButton btnAgregarFila = crearBoton("+ Agregar Fila", ACENTO);
            btnAgregarFila.setPreferredSize(new Dimension(140, 30));
            btnAgregarFila.addActionListener(e -> agregarFilaVacia());
            
            JButton btnEliminar = crearBoton("- Eliminar", ROJO);
            btnEliminar.setPreferredSize(new Dimension(120, 30));
            btnEliminar.addActionListener(e -> eliminarItem());
            
            panelBotonesTabla.add(btnAgregarFila);
            panelBotonesTabla.add(btnEliminar);
        }
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelTotal.setBackground(BG_PRINCIPAL);
        
        lblTotalCalculado = new JLabel("TOTAL: $0");
        lblTotalCalculado.setForeground(VERDE);
        lblTotalCalculado.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelTotal.add(lblTotalCalculado);
        
        panelInferior.add(panelBotonesTabla, BorderLayout.WEST);
        panelInferior.add(panelTotal, BorderLayout.EAST);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        
        if (soloLectura) {
            // Modo solo lectura: solo botón Cerrar
            JButton btnCerrar = crearBoton("Cerrar", BG_INPUT);
            btnCerrar.addActionListener(e -> {
                if (timerReloj != null) {
                    timerReloj.stop();
                }
                dispose();
            });
            panelBotones.add(btnCerrar);
        } else {
            // Modo edición: botones Aceptar y Cancelar
            JButton btnAceptar = crearBoton("Aceptar", VERDE);
            JButton btnCancelar = crearBoton("Cancelar", BG_INPUT);
            
            btnAceptar.addActionListener(e -> aceptar());
            btnCancelar.addActionListener(e -> cancelar());
            
            panelBotones.add(btnCancelar);
            panelBotones.add(btnAceptar);
        }
        
        JPanel panelInferiorCompleto = new JPanel(new BorderLayout());
        panelInferiorCompleto.setBackground(BG_PRINCIPAL);
        panelInferiorCompleto.add(panelInferior, BorderLayout.NORTH);
        panelInferiorCompleto.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelInferiorCompleto, BorderLayout.SOUTH);
    }
    
    private JTextField crearCampoConPlaceholder(String placeholder, int cols) {
        JTextField campo = new JTextField(cols);
        campo.setBackground(BG_INPUT);
        campo.setForeground(TEXTO_SECUNDARIO);
        campo.setCaretColor(TEXTO_PRINCIPAL);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACENTO, 2),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        campo.setText(placeholder);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(TEXTO_PRINCIPAL);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(VERDE, 2),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setForeground(TEXTO_SECUNDARIO);
                    campo.setText(placeholder);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACENTO, 2),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        
        return campo;
    }
    
    private void configurarTablaEstiloExcel() {
        // Apariencia
        tablaItems.setBackground(BG_INPUT);
        tablaItems.setForeground(TEXTO_PRINCIPAL);
        tablaItems.setGridColor(new Color(100, 100, 100));
        tablaItems.setSelectionBackground(ACENTO);
        tablaItems.setSelectionForeground(Color.WHITE);
        tablaItems.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaItems.setRowHeight(35);
        tablaItems.setShowGrid(true);
        tablaItems.setIntercellSpacing(new Dimension(1, 1));
        
        tablaItems.getTableHeader().setBackground(new Color(45, 45, 45));
        tablaItems.getTableHeader().setForeground(TEXTO_PRINCIPAL);
        tablaItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaItems.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        tablaItems.getTableHeader().setReorderingAllowed(false);
        
        tablaItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaItems.setFillsViewportHeight(true);
        
        // COMPORTAMIENTO TIPO EXCEL
        tablaItems.setCellSelectionEnabled(true);
        tablaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaItems.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tablaItems.setSurrendersFocusOnKeystroke(true);
        
        // Editor personalizado con 1 clic
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, 
                    boolean isSelected, int row, int column) {
                JTextField field = (JTextField) super.getTableCellEditorComponent(
                        table, value, isSelected, row, column);
                field.setBackground(Color.WHITE);
                field.setForeground(Color.BLACK);
                field.setCaretColor(Color.BLACK);
                field.setBorder(BorderFactory.createLineBorder(VERDE, 2));
                field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                
                String texto = value != null ? value.toString().trim() : "";
                field.setText(texto);
                
                SwingUtilities.invokeLater(() -> field.selectAll());
                
                return field;
            }
        };
        editor.setClickCountToStart(1);
        
        // Aplicar editor a columnas editables
        for (int i = 1; i < tablaItems.getColumnCount(); i++) {
            if (i != 6) { // Excepto TOTAL
                tablaItems.getColumnModel().getColumn(i).setCellEditor(editor);
            }
        }
        
        // NAVEGACIÓN CON TECLADO
        InputMap im = tablaItems.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = tablaItems.getActionMap();
        
        // Enter: bajar una fila
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextRow");
        am.put("selectNextRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerEdicion();
                int row = tablaItems.getSelectedRow();
                int col = tablaItems.getSelectedColumn();
                if (row < tablaItems.getRowCount() - 1) {
                    tablaItems.changeSelection(row + 1, col, false, false);
                    iniciarEdicion(row + 1, col);
                }
            }
        });
        
        // Tab: siguiente celda editable
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "selectNextColumn");
        am.put("selectNextColumn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerEdicion();
                int row = tablaItems.getSelectedRow();
                int col = tablaItems.getSelectedColumn();
                
                int[] nextCell = siguienteCeldaEditable(row, col);
                if (nextCell != null) {
                    tablaItems.changeSelection(nextCell[0], nextCell[1], false, false);
                    iniciarEdicion(nextCell[0], nextCell[1]);
                }
            }
        });
        
        // Shift+Tab: celda anterior
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK), "selectPreviousColumn");
        am.put("selectPreviousColumn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerEdicion();
                int row = tablaItems.getSelectedRow();
                int col = tablaItems.getSelectedColumn();
                
                int[] prevCell = anteriorCeldaEditable(row, col);
                if (prevCell != null) {
                    tablaItems.changeSelection(prevCell[0], prevCell[1], false, false);
                    iniciarEdicion(prevCell[0], prevCell[1]);
                }
            }
        });
        
        // Doble clic o tecla F2 también edita
        tablaItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaItems.rowAtPoint(e.getPoint());
                int col = tablaItems.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0 && col != 0 && col != 6) {
                    detenerEdicion();
                    tablaItems.changeSelection(row, col, false, false);
                    iniciarEdicion(row, col);
                }
            }
        });
        
        // Listener para actualizar items cuando cambia el modelo
        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && !actualizandoCelda) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (row >= 0 && row < listaItems.size() && col >= 1 && col != 6) {
                    SwingUtilities.invokeLater(() -> actualizarItemRapido(row, col));
                }
            }
        });
        
        // Anchos de columnas
        tablaItems.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaItems.getColumnModel().getColumn(1).setPreferredWidth(90);
        tablaItems.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablaItems.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaItems.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaItems.getColumnModel().getColumn(5).setPreferredWidth(120);
        tablaItems.getColumnModel().getColumn(6).setPreferredWidth(120);
        tablaItems.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        // Renderizadores
        configurarRenderizadores();
    }
    
    private void configurarRenderizadores() {
        // Columna # (índice)
        DefaultTableCellRenderer numRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(isSelected ? ACENTO : new Color(50, 50, 50));
                setForeground(isSelected ? Color.WHITE : TEXTO_SECUNDARIO);
                return c;
            }
        };
        tablaItems.getColumnModel().getColumn(0).setCellRenderer(numRenderer);
        
        // Columnas editables con placeholders
        DefaultTableCellRenderer placeholderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String texto = value != null ? value.toString().trim() : "";
                
                if (texto.isEmpty() && !isSelected) {
                    setForeground(TEXTO_SECUNDARIO);
                    setFont(new Font("Segoe UI", Font.ITALIC, 13));
                    
                    switch (column) {
                        case 1: setText("0"); break;
                        case 2: setText("Descripción del producto..."); break;
                        case 3: setText(""); break;
                        case 4: setText(""); break;
                        case 5: setText("$0"); break;
                        case 7: setText("0"); break;
                        default: setText("");
                    }
                } else {
                    setForeground(isSelected ? Color.WHITE : TEXTO_PRINCIPAL);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    
                    // Formatear números en CANTIDAD y MÍNIMO
                    if ((column == 1 || column == 7) && !texto.isEmpty()) {
                        try {
                            int num = Integer.parseInt(texto.replaceAll("[^0-9]", ""));
                            setText(String.format("%,d", num).replace(",", "."));
                        } catch (NumberFormatException e) {
                            setText(texto);
                        }
                    } else {
                        setText(texto);
                    }
                }
                
                setBackground(isSelected ? ACENTO : BG_INPUT);
                return c;
            }
        };
        
        for (int i = 1; i < 8; i++) {
            if (i != 6) {
                tablaItems.getColumnModel().getColumn(i).setCellRenderer(placeholderRenderer);
            }
        }
        
        // Columna TOTAL (no editable, resaltada)
        DefaultTableCellRenderer totalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                
                String texto = value != null ? value.toString().trim() : "";
                
                if (texto.isEmpty()) {
                    setForeground(TEXTO_SECUNDARIO);
                    setText("");
                } else {
                    setForeground(isSelected ? Color.WHITE : VERDE);
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                    setText(texto);
                }
                
                setBackground(isSelected ? ACENTO : new Color(45, 45, 45));
                return c;
            }
        };
        tablaItems.getColumnModel().getColumn(6).setCellRenderer(totalRenderer);
    }
    
    private void detenerEdicion() {
        if (tablaItems.isEditing()) {
            TableCellEditor editor = tablaItems.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }
    
    private void iniciarEdicion(int row, int col) {
        if (row >= 0 && row < tablaItems.getRowCount() && 
            col >= 0 && col < tablaItems.getColumnCount() &&
            col != 0 && col != 6) {
            tablaItems.editCellAt(row, col);
            Component editor = tablaItems.getEditorComponent();
            if (editor != null) {
                editor.requestFocusInWindow();
            }
        }
    }
    
    private int[] siguienteCeldaEditable(int row, int col) {
        col++;
        
        while (row < tablaItems.getRowCount()) {
            while (col < tablaItems.getColumnCount()) {
                if (col != 0 && col != 6) {
                    return new int[]{row, col};
                }
                col++;
            }
            row++;
            col = 1; // Empezar en CANTIDAD
        }
        
        return null;
    }
    
    private int[] anteriorCeldaEditable(int row, int col) {
        col--;
        
        while (row >= 0) {
            while (col >= 0) {
                if (col != 0 && col != 6) {
                    return new int[]{row, col};
                }
                col--;
            }
            row--;
            col = tablaItems.getColumnCount() - 1; // Empezar en MÍNIMO
        }
        
        return null;
    }
    
    private void cargarItemsExistentes() {
        int numeroFila = 1;
        for (ItemCompra item : listaItems) {
            String ref = "";
            String codigo = item.getCodigo();
            if (codigo != null && codigo.contains("-")) {
                String[] partes = codigo.split("-", 2);
                ref = partes[0];
                codigo = partes.length > 1 ? partes[1] : "";
            }
            
            modeloTabla.addRow(new Object[]{
                String.valueOf(numeroFila++),
                String.valueOf(item.getCantidad()),
                item.getDescripcion(),
                ref,
                codigo != null ? codigo : "",
                formatearMoneda(item.getPrecioUnitario()),
                formatearMoneda(item.getSubtotal()),
                "0"
            });
        }
        
        if (listaItems.isEmpty()) {
            for (int i = 0; i < 15; i++) {
                agregarFilaVaciaInicial();
            }
        }
        
        actualizarTotal();
    }
    
    private void agregarFilaVaciaInicial() {
        ItemCompra item = new ItemCompra();
        item.setCantidad(0);
        item.setDescripcion("");
        item.setCodigo("");
        item.setPrecioUnitario(BigDecimal.ZERO);
        item.recalcularSubtotal();
        
        listaItems.add(item);
        
        modeloTabla.addRow(new Object[]{
            String.valueOf(listaItems.size()),
            "", "", "", "", "", "", ""
        });
    }
    
    private void agregarFilaVacia() {
        agregarFilaVaciaInicial();
        
        int nuevaFila = modeloTabla.getRowCount() - 1;
        tablaItems.changeSelection(nuevaFila, 1, false, false);
        iniciarEdicion(nuevaFila, 1);
    }
    
    private void eliminarItem() {
        int filaSeleccionada = tablaItems.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un item para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este item?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            detenerEdicion();
            listaItems.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            
            // Renumerar filas
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                modeloTabla.setValueAt(String.valueOf(i + 1), i, 0);
            }
            
            actualizarTotal();
        }
    }
    
    private void actualizarItemRapido(int row, int col) {
        if (row < 0 || row >= listaItems.size() || actualizandoCelda) return;
        
        try {
            actualizandoCelda = true;
            
            ItemCompra item = listaItems.get(row);
            Object valor = modeloTabla.getValueAt(row, col);
            String valorStr = valor != null ? valor.toString().trim() : "";
            
            switch (col) {
                case 1: // CANTIDAD
                    int cantidad = parsearEnteroSeguro(valorStr);
                    item.setCantidad(cantidad);
                    
                    // Formatear la cantidad para mostrarla correctamente
                    if (cantidad > 0) {
                        modeloTabla.setValueAt(String.format("%,d", cantidad).replace(",", "."), row, 1);
                    } else {
                        modeloTabla.setValueAt("", row, 1);
                    }
                    
                    item.recalcularSubtotal();
                    actualizarCeldaTotal(row);
                    break;
                    
                case 2: // DESCRIPCIÓN
                    item.setDescripcion(valorStr);
                    break;
                    
                case 3: // REF
                case 4: // CÓDIGO
                    actualizarCodigoCompleto(row);
                    break;
                    
                case 5: // COSTO
                    BigDecimal precio = parsearMonedaSeguro(valorStr);
                    item.setPrecioUnitario(precio);
                    
                    // Auto-completar cantidad si falta
                    if (precio.compareTo(BigDecimal.ZERO) > 0 && item.getCantidad() == 0) {
                        item.setCantidad(1);
                        modeloTabla.setValueAt("1", row, 1);
                    }
                    
                    item.recalcularSubtotal();
                    
                    // Formatear costo
                    if (precio.compareTo(BigDecimal.ZERO) > 0) {
                        modeloTabla.setValueAt(formatearMoneda(precio), row, 5);
                    } else {
                        modeloTabla.setValueAt("", row, 5);
                    }
                    
                    actualizarCeldaTotal(row);
                    break;
                    
                case 7: // MÍNIMO
                    int minimo = parsearEnteroSeguro(valorStr);
                    // Formatear el mínimo para mostrarlo correctamente
                    if (minimo > 0) {
                        modeloTabla.setValueAt(String.valueOf(minimo), row, 7);
                    } else {
                        modeloTabla.setValueAt("", row, 7);
                    }
                    break;
            }
            
            actualizarTotal();
            
        } catch (Exception e) {
            System.err.println("Error actualizando celda [" + row + "," + col + "]: " + e.getMessage());
        } finally {
            actualizandoCelda = false;
        }
    }
    
    private void actualizarCodigoCompleto(int row) {
        Object refObj = modeloTabla.getValueAt(row, 3);
        Object codObj = modeloTabla.getValueAt(row, 4);
        String ref = refObj != null ? refObj.toString().trim() : "";
        String codigo = codObj != null ? codObj.toString().trim() : "";
        
        String codigoCompleto = ref.isEmpty() ? codigo : 
                               (codigo.isEmpty() ? ref : ref + "-" + codigo);
        
        listaItems.get(row).setCodigo(codigoCompleto);
    }
    
    private void actualizarCeldaTotal(int row) {
        ItemCompra item = listaItems.get(row);
        if (item.getSubtotal().compareTo(BigDecimal.ZERO) > 0) {
            modeloTabla.setValueAt(formatearMoneda(item.getSubtotal()), row, 6);
        } else {
            modeloTabla.setValueAt("", row, 6);
        }
    }
    
    private int parsearEnteroSeguro(String str) {
        if (str == null || str.isEmpty()) return 0;
        
        str = str.replaceAll("[^0-9]", "");
        if (str.isEmpty()) return 0;
        
        try {
            int valor = Integer.parseInt(str);
            if (valor < 0) return 0;
            if (valor > 999999) return 999999;
            return valor;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private BigDecimal parsearMonedaSeguro(String str) {
        if (str == null || str.isEmpty()) return BigDecimal.ZERO;
        
        // Limpiar formato
        str = str.replace("$", "")
                 .replace(" ", "")
                 .replace(",", "")
                 .replace(".", "");
        
        if (str.isEmpty()) return BigDecimal.ZERO;
        
        try {
            BigDecimal valor = new BigDecimal(str);
            
            // Ajustar escala (dividir por 100 para convertir centavos a pesos)
            if (valor.compareTo(new BigDecimal("1000")) > 0) {
                // Si es mayor a 1000, no dividir (ya está en formato correcto)
                return valor.max(BigDecimal.ZERO).min(new BigDecimal("999999999"));
            }
            
            return valor.max(BigDecimal.ZERO).min(new BigDecimal("999999999"));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCompra item : listaItems) {
            if (item != null) {
                total = total.add(item.getSubtotal());
            }
        }
        
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            lblTotalCalculado.setText("TOTAL: " + formatearMoneda(total));
        } else {
            lblTotalCalculado.setText("TOTAL: $0");
        }
        
        // Actualizar resumen de items
        actualizarResumenItems();
    }
    
    private String formatearMoneda(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }
        
        String formatted = String.format("%,d", valor.longValue());
        return "$" + formatted.replace(",", ".");
    }
    
    private void aceptar() {
        // Detener cualquier edición en curso
        if (tablaItems.isEditing()) {
            tablaItems.getCellEditor().stopCellEditing();
        }
        
        System.out.println("=== VALIDANDO ITEMS EN DIALOGO ===");
        System.out.println("Total de items en lista: " + listaItems.size());
        
        List<ItemCompra> itemsValidos = new ArrayList<>();
        for (int i = 0; i < listaItems.size(); i++) {
            ItemCompra item = listaItems.get(i);
            System.out.println("\nItem " + (i+1) + ":");
            System.out.println("  Descripción: " + (item.getDescripcion() != null ? "'" + item.getDescripcion() + "'" : "NULL"));
            System.out.println("  Cantidad: " + item.getCantidad());
            System.out.println("  Precio: " + item.getPrecioUnitario());
            System.out.println("  Subtotal: " + item.getSubtotal());
            System.out.println("  Código: " + (item.getCodigo() != null ? "'" + item.getCodigo() + "'" : "NULL"));
            
            // Validar que el item tenga TODOS los datos importantes
            if (item != null &&
                item.getDescripcion() != null && 
                !item.getDescripcion().trim().isEmpty() &&
                item.getCantidad() > 0 &&
                item.getPrecioUnitario() != null &&
                item.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0) {
                
                // Recalcular subtotal antes de guardar
                item.recalcularSubtotal();
                itemsValidos.add(item);
                System.out.println("  ✓ VÁLIDO");
            } else {
                System.out.println("  ✗ INVÁLIDO (no cumple requisitos)");
            }
        }
        
        System.out.println("\nItems válidos: " + itemsValidos.size() + " de " + listaItems.size());
        
        if (itemsValidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe agregar al menos un producto válido.\n\n" +
                "Un producto válido debe tener:\n" +
                "• Descripción\n" +
                "• Cantidad mayor a 0\n" +
                "• Costo mayor a $0",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        listaItems.clear();
        listaItems.addAll(itemsValidos);
        
        System.out.println("=== ITEMS ACEPTADOS ===");
        
        aceptado = true;
        if (timerReloj != null) {
            timerReloj.stop();
        }
        dispose();
    }
    
    private void cancelar() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cancelar? Se perderán los cambios no guardados.",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            aceptado = false;
            if (timerReloj != null) {
                timerReloj.stop();
            }
            dispose();
        }
    }
    
    private void actualizarHora() {
        java.time.LocalTime ahora = java.time.LocalTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        lblHoraActual.setText(ahora.format(formatter));
    }
    
    private void actualizarResumenItems() {
        int totalFilas = listaItems.size();
        int itemsConDatos = 0;
        
        for (ItemCompra item : listaItems) {
            if (item != null && 
                item.getDescripcion() != null && 
                !item.getDescripcion().trim().isEmpty() &&
                item.getPrecioUnitario() != null &&
                item.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0) {
                itemsConDatos++;
            }
        }
        
        lblResumenItems.setText(String.format("Productos inscritos: %d | Items con datos: %d", 
            totalFilas, itemsConDatos));
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
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    // Getters
    public boolean isAceptado() {
        return aceptado;
    }
    
    public List<ItemCompra> getItems() {
        return new ArrayList<>(listaItems);
    }
    
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCompra item : listaItems) {
            if (item != null) {
                total = total.add(item.getSubtotal());
            }
        }
        return total;
    }
    
    public String getNumeroFactura() {
        String texto = txtNumeroFactura.getText().trim();
        return texto.equals("Ingrese número de factura") ? "" : texto;
    }
    
    public String getFechaCompra() {
        String texto = txtFechaCompra.getText().trim();
        return texto.equals("dd/mm/aa") ? "" : texto;
    }
}
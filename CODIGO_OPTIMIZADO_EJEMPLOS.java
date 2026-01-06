// ============================================================================
// EJEMPLOS DE CÓDIGO OPTIMIZADO - LISTO PARA IMPLEMENTAR
// ============================================================================

// ----------------------------------------------------------------------------
// 1. CONSTANTES REUTILIZABLES (VentanaUnificada.java)
// ----------------------------------------------------------------------------

public class VentanaUnificada extends JFrame {
    
    // Formatters como constantes (evita crear en cada llamada)
    private static final NumberFormat FORMATO_MONEDA = 
        NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    private static final DateTimeFormatter FORMATO_FECHA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_INPUT = 
        DateTimeFormatter.ofPattern("[dd/MM/yy][dd/MM/yyyy][dd-MM-yy][dd-MM-yyyy]");
    
    // Colores como constantes (ya están bien implementados)
    // ...
}

// ----------------------------------------------------------------------------
// 2. CACHÉ SIMPLE PARA CANTIDADES (CompraService.java)
// ----------------------------------------------------------------------------

public class CompraService {
    
    private final CompraDAO compraDAO;
    private final ItemCompraDAO itemCompraDAO;
    
    // Caché con límite de 100 entradas
    private final Map<Integer, Integer> cacheCantidades = 
        Collections.synchronizedMap(new LinkedHashMap<Integer, Integer>(100, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > 100;
            }
        });
    
    /**
     * Suma las cantidades con caché
     */
    public int sumarCantidadesDeCompra(int idCompra) {
        return cacheCantidades.computeIfAbsent(idCompra, 
            id -> itemCompraDAO.sumarCantidadesPorCompra(id));
    }
    
    /**
     * Invalida caché después de actualizar
     */
    public String actualizarCompraConItems(Compra compra, List<ItemCompra> items) {
        // ... código existente ...
        
        // Invalidar caché después de actualizar
        cacheCantidades.remove(compra.getId());
        
        return resultado;
    }
    
    /**
     * Limpia toda la caché (llamar al cerrar sesión)
     */
    public void limpiarCache() {
        cacheCantidades.clear();
    }
}

// ----------------------------------------------------------------------------
// 3. BATCH QUERY PARA CANTIDADES (ItemCompraDAO.java)
// ----------------------------------------------------------------------------

public interface ItemCompraDAO {
    // ... métodos existentes ...
    
    /**
     * Obtiene cantidades de múltiples compras en una sola consulta
     */
    Map<Integer, Integer> sumarCantidadesPorCompras(List<Integer> idsCompras);
}

// Implementación en ItemCompraDAOMySQL.java
public class ItemCompraDAOMySQL implements ItemCompraDAO {
    
    @Override
    public Map<Integer, Integer> sumarCantidadesPorCompras(List<Integer> idsCompras) {
        Map<Integer, Integer> resultado = new HashMap<>();
        
        if (idsCompras == null || idsCompras.isEmpty()) {
            return resultado;
        }
        
        // Construir placeholders para IN clause
        String placeholders = String.join(",", Collections.nCopies(idsCompras.size(), "?"));
        String sql = "SELECT id_compra, SUM(cantidad) as total " +
                     "FROM items_compra " +
                     "WHERE id_compra IN (" + placeholders + ") " +
                     "GROUP BY id_compra";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Setear parámetros
            for (int i = 0; i < idsCompras.size(); i++) {
                stmt.setInt(i + 1, idsCompras.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.put(rs.getInt("id_compra"), rs.getInt("total"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al sumar cantidades batch: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }
}

// ----------------------------------------------------------------------------
// 4. MÉTODO OPTIMIZADO cargarPaginaActual() (VentanaUnificada.java)
// ----------------------------------------------------------------------------

private void cargarPaginaActual() {
    modeloTablaCompras.setRowCount(0);
    
    if (comprasCompletas == null || comprasCompletas.isEmpty()) {
        return;
    }
    
    int inicio = paginaActual * FACTURAS_POR_PAGINA;
    int fin = Math.min(inicio + FACTURAS_POR_PAGINA, comprasCompletas.size());
    
    // Obtener IDs de la página actual
    List<Integer> ids = new ArrayList<>(fin - inicio);
    for (int i = inicio; i < fin; i++) {
        ids.add(comprasCompletas.get(i).getId());
    }
    
    // Una sola consulta para todas las cantidades
    Map<Integer, Integer> cantidades = compraService.obtenerCantidadesBatch(ids);
    
    // Cargar filas
    for (int i = inicio; i < fin; i++) {
        Compra c = comprasCompletas.get(i);
        
        Object[] fila = {
            c.getNumeroFactura(),
            capitalizarCategoria(c.getCategoria()),
            truncarDescripcion(c.getDescripcion(), 40),
            cantidades.getOrDefault(c.getId(), 0),
            FORMATO_MONEDA.format(c.getTotal()),
            c.getFechaCompra().format(FORMATO_FECHA),
            c.getFormaPago().getEtiqueta(),
            obtenerEstadoDisplay(c),
            c.getFechaPago() != null ? c.getFechaPago().format(FORMATO_FECHA) : ""
        };
        modeloTablaCompras.addRow(fila);
    }
}

// Métodos helper
private String capitalizarCategoria(String categoria) {
    if (categoria == null || categoria.isEmpty()) return "";
    return categoria.substring(0, 1).toUpperCase() + categoria.substring(1);
}

private String truncarDescripcion(String desc, int maxLen) {
    if (desc == null) return "";
    return desc.length() > maxLen ? desc.substring(0, maxLen - 3) + "..." : desc;
}

private String obtenerEstadoDisplay(Compra c) {
    if (c.getFormaPago() == FormaPago.CREDITO) {
        return c.getEstadoCredito() != null ? c.getEstadoCredito().getEtiqueta() : "";
    }
    return c.getFechaPago() != null ? "Pagado" : "Pendiente";
}

// En CompraService.java
public Map<Integer, Integer> obtenerCantidadesBatch(List<Integer> idsCompras) {
    return itemCompraDAO.sumarCantidadesPorCompras(idsCompras);
}

// ----------------------------------------------------------------------------
// 5. DEBOUNCE PARA BÚSQUEDA (VentanaUnificada.java)
// ----------------------------------------------------------------------------

private Timer searchTimer;

private void configurarBusquedaConDebounce() {
    txtBuscarProveedor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void changedUpdate(javax.swing.event.DocumentEvent e) { buscarConDebounce(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { buscarConDebounce(); }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { buscarConDebounce(); }
    });
}

private void buscarConDebounce() {
    if (searchTimer != null && searchTimer.isRunning()) {
        searchTimer.stop();
    }
    
    searchTimer = new Timer(300, e -> filtrarProveedores());
    searchTimer.setRepeats(false);
    searchTimer.start();
}

// ----------------------------------------------------------------------------
// 6. ELIMINAR CÓDIGO DUPLICADO (CompraService.java)
// ----------------------------------------------------------------------------

public String registrarCompra(Compra compra) {
    // Usar el método de validación existente
    String validacion = validarDatosCompra(compra);
    if (validacion != null) {
        return validacion;
    }
    
    boolean exito = compraDAO.insertar(compra);
    
    if (exito) {
        // Invalidar caché si existe
        cacheCantidades.remove(compra.getId());
        return "Compra registrada exitosamente con ID: " + compra.getId();
    } else {
        return "Error: No se pudo registrar la compra";
    }
}

// ----------------------------------------------------------------------------
// 7. ÍNDICES DE BASE DE DATOS (SQL)
// ----------------------------------------------------------------------------

/*
-- Ejecutar estos comandos en MySQL para mejorar rendimiento

-- Índices individuales
CREATE INDEX idx_compras_proveedor ON compras(id_proveedor);
CREATE INDEX idx_compras_fecha ON compras(fecha_compra);
CREATE INDEX idx_compras_forma_pago ON compras(forma_pago);
CREATE INDEX idx_compras_estado ON compras(estado_credito);
CREATE INDEX idx_items_compra ON items_compra(id_compra);

-- Índice compuesto para filtros comunes
CREATE INDEX idx_compras_filtros ON compras(id_proveedor, forma_pago, estado_credito, fecha_compra);

-- Verificar índices creados
SHOW INDEX FROM compras;
SHOW INDEX FROM items_compra;

-- Analizar rendimiento de consultas
EXPLAIN SELECT * FROM compras WHERE id_proveedor = 1;
*/

// ----------------------------------------------------------------------------
// 8. CLASE PARA FILTROS (nuevo archivo FiltrosCompra.java)
// ----------------------------------------------------------------------------

package modelo;

import java.time.LocalDate;

/**
 * Clase para encapsular filtros de búsqueda de compras.
 * Usa patrón Builder para construcción flexible.
 */
public class FiltrosCompra {
    private final int idProveedor;
    private final String textoBusqueda;
    private final FormaPago formaPago;
    private final EstadoCredito estado;
    private final LocalDate fechaDesde;
    private final LocalDate fechaHasta;
    
    private FiltrosCompra(Builder builder) {
        this.idProveedor = builder.idProveedor;
        this.textoBusqueda = builder.textoBusqueda;
        this.formaPago = builder.formaPago;
        this.estado = builder.estado;
        this.fechaDesde = builder.fechaDesde;
        this.fechaHasta = builder.fechaHasta;
    }
    
    // Getters
    public int getIdProveedor() { return idProveedor; }
    public String getTextoBusqueda() { return textoBusqueda; }
    public FormaPago getFormaPago() { return formaPago; }
    public EstadoCredito getEstado() { return estado; }
    public LocalDate getFechaDesde() { return fechaDesde; }
    public LocalDate getFechaHasta() { return fechaHasta; }
    
    public static class Builder {
        private int idProveedor;
        private String textoBusqueda = "";
        private FormaPago formaPago;
        private EstadoCredito estado;
        private LocalDate fechaDesde;
        private LocalDate fechaHasta;
        
        public Builder idProveedor(int idProveedor) {
            this.idProveedor = idProveedor;
            return this;
        }
        
        public Builder textoBusqueda(String texto) {
            this.textoBusqueda = texto != null ? texto.trim() : "";
            return this;
        }
        
        public Builder formaPago(FormaPago formaPago) {
            this.formaPago = formaPago;
            return this;
        }
        
        public Builder estado(EstadoCredito estado) {
            this.estado = estado;
            return this;
        }
        
        public Builder fechaDesde(LocalDate fecha) {
            this.fechaDesde = fecha;
            return this;
        }
        
        public Builder fechaHasta(LocalDate fecha) {
            this.fechaHasta = fecha;
            return this;
        }
        
        public FiltrosCompra build() {
            return new FiltrosCompra(this);
        }
    }
}

// ----------------------------------------------------------------------------
// 9. CARGA ASÍNCRONA CON SWINGWORKER (VentanaUnificada.java)
// ----------------------------------------------------------------------------

private void cargarProveedoresAsync() {
    // Mostrar indicador de carga
    lblProveedorSeleccionado.setText("Cargando proveedores...");
    
    SwingWorker<List<Proveedor>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<Proveedor> doInBackground() throws Exception {
            // Ejecuta en background thread
            return proveedorService.obtenerProveedoresActivos();
        }
        
        @Override
        protected void done() {
            try {
                // Ejecuta en EDT (Event Dispatch Thread)
                proveedores = get();
                actualizarListaProveedores();
                
                if (!proveedores.isEmpty()) {
                    listaProveedores.setSelectedIndex(0);
                } else {
                    lblProveedorSeleccionado.setText("Sin proveedores");
                }
            } catch (Exception e) {
                lblProveedorSeleccionado.setText("Error al cargar proveedores");
                e.printStackTrace();
            }
        }
    };
    
    worker.execute();
}

private void actualizarListaProveedores() {
    modeloListaProveedores.clear();
    for (Proveedor p : proveedores) {
        String tipo = p.getTipo() != null ? " [" + p.getTipo() + "]" : "";
        modeloListaProveedores.addElement(p.getNombre() + tipo);
    }
}

// ----------------------------------------------------------------------------
// 10. CONNECTION POOLING CON HIKARICP (ConexionBD.java)
// ----------------------------------------------------------------------------

/*
Primero agregar dependencia en pom.xml o descargar JAR:

<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
*/

package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {
    
    private static HikariDataSource dataSource;
    
    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/gestion_proveedores");
            config.setUsername("proveedor_app");
            config.setPassword("Amell123");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            
            // Configuración del pool
            config.setMaximumPoolSize(10);          // Máximo 10 conexiones
            config.setMinimumIdle(2);               // Mínimo 2 conexiones idle
            config.setConnectionTimeout(30000);     // 30 segundos timeout
            config.setIdleTimeout(600000);          // 10 minutos idle
            config.setMaxLifetime(1800000);         // 30 minutos max lifetime
            
            // Optimizaciones
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
            dataSource = new HikariDataSource(config);
            
            System.out.println("✓ Connection pool inicializado correctamente");
            
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConexion() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool no inicializado");
        }
        return dataSource.getConnection();
    }
    
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("✓ Connection pool cerrado");
        }
    }
    
    // Método para obtener estadísticas del pool
    public static void imprimirEstadisticas() {
        if (dataSource != null) {
            System.out.println("=== Estadísticas del Pool ===");
            System.out.println("Conexiones activas: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("Conexiones idle: " + dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("Conexiones totales: " + dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("Threads esperando: " + dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
}

// ============================================================================
// RESUMEN DE IMPLEMENTACIÓN
// ============================================================================

/*
PRIORIDAD ALTA (Implementar primero):
1. ✅ Constantes para formatters (5 minutos)
2. ✅ Índices de base de datos (10 minutos)
3. ✅ Eliminar código duplicado en registrarCompra (15 minutos)
4. ✅ Batch query para cantidades (30 minutos)

PRIORIDAD MEDIA:
5. ✅ Caché simple para cantidades (20 minutos)
6. ✅ Debounce en búsqueda (15 minutos)
7. ✅ Métodos helper para cargarPaginaActual (30 minutos)

PRIORIDAD BAJA (Opcional):
8. ✅ Clase FiltrosCompra (45 minutos)
9. ✅ Carga asíncrona (30 minutos)
10. ✅ Connection pooling (1 hora + testing)

TIEMPO TOTAL ESTIMADO:
- Alta prioridad: ~1 hora
- Media prioridad: ~1 hora
- Baja prioridad: ~2.5 horas
- TOTAL: ~4.5 horas para todas las optimizaciones

MEJORA ESPERADA:
- Rendimiento: 4-6x más rápido
- Memoria: 60% menos uso
- Consultas SQL: 90% menos
- Código: 30% menos líneas
*/

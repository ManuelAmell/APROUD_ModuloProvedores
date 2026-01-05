package dao;

import modelo.ItemCompra;
import util.ConexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL de ItemCompraDAO.
 */
public class ItemCompraDAOMySQL implements ItemCompraDAO {
    
    private final ConexionBD conexionBD;
    
    public ItemCompraDAOMySQL() {
        this.conexionBD = ConexionBD.getInstance();
    }
    
    @Override
    public boolean insertar(ItemCompra item) {
        String sql = "INSERT INTO items_compra (id_compra, cantidad, descripcion, codigo, " +
                     "precio_unitario, subtotal, orden) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = conexionBD.getConexion();
            
            // Validar datos antes de insertar
            if (item.getIdCompra() <= 0) {
                System.err.println("ERROR: idCompra inválido: " + item.getIdCompra());
                return false;
            }
            if (item.getCantidad() <= 0) {
                System.err.println("ERROR: cantidad inválida: " + item.getCantidad());
                return false;
            }
            if (item.getDescripcion() == null || item.getDescripcion().trim().isEmpty()) {
                System.err.println("ERROR: descripción vacía o nula");
                return false;
            }
            if (item.getPrecioUnitario() == null || item.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("ERROR: precio unitario inválido: " + item.getPrecioUnitario());
                return false;
            }
            if (item.getSubtotal() == null || item.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("ERROR: subtotal inválido: " + item.getSubtotal());
                return false;
            }
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, item.getIdCompra());
            stmt.setInt(2, item.getCantidad());
            stmt.setString(3, item.getDescripcion().trim());
            
            // Manejar código opcional - asegurar que nunca sea string vacío
            String codigo = item.getCodigo();
            if (codigo != null && !codigo.trim().isEmpty()) {
                stmt.setString(4, codigo.trim());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setBigDecimal(5, item.getPrecioUnitario());
            stmt.setBigDecimal(6, item.getSubtotal());
            stmt.setInt(7, item.getOrden());
            
            System.out.println("Insertando item: cant=" + item.getCantidad() + 
                             ", desc=" + item.getDescripcion() + 
                             ", precio=" + item.getPrecioUnitario() +
                             ", subtotal=" + item.getSubtotal());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
                System.out.println("✓ Item insertado exitosamente con ID: " + item.getId());
                return true;
            } else {
                System.err.println("✗ No se insertó ninguna fila");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ ERROR SQL al insertar item de compra:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Código SQL: " + e.getErrorCode());
            System.err.println("   Estado SQL: " + e.getSQLState());
            System.err.println("   Item: cant=" + item.getCantidad() + 
                             ", desc=" + item.getDescripcion() + 
                             ", codigo=" + item.getCodigo());
            e.printStackTrace();
            return false;
        } finally {
            // Cerrar solo statement y resultset, NO la conexión
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }
    
    @Override
    public ItemCompra obtenerPorId(int id) {
        String sql = "SELECT * FROM items_compra WHERE id = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener item por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<ItemCompra> obtenerTodos() {
        List<ItemCompra> items = new ArrayList<>();
        String sql = "SELECT * FROM items_compra ORDER BY id_compra, orden";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapearResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los items: " + e.getMessage());
            e.printStackTrace();
        }
        
        return items;
    }
    
    @Override
    public boolean actualizar(ItemCompra item) {
        String sql = "UPDATE items_compra SET id_compra = ?, cantidad = ?, descripcion = ?, " +
                     "codigo = ?, precio_unitario = ?, subtotal = ?, orden = ? WHERE id = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, item.getIdCompra());
            stmt.setInt(2, item.getCantidad());
            stmt.setString(3, item.getDescripcion());
            
            if (item.getCodigo() != null && !item.getCodigo().trim().isEmpty()) {
                stmt.setString(4, item.getCodigo());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setBigDecimal(5, item.getPrecioUnitario());
            stmt.setBigDecimal(6, item.getSubtotal());
            stmt.setInt(7, item.getOrden());
            stmt.setInt(8, item.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar item: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM items_compra WHERE id = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public List<ItemCompra> obtenerPorCompra(int idCompra) {
        List<ItemCompra> items = new ArrayList<>();
        String sql = "SELECT * FROM items_compra WHERE id_compra = ? ORDER BY orden";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapearResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener items por compra: " + e.getMessage());
            e.printStackTrace();
        }
        
        return items;
    }
    
    @Override
    public boolean eliminarPorCompra(int idCompra) {
        String sql = "DELETE FROM items_compra WHERE id_compra = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar items por compra: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public int contarItemsPorCompra(int idCompra) {
        String sql = "SELECT COUNT(*) as total FROM items_compra WHERE id_compra = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar items: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    private ItemCompra mapearResultSet(ResultSet rs) throws SQLException {
        ItemCompra item = new ItemCompra();
        item.setId(rs.getInt("id"));
        item.setIdCompra(rs.getInt("id_compra"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setDescripcion(rs.getString("descripcion"));
        item.setCodigo(rs.getString("codigo"));
        item.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        item.setOrden(rs.getInt("orden"));
        return item;
    }
}

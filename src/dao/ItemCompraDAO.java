package dao;

import modelo.ItemCompra;
import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de ItemCompra.
 */
public interface ItemCompraDAO {
    
    // CRUD básico
    boolean insertar(ItemCompra item);
    ItemCompra obtenerPorId(int id);
    List<ItemCompra> obtenerTodos();
    boolean actualizar(ItemCompra item);
    boolean eliminar(int id);
    
    // Consultas específicas
    List<ItemCompra> obtenerPorCompra(int idCompra);
    boolean eliminarPorCompra(int idCompra);
    int contarItemsPorCompra(int idCompra);
    int sumarCantidadesPorCompra(int idCompra);
}

package servicio;

import dao.CompraDAO;
import dao.CompraDAOMySQL;
import dao.ItemCompraDAO;
import dao.ItemCompraDAOMySQL;
import modelo.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que maneja la lógica de negocio para Compras.
 */
public class CompraService {
    
    private final CompraDAO compraDAO;
    private final ItemCompraDAO itemCompraDAO;
    
    public CompraService() {
        this.compraDAO = new CompraDAOMySQL();
        this.itemCompraDAO = new ItemCompraDAOMySQL();
    }
    
    public String registrarCompra(Compra compra) {
        // Validaciones
        if (compra.getIdProveedor() <= 0) {
            return "Error: Debe seleccionar un proveedor";
        }
        
        if (compra.getNumeroFactura() == null || compra.getNumeroFactura().trim().isEmpty()) {
            return "Error: El número de factura es obligatorio";
        }
        
        if (compra.getCategoria() == null) {
            return "Error: Debe seleccionar una categoría";
        }
        
        if (compra.getDescripcion() == null || compra.getDescripcion().trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }
        
        if (compra.getTotal() == null || compra.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return "Error: El total debe ser mayor a cero";
        }
        
        if (compra.getFechaCompra() == null) {
            return "Error: La fecha de compra es obligatoria";
        }
        
        if (compra.getFormaPago() == null) {
            return "Error: Debe seleccionar una forma de pago";
        }
        
        // Validaciones específicas para crédito
        if (compra.getFormaPago() == FormaPago.CREDITO) {
            if (compra.getEstadoCredito() == null) {
                return "Error: Debe especificar el estado del crédito";
            }
            
            if (compra.getEstadoCredito() == EstadoCredito.PAGADO && compra.getFechaPago() == null) {
                return "Error: Si el crédito está pagado, debe especificar la fecha de pago";
            }
            
            if (compra.getEstadoCredito() == EstadoCredito.PENDIENTE && compra.getFechaPago() != null) {
                return "Error: Un crédito pendiente no puede tener fecha de pago";
            }
        } else {
            // Para efectivo y transferencia, no debe tener estado de crédito
            if (compra.getEstadoCredito() != null) {
                return "Error: Solo las compras a crédito pueden tener estado de crédito";
            }
        }
        
        boolean exito = compraDAO.insertar(compra);
        
        if (exito) {
            return "Compra registrada exitosamente con ID: " + compra.getId();
        } else {
            return "Error: No se pudo registrar la compra";
        }
    }
    
    public String actualizarCompra(Compra compra) {
        if (compraDAO.obtenerPorId(compra.getId()) == null) {
            return "Error: No existe una compra con ID " + compra.getId();
        }
        
        // Aplicar las mismas validaciones que en registrar
        String validacion = validarDatosCompra(compra);
        if (validacion != null) {
            return validacion;
        }
        
        boolean exito = compraDAO.actualizar(compra);
        
        if (exito) {
            return "Compra actualizada exitosamente";
        } else {
            return "Error: No se pudo actualizar la compra";
        }
    }
    
    public String eliminarCompra(int id) {
        Compra compra = compraDAO.obtenerPorId(id);
        if (compra == null) {
            return "Error: No existe una compra con ID " + id;
        }
        
        boolean exito = compraDAO.eliminar(id);
        
        if (exito) {
            return "Compra eliminada exitosamente";
        } else {
            return "Error: No se pudo eliminar la compra";
        }
    }
    
    public String marcarCreditoComoPagado(int idCompra, LocalDate fechaPago) {
        Compra compra = compraDAO.obtenerPorId(idCompra);
        
        if (compra == null) {
            return "Error: No existe una compra con ID " + idCompra;
        }
        
        if (compra.getFormaPago() != FormaPago.CREDITO) {
            return "Error: Esta compra no es a crédito";
        }
        
        if (compra.getEstadoCredito() == EstadoCredito.PAGADO) {
            return "Error: Este crédito ya está marcado como pagado";
        }
        
        if (fechaPago == null) {
            return "Error: Debe especificar la fecha de pago";
        }
        
        compra.setEstadoCredito(EstadoCredito.PAGADO);
        compra.setFechaPago(fechaPago);
        
        boolean exito = compraDAO.actualizar(compra);
        
        if (exito) {
            return "Crédito marcado como pagado exitosamente";
        } else {
            return "Error: No se pudo actualizar el estado del crédito";
        }
    }
    
    private String validarDatosCompra(Compra compra) {
        if (compra.getIdProveedor() <= 0) {
            return "Error: Debe seleccionar un proveedor";
        }
        
        if (compra.getNumeroFactura() == null || compra.getNumeroFactura().trim().isEmpty()) {
            return "Error: El número de factura es obligatorio";
        }
        
        if (compra.getCategoria() == null) {
            return "Error: Debe seleccionar una categoría";
        }
        
        if (compra.getDescripcion() == null || compra.getDescripcion().trim().isEmpty()) {
            return "Error: La descripción es obligatoria";
        }
        
        if (compra.getTotal() == null || compra.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return "Error: El total debe ser mayor a cero";
        }
        
        if (compra.getFechaCompra() == null) {
            return "Error: La fecha de compra es obligatoria";
        }
        
        if (compra.getFormaPago() == null) {
            return "Error: Debe seleccionar una forma de pago";
        }
        
        if (compra.getFormaPago() == FormaPago.CREDITO) {
            if (compra.getEstadoCredito() == null) {
                return "Error: Debe especificar el estado del crédito";
            }
            
            if (compra.getEstadoCredito() == EstadoCredito.PAGADO && compra.getFechaPago() == null) {
                return "Error: Si el crédito está pagado, debe especificar la fecha de pago";
            }
            
            if (compra.getEstadoCredito() == EstadoCredito.PENDIENTE && compra.getFechaPago() != null) {
                return "Error: Un crédito pendiente no puede tener fecha de pago";
            }
        } else {
            if (compra.getEstadoCredito() != null) {
                return "Error: Solo las compras a crédito pueden tener estado de crédito";
            }
        }
        
        return null;
    }
    
    // Métodos de consulta
    public Compra obtenerCompra(int id) {
        return compraDAO.obtenerPorId(id);
    }
    
    public List<Compra> obtenerTodasCompras() {
        return compraDAO.obtenerTodas();
    }
    
    public List<Compra> obtenerComprasPorProveedor(int idProveedor) {
        return compraDAO.obtenerPorProveedor(idProveedor);
    }
    
    public List<Compra> obtenerComprasPorCategoria(String categoria) {
        return compraDAO.obtenerPorCategoria(categoria);
    }
    
    public List<Compra> obtenerComprasPorFormaPago(FormaPago formaPago) {
        return compraDAO.obtenerPorFormaPago(formaPago);
    }
    
    public List<Compra> obtenerComprasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return compraDAO.obtenerPorRangoFechas(fechaInicio, fechaFin);
    }
    
    public List<Compra> obtenerCreditosPendientes() {
        return compraDAO.obtenerCreditosPendientes();
    }
    
    public List<Compra> obtenerCreditosPagados() {
        return compraDAO.obtenerCreditosPagados();
    }
    
    // Métodos de cálculo
    public BigDecimal calcularTotalPorProveedor(int idProveedor) {
        return compraDAO.calcularTotalPorProveedor(idProveedor);
    }
    
    public BigDecimal calcularTotalPorCategoria(String categoria) {
        return compraDAO.calcularTotalPorCategoria(categoria);
    }
    
    public BigDecimal calcularTotalPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return compraDAO.calcularTotalPorPeriodo(fechaInicio, fechaFin);
    }
    
    public BigDecimal calcularTotalCreditosPendientes() {
        return compraDAO.calcularTotalCreditosPendientes();
    }
    
    public BigDecimal calcularPendientesPorProveedor(int idProveedor) {
        return compraDAO.calcularPendientesPorProveedor(idProveedor);
    }
    
    // ========== Métodos para Items de Compra ==========
    
    /**
     * Guarda una compra junto con sus items.
     * Calcula el total automáticamente sumando los subtotales de los items.
     */
    public String guardarCompraConItems(Compra compra, List<ItemCompra> items) {
        // Validar que haya al menos un item
        if (items == null || items.isEmpty()) {
            return "Error: Debe agregar al menos un item a la compra";
        }
        
        // Calcular total sumando subtotales de items
        BigDecimal totalCalculado = BigDecimal.ZERO;
        for (ItemCompra item : items) {
            item.recalcularSubtotal();
            totalCalculado = totalCalculado.add(item.getSubtotal());
        }
        compra.setTotal(totalCalculado);
        
        // Guardar la compra primero
        String resultado = registrarCompra(compra);
        if (resultado.startsWith("Error")) {
            return resultado;
        }
        
        // Guardar cada item
        int orden = 0;
        for (ItemCompra item : items) {
            item.setIdCompra(compra.getId());
            item.setOrden(orden++);
            
            if (!itemCompraDAO.insertar(item)) {
                return "Error: No se pudieron guardar todos los items";
            }
        }
        
        return "Compra con " + items.size() + " items registrada exitosamente";
    }
    
    /**
     * Actualiza una compra junto con sus items.
     * Elimina los items anteriores y guarda los nuevos.
     * Usa transacciones para garantizar consistencia.
     */
    public String actualizarCompraConItems(Compra compra, List<ItemCompra> items) {
        // Validar que haya al menos un item
        if (items == null || items.isEmpty()) {
            return "Error: Debe agregar al menos un item a la compra";
        }
        
        System.out.println("\n=== ACTUALIZANDO COMPRA CON ITEMS ===");
        System.out.println("Compra ID: " + compra.getId());
        System.out.println("Número de items recibidos: " + items.size());
        
        // Calcular total sumando subtotales de items ANTES de actualizar
        BigDecimal totalCalculado = BigDecimal.ZERO;
        for (int i = 0; i < items.size(); i++) {
            ItemCompra item = items.get(i);
            
            // Asegurar que el subtotal esté calculado
            item.recalcularSubtotal();
            
            totalCalculado = totalCalculado.add(item.getSubtotal());
            
            System.out.println("Item " + (i+1) + ":");
            System.out.println("  - Cantidad: " + item.getCantidad());
            System.out.println("  - Descripción: " + item.getDescripcion());
            System.out.println("  - Código: " + (item.getCodigo() != null ? item.getCodigo() : "NULL"));
            System.out.println("  - Precio: $" + item.getPrecioUnitario());
            System.out.println("  - Subtotal: $" + item.getSubtotal());
        }
        
        compra.setTotal(totalCalculado);
        System.out.println("Total calculado: $" + totalCalculado);
        
        // Validar que el total sea mayor a cero
        if (totalCalculado.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("ERROR: Total es cero o negativo");
            return "Error: El total de la compra debe ser mayor a cero";
        }
        
        // Actualizar la compra primero
        System.out.println("\n[1/3] Actualizando datos de la compra...");
        String resultado = actualizarCompra(compra);
        if (resultado.startsWith("Error")) {
            System.err.println("ERROR al actualizar compra: " + resultado);
            return resultado;
        }
        System.out.println("✓ Compra actualizada");
        
        // Eliminar items anteriores
        System.out.println("\n[2/3] Eliminando items anteriores...");
        boolean eliminados = itemCompraDAO.eliminarPorCompra(compra.getId());
        if (!eliminados) {
            System.err.println("ADVERTENCIA: No se pudieron eliminar items anteriores");
        } else {
            System.out.println("✓ Items anteriores eliminados");
        }
        
        // Guardar nuevos items
        System.out.println("\n[3/3] Guardando nuevos items...");
        int orden = 0;
        int itemsGuardados = 0;
        
        for (ItemCompra item : items) {
            // CRÍTICO: Resetear el ID para forzar INSERT nuevo
            item.setId(0);
            item.setIdCompra(compra.getId());
            item.setOrden(orden);
            
            System.out.println("\nGuardando item " + (orden + 1) + "...");
            
            boolean guardado = itemCompraDAO.insertar(item);
            
            if (!guardado) {
                System.err.println("✗ FALLÓ al guardar item " + (orden + 1));
                System.err.println("  Descripción: " + item.getDescripcion());
                System.err.println("  Cantidad: " + item.getCantidad());
                System.err.println("  Precio: " + item.getPrecioUnitario());
                return "Error: No se pudieron actualizar todos los items (falló en item " + (orden + 1) + ")";
            }
            
            itemsGuardados++;
            orden++;
            System.out.println("✓ Item " + orden + " guardado con ID: " + item.getId());
        }
        
        System.out.println("\n=== ACTUALIZACIÓN COMPLETADA ===");
        System.out.println("Items guardados: " + itemsGuardados + " de " + items.size());
        System.out.println("Total final: $" + totalCalculado);
        
        return "Compra con " + items.size() + " items actualizada exitosamente";
    }
    
    /**
     * Obtiene todos los items de una compra.
     */
    public List<ItemCompra> obtenerItemsDeCompra(int idCompra) {
        return itemCompraDAO.obtenerPorCompra(idCompra);
    }
    
    /**
     * Cuenta cuántos items tiene una compra.
     */
    public int contarItemsDeCompra(int idCompra) {
        return itemCompraDAO.contarItemsPorCompra(idCompra);
    }
    
    /**
     * Suma las cantidades de todos los items de una compra.
     */
    public int sumarCantidadesDeCompra(int idCompra) {
        return itemCompraDAO.sumarCantidadesPorCompra(idCompra);
    }
}

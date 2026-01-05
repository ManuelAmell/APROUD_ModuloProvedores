package modelo;

import java.math.BigDecimal;

/**
 * Representa un item/producto individual dentro de una compra.
 * Permite tener múltiples productos en una sola factura.
 */
public class ItemCompra {
    
    private int id;
    private int idCompra;
    private int cantidad;
    private String descripcion;
    private String codigo;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private int orden;
    
    // Constructores
    public ItemCompra() {
        this.cantidad = 1;
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
        this.orden = 0;
    }
    
    public ItemCompra(int cantidad, String descripcion, String codigo, BigDecimal precioUnitario) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        this.orden = 0;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdCompra() {
        return idCompra;
    }
    
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    // Métodos auxiliares
    private void calcularSubtotal() {
        if (precioUnitario != null && cantidad > 0) {
            this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
    
    public void recalcularSubtotal() {
        calcularSubtotal();
    }
    
    @Override
    public String toString() {
        return String.format("ItemCompra[id=%d, cant=%d, desc=%s, codigo=%s, precio=%s, subtotal=%s]",
                id, cantidad, descripcion, codigo, precioUnitario, subtotal);
    }
}

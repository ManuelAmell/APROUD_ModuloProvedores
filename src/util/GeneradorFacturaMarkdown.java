package util;

import modelo.Compra;
import modelo.ItemCompra;
import modelo.Proveedor;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generador de facturas en formato Markdown
 */
public class GeneradorFacturaMarkdown {
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Genera un archivo Markdown con el formato de factura
     */
    public static String generarFactura(Compra compra, Proveedor proveedor, List<ItemCompra> items, String rutaArchivo) {
        try {
            StringBuilder sb = new StringBuilder();
            
            // Encabezado
            sb.append("# FACTURA / REGISTRO DE COMPRAS\n\n");
            sb.append("---\n\n");
            
            // Datos generales
            sb.append("**PROVEEDOR:** ").append(proveedor.getNombre()).append("\n\n");
            sb.append("**FECHA:** ").append(compra.getFechaCompra().format(FORMATO_FECHA)).append("\n\n");
            sb.append("**No. FACTURA:** ").append(compra.getNumeroFactura()).append("\n\n");
            sb.append("---\n\n");
            
            // Tabla de productos
            sb.append("## Detalle de Productos\n\n");
            sb.append("| CANTIDAD | DESCRIPCIÓN | REF | CÓDIGO | COSTO | TOTAL | MÍNIMO |\n");
            sb.append("|:--------:|:------------|:---:|:------:|:-----:|:-----:|:------:|\n");
            
            // Items
            if (items != null && !items.isEmpty()) {
                for (ItemCompra item : items) {
                    // Separar REF y CÓDIGO
                    String ref = "-";
                    String codigo = "-";
                    
                    if (item.getCodigo() != null && !item.getCodigo().isEmpty()) {
                        if (item.getCodigo().contains("-")) {
                            String[] partes = item.getCodigo().split("-", 2);
                            ref = partes[0];
                            codigo = partes.length > 1 ? partes[1] : "-";
                        } else {
                            codigo = item.getCodigo();
                        }
                    }
                    
                    sb.append("| ").append(item.getCantidad()).append(" | ");
                    sb.append(item.getDescripcion()).append(" | ");
                    sb.append(ref).append(" | ");
                    sb.append(codigo).append(" | ");
                    sb.append(formatearMoneda(item.getPrecioUnitario())).append(" | ");
                    sb.append(formatearMoneda(item.getSubtotal())).append(" | ");
                    sb.append("0").append(" |\n");
                }
                
                // Rellenar filas vacías hasta 25
                int filasVacias = 25 - items.size();
                for (int i = 0; i < filasVacias; i++) {
                    sb.append("|          |             |     |        |       |       |        |\n");
                }
            } else {
                // 25 filas vacías
                for (int i = 0; i < 25; i++) {
                    sb.append("|          |             |     |        |       |       |        |\n");
                }
            }
            
            sb.append("\n---\n\n");
            
            // Total
            sb.append("**TOTAL:** ").append(formatearMoneda(compra.getTotal())).append("\n");
            
            // Guardar archivo
            try (FileWriter writer = new FileWriter(rutaArchivo)) {
                writer.write(sb.toString());
            }
            
            return "Factura generada exitosamente: " + rutaArchivo;
            
        } catch (IOException e) {
            return "Error al generar factura: " + e.getMessage();
        }
    }
    
    /**
     * Genera una plantilla vacía
     */
    public static String generarPlantillaVacia(String rutaArchivo) {
        try {
            StringBuilder sb = new StringBuilder();
            
            sb.append("# FACTURA / REGISTRO DE COMPRAS\n\n");
            sb.append("---\n\n");
            sb.append("**PROVEEDOR:** _____________________________________________________________________________\n\n");
            sb.append("**FECHA:** _____ / _____ / _________\n\n");
            sb.append("**No. FACTURA:** _____________________________________________________________________________\n\n");
            sb.append("---\n\n");
            sb.append("## Detalle de Productos\n\n");
            sb.append("| CANTIDAD | DESCRIPCIÓN | REF | CÓDIGO | COSTO | TOTAL | MÍNIMO |\n");
            sb.append("|:--------:|:------------|:---:|:------:|:-----:|:-----:|:------:|\n");
            
            for (int i = 0; i < 25; i++) {
                sb.append("|          |             |     |        |       |       |        |\n");
            }
            
            sb.append("\n---\n\n");
            sb.append("**TOTAL:** _____________________________________________________________________________\n");
            
            try (FileWriter writer = new FileWriter(rutaArchivo)) {
                writer.write(sb.toString());
            }
            
            return "Plantilla generada exitosamente: " + rutaArchivo;
            
        } catch (IOException e) {
            return "Error al generar plantilla: " + e.getMessage();
        }
    }
    
    private static String formatearMoneda(BigDecimal valor) {
        if (valor == null) return "$0";
        return String.format("$%,.0f", valor);
    }
}

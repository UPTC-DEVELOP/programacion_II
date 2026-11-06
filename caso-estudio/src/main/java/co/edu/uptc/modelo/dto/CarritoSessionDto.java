package co.edu.uptc.modelo.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Representa un carrito completo dentro del archivo JSON. Agrupa el
 * identificador del cliente, su tipo, las líneas del carrito y los totales
 * calculados al momento de guardar, lo que permite conservar varias sesiones
 * activas en un solo archivo.
 */
public class CarritoSessionDto {

    private int idCarrito;
    private int idCliente;
    private TipoCliente tipoCliente;
    private List<ItemCarritoPersistenciaDto> items = new ArrayList<>();
    private double subtotalGeneral;
    private double totalImpuestos;
    private double descuentoAplicado;
    private double totalFinal;

    public CarritoSessionDto() { }

    public int getIdCarrito() { return idCarrito; }
    public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
    public List<ItemCarritoPersistenciaDto> getItems() { return items; }
    public void setItems(List<ItemCarritoPersistenciaDto> items) {
        this.items = (items == null) ? new ArrayList<>() : items;
    }
    public double getSubtotalGeneral() { return subtotalGeneral; }
    public void setSubtotalGeneral(double subtotalGeneral) { this.subtotalGeneral = subtotalGeneral; }
    public double getTotalImpuestos() { return totalImpuestos; }
    public void setTotalImpuestos(double totalImpuestos) { this.totalImpuestos = totalImpuestos; }
    public double getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(double descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }
    public double getTotalFinal() { return totalFinal; }
    public void setTotalFinal(double totalFinal) { this.totalFinal = totalFinal; }
}

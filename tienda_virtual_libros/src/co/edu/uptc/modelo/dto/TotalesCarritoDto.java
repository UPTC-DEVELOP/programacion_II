package co.edu.uptc.modelo.dto;

/**
 * DTO TotalesCarritoDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Resultado del cálculo de TOTALES del carrito (RF13) para la capa de
 * PRESENTACIÓN: subtotal general, total de IVA, descuento aplicado y total final.
 *
 * ¿POR QUÉ UN SOLO DTO CON LOS 4 VALORES?
 *  - La GUI necesita los 4 números en la misma pantalla (resumen del carrito)
 *    en UNA sola llamada al servicio: evita 4 viajes ida y vuelta y garantiza
 *    que todos los valores provengan del MISMO snapshot del carrito (consistencia).
 */
public class TotalesCarritoDto {

    private double subtotalGeneral;
    private double totalImpuestos;
    private double descuento;
    private double totalFinal;

    public TotalesCarritoDto() { }

    public TotalesCarritoDto(double subtotalGeneral, double totalImpuestos,
                             double descuento, double totalFinal) {
        this.subtotalGeneral = subtotalGeneral;
        this.totalImpuestos = totalImpuestos;
        this.descuento = descuento;
        this.totalFinal = totalFinal;
    }

    // --- GETTERS Y SETTERS ---
    public double getSubtotalGeneral() { return subtotalGeneral; }
    public void setSubtotalGeneral(double subtotalGeneral) { this.subtotalGeneral = subtotalGeneral; }
    public double getTotalImpuestos() { return totalImpuestos; }
    public void setTotalImpuestos(double totalImpuestos) { this.totalImpuestos = totalImpuestos; }
    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public double getTotalFinal() { return totalFinal; }
    public void setTotalFinal(double totalFinal) { this.totalFinal = totalFinal; }
}

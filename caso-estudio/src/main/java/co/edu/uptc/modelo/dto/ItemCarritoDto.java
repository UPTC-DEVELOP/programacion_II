package co.edu.uptc.modelo.dto;

/**
 * DTO ItemCarritoDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Representa UNA LÍNEA del carrito para la capa de PRESENTACIÓN (GUI).
 *
 * 
 * 
 *  - Arquitectura multicapa: la GUI debe depender del CONTRATO de datos que
 *    expone el servicio (este DTO), nunca de las clases internas del dominio
 *    (ItemCarrito). Si mañana ItemCarrito cambia de estructura, solo cambia
 *    el mapper interno del servicio; la GUI no se entera (DIP + encapsulamiento).
 *  - Se agrega el TITULO del libro (a ItemCarrito solo guarda el ISBN) porque
 *    la tabla del carrito necesita mostrar "Effective Java", no solo el ISBN.
 *    El servicio lo completa consultando el catálogo.
 */
public class ItemCarritoDto {

    private String isbn;
    private String titulo;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double iva;

    // Constructor vacío: permite mapear desde la GUI y desde el servicio con setters.
    public ItemCarritoDto() { }

    // Constructor completo: cómodo para construir el DTO desde el servicio de una vez.
    public ItemCarritoDto(String isbn, String titulo, int cantidad,
                          double precioUnitario, double subtotal, double iva) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.iva = iva;
    }

    // --- GETTERS Y SETTERS ---
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }
}

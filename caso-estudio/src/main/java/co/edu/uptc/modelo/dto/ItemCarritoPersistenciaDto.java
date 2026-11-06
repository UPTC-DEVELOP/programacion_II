package co.edu.uptc.modelo.dto;

/**
 * Representa una línea del carrito dentro del archivo JSON. Conserva el ISBN,
 * el título congelado, la cantidad, el precio unitario, el porcentaje de IVA
 * aplicado y el subtotal calculado al momento de persistir.
 */
public class ItemCarritoPersistenciaDto {

    private String isbn;
    private String titulo;
    private int cantidad;
    private double precioUnitario;
    private double ivaAplicado;
    private double subtotal;

    public ItemCarritoPersistenciaDto() { }

    public ItemCarritoPersistenciaDto(String isbn, String titulo, int cantidad,
                                      double precioUnitario, double ivaAplicado,
                                      double subtotal) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.ivaAplicado = ivaAplicado;
        this.subtotal = subtotal;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getIvaAplicado() { return ivaAplicado; }
    public void setIvaAplicado(double ivaAplicado) { this.ivaAplicado = ivaAplicado; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

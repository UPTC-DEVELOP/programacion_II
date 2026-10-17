package co.edu.uptc.modelo.dto;

/**
 * DTO LibroVendidoDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Resultado del reporte "Libros más vendidos en carritos" 
 *
 * ¿POR QUÉ EXISTE?
 *  - El administrador necesita ver, para cada libro, CUÁNTAS unidades hay
 *    acumuladas en TODOS los carritos activos de los clientes (no solo ventas
 *    confirmadas: eso lo da el dashboard de LocalVenta). Este reporte permite
 *    detectar qué libros tienen más demanda POTENCIAL en los carritos.
 *  - Se agrupan los datos de ICarritoRepository (cantidades por ISBN) con los
 *    de ILibroRepositorio (título del libro) en un solo DTO de presentación.
 *
 * ES INMUTABLE (solo getters): los DTOs de lectura no deben ser modificables
 * por el consumidor (encapsulamiento).
 */
public class LibroVendidoDto {

    private final String isbn;
    private final String titulo;
    private final int cantidadTotal;   // suma de cantidades de ese ISBN en TODOS los carritos
    private final double ingresoPotencial; // cantidadTotal * precioUnitario del libro

    public LibroVendidoDto(String isbn, String titulo, int cantidadTotal, double ingresoPotencial) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.cantidadTotal = cantidadTotal;
        this.ingresoPotencial = ingresoPotencial;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getCantidadTotal() { return cantidadTotal; }
    public double getIngresoPotencial() { return ingresoPotencial; }
}

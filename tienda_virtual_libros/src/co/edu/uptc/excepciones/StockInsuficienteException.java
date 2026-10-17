package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN StockInsuficienteException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando la cantidad solicitada SUPERA el stock disponible del libro
 * (validación del RF10: "cantidad <= stock").
 *
 * Se guardan los valores involucrados (pedido y disponible) para que la GUI
 * pueda ofrecer un mensaje rico ("Solo quedan 3 unidades") sin tener que
 * volver a consultar el catálogo.
 *
 * ¿POR QUÉ EXTIENDE ReglaNegocioException? Ver comentario en
 * LibroNoEncontradoException: misma jerarquía y misma justificación (checked).
 */
public class StockInsuficienteException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    /** Cantidad que el cliente intentó llevar. */
    private final int cantidadSolicitada;

    /** Stock que realmente tenía el libro. */
    private final int stockDisponible;

    public StockInsuficienteException(String mensaje, int cantidadSolicitada, int stockDisponible) {
        super(mensaje);
        this.cantidadSolicitada = cantidadSolicitada;
        this.stockDisponible = stockDisponible;
    }

    public int getCantidadSolicitada() { return cantidadSolicitada; }

    public int getStockDisponible() { return stockDisponible; }
}

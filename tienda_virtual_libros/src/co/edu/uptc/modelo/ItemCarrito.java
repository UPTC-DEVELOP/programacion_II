package co.edu.uptc.modelo;

/**
 * CLASE ItemCarrito  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Representa UNA LÍNEA del carrito de compras: un libro (identificado por su
 * ISBN) junto con la cantidad que el cliente desea llevarse.
 *
 * ¿POR QUÉ CLASE SEPARADA de CarritoCompras?
 *  - Principio de Responsabilidad Única (SRP): el ítem encapsula SUS datos
 *    (cantidad, precio, IVA) y SUS cálculos básicos; el carrito solo orquesta
 *    la colección de ítems y los totales generales.
 *  - El RF10 exige acumular cantidad si el mismo ISBN ya está en el carrito:
 *    para eso basta SUMAR en un ítem existente, sin duplicar líneas.
 *
 * ¿POR QUÉ SE GUARDA EL VALOR DEL IVA Y NO SOLO LA TASA?
 *  - El RF10 pide calcular el IVA (5% o 19% según categoría del libro) y el
 *    RF13 pedirá mostrar el total de impuestos: almacenar el VALOR monetario
 *    del IVA permite sumarlo directamente sin recalcular cada ítem.
 *  - Se guarda TAMBIÉN la tasa (tasaIva) para poder RECALCULAR el IVA cuando
 *    cambie la cantidad (RF11) sin que la capa de negocio tenga que volver a
 *    buscar la categoría del libro: el ítem es autosuficiente.
 *
 * ¿POR QUÉ double Y NO BigDecimal?
 *  - Todo el proyecto (Libro.precioVenta, Cliente.calcularDescuento, etc.)
 *    trabaja con double; usar BigDecimal aquí rompería la coherencia del
 *    modelo. La "precisión de 2 decimales" que exige el RF13 se garantiza con
 *    el helper privado redondear().
 */
public class ItemCarrito {

    /** Cantidad de decimales monetarios que exige el RF13. */
    private static final int DECIMALES = 2;

    /** ISBN del libro asociado a este ítem (clave lógica dentro del carrito). */
    private String isbn;

    /** Cantidad de unidades solicitadas. Siempre > 0 (valida el RF10 en negocio). */
    private int cantidad;

    /** Precio de UNA unidad al momento de agregarla al carrito (puede cambiar en catálogo). */
    private double precioUnitario;

    /**
     * Tasa de IVA aplicable al libro: 0.05 (5%) o 0.19 (19%) según la categoría
     * del libro (RF10). Se define en el ítem porque cada línea puede tener un
     * libro de categoría distinta.
     */
    private double tasaIva;

    /** Subtotal de la línea: cantidad * precioUnitario (redondeado a 2 decimales). */
    private double subtotal;

    /** Valor MONETARIO del IVA de la línea: subtotal * tasaIva (redondeado a 2 decimales). */
    private double iva;

    /**
     * Constructor único: obliga a que todo ítem nac ya con su tasa de IVA y con
     * sus cálculos listos (subtotal e IVA inicializados vía recalcular()).
     * Así nunca existe un ítem "a medio calcular" en memoria.
     *
     * @param isbn          ISBN del libro (no se valida aquí: lo hace el servicio, RF10)
     * @param cantidad      unidades a comprar (debe ser > 0, lo valida el servicio)
     * @param precioUnitario precio de una unidad
     * @param tasaIva       0.05 o 0.19 según la categoría del libro
     */
    public ItemCarrito(String isbn, int cantidad, double precioUnitario, double tasaIva) {
        this.isbn = isbn;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tasaIva = tasaIva;
        // Se calculan subtotal e IVA de una sola vez para mantener el objeto consistente.
        recalcular();
    }

    /**
     * Recalcula subtotal e IVA a partir de la cantidad y el precio unitario actuales.
     *
     * ¿POR QUÉ PÚBLICO? Porque el RF11 (gestionar cantidad) cambiará la cantidad
     * desde la capa de negocio y pedirá "recalcular subtotal"; centralizar el
     * cálculo aquí evita duplicar la fórmula fuera de la clase (DRY).
     */
    public void recalcular() {
        this.subtotal = redondear(this.cantidad * this.precioUnitario);
        this.iva = redondear(this.subtotal * this.tasaIva);
    }

    /**
     * Redondea un valor monetario a 2 decimales (precisión que exige el RF13).
     * Math.round(valor * 100) / 100.0 es el redondeo "escolar" más simple y
     * suficiente para un proyecto con double.
     */
    private double redondear(double valor) {
        double factor = Math.pow(10, DECIMALES);
        return Math.round(valor * factor) / factor;
    }

    /** Devuelve el total de la línea YA INCLUYENDO el IVA (subtotal + iva). */
    public double getTotalConIva() {
        return redondear(subtotal + iva);
    }

    // ----------------------------- Getters y Setters -----------------------------

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getCantidad() { return cantidad; }

    /**
     * Cambia la cantidad y recalcula automáticamente subtotal e IVA.
     * El caso "cantidad llegó a 0 => sugerir eliminación" (RF11) lo decide la
     * capa de SERVICIO, no el modelo: aquí solo se refleja el cambio.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        recalcular();
    }

    public double getPrecioUnitario() { return precioUnitario; }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        recalcular(); // el subtotal depende del precio, hay que mantenerlo consistente
    }

    public double getTasaIva() { return tasaIva; }

    public void setTasaIva(double tasaIva) {
        this.tasaIva = tasaIva;
        recalcular(); // el IVA depende de la tasa
    }

    public double getSubtotal() { return subtotal; }

    public double getIva() { return iva; }
}

package co.edu.uptc.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASE CarritoCompras  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * AGGREGATE ROOT del carrito: agrupa los ItemCarrito de UN cliente y expone
 * los cálculos de totales que pide el RF13.
 *
 * ¿POR QUÉ SE IDENTIFICA POR idCliente Y NO POR UN ID PROPIO?
 * 
 *  - La regla de negocio es simple: cada cliente tiene UN carrito activo.
 *    Usar idCliente como identificador lógico evita la pregunta "¿qué hago si
 *    un cliente tiene dos carritos?" y hace directo el lookup en el repositorio
 *    (HashMap<Integer, CarritoCompras>).
 *  - idCarrito se conserva como identificador técnico por si más adelante se
 *    persiste en JSON/base de datos y se necesita una clave única independiente.
 *
 * ¿POR QUÉ RECIBE UN Cliente PARA CALCULAR EL DESCUENTO (RF13)?
 * 
 *  - El 10% de descuento es una regla del TIPO de cliente, no del carrito.
 *    La jerarquía Cliente ya expone el método abstracto calcularDescuento()
 *    (implementado en ClientePremium con 10% y en ClienteRegular con 0.0).
 *    Pasar el objeto Cliente y delegar en ese método es POLIMORFISMO: el
 *    carrito NO pregunta "¿es premium?" (violación del OCP), solo llama a la
 *    operación correcta según el tipo real del objeto.
 *
 * ¿POR QUÉ double CON redondear()?
 * 
 *  - Misma decisión que en ItemCarrito: coherencia con el resto del proyecto
 *    y precisión de 2 decimales exigiada por el RF13.
 */
public class CarritoCompras {

    /** Cantidad de decimales monetarios (RF13). */
    private static final int DECIMALES = 2;

    /** Identificador técnico del carrito (para persistencia futura en JSON). */
    private int idCarrito;

    /** Identificador del cliente dueño del carrito: clave lógica única. */
    private int idCliente;

    /**
     * Líneas del carrito. Se usa ArrayList porque el tamaño es pequeño y se
     * necesita mantener el ORDEN de inserción para mostrarlo así en la GUI.
     * Encapsulado: solo se expone una copia inmutable vía obtenerItems().
     */
    private final List<ItemCarrito> items;

    public CarritoCompras(int idCarrito, int idCliente) {
        this.idCarrito = idCarrito;
        this.idCliente = idCliente;
        this.items = new ArrayList<>();
    }

    // ----------------------------- Gestión de ítems -----------------------------

    /**
     * Agrega una línea al carrito SIN acumular (la acumulación por ISBN es
     * responsabilidad del SERVICIO de carrito en la Fase 2, que primero debe
     * validar sesión, stock y ISBN en catálogo: RF10).
     * El modelo solo garantiza la integridad de su propia colección.
     */
    public void agregarItem(ItemCarrito item) {
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * Busca un ítem por ISBN. Devuelve null si no existe.
     * Lo usará el servicio (Fase 2) para decidir si acumula cantidad (RF10)
     * o si debe crear una línea nueva.
     */
    public ItemCarrito buscarItemPorIsbn(String isbn) {
        if (isbn == null) return null;
        for (ItemCarrito item : items) {
            if (item.getIsbn().equals(isbn)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Elimina la línea con el ISBN indicado (RF12). Devuelve true si se
     * eliminó algo (útil para que el servicio sepa si hubo efecto real).
     */
    public boolean eliminarItemPorIsbn(String isbn) {
        if (isbn == null) return false;
        return items.removeIf(item -> item.getIsbn().equals(isbn));
    }

    /**
     * Devuelve una COPIA de la lista de ítems.
     * ¿POR QUÉ UNA COPIA? Encapsulamiento: nadie fuera de la clase debe poder
     * hacer items.add(...) o items.clear() directamente sobre el estado interno
     * del carrito (evita efectos secundarios difíciles de depurar).
     */
    public List<ItemCarrito> obtenerItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    /** Vacia el carrito (se usará al confirmar una venta en fases posteriores). */
    public void limpiar() {
        items.clear();
    }

    // ------------------------- Cálculos de totales (RF13) -------------------------

    /**
     * Subtotal general = suma de los subtotales de cada línea.
     * NOTA: los ítems ya guardan su subtotal redondeado; aquí solo se suman y
     * se redondea el resultado por seguridad (evita arrastre de decimales).
     */
    public double calcularSubtotalGeneral() {
        double subtotal = 0.0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return redondear(subtotal);
    }

    /**
     * Total de impuestos = suma del IVA de cada línea.
     * Cada ítem ya calculó su IVA con la tasa de su categoría (5% o 19%: RF10),
     * por eso aquí solo se acumula.
     */
    public double calcularTotalImpuestos() {
        double totalIva = 0.0;
        for (ItemCarrito item : items) {
            totalIva += item.getIva();
        }
        return redondear(totalIva);
    }

    /**
     * Descuento según el tipo de cliente (RF13: 10% si es PREMIUM).
     * DELEGA en Cliente.calcularDescuento(): polimorfismo puro, el carrito no
     * conoce los porcentajes ni pregunta el tipo de cliente (OCP/SRP).
     */
    public double calcularDescuento(Cliente cliente) {
        if (cliente == null) {
            return 0.0;
        }
        return redondear(cliente.calcularDescuento(calcularSubtotalGeneral()));
    }

    /**
     * Total final = subtotal general + total IVA - descuento.
     * Fórmula del RF13. Se redondea al final para cerrar la operación con
     * precisión de 2 decimales.
     */
    public double calcularTotalFinal(Cliente cliente) {
        double total = calcularSubtotalGeneral() + calcularTotalImpuestos() - calcularDescuento(cliente);
        return redondear(total);
    }

    /** Redondeo a 2 decimales, igual que en ItemCarrito (consistencia del modelo). */
    private double redondear(double valor) {
        double factor = Math.pow(10, DECIMALES);
        return Math.round(valor * factor) / factor;
    }

    // ----------------------------- Getters y Setters -----------------------------

    public int getIdCarrito() { return idCarrito; }

    public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }

    public int getIdCliente() { return idCliente; }

    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
}

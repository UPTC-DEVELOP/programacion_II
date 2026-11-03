package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;

/**
 * INTERFAZ ICarritoService  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Contrato de la capa de NEGOCIO del carrito de compras. La capa de
 * presentación (GUI) depende SOLO de esta abstracción (DIP), nunca de la clase
 * concreta CarritoServiceImpl.
 *
 * ¿POR QUÉ SE LLAMA ICarritoService (y no IGestionCarrito)?
 *  
 *  las interfaces de negocio ya existentes usan el patrón IGestionXxx
 *    (IGestionLibro, IGestionCliente); al ser un servicio nuevo y autónomo se
 *    usa el sufijo Service para distinguirlo del CRUD de gestión.
 *
 * Todas las operaciones que tocan el carrito lanzan ReglaNegocioException (o
 * alguna de sus hijas: LibroNoEncontradoException, StockInsuficienteException,
 * CantidadInvalidaException, ClienteNoEncontradoException) cuando falla una
 * validación: la GUI debe estar preparada para capturarlas y mostrar el
 * mensaje, que ya viene en texto listo para el usuario.
 */
public interface ICarritoService {

    /**
     * RF10 - Agrega un libro al carrito del cliente (o acumula cantidad si el
     * ISBN ya estaba).
     *
     * Validaciones: sesión/cliente existe, ISBN en catálogo, cantidad > 0 y
     * cantidad <= stock (considerando lo ya acumulado en el carrito).
     *
     * @return la línea del carrito resultante (con el IVA ya calculado)
     */
    ItemCarritoDto agregarLibro(int idCliente, String isbn, int cantidad) throws ReglaNegocioException;

    /**
     * RF11 - Aumenta la cantidad de un ítem YA presente en el carrito.
     * Valida que el nuevo total no supere el stock del libro.
     *
     * @return la línea del carrito actualizada
     */
    ItemCarritoDto aumentarCantidad(int idCliente, String isbn, int cantidadAdicional) throws ReglaNegocioException;

    /**
     * RF11 - Disminuye la cantidad de un ítem YA presente en el carrito.
     * Si la cantidad llega a 0, la línea se conserva con cantidad 0 y el DTO
     * resultante permite a la GUI SUGERIR la eliminación (RF11: "si llega a 0,
     * sugerir eliminación"). La eliminación real la ejecuta eliminarItem (RF12).
     *
     * @return la línea del carrito actualizada (cantidad puede ser 0)
     */
    ItemCarritoDto disminuirCantidad(int idCliente, String isbn, int cantidadAReducir) throws ReglaNegocioException;

    /**
     * RF12 - Elimina por completo una línea del carrito.
     * La CONFIRMACIÓN previa al borrado es responsabilidad de la GUI (capa de
     * presentación); el servicio solo ejecuta la eliminación pedida.
     */
    void eliminarItem(int idCliente, String isbn) throws ReglaNegocioException;

    /**
     * Devuelve las líneas del carrito (mapeadas a DTOs, con título del libro)
     * para que la GUI las muestre. Lista vacía si el cliente no tiene carrito.
     */
    List<ItemCarritoDto> obtenerItems(int idCliente);

    /**
     * RF13 - Calcula los totales del carrito: subtotal general, total de IVA,
     * descuento (10% si PREMIUM) y total final.
     *
     * @return los 4 valores del resumen; todo en 0.0 si el carrito está vacío
     */
    TotalesCarritoDto calcularTotales(int idCliente) throws ReglaNegocioException;
}

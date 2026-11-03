package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.modelo.CarritoCompras;

/**
 * IMPLEMENTACIÓN EN MEMORIA del repositorio de carritos.
 * Implementa ICarritoRepository: la capa de negocio solo ve la interfaz.
 *
 * ¿POR QUÉ HashMap<Integer, CarritoCompras> CON idCliente COMO KEY?
 *  - La regla de negocio es "un carrito activo por cliente": el HashMap
 *    garantiza esa unicidad POR DISEÑO (una key no puede repetirse) y ofrece
 *    búsqueda O(1) al consultar el carrito de un cliente.
 *  - Al volver a guardar un carrito del mismo cliente se sobrescribe el
 *    anterior (semántica natural de put en un Map).
 *
 * ¿POR QUÉ SIN SEMILLAS DE DATOS?
 *  - A diferencia del catálogo de libros, un carrito vacío es un estado
 *    VÁLIDO y esperado: no hay nada que precargar.
 *
 * NOTA: los datos viven solo en RAM; al reiniciar la aplicación se pierden.
 * Ese es justamente el contrato de "en memoria" hasta que llegue el módulo JSON.
 */
public class CarritoRepositoryEnMemoria implements ICarritoRepository {

    /** Key: idCliente | Value: carrito activo de ese cliente. */
    private final HashMap<Integer, CarritoCompras> carritosPorCliente = new HashMap<>();

    // -------------------------- ICarritoRepository --------------------------

    @Override
    public void guardarCarrito(CarritoCompras carrito) {
        // Se valida null por seguridad: un repositorio nunca debe fallar en
        // runtime por un dato faltante; es más robusto ignorar la llamada.
        if (carrito == null) {
            return;
        }
        // put() sobrescribe: si el cliente ya tenía carrito, se actualiza.
        carritosPorCliente.put(carrito.getIdCliente(), carrito);
    }

    @Override
    public CarritoCompras obtenerCarritoPorCliente(int idCliente) {
        // get() devuelve null si no existe: el servicio decidirá qué hacer
        // (por ejemplo, crear uno nuevo al primer agregar de libro).
        return carritosPorCliente.get(idCliente);
    }

    @Override
    public void eliminarCarrito(int idCliente) {
        // remove() es inocuo si la key no existe (no lanza excepción).
        carritosPorCliente.remove(idCliente);
    }

    /**
     * Retorna TODOS los carritos activos.
     * Se retorna una COPIA (new ArrayList<>) por ENCAPSULAMIENTO: el consumidor
     * (servicio de reportes / gestión de carritos) no debe poder modificar la
     * colección interna del repositorio directamente (evita efectos secundarios).
     */
    @Override
    public List<CarritoCompras> listarTodos() {
        return new ArrayList<>(carritosPorCliente.values());
    }
}

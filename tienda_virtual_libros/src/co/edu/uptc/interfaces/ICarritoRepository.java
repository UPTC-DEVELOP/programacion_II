package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.CarritoCompras;

/**
 * INTERFAZ ICarritoRepository  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA del carrito de compras. Cumple el principio de
 * Inversión de Dependencias (DIP): la capa de negocio (CarritoServiceImpl y
 * GestionReporte) depende de ESTA ABSTRACCIÓN, nunca de la implementación
 * concreta.
 *
 * Hoy la implementa CarritoRepositoryEnMemoria (HashMap en RAM); cuando se
 * agregue el módulo JSON basta crear CarritoRepositoryJson y cambiar UNA línea
 * en el Main/inyección de dependencias, sin tocar la lógica de negocio (OCP).
 *
 * MÉTODOS (nomenclatura en español, igual que ILibroRepositorio):
 *  - guardarCarrito: crea o reemplaza el carrito del cliente (upsert).
 *  - obtenerCarritoPorCliente: retorna el carrito o null si no existe.
 *  - eliminarCarrito: borra el carrito del cliente (se usará al confirmar venta).
 *  - listarTodos: retorna TODOS los carritos activos (FASE 4: lo necesitan los
 *    reportes del administrador "Libros más vendidos en carritos" e "Ingresos
 *    estimados por cliente", y el panel de gestión de carritos del admin).
 */
public interface ICarritoRepository {

    /** Guarda (o reemplaza) el carrito indexándolo por idCliente. */
    void guardarCarrito(CarritoCompras carrito);

    /** Consulta el carrito de un cliente; devuelve null si no tiene carrito. */
    CarritoCompras obtenerCarritoPorCliente(int idCliente);

    /** Elimina el carrito de un cliente (por ejemplo, al finalizar la compra). */
    void eliminarCarrito(int idCliente);

    /**
     * FASE 4: Retorna TODOS los carritos actualmente persistidos.
     * Lo consumen los reportes del administrador (que necesitan recorrer todos
     * los carritos para agregar ventas por ISBN e ingresos por cliente) y el
     * panel de gestión de carritos (que lista quién tiene qué en su carrito).
     */
    List<CarritoCompras> listarTodos();
}

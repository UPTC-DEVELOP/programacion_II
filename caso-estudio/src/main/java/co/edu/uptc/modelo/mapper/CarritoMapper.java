package co.edu.uptc.modelo.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.dto.CarritoSessionDto;
import co.edu.uptc.modelo.dto.ItemCarritoPersistenciaDto;

/**
 * Convierte entre el agregado del dominio y los DTOs usados por el archivo
 * JSON. El mapeo mantiene separadas las entidades de la capa de persistencia y
 * evita que Gson dependa de la estructura interna de CarritoCompras.
 */
public final class CarritoMapper {

    private CarritoMapper() {
    }

    /**
     * Construye el DTO de persistencia a partir del carrito del dominio. Los
     * totales se completan en el repositorio porque requieren el tipo de cliente
     * y las reglas de descuento vigentes.
     *
     * @param carrito carrito que se desea convertir
     * @return el DTO listo para serializar, o null si el carrito es null
     */
    public static CarritoSessionDto toDto(CarritoCompras carrito) {
        if (carrito == null) {
            return null;
        }

        CarritoSessionDto dto = new CarritoSessionDto();
        dto.setIdCarrito(carrito.getIdCarrito());
        dto.setIdCliente(carrito.getIdCliente());
        dto.setTipoCliente(carrito.getTipoCliente());

        List<ItemCarritoPersistenciaDto> items = new ArrayList<>();
        for (ItemCarrito item : carrito.obtenerItems()) {
            items.add(new ItemCarritoPersistenciaDto(
                    item.getIsbn(),
                    item.getTitulo(),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    item.getTasaIva() * 100.0,
                    item.getSubtotal()));
        }
        dto.setItems(items);
        return dto;
    }

    /**
     * Reconstruye el carrito del dominio a partir del DTO leído del archivo
     * JSON. Cada línea recupera su tasa de IVA a partir del porcentaje
     * almacenado y el agregado vuelve a calcular sus propios subtotales.
     *
     * @param dto datos leídos del archivo JSON
     * @return el carrito reconstruido, o null si el DTO es null
     */
    public static CarritoCompras toEntity(CarritoSessionDto dto) {
        if (dto == null) {
            return null;
        }

        CarritoCompras carrito = new CarritoCompras(dto.getIdCarrito(), dto.getIdCliente());
        carrito.setTipoCliente(dto.getTipoCliente());

        if (dto.getItems() != null) {
            for (ItemCarritoPersistenciaDto item : dto.getItems()) {
                ItemCarrito entidad = new ItemCarrito(
                        item.getIsbn(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getIvaAplicado() / 100.0);
                entidad.setTitulo(item.getTitulo());
                carrito.agregarItem(entidad);
            }
        }
        return carrito;
    }
}

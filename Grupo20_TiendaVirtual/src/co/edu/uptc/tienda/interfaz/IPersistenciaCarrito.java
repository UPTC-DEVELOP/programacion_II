package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.modelo.RegistroCarrito;

public interface IPersistenciaCarrito {

    void guardar(List<RegistroCarrito> registros) throws CarritoExcepcion;

    List<RegistroCarrito> leer() throws CarritoExcepcion;
}

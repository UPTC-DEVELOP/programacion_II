package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.modelo.Compra;

public interface IPersistenciaCompra {

    void guardar(Compra compra) throws CompraExcepcion;

    List<Compra> leer() throws CompraExcepcion;
}

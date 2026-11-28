package co.edu.uptc.tienda.interfaz;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;

public interface IPersistenciaRecibo {

    String guardar(int numeroCompra, String texto) throws CompraExcepcion;
}

package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.modelo.Calificacion;

public interface IPersistenciaCalificacion {

    void guardar(Calificacion calificacion) throws LibroExcepcion;

    List<Calificacion> leer() throws LibroExcepcion;
}

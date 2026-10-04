package co.edu.uptc.tienda.interfaces;

import java.util.List;

public interface IGestionable<T> {

	void agregar(T objeto);

	List<T> listar();

	T buscar(String identificacion);

	boolean actualizar(T objeto);

	boolean eliminar(String identificacion);

}

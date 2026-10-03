package co.edu.uptc.libreria.persistencia;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import java.util.List;

public interface CarritoPersistencia {
	
	void guardar(List<ItemCarrito> items);
	List<ItemCarrito> cargar();

}

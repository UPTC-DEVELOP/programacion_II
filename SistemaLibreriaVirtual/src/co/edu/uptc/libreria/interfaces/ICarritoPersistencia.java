package co.edu.uptc.libreria.interfaces;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import java.util.List;

public interface ICarritoPersistencia {
	
	void guardar(List<ItemCarrito> items);
	List<ItemCarrito> cargar();

}

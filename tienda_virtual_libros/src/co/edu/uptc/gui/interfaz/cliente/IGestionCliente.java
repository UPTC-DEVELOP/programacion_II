package co.edu.uptc.gui.interfaz.cliente;

import co.edu.uptc.negocio.modelo.Cliente;
import java.util.List;

/**
 * INTERFAZ IGestionCliente  (paquete: gui.interfaz.cliente)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA de clientes. La capa de negocio (GestionCliente)
 * solo conoce esta interfaz, nunca la clase concreta que guarda los datos.
 * Hoy la implementa ClienteRepositorioMemoria (lista en RAM); para usar
 * archivos, XML o JDBC basta crear otra clase que la implemente y cambiar
 * UNA línea en AppLibros (OCP / DIP).
 */
public interface IGestionCliente {

	void guardar(Cliente cliente);
	void actualizar(Cliente cliente);          // reemplaza el que tenga la misma identificación
	void eliminar(String identificacion);
	List<Cliente> listar();
	Cliente buscar(String identificacion);
	Cliente buscarPorCorreo(String correo);

}

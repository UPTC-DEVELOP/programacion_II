package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Evento implements ActionListener {

	public static final String CANCELAR = "Cancelar";
	public static final String LOGIN = "Login";

	public static final String ELIMINAR_CLIENTE = "Eliminar Cliente";
	public static final String VER_CLIENTES = "Ver Clientes";
	public static final String ACTUALIZAR_CLIENTE = "Actualizar Clientes";
	public static final String CREAR_CLIENTE = "Nuevo Cliente";
	public static final String BUSCAR_CLIENTE = "Buscar Cliente";
	public static final String GUARDAR_CLIENTE = "Guardar Cliente";
	public static final String EDITAR_CLIENTE = "Editar Cliente";

	public static final String LIMPIAR = "Limpiar";

	public static final String ELIMINAR_LIBRO = "Eliminar Libro";
	public static final String VER_LIBROS = "Ver Libros";
	public static final String ACTUALIZAR_LIBRO = "Actualizar Libro";
	public static final String CREAR_LIBRO = "Nuevo Libro";
	public static final String BUSCAR_LIBRO = "Buscar Libro";
	public static final String REGISTRAR_AUTOR_LIBRO = "Registrar Autor";
	public static final String GUARDAR_LIBRO = "Registrar Libro";
	public static final String CANCELAR_REGISTRO_LIBRO = "Cancelar Registro";

	public static final String ACTUALIZAR_TABLA = "ACTUALIZAR_TABLA";

	private VentanaPrincipal ventana;

	public Evento(VentanaPrincipal ventana) {
		this.ventana = ventana;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comandoAccion = e.getActionCommand();

		if (comandoAccion.equals(LOGIN)) {
			ventana.loguear();
		}

	}

}

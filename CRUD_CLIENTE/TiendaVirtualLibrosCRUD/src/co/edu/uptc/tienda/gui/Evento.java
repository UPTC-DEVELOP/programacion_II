package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Evento implements ActionListener {

	public static final String CANCELAR = "Cancelar";
	public static final String LOGIN = "Login";

	public static final String ELIMINAR_CLIENTE = "Eliminar";
	public static final String VER_CLIENTES = "Ver";
	public static final String ACTUALIZAR_CLIENTE = "Actualizar";
	public static final String CREAR_CLIENTE = "Nuevo";
	public static final String BUSCAR_CLIENTE = "Buscar";
	public static final String GUARDAR_CLIENTE = "Guardar";
	public static final String EDITAR_CLIENTE = "Editar";

	public static final String LIMPIAR = "Limpiar";

	public static final String ELIMINAR_LIBRO = "Eliminar";
	public static final String VER_LIBROS = "Ver";
	public static final String ACTUALIZAR_LIBRO = "Actualizar";
	public static final String CREAR_LIBRO = "Nuevo";
	public static final String BUSCAR_LIBRO = "Buscar";

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

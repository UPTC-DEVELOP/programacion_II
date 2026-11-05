package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.persistencia.*;

public class ConfiguracionLibro {

	private static GestionLibro gestionLibro;

	static {
		gestionLibro = new GestionLibro(new BaseDeDatosLibro());
	}

	public static GestionLibro getGestionLibro() {
		return gestionLibro;
	}

}

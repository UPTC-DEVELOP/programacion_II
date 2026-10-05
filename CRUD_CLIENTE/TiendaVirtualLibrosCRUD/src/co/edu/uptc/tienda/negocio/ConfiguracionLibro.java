package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.persistencia.LocalLibro;

public class ConfiguracionLibro {

	private static GestionLibro gestionLibro;

	static {
		gestionLibro = new GestionLibro(new LocalLibro());
	}

	public static GestionLibro getGestionLibro() {
		return gestionLibro;
	}

}

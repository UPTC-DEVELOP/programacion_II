package co.edu.uptc.libreria.negocio;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.interfaces.IRegistroOperaciones;
import co.edu.uptc.libreria.persistencia.ArchivoRegistroOperaciones;
import co.edu.uptc.libreria.persistencia.LocalCliente;

/** Punto unico donde se arman las capas (equivale a NominaConfig). */
public class TiendaConfig {

	public static final String RUTA_REGISTRO_OPERACIONES = "datos/operaciones.txt";

	private IGestionCliente iCliente;
	private IRegistroOperaciones iRegistro;
	private GestionCliente gestCliente;

	public TiendaConfig() {
		super();
		iCliente = new LocalCliente();
		iRegistro = new ArchivoRegistroOperaciones(RUTA_REGISTRO_OPERACIONES);
		gestCliente = new GestionCliente(iCliente, iRegistro);
	}

	public GestionCliente getGestCliente() {
		return gestCliente;
	}

	public void setGestCliente(GestionCliente gestCliente) {
		this.gestCliente = gestCliente;
	}

	public IGestionCliente getiCliente() {
		return iCliente;
	}

	public void setiCliente(IGestionCliente iCliente) {
		this.iCliente = iCliente;
	}

	public IRegistroOperaciones getiRegistro() {
		return iRegistro;
	}

	public void setiRegistro(IRegistroOperaciones iRegistro) {
		this.iRegistro = iRegistro;
	}

}
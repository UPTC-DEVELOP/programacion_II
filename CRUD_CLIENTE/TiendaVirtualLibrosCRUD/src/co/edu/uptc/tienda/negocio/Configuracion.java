package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.persistencia.LocalCliente;

public class Configuracion {

    private static GestionCliente gestionCliente;

    static {
        gestionCliente = new GestionCliente(new LocalCliente());
    }

    public static GestionCliente getGestionCliente() {
        return gestionCliente;
    }

}

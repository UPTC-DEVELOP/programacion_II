package co.edu.uptc.negocio;

import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.persistencia.LocalCliente;

public class ClienteConfig {

    private static ClienteConfig instancia;

    private GestionCliente gestionCliente;
    private IGestionCliente iCliente;

    private ClienteConfig() {

        iCliente = new LocalCliente();

        gestionCliente = new GestionCliente(iCliente);
    }

    public static ClienteConfig getInstancia() {

        if (instancia == null) {

            instancia = new ClienteConfig();
        }

        return instancia;
    }

    public GestionCliente getGestionCliente() {

        return gestionCliente;
    }

    public IGestionCliente getiCliente() {

        return iCliente;
    }
}
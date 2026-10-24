package co.edu.uptc.config;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.repository.LocalCliente;
import co.edu.uptc.service.GestionCliente;

public class ClienteConfig {

    private static ClienteConfig instancia;

    private IGestionCliente gestionCliente;
    private IClienteRepositorio iCliente;

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

    public IGestionCliente getGestionCliente() {

        return gestionCliente;
    }

    public IClienteRepositorio getiCliente() {

        return iCliente;
    }
}

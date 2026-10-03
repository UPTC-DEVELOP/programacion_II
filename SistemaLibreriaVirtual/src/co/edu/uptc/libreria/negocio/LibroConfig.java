package co.edu.uptc.negocio;

import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.persistencia.LocalLibro;

public class LibroConfig {

    private static LibroConfig instancia;

    private GestionLibro gestionLibro;
    private IGestionLibro iLibro;

    private LibroConfig() {

        iLibro = new LocalLibro();

        gestionLibro = new GestionLibro(iLibro);
    }

    public static LibroConfig getInstancia() {

        if (instancia == null) {
            instancia = new LibroConfig();
        }

        return instancia;
    }

    public GestionLibro getGestionLibro() {

        return gestionLibro;
    }

    public IGestionLibro getiLibro() {

        return iLibro;
    }
}
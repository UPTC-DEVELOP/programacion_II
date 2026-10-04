package co.edu.uptc.libreria.negocio;

import co.edu.uptc.libreria.interfaces.IGestionLibro;
import co.edu.uptc.libreria.persistencia.LocalLibro;

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
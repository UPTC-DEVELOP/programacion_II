package util.aplicacion;

import javax.swing.SwingUtilities;

import controlador.ControladorLogin;
import modelo.RepositorioLibros;
import modelo.RepositorioUsuarios;
import util.Estilos;
import vista.VistaLogin;

public class Main {
    public static void main(String[] args) {
        Estilos.aplicarNimbus();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();
                RepositorioLibros repositorioLibros = new RepositorioLibros();
                VistaLogin login = new VistaLogin();
                new ControladorLogin(login, repositorioUsuarios, repositorioLibros);
                login.setVisible(true);
            }
        });
    }
}

package co.uptc.edu.caso.estudio;

import javax.swing.SwingUtilities;

import co.uptc.edu.caso.estudio.controller.LibroController;
import co.uptc.edu.caso.estudio.gui.Login;
import co.uptc.edu.caso.estudio.persistencia.ConexionBD;
import co.uptc.edu.caso.estudio.repository.LibroJsonRepository;
import co.uptc.edu.caso.estudio.service.LibroService;

public class App {

    public static void main(String[] args) {

        // Inicializa las tablas que ya utiliza el proyecto
        ConexionBD.inicializarTablas();

        // Construye las dependencias del CRUD de Libro
        LibroJsonRepository repositorio =
                new LibroJsonRepository();

        LibroService servicio =
                new LibroService(repositorio);

        LibroController controller =
                new LibroController(servicio);

        // Inicia la interfaz principal
        SwingUtilities.invokeLater(() -> {
            new Login(controller).setVisible(true);
        });
    }
}
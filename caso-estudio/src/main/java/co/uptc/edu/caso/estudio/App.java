package co.uptc.edu.caso.estudio;

import javax.swing.SwingUtilities;

import co.uptc.edu.caso.estudio.controller.CompraController;
import co.uptc.edu.caso.estudio.controller.LibroController;
import co.uptc.edu.caso.estudio.gui.Login;
import co.uptc.edu.caso.estudio.persistencia.ConexionBD;
import co.uptc.edu.caso.estudio.repository.CompraBDRepository;
import co.uptc.edu.caso.estudio.repository.CompraJsonRepository;
import co.uptc.edu.caso.estudio.repository.LibroJsonRepository;
import co.uptc.edu.caso.estudio.service.CompraService;
import co.uptc.edu.caso.estudio.service.LibroService;

public class App {

    public static void main(String[] args) {

        // Inicializa las tablas en MariaDB (cliente, compra y detalle_compra)
        ConexionBD.inicializarTablas();

        // Construye las dependencias del CRUD de Libro
        LibroJsonRepository libroRepo = new LibroJsonRepository();
        LibroService libroService = new LibroService(libroRepo);
        LibroController libroController = new LibroController(libroService);

        // Construye las dependencias del CRUD de Compra
        CompraBDRepository compraBDRepo = new CompraBDRepository();
        CompraJsonRepository compraJsonRepo = new CompraJsonRepository();
        CompraService compraService = new CompraService(compraBDRepo, compraJsonRepo, libroService);
        CompraController compraController = new CompraController(compraService);

        // Inicia la interfaz principal
        SwingUtilities.invokeLater(() -> {
            new Login(libroController, compraController).setVisible(true);
        });
    }
}
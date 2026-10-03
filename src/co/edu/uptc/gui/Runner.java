package co.edu.uptc.gui;

import co.edu.uptc.negocio.BitacoraRepositorio;
import co.edu.uptc.negocio.BitacoraRepositorioTxt;
import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.LibroRepositorio;
import co.edu.uptc.negocio.LibroRepositorioJson;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.VentaRepositorio;
import co.edu.uptc.negocio.VentaRepositorioJson;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Runner {

    public static void main(String[] args) {
        // Swing debe crearse en el hilo de eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // Si falla, se usa el Look and Feel por defecto
            }

            try {
                // Persistencia (carpeta "data" en la raíz del proyecto)
                Path carpetaDatos = Paths.get("data");
                LibroRepositorio libros = new LibroRepositorioJson(carpetaDatos.resolve("libros.json"));
                VentaRepositorio ventas = new VentaRepositorioJson(carpetaDatos.resolve("ventas.json"));
                BitacoraRepositorio bitacora = new BitacoraRepositorioTxt(carpetaDatos.resolve("bitacora.txt"));

                // Negocio
                CatalogoService servicio = new CatalogoService(libros, ventas, bitacora);

                // Interfaz gráfica + controlador
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                VentanaLibro ventanaLibro = new VentanaLibro();
                ControladorGUI controlador = new ControladorGUI(ventanaPrincipal, ventanaLibro, servicio);
                controlador.iniciar();

            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error al iniciar", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}


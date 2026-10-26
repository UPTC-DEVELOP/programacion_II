package co.edu.uptc;

import javax.swing.SwingUtilities;

import co.edu.uptc.config.ClienteConfig;
import co.edu.uptc.controller.ClienteControlador;
import co.edu.uptc.controller.Evento;
import co.edu.uptc.controller.LibroControlador;
import co.edu.uptc.controller.SesionControlador;
import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.interfaces.ILibreria;
import co.edu.uptc.repository.LocalLibro;
import co.edu.uptc.service.Libreria;

/** Punto de entrada y raiz de composicion: aqui se ensamblan las dependencias. */
public class Inicio {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ILibreria libreria = new Libreria(new LocalLibro());
            IGestionCliente gestionCliente = ClienteConfig.getInstancia().getGestionCliente();

            Evento evento = new Evento();
            VentanaPrincipal ventana = new VentanaPrincipal(evento);

            LibroControlador libros = new LibroControlador(libreria, ventana.getPanelLibros(), ventana, evento);
            ClienteControlador clientes = new ClienteControlador(gestionCliente, ventana.getPanelClientes(), ventana, evento);
            new SesionControlador(ventana, evento, () -> {
                libros.refrescarTabla();
                clientes.refrescarTablaClientes();
            });

            ventana.setVisible(true);
        });
    }
}

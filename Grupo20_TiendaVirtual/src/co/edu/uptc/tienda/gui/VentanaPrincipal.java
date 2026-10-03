package co.edu.uptc.tienda.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.modelo.Carrito;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.GestionCarrito;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaPrincipal() {
        super("Tienda Virtual de Libros - Carrito");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        IGestionCarrito gestion = new GestionCarrito(new Carrito());
        add(new PanelCarrito(gestion, crearCatalogoDePrueba()));

        setSize(800, 480);
        setLocationRelativeTo(null);
    }

    private List<Libro> crearCatalogoDePrueba() {
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro("9780000000001", "Cien años de soledad", 55000, 5, 10));
        libros.add(new Libro("9780000000002", "El coronel no tiene quien le escriba", 32000, 5, 3));
        libros.add(new Libro("9780000000003", "Clean Code", 120000, 19, 5));
        libros.add(new Libro("9780000000004", "Effective Java", 150000, 19, 0));
        return libros;
    }

    private static class Iniciar implements Runnable {
        @Override
        public void run() {
            new VentanaPrincipal().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Iniciar());
    }
}

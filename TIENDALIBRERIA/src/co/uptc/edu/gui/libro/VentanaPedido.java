package co.uptc.edu.gui.libro;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import co.uptc.edu.libro.modelo.Categoria;
import co.uptc.edu.libro.modelo.Formato;
import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.Libreria;
import co.uptc.edu.libro.negocio.LibreriaException;

public class VentanaPedido extends JFrame {

    private PanelPedido panelPedido;

    private Libreria libreria;

    public VentanaPedido(Libreria libreria) {

        this.libreria = libreria;

        setTitle("Módulo B - Registrar Pedido");

        setSize(600, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        panelPedido = new PanelPedido();

        panelPedido.cargarLibros(libreria);

        add(panelPedido, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Libreria libreria = new Libreria();

            try {

            Libro libro1 = new Libro(
                   "978001",
                   "Java Básico",
                   "Autor 1",
                    2024,
                    null,
                   "Editorial 1",
                    300,
                    50000,
                    10,
                        null
                );

                Libro libro2 = new Libro(
                        "978002",
                        "Programación POO",
                        "Autor 2",
                        2025,
                        null,
                        "Editorial 2",
                        400,
                        70000,
                        5,
                        null
                );

                libreria.agregarLibro(libro1);
                libreria.agregarLibro(libro2);

            } catch (LibreriaException e) {

                e.printStackTrace();
            }

            VentanaPedido ventana =
                    new VentanaPedido(libreria);

            ventana.setVisible(true);
        });
    }
}
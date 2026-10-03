package co.edu.uptc.tienda.gui.parcial;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import co.edu.uptc.negocio.parcial.Producto;

public class VentanaPrincipal_Parcial
        extends JFrame {

    private PanelExpresion panel;

    /*
     * Lista donde se almacenan
     * los productos creados.
     */
    private List<Producto> productos;

    public VentanaPrincipal_Parcial() {

        setTitle(
                "Sistema de Tienda - Parcial");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setSize(
                750,
                550);

        setLocationRelativeTo(null);

        /*
         * Crear lista de productos.
         */
        productos =
                new ArrayList<>();

        /*
         * Crear panel.
         */
        panel =
                new PanelExpresion();

        /*
         * Registrar eventos.
         */
        agregarEventos();

        add(
                panel,
                BorderLayout.CENTER);
    }

    /**
     * Registrar los eventos de la interfaz.
     */
    private void agregarEventos() {

        Evento evento =
                new Evento(
                        panel,
                        this);

        /*
         * Clic sobre Producto.
         *
         * Abre JOptionPane.
         */
        panel.getComboProductos()
                .addMouseListener(evento);

        /*
         * Registrar pedido.
         */
        panel.getBtnRegistrarPedido()
                .addActionListener(evento);

        /*
         * Cancelar pedido.
         */
        panel.getBtnCancelarPedido()
                .addActionListener(evento);
    }

    /**
     * Agrega un producto a la lista.
     */
    public void agregarProducto(
            Producto producto) {

        if (producto != null) {

            productos.add(producto);
        }
    }

    /**
     * Busca un producto por su nombre.
     */
    public Producto buscarProducto(
            String nombre) {

        if (nombre == null) {

            return null;
        }

        nombre =
                nombre.trim();

        /*
         * Recorrer todos los productos
         * almacenados.
         */
        for (Producto producto : productos) {

            if (producto.getNombre()
                    .equalsIgnoreCase(nombre)) {

                return producto;
            }
        }

        return null;
    }

    /**
     * Retorna la lista de productos.
     */
    public List<Producto> getProductos() {

        return productos;
    }

    public PanelExpresion getPanel() {

        return panel;
    }

    /**
     * Método principal.
     */
    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    VentanaPrincipal_Parcial ventana =
                            new VentanaPrincipal_Parcial();

                    ventana.setVisible(true);
                });
    }
}
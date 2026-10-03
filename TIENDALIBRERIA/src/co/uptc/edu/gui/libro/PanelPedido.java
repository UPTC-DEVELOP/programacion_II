package co.uptc.edu.gui.libro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.GestorPedidos;
import co.uptc.edu.libro.negocio.Libreria;

public class PanelPedido extends JPanel {

    private JComboBox<Libro> comboLibros;
    private JTextField txtCantidad;
    private JLabel lblPrecio;
    private JLabel lblTotal;
    private JButton btnRegistrar;

    private GestorPedidos gestorPedidos;
    private Libreria libreria;

    public PanelPedido() {

        gestorPedidos = new GestorPedidos();

        setLayout(new BorderLayout(10, 10));

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 10, 15));

        panelDatos.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20)
        );

        // LIBRO
        panelDatos.add(new JLabel("Libro:"));

        comboLibros = new JComboBox<>();
        panelDatos.add(comboLibros);

        // CANTIDAD
        panelDatos.add(new JLabel("Cantidad:"));

        txtCantidad = new JTextField();
        panelDatos.add(txtCantidad);

        // PRECIO
        panelDatos.add(new JLabel("Precio:"));

        lblPrecio = new JLabel("$0");
        panelDatos.add(lblPrecio);

        // TOTAL
        panelDatos.add(new JLabel("Total:"));

        lblTotal = new JLabel("$0");
        panelDatos.add(lblTotal);

        add(panelDatos, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        btnRegistrar = new JButton("Registrar Pedido");

        panelBoton.add(btnRegistrar);

        add(panelBoton, BorderLayout.SOUTH);

        configurarEventos();
    }

    private void configurarEventos() {

        comboLibros.addActionListener(e -> {

            Libro libroSeleccionado =
                    (Libro) comboLibros.getSelectedItem();

            if (libroSeleccionado != null) {

                lblPrecio.setText(
                        "$" + libroSeleccionado.getPrecioVenta()
                );
            }
        });

        btnRegistrar.addActionListener(e -> registrarPedido());
    }

    private void registrarPedido() {

        Libro libroSeleccionado =
                (Libro) comboLibros.getSelectedItem();

        if (libroSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro."
            );

            return;
        }

        int cantidad;

        try {

            cantidad = Integer.parseInt(
                    txtCantidad.getText()
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad debe ser un número."
            );

            return;
        }

        if (cantidad <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad debe ser mayor que cero."
            );

            return;
        }

        if (cantidad > libroSeleccionado.getCantidadDisponible()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad supera el stock disponible."
            );

            return;
        }

        double total =
                gestorPedidos.calcularTotal(
                        libroSeleccionado,
                        cantidad
                );

        lblTotal.setText("$" + total);

        gestorPedidos.registrarPedido(
                libroSeleccionado,
                cantidad,
                total
        );

        JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente."
        );
    }

    public void cargarLibros(Libreria libreria) {

        this.libreria = libreria;

        comboLibros.removeAllItems();

        for (Libro libro : libreria.getListaLibros()) {

            comboLibros.addItem(libro);
        }

        // Mostrar solamente el título en el JComboBox
        comboLibros.setRenderer(new javax.swing.DefaultListCellRenderer() {

            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                if (value instanceof Libro) {

                    Libro libro = (Libro) value;

                    setText(libro.getTituloLibro());
                }

                return this;
            }
        });
    }
}
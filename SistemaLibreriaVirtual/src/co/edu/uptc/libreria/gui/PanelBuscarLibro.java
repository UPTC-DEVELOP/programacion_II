package co.edu.uptc.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.LibroConfig;

public class PanelBuscarLibro extends JFrame {

    private JTextField campoCodigo;
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoEditorial;
    private JTextField campoPrecio;
    private JTextField campoStock;

    private GestionLibro gestionLibro;

    public PanelBuscarLibro() {

        LibroConfig libroConfig = LibroConfig.getInstancia();
        gestionLibro = libroConfig.getGestionLibro();

        setTitle("Buscar Libro");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(
                new GridLayout(8, 2, 10, 10)
        );

        panel.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        panel.add(campoCodigo);

        JButton botonBuscar = new JButton("Buscar");
        panel.add(botonBuscar);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        campoTitulo.setEditable(false);
        panel.add(campoTitulo);

        panel.add(new JLabel("Autor:"));
        campoAutor = new JTextField();
        campoAutor.setEditable(false);
        panel.add(campoAutor);

        panel.add(new JLabel("Editorial:"));
        campoEditorial = new JTextField();
        campoEditorial.setEditable(false);
        panel.add(campoEditorial);

        panel.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        campoPrecio.setEditable(false);
        panel.add(campoPrecio);

        panel.add(new JLabel("Stock:"));
        campoStock = new JTextField();
        campoStock.setEditable(false);
        panel.add(campoStock);

        JButton botonVolver = new JButton("Volver");
        panel.add(botonVolver);
        panel.add(new JLabel(""));

        add(panel);

        botonBuscar.addActionListener(e -> buscarLibro());

        botonVolver.addActionListener(e -> {
            new PanelCentral().setVisible(true);
            dispose();
        });
    }

    private void buscarLibro() {

        String codigo = campoCodigo.getText();

        for (Libro libro : gestionLibro.listarLibros()) {

            if (libro.getCodigo().equals(codigo)) {

                campoTitulo.setText(libro.getTitulo());
                campoAutor.setText(libro.getAutor());
                campoEditorial.setText(libro.getEditorial());
                campoPrecio.setText(libro.getPrecio());
                campoStock.setText(libro.getStock());

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Libro no encontrado"
        );

        limpiarResultados();
    }

    private void limpiarResultados() {

        campoTitulo.setText("");
        campoAutor.setText("");
        campoEditorial.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
    }
}
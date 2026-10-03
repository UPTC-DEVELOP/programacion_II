package co.edu.uptc.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.LibroConfig;

public class PanelRegistrarLibro extends JFrame {

    private JTextField campoCodigo;
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoEditorial;
    private JTextField campoAnio;
    private JComboBox<String> campoGenero;
    private JTextField campoPrecio;
    private JTextField campoStock;

    private GestionLibro gestionLibro;

    public PanelRegistrarLibro() {

        LibroConfig libroConfig = LibroConfig.getInstancia();
        gestionLibro = libroConfig.getGestionLibro();

        setTitle("Registrar Libro");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(
                new GridLayout(10, 2, 10, 10)
        );

        panel.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        panel.add(campoCodigo);

        panel.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        panel.add(campoTitulo);

        panel.add(new JLabel("Autor:"));
        campoAutor = new JTextField();
        panel.add(campoAutor);

        panel.add(new JLabel("Editorial:"));
        campoEditorial = new JTextField();
        panel.add(campoEditorial);

        panel.add(new JLabel("Año de publicación:"));
        campoAnio = new JTextField();
        panel.add(campoAnio);

        panel.add(new JLabel("Género:"));
        campoGenero = new JComboBox<>(
                new String[] {
                        "Novela",
                        "Ciencia ficción",
                        "Romance",
                        "Misterio",
                        "Historia",
                        "Fantasía"
                }
        );
        panel.add(campoGenero);

        panel.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        panel.add(campoPrecio);

        panel.add(new JLabel("Stock:"));
        campoStock = new JTextField();
        panel.add(campoStock);

        JButton botonRegistrar = new JButton("Registrar");
        JButton botonLimpiar = new JButton("Limpiar");

        panel.add(botonRegistrar);
        panel.add(botonLimpiar);

        JButton botonVolver = new JButton("Volver");

        panel.add(botonVolver);
        panel.add(new JLabel(""));

        add(panel);

        botonRegistrar.addActionListener(e -> registrarLibro());

        botonLimpiar.addActionListener(e -> limpiar());

        botonVolver.addActionListener(e -> {
            new PanelCentral().setVisible(true);
            dispose();
        });
    }

    private void registrarLibro() {

        Libro libro = new Libro(
                campoCodigo.getText(),
                campoTitulo.getText(),
                campoAutor.getText(),
                campoEditorial.getText(),
                campoAnio.getText(),
                campoGenero.getSelectedItem().toString(),
                campoPrecio.getText(),
                campoStock.getText()
        );

        gestionLibro.guardarLibro(libro);

        JOptionPane.showMessageDialog(
                this,
                "Libro registrado correctamente"
        );

        limpiar();
    }

    private void limpiar() {

        campoCodigo.setText("");
        campoTitulo.setText("");
        campoAutor.setText("");
        campoEditorial.setText("");
        campoAnio.setText("");
        campoGenero.setSelectedIndex(0);
        campoPrecio.setText("");
        campoStock.setText("");
    }
}
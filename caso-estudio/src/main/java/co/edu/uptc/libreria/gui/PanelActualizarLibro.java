package co.edu.uptc.libreria.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.libreria.clientes.gui.PanelCentral;
import co.edu.uptc.libreria.modelo.Libro;
import co.edu.uptc.libreria.negocio.GestionLibro;
import co.edu.uptc.libreria.negocio.LibroConfig;

public class PanelActualizarLibro extends JFrame {

    private JTextField campoBuscarCodigo;
    private JTextField campoCodigo;
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoEditorial;
    private JTextField campoAnio;
    private JComboBox<String> campoGenero;
    private JTextField campoPrecio;
    private JTextField campoStock;

    private GestionLibro gestionLibro;
    private Libro libroEncontrado;

    public PanelActualizarLibro() {

        LibroConfig libroConfig = LibroConfig.getInstancia();
        gestionLibro = libroConfig.getGestionLibro();

        setTitle("Actualizar Libro");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(10, 10)
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel panelBusqueda = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        campoBuscarCodigo = new JTextField();

        JButton botonBuscar = new JButton("Buscar");

        panelBusqueda.add(campoBuscarCodigo);
        panelBusqueda.add(botonBuscar);

        JPanel panelDatos = new JPanel(
                new GridLayout(8, 2, 10, 10)
        );

        panelDatos.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        campoCodigo.setEditable(false);
        panelDatos.add(campoCodigo);

        panelDatos.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        panelDatos.add(campoTitulo);

        panelDatos.add(new JLabel("Autor:"));
        campoAutor = new JTextField();
        panelDatos.add(campoAutor);

        panelDatos.add(new JLabel("Editorial:"));
        campoEditorial = new JTextField();
        panelDatos.add(campoEditorial);

        panelDatos.add(new JLabel("Año de publicación:"));
        campoAnio = new JTextField();
        panelDatos.add(campoAnio);

        panelDatos.add(new JLabel("Género:"));
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
        panelDatos.add(campoGenero);

        panelDatos.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        panelDatos.add(campoPrecio);

        panelDatos.add(new JLabel("Stock:"));
        campoStock = new JTextField();
        panelDatos.add(campoStock);

        JPanel panelBotones = new JPanel(
                new GridLayout(1, 3, 10, 10)
        );

        JButton botonIngresar = new JButton("Ingresar");
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonVolver = new JButton("Volver");

        panelBotones.add(botonIngresar);
        panelBotones.add(botonCancelar);
        panelBotones.add(botonVolver);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        botonBuscar.addActionListener(e -> buscarLibro());

        botonIngresar.addActionListener(e -> actualizarLibro());

        botonCancelar.addActionListener(e -> limpiarCampos());

        botonVolver.addActionListener(e -> {
            new ventanaPrincipal().setVisible(true);
            dispose();
        });

        limpiarCampos();
    }

    private void buscarLibro() {

        String codigo = campoBuscarCodigo.getText();

        libroEncontrado = null;

        for (Libro libro : gestionLibro.listarLibros()) {

            if (libro.getCodigo().equals(codigo)) {

                libroEncontrado = libro;

                campoCodigo.setText(libro.getCodigo());
                campoTitulo.setText(libro.getTitulo());
                campoAutor.setText(libro.getAutor());
                campoEditorial.setText(libro.getEditorial());
                campoAnio.setText(libro.getAnioPublicacion());

                campoGenero.setSelectedItem(
                        libro.getGenero()
                );

                campoPrecio.setText(libro.getPrecio());
                campoStock.setText(libro.getStock());

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Libro no encontrado"
        );
    }

    private void actualizarLibro() {

        if (libroEncontrado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero busque un libro"
            );

            return;
        }

        Libro libroActualizado = new Libro(
                campoCodigo.getText(),
                campoTitulo.getText(),
                campoAutor.getText(),
                campoEditorial.getText(),
                campoAnio.getText(),
                campoGenero.getSelectedItem().toString(),
                campoPrecio.getText(),
                campoStock.getText()
        );

        gestionLibro.actualizarLibro(libroActualizado);

        JOptionPane.showMessageDialog(
                this,
                "Libro actualizado correctamente"
        );

        libroEncontrado = libroActualizado;
    }

    private void limpiarCampos() {

        campoBuscarCodigo.setText("");
        campoCodigo.setText("");
        campoTitulo.setText("");
        campoAutor.setText("");
        campoEditorial.setText("");
        campoAnio.setText("");
        campoGenero.setSelectedIndex(0);
        campoPrecio.setText("");
        campoStock.setText("");

        libroEncontrado = null;
    }
}
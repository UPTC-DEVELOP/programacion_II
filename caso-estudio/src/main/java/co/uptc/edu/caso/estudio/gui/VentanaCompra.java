package co.uptc.edu.caso.estudio.gui;

import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.persistencia.CompraDAO;
import co.uptc.edu.caso.estudio.persistencia.GestorJson;
import co.uptc.edu.caso.estudio.controller.LibroController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaCompra extends JFrame {

    private JTextField txtId;
    private JTextField txtEstado;
    private JTextArea areaSalida;

    private final CompraDAO compraDAO = new CompraDAO();
    private final GestorJson gestorJson = new GestorJson();

    // Controlador del CRUD de Libro
    private LibroController libroController;

    public VentanaCompra() {
        setTitle("Gestión de Compras (CRUD - MariaDB + JSON)");
        setSize(560, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));

        panelForm.add(new JLabel(" ID Compra:"));
        txtId = new JTextField();
        panelForm.add(txtId);

        panelForm.add(new JLabel(" Estado:"));
        txtEstado = new JTextField("Pendiente");
        panelForm.add(txtEstado);

        add(panelForm, BorderLayout.NORTH);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        add(new JScrollPane(areaSalida), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnGuardar = new JButton("Guardar (C)");
        JButton btnBuscar = new JButton("Buscar (R)");
        JButton btnActualizar = new JButton("Actualizar (U)");
        JButton btnEliminar = new JButton("Eliminar (D)");
        JButton btnListar = new JButton("Listar Todo");
        JButton btnLibros = new JButton("Gestión de Libros");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnLibros);

        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String estado = txtEstado.getText().trim();

                Compra nueva = new Compra(id, estado);

                if (compraDAO.guardar(nueva)) {
                    sincronizarJson();

                    JOptionPane.showMessageDialog(
                            this,
                            "Compra guardada en MariaDB y compras.json."
                    );

                    limpiarCampos();
                    listarTodas();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error: Ya existe una compra con ese ID o falló la BD."
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número entero válido."
                );
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());

                Compra buscada = compraDAO.buscarPorId(id);

                if (buscada != null) {
                    txtEstado.setText(buscada.getEstado());

                    areaSalida.setText(
                            "COMPRA ENCONTRADA EN BD:\n"
                            + buscada.generarResumen()
                    );

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se encontró ninguna compra con el ID " + id
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un ID válido para buscar."
                );
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nuevoEstado = txtEstado.getText().trim();

                if (compraDAO.actualizarEstado(id, nuevoEstado)) {
                    sincronizarJson();

                    JOptionPane.showMessageDialog(
                            this,
                            "Estado actualizado en BD y JSON."
                    );

                    listarTodas();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error: No existe la compra con el ID " + id
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un ID válido."
                );
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());

                if (compraDAO.eliminar(id)) {
                    sincronizarJson();

                    JOptionPane.showMessageDialog(
                            this,
                            "Compra eliminada de BD y JSON."
                    );

                    limpiarCampos();
                    listarTodas();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error: No existe una compra con ese ID."
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un ID válido para eliminar."
                );
            }
        });

        btnListar.addActionListener(e -> listarTodas());
        btnLibros.addActionListener(e -> {
            GestionLibro ventanaLibros =
                    new GestionLibro(libroController);

            ventanaLibros.setVisible(true);
        });
        listarTodas();
    }

    // Nuevo constructor para recibir el controlador de Libro
    public VentanaCompra(LibroController libroController) {
        this();
        this.libroController = libroController;
    }

    private void sincronizarJson() {
        List<Compra> todas = compraDAO.obtenerTodas();
        gestorJson.guardarCompras(todas);
    }

    private void listarTodas() {
        List<Compra> comprasBD = compraDAO.obtenerTodas();

        areaSalida.setText(
                "COMPRAS REGISTRADAS EN MARIADB ("
                + comprasBD.size()
                + "):\n"
        );

        for (Compra c : comprasBD) {
            areaSalida.append(
                    c.generarResumen() + "\n"
            );
        }

        areaSalida.append(
                "\n--- RESPALDO LEÍDO DESDE compras.json ---\n"
        );

        for (Compra c : gestorJson.leerCompras()) {
            areaSalida.append(
                    "[JSON] "
                    + c.generarResumen()
                    + "\n"
            );
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtEstado.setText("Pendiente");
    }
}
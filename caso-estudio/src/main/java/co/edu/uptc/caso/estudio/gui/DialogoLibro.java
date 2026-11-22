package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Libro;
import java.awt.*;
import javax.swing.*;

public class DialogoLibro extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtEditorial, txtPaginas, txtPrecio, txtStock, txtIva;
    private JComboBox<String> cbCategoria, cbFormato;
    private Libro libroResultado;

    public DialogoLibro(JFrame padre, Libro libroExistente) {
        super(padre, libroExistente != null ? "Editar Libro" : "Agregar Nuevo Libro", true);
        initUI(libroExistente);
    }

    private void initUI(Libro libroExistente) {
        boolean esEdicion = (libroExistente != null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(11, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        txtIsbn = new JTextField(esEdicion ? libroExistente.getIsbn() : "");
        txtIsbn.setEditable(!esEdicion);
        txtTitulo = new JTextField(esEdicion ? libroExistente.getTitulo() : "");
        txtAutor = new JTextField(esEdicion ? libroExistente.getAutor() : "");
        txtAnio = new JTextField(esEdicion ? String.valueOf(libroExistente.getAnio()) : "");

        String[] categorias = {"Programación", "Arquitectura", "Literatura", "Ciencia", "Ingeniería", "Historia"};
        cbCategoria = new JComboBox<>(categorias);
        if (esEdicion && libroExistente.getCategoria() != null) cbCategoria.setSelectedItem(libroExistente.getCategoria());

        txtEditorial = new JTextField(esEdicion ? libroExistente.getEditorial() : "");
        txtPaginas = new JTextField(esEdicion ? String.valueOf(libroExistente.getPaginas()) : "");
        txtPrecio = new JTextField(esEdicion ? String.valueOf(libroExistente.getPrecioVenta()) : "");
        txtStock = new JTextField(esEdicion ? String.valueOf(libroExistente.getStock()) : "");

        cbFormato = new JComboBox<>(new String[]{"Físico", "Digital"});
        if (esEdicion && libroExistente.getFormato() != null) cbFormato.setSelectedItem(libroExistente.getFormato());

        txtIva = new JTextField(esEdicion ? String.valueOf(libroExistente.getPorcentajeIva()) : "19.0");

        panelForm.add(new JLabel("ISBN:")); panelForm.add(txtIsbn);
        panelForm.add(new JLabel("Título:")); panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:")); panelForm.add(txtAutor);
        panelForm.add(new JLabel("Año Publicación:")); panelForm.add(txtAnio);
        panelForm.add(new JLabel("Categoría:")); panelForm.add(cbCategoria);
        panelForm.add(new JLabel("Editorial:")); panelForm.add(txtEditorial);
        panelForm.add(new JLabel("Páginas:")); panelForm.add(txtPaginas);
        panelForm.add(new JLabel("Precio Venta (con IVA):")); panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Stock:")); panelForm.add(txtStock);
        panelForm.add(new JLabel("Formato:")); panelForm.add(cbFormato);
        panelForm.add(new JLabel("Porcentaje IVA (%):")); panelForm.add(txtIva);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(_ -> confirmar());
        btnCancelar.addActionListener(_ -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnGuardar);
        pack();
        setLocationRelativeTo(getParent());
    }

    private void confirmar() {
        try {
            Libro l = new Libro();
            l.setIsbn(txtIsbn.getText().trim());
            l.setTitulo(txtTitulo.getText().trim());
            l.setAutor(txtAutor.getText().trim());
            l.setAnio(Integer.parseInt(txtAnio.getText().trim()));
            l.setCategoria((String) cbCategoria.getSelectedItem());
            l.setEditorial(txtEditorial.getText().trim());
            l.setPaginas(Integer.parseInt(txtPaginas.getText().trim()));
            l.setPrecioVenta(Double.parseDouble(txtPrecio.getText().trim()));
            l.setStock(Integer.parseInt(txtStock.getText().trim()));
            l.setFormato((String) cbFormato.getSelectedItem());
            l.setPorcentajeIva(Double.parseDouble(txtIva.getText().trim()));
            this.libroResultado = l;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos en Año, Páginas, Precio, Stock e IVA.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Libro getLibroResultado() {
        return libroResultado;
    }
}

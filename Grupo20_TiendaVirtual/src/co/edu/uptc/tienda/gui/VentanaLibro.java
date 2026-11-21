package co.edu.uptc.tienda.gui;

import javax.swing.*;
import java.awt.*;
import co.edu.uptc.tienda.negocio.ControlLibro;

public class VentanaLibro extends JFrame {

    private JLabel lblTitulo;
    private JLabel lblIsbn;
    private JLabel lblAutor;
    private JLabel lblAnio;
    private JLabel lblEditorial;
    private JLabel lblPaginas;
    private JLabel lblPrecio;
    private JLabel lblIva;
    private JLabel lblStock;
    private JLabel lblFormato;

    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnio;
    private JTextField txtEditorial;
    private JTextField txtPaginas;
    private JTextField txtPrecio;
    private JTextField txtIva;
    private JTextField txtStock;

    private JComboBox<String> cmbFormato;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JButton btnLimpiar;

    public VentanaLibro() {
        this(new ControlLibro());
    }

    public VentanaLibro(ControlLibro controlLibro) {

        setTitle("Gestión de Libros - Tienda Virtual de Libros");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();

        new EventosLibro(this, controlLibro);
    }
    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "GESTIÓN DE LIBROS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(
                new GridLayout(10, 2, 8, 8)
        );

        lblIsbn = new JLabel("ISBN:");
        lblTitulo = new JLabel("Título:");
        lblAutor = new JLabel("Autor:");
        lblAnio = new JLabel("Año de publicación:");
        lblEditorial = new JLabel("Editorial:");
        lblPaginas = new JLabel("Número de páginas:");
        lblPrecio = new JLabel("Precio:");
        lblIva = new JLabel("Porcentaje IVA:");
        lblStock = new JLabel("Cantidad disponible:");
        lblFormato = new JLabel("Formato:");

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnio = new JTextField();
        txtEditorial = new JTextField();
        txtPaginas = new JTextField();
        txtPrecio = new JTextField();
        txtIva = new JTextField();
        txtStock = new JTextField();

        cmbFormato = new JComboBox<>(
                new String[]{"Físico", "Digital"}
        );

        panelFormulario.add(lblIsbn);
        panelFormulario.add(txtIsbn);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(txtTitulo);

        panelFormulario.add(lblAutor);
        panelFormulario.add(txtAutor);

        panelFormulario.add(lblAnio);
        panelFormulario.add(txtAnio);

        panelFormulario.add(lblEditorial);
        panelFormulario.add(txtEditorial);

        panelFormulario.add(lblPaginas);
        panelFormulario.add(txtPaginas);

        panelFormulario.add(lblPrecio);
        panelFormulario.add(txtPrecio);

        panelFormulario.add(lblIva);
        panelFormulario.add(txtIva);

        panelFormulario.add(lblStock);
        panelFormulario.add(txtStock);

        panelFormulario.add(lblFormato);
        panelFormulario.add(cmbFormato);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        btnRegistrar = new JButton(EventosLibro.REGISTRAR);
        btnActualizar = new JButton(EventosLibro.ACTUALIZAR);
        btnEliminar = new JButton(EventosLibro.ELIMINAR);
        btnListar = new JButton(EventosLibro.LISTAR);
        btnLimpiar = new JButton(EventosLibro.LIMPIAR);

        btnRegistrar.setActionCommand(EventosLibro.REGISTRAR);
        btnActualizar.setActionCommand(EventosLibro.ACTUALIZAR);
        btnEliminar.setActionCommand(EventosLibro.ELIMINAR);
        btnListar.setActionCommand(EventosLibro.LISTAR);
        btnLimpiar.setActionCommand(EventosLibro.LIMPIAR);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnLimpiar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JTextField getTxtIsbn() {
        return txtIsbn;
    }

    public JTextField getTxtTitulo() {
        return txtTitulo;
    }

    public JTextField getTxtAutor() {
        return txtAutor;
    }

    public JTextField getTxtAnio() {
        return txtAnio;
    }

    public JTextField getTxtEditorial() {
        return txtEditorial;
    }

    public JTextField getTxtPaginas() {
        return txtPaginas;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JTextField getTxtIva() {
        return txtIva;
    }

    public JTextField getTxtStock() {
        return txtStock;
    }

    public JComboBox<String> getCmbFormato() {
        return cmbFormato;
    }

    public void limpiarCampos() {

        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtIva.setText("");
        txtStock.setText("");

        cmbFormato.setSelectedIndex(0);

        txtIsbn.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaLibro ventana = new VentanaLibro();

            ventana.setVisible(true);
        });
    }
}

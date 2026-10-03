package co.edu.uptc.gui;

import co.edu.uptc.negocio.FormatoLibro;
import co.edu.uptc.negocio.Libro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PanelDetalleLibro extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnioPublicacion;
    private JTextField txtCategoria;
    private JTextField txtEditorial;
    private JTextField txtNumeroPaginas;
    private JTextField txtPrecioBase;
    private JTextField txtCantidadDisponible;
    private JComboBox<FormatoLibro> cmbFormato;
    private PanelBotones panelBotones;

    public PanelDetalleLibro() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detalles del Libro"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        setPreferredSize(new Dimension(360, 500));

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnioPublicacion = new JTextField();
        txtCategoria = new JTextField();
        txtEditorial = new JTextField();
        txtNumeroPaginas = new JTextField();
        txtPrecioBase = new JTextField();
        txtCantidadDisponible = new JTextField();
        cmbFormato = new JComboBox<>(FormatoLibro.values());
        cmbFormato.setActionCommand(Comandos.FORMATO_CAMBIO);

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 6, 8));
        agregarCampo(panelCampos, "ISBN (13):", txtIsbn);
        agregarCampo(panelCampos, "Título:", txtTitulo);
        agregarCampo(panelCampos, "Autor(es):", txtAutor);
        agregarCampo(panelCampos, "Año Publicación:", txtAnioPublicacion);
        agregarCampo(panelCampos, "Categoría:", txtCategoria);
        agregarCampo(panelCampos, "Editorial:", txtEditorial);
        agregarCampo(panelCampos, "Formato:", cmbFormato);
        agregarCampo(panelCampos, "Número Páginas:", txtNumeroPaginas);
        agregarCampo(panelCampos, "Precio Base ($):", txtPrecioBase);
        agregarCampo(panelCampos, "Cantidad Disponible:", txtCantidadDisponible);

        panelBotones = new PanelBotones();

        add(panelCampos, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarCampoPaginas();
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    // ===================== Eventos =====================

    public void agregarListener(ActionListener listener) {
        cmbFormato.addActionListener(listener);
        panelBotones.agregarListener(listener);
    }

    // ===================== Lectura de datos =====================

    public String getIsbn() { return txtIsbn.getText().trim(); }
    public String getTitulo() { return txtTitulo.getText().trim(); }
    public String getAutor() { return txtAutor.getText().trim(); }
    public String getAnioPublicacion() { return txtAnioPublicacion.getText().trim(); }
    public String getCategoria() { return txtCategoria.getText().trim(); }
    public String getEditorial() { return txtEditorial.getText().trim(); }
    public String getNumeroPaginas() { return txtNumeroPaginas.getText().trim(); }
    public String getPrecioBase() { return txtPrecioBase.getText().trim(); }
    public String getCantidadDisponible() { return txtCantidadDisponible.getText().trim(); }

    public FormatoLibro getFormatoSeleccionado() {
        return (FormatoLibro) cmbFormato.getSelectedItem();
    }

    // ===================== Estado visual =====================

    public void cargarLibro(Libro libro) {
        txtIsbn.setText(libro.getIsbn());
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnioPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
        txtCategoria.setText(libro.getCategoria());
        txtEditorial.setText(libro.getEditorial());
        cmbFormato.setSelectedItem(libro.getFormato());
        actualizarCampoPaginas();
        txtNumeroPaginas.setText(libro.getNumeroPaginas() > 0
                ? String.valueOf(libro.getNumeroPaginas()) : "");
        txtPrecioBase.setText(BigDecimal.valueOf(libro.getPrecioBase())
                .stripTrailingZeros().toPlainString());
        txtCantidadDisponible.setText(String.valueOf(libro.getCantidadDisponible()));
    }

    public void limpiar() {
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnioPublicacion.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtNumeroPaginas.setText("");
        txtPrecioBase.setText("");
        txtCantidadDisponible.setText("");
        cmbFormato.setSelectedIndex(0);
        actualizarCampoPaginas();
        setModoEdicion(false);
    }

 
    public void setModoEdicion(boolean edicion) {
        txtIsbn.setEditable(!edicion);
        panelBotones.setModoEdicion(edicion);
    }

 
    public void actualizarCampoPaginas() {
        boolean fisico = cmbFormato.getSelectedItem() == FormatoLibro.FISICO;
        txtNumeroPaginas.setEnabled(fisico);
        if (!fisico) {
            txtNumeroPaginas.setText("");
        }
    }

    public void enfocarIsbn() {
        txtIsbn.requestFocusInWindow();
    }
}

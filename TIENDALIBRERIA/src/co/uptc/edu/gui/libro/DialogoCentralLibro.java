package co.uptc.edu.gui.libro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import co.uptc.edu.libro.modelo.Categoria;
import co.uptc.edu.libro.modelo.Formato;
import co.uptc.edu.libro.modelo.Libro;


public abstract class DialogoCentralLibro extends JDialog {

    protected boolean isCrear;
    protected String tituloDialogo;

    // Campos de libro
    protected JTextField txIsbn;
    protected JTextField txTitulo;
    protected JTextField txAutor;
    protected JTextField txFecha;
    protected JTextField txEditorial;
    protected JTextField txPaginas;
    protected JTextField txPrecio;
    protected JTextField txCantidad;
    protected JComboBox<Categoria> cbxCategoria;
    protected JComboBox<Formato> cbxFormato;

    // Botones
    protected JButton btnGuardar;
    protected JButton btnCerrar;

    public DialogoCentralLibro(Evento evento, String tituloDialogo, boolean isCrear) {
        this.isCrear = isCrear;
        this.tituloDialogo = tituloDialogo;

        setSize(420, 500);
        setTitle(tituloDialogo);
        setModal(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes(evento);
    }

    private void inicializarComponentes(Evento evento) {
        JPanel pLibro = new JPanel(new GridLayout(10, 2, 8, 8));
        pLibro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txIsbn = new JTextField();
        txTitulo = new JTextField();
        txAutor = new JTextField();
        txFecha = new JTextField();
        txEditorial = new JTextField();
        txPaginas = new JTextField();
        txPrecio = new JTextField();
        txCantidad = new JTextField();
        cbxCategoria = new JComboBox<>(Categoria.values());
        cbxFormato = new JComboBox<>(Formato.values());

        pLibro.add(new JLabel("ISBN:")); pLibro.add(txIsbn);
        pLibro.add(new JLabel("Título:")); pLibro.add(txTitulo);
        pLibro.add(new JLabel("Autor:")); pLibro.add(txAutor);
        pLibro.add(new JLabel("Año Publicación:")); pLibro.add(txFecha);
        pLibro.add(new JLabel("Editorial:")); pLibro.add(txEditorial);
        pLibro.add(new JLabel("Páginas:")); pLibro.add(txPaginas);
        pLibro.add(new JLabel("Precio Venta:")); pLibro.add(txPrecio);
        pLibro.add(new JLabel("Cantidad Disponible:")); pLibro.add(txCantidad);
        pLibro.add(new JLabel("Categoría:")); pLibro.add(cbxCategoria);
        pLibro.add(new JLabel("Formato:")); pLibro.add(cbxFormato);

        // Botones
        btnGuardar = new JButton(isCrear ? "Guardar" : "Actualizar");
        btnCerrar = new JButton("Cancelar");

        btnGuardar.addActionListener(evento);
        btnCerrar.addActionListener(evento);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBotones.add(btnCerrar);
        pBotones.add(btnGuardar);

        add(pLibro, BorderLayout.CENTER);
        add(pBotones, BorderLayout.SOUTH);

        asignarComandoBotones();
    }

    public abstract void asignarComandoBotones();
}
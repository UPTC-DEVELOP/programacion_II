package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.FormatoLibro;
import model.Libro;
import service.IServicioLibros;

/**
 * Manejador de eventos de la ventana de gestion de libros.
 */
public class EventosLibros extends Eventos {

    private static final String IVA_POR_DEFECTO = "19";

    private final IServicioLibros controladorLibro;

    public EventosLibros(VentanaLibros vista, IServicioLibros controladorLibro) {
        super(vista);
        this.controladorLibro = controladorLibro;
    }

    public void registrarEventos(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JTextField txtBuscar,
            JComboBox<FormatoLibro> cmbFormato,
            JTable tblLibros, DefaultTableModel modeloTabla,
            JButton btnBuscar, JButton btnLimpiarBusqueda,
            JButton btnRegistrar, JButton btnActualizar,
            JButton btnEliminar, JButton btnLimpiar, JButton btnAtras,
            JFrame padre) {

        btnRegistrar.addActionListener(e -> registrarLibro(
                txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                txtPaginas, txtPrecio, txtIva, txtCantidad, cmbFormato,
                tblLibros, modeloTabla));

        btnActualizar.addActionListener(e -> actualizarLibro(
                txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                txtPaginas, txtPrecio, txtIva, txtCantidad, cmbFormato,
                tblLibros, modeloTabla));

        btnEliminar.addActionListener(e -> eliminarLibro(
                txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                txtPaginas, txtPrecio, txtIva, txtCantidad, tblLibros, modeloTabla));

        btnBuscar.addActionListener(e -> buscarLibros(txtBuscar, modeloTabla));
        btnLimpiarBusqueda.addActionListener(e -> actualizarTabla(controladorLibro.listarUltimos(10), modeloTabla));

        btnLimpiar.addActionListener(e -> limpiarCampos(
                txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                txtPaginas, txtPrecio, txtIva, txtCantidad, tblLibros));

        btnAtras.addActionListener(e -> volver(padre));

        tblLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario(
                        txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                        txtPaginas, txtPrecio, txtIva, txtCantidad, cmbFormato,
                        tblLibros, modeloTabla);
            }
        });

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });

        actualizarTabla(controladorLibro.listarUltimos(10), modeloTabla);
    }

    private void registrarLibro(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JComboBox<FormatoLibro> cmbFormato,
            JTable tblLibros, DefaultTableModel modeloTabla) {
        try {
            Libro libro = extraerLibroDelFormulario(
                    txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                    txtPaginas, txtPrecio, txtIva, txtCantidad, cmbFormato);
            if (controladorLibro.registrar(libro)) {
                mostrarInfo("Libro registrado exitosamente.");
                actualizarTabla(controladorLibro.listarUltimos(10), modeloTabla);
                limpiarCampos(txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                        txtPaginas, txtPrecio, txtIva, txtCantidad, tblLibros);
            } else if (controladorLibro.buscar(libro.getIsbn()) != null) {
                mostrarError("El ISBN ya existe.");
            } else {
                mostrarError("No se pudo guardar el libro. Verifique los datos o el almacenamiento.");
            }
        } catch (NumberFormatException e) {
            mostrarError("Por favor ingrese valores num\u00e9ricos v\u00e1lidos.");
        }
    }

    private void actualizarLibro(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JComboBox<FormatoLibro> cmbFormato,
            JTable tblLibros, DefaultTableModel modeloTabla) {
        try {
            String isbn = txtISBN.getText().trim();
            Libro datosNuevos = extraerLibroDelFormulario(
                    txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                    txtPaginas, txtPrecio, txtIva, txtCantidad, cmbFormato);
            if (controladorLibro.actualizar(isbn, datosNuevos)) {
                mostrarInfo("Libro actualizado correctamente.");
                actualizarTabla(controladorLibro.listarUltimos(10), modeloTabla);
                limpiarCampos(txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                        txtPaginas, txtPrecio, txtIva, txtCantidad, tblLibros);
            } else if (controladorLibro.buscar(isbn) == null) {
                mostrarError("No se encontr\u00f3 un libro con el ISBN ingresado.");
            } else {
                mostrarError("No se pudo guardar la actualizaci\u00f3n. Verifique los datos o el almacenamiento.");
            }
        } catch (NumberFormatException e) {
            mostrarError("Verifique los datos num\u00e9ricos.");
        }
    }

    private void eliminarLibro(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JTable tblLibros, DefaultTableModel modeloTabla) {
        String isbn = txtISBN.getText().trim();
        if (isbn.isEmpty()) {
            mostrarError("Seleccione o ingrese un ISBN para eliminar.");
            return;
        }

        if (confirmar("\u00bfEst\u00e1 seguro de eliminar el libro?")) {
            if (controladorLibro.eliminar(isbn)) {
                mostrarInfo("Libro eliminado con \u00e9xito.");
                actualizarTabla(controladorLibro.listarUltimos(10), modeloTabla);
                limpiarCampos(txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                        txtPaginas, txtPrecio, txtIva, txtCantidad, tblLibros);
            } else if (controladorLibro.buscar(isbn) == null) {
                mostrarError("No se encontr\u00f3 un libro con el ISBN ingresado.");
            } else {
                mostrarError("No se pudo eliminar el libro. Verifique si tiene ventas asociadas o el almacenamiento.");
            }
        }
    }

    private void buscarLibros(JTextField txtBuscar, DefaultTableModel modeloTabla) {
        String criterio = txtBuscar.getText().trim();
        List<Libro> resultados = controladorLibro.buscarPorCriterio(criterio);
        actualizarTabla(resultados, modeloTabla);
    }

    private Libro extraerLibroDelFormulario(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JComboBox<FormatoLibro> cmbFormato) {
        Libro libro = new Libro();
        libro.setIsbn(txtISBN.getText().trim());
        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAutor(txtAutor.getText().trim());
        libro.setAnioPublicacion(txtAnio.getText().isEmpty() ? 0 : Integer.parseInt(txtAnio.getText().trim()));
        libro.setCategoria(txtCategoria.getText().trim());
        libro.setEditorial(txtEditorial.getText().trim());
        libro.setNumeroPaginas(txtPaginas.getText().isEmpty() ? 0 : Integer.parseInt(txtPaginas.getText().trim()));
        libro.setPrecio(txtPrecio.getText().isEmpty() ? 0.0 : Double.parseDouble(txtPrecio.getText().trim()));
        libro.setPorcentajeIva(txtIva.getText().isEmpty() ? 0.0 : Double.parseDouble(txtIva.getText().trim()));
        libro.setCantidadDisponible(txtCantidad.getText().isEmpty() ? 0 : Integer.parseInt(txtCantidad.getText().trim()));
        libro.setFormato((FormatoLibro) cmbFormato.getSelectedItem());
        return libro;
    }

    public void actualizarTabla(List<Libro> lista, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        if (lista != null) {
            for (Libro l : lista) {
                modeloTabla.addRow(new Object[] {
                        l.getIsbn(),
                        l.getTitulo(),
                        l.getAutor(),
                        l.getPrecio(),
                        l.getCantidadDisponible(),
                        l.getFormato()
                });
            }
        }
    }

    private void cargarSeleccionEnFormulario(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JComboBox<FormatoLibro> cmbFormato,
            JTable tblLibros, DefaultTableModel modeloTabla) {
        int fila = tblLibros.getSelectedRow();
        if (fila >= 0) {
            String isbn = modeloTabla.getValueAt(fila, 0).toString();
            Libro libro = controladorLibro.buscar(isbn);
            if (libro != null) {
                txtISBN.setText(libro.getIsbn());
                txtISBN.setEditable(false);
                txtTitulo.setText(libro.getTitulo());
                txtAutor.setText(libro.getAutor());
                txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
                txtCategoria.setText(libro.getCategoria());
                txtEditorial.setText(libro.getEditorial());
                txtPaginas.setText(String.valueOf(libro.getNumeroPaginas()));
                txtPrecio.setText(String.valueOf(libro.getPrecio()));
                txtIva.setText(String.valueOf(libro.getPorcentajeIva()));
                txtCantidad.setText(String.valueOf(libro.getCantidadDisponible()));
                cmbFormato.setSelectedItem(libro.getFormato());
            }
        }
    }

    private void limpiarCampos(
            JTextField txtISBN, JTextField txtTitulo, JTextField txtAutor,
            JTextField txtAnio, JTextField txtCategoria, JTextField txtEditorial,
            JTextField txtPaginas, JTextField txtPrecio, JTextField txtIva,
            JTextField txtCantidad, JTable tblLibros) {
        txtISBN.setText("");
        txtISBN.setEditable(true);
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtIva.setText(IVA_POR_DEFECTO);
        txtCantidad.setText("");
        tblLibros.clearSelection();
    }
}

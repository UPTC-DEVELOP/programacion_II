package co.uptc.edu.gui.libro;


import co.uptc.edu.libro.modelo.Categoria;
import co.uptc.edu.libro.modelo.Formato;
import co.uptc.edu.libro.modelo.Libro;

public class DialogoCrearLibro extends DialogoCentralLibro {

    public DialogoCrearLibro(Evento evento, String tituloDialogo, boolean isCrear) {
        super(evento, tituloDialogo, isCrear);
    }

    @Override
    public void asignarComandoBotones() {
        btnGuardar.setActionCommand(Evento.GUARDAR_LIBRO);
        btnCerrar.setActionCommand(Evento.CANCELAR_CREACION_LIBRO);
    }

    public Libro capturarDatos() throws NumberFormatException, IllegalArgumentException {
        if (txIsbn.getText().trim().isEmpty() || txTitulo.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Los campos obligatorios (ISBN, Título) no pueden estar vacíos.");
        }

        return new Libro(
            txIsbn.getText().trim(),
            txTitulo.getText().trim(),
            txAutor.getText().trim(),
            Integer.parseInt(txFecha.getText().trim()),
            (Categoria) cbxCategoria.getSelectedItem(),
            txEditorial.getText().trim(),
            Integer.parseInt(txPaginas.getText().trim()),
            Double.parseDouble(txPrecio.getText().trim()),
            Integer.parseInt(txCantidad.getText().trim()),
            (Formato) cbxFormato.getSelectedItem()
        );
    }
}
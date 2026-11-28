package co.edu.uptc.gui;

import java.awt.event.ActionListener;

import co.edu.uptc.util.Comandos;

import co.edu.uptc.model.Categoria;
import co.edu.uptc.model.Formato;
import co.edu.uptc.model.Libro;

public class DialogoCrearLibro extends DialogoCentralLibro {

    public DialogoCrearLibro(ActionListener evento, String tituloDialogo, boolean isCrear) {
        super(evento, tituloDialogo, isCrear);
    }

    @Override
    public void asignarComandoBotones() {
        btnGuardar.setActionCommand(Comandos.GUARDAR_LIBRO);
        btnCerrar.setActionCommand(Comandos.CANCELAR_CREACION_LIBRO);
    }

    @Override
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
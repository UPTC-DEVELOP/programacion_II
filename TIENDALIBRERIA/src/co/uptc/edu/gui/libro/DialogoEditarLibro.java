package co.uptc.edu.gui.libro;

import co.uptc.edu.libro.modelo.Categoria;
import co.uptc.edu.libro.modelo.Formato;
import co.uptc.edu.libro.modelo.Libro;

public class DialogoEditarLibro extends DialogoCentralLibro {

        private boolean guardado = false;

        public DialogoEditarLibro(Evento evento, String tituloDialogo, boolean isCrear) {
            super(evento, tituloDialogo, isCrear);
            // Bloquear el ISBN para que no pueda ser modificado
            txIsbn.setEnabled(false); 
        }

        @Override
        public void asignarComandoBotones() {
            btnGuardar.setActionCommand(Evento.ACTUALIZAR_LIBRO);
            btnCerrar.setActionCommand(Evento.CANCELAR_LIBRO);
            btnGuardar.setActionCommand(Evento.GUARDAR_ACTUALIZACION);
            btnCerrar.setActionCommand(Evento.CANCELAR_LIBRO);
            }
        

        // Método para precargar los datos actuales del libro en las casillas
        public void cargarDatosLibro(Libro libro) {
            txIsbn.setText(libro.getIsbn());
            txTitulo.setText(libro.getTituloLibro());
            txAutor.setText(libro.getAutor());
            txFecha.setText(String.valueOf(libro.getFechaPublicacion()));
            txEditorial.setText(libro.getEditorial());
            txPaginas.setText(String.valueOf(libro.getPaginas()));
            txPrecio.setText(String.valueOf(libro.getPrecioVenta()));
            txCantidad.setText(String.valueOf(libro.getCantidadDisponible()));
            cbxCategoria.setSelectedItem(libro.getCategoria());
            cbxFormato.setSelectedItem(libro.getFormato());
        }

        // Método para capturar los datos modificados
        public Libro capturarDatos() throws NumberFormatException, IllegalArgumentException {
            if (txTitulo.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("El título no puede estar vacío.");
            }
            
            guardado = true;
            return new Libro(
                txIsbn.getText().trim(), // Mantiene el ISBN original aunque esté deshabilitado
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

        public boolean isGuardado() {
            return guardado;
        }
}

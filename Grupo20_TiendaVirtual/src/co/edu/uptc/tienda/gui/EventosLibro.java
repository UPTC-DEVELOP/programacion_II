package co.edu.uptc.tienda.gui;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.negocio.ControlLibro;
import co.edu.uptc.tienda.modelo.Libro;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventosLibro implements ActionListener {

    public final static String REGISTRAR = "Registrar";
    public final static String ACTUALIZAR = "Actualizar";
    public final static String ELIMINAR = "Eliminar";
    public final static String LISTAR = "Listar";
    public final static String LIMPIAR = "Limpiar";

    private VentanaLibro ventana;
    private ControlLibro controlLibro;

    public EventosLibro(VentanaLibro ventana, ControlLibro controlLibro) {
        this.ventana = ventana;
        this.controlLibro = controlLibro;

        ventana.getBtnRegistrar().addActionListener(this);
        ventana.getBtnActualizar().addActionListener(this);
        ventana.getBtnEliminar().addActionListener(this);
        ventana.getBtnListar().addActionListener(this);
        ventana.getBtnLimpiar().addActionListener(this);

        if (controlLibro.getAvisoCarga() != null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    controlLibro.getAvisoCarga(),
                    "Libros",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void avisarPersistencia(LibroExcepcion ex) {

        JOptionPane.showMessageDialog(
                ventana,
                ex.getMessage(),
                "Libros",
                JOptionPane.WARNING_MESSAGE
        );

        ventana.limpiarCampos();
    }

    
    public void actionPerformed(ActionEvent e) {

        String evento = e.getActionCommand();

        if (evento.equals(REGISTRAR)) {
            registrar();
        }

        else if (evento.equals(ACTUALIZAR)) {
            actualizar();
        }

        else if (evento.equals(ELIMINAR)) {
            eliminar();
        }

        else if (evento.equals(LISTAR)) {
            listar();
        }

        else if (evento.equals(LIMPIAR)) {
            ventana.limpiarCampos();
        }
    }

    private void registrar() {

        try {

            String isbn = ventana.getTxtIsbn().getText().trim();
            String titulo = ventana.getTxtTitulo().getText().trim();
            String autor = ventana.getTxtAutor().getText().trim();
            String editorial = ventana.getTxtEditorial().getText().trim();

            String anioTexto = ventana.getTxtAnio().getText().trim();
            String paginasTexto = ventana.getTxtPaginas().getText().trim();
            String precioTexto = ventana.getTxtPrecio().getText().trim();
            String ivaTexto = ventana.getTxtIva().getText().trim();
            String stockTexto = ventana.getTxtStock().getText().trim();

            String formato = ventana.getCmbFormato()
                    .getSelectedItem()
                    .toString();

            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()
                    || editorial.isEmpty() || anioTexto.isEmpty()
                    || paginasTexto.isEmpty() || precioTexto.isEmpty()
                    || ivaTexto.isEmpty() || stockTexto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Todos los campos son obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (isbn.length() != 13) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "El ISBN debe tener 13 caracteres.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (controlLibro.buscarLibro(isbn) != null) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Ya existe un libro registrado con ese ISBN.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int anio = Integer.parseInt(anioTexto);
            int paginas = Integer.parseInt(paginasTexto);
            double precio = Double.parseDouble(precioTexto);
            double iva = Double.parseDouble(ivaTexto);
            int stock = Integer.parseInt(stockTexto);

            if (anio < 1 || anio > 9999) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "El año de publicación no es válido.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (paginas <= 0) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "El número de páginas debe ser mayor que 0.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (precio <= 0) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "El precio debe ser mayor que 0.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (iva != 5 && iva != 19) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "El IVA solamente puede ser 5% o 19%.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (stock < 0) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "La cantidad disponible no puede ser negativa.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Libro libro = new Libro(
                    isbn,
                    titulo,
                    autor,
                    anio,
                    editorial,
                    paginas,
                    precio,
                    iva,
                    stock,
                    formato
            );

            boolean registrado = controlLibro.registrarLibro(libro);

            if (registrado) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Libro registrado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                ventana.limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        ventana,
                        "No fue posible registrar el libro.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Los campos de año, páginas, precio, IVA y stock "
                    + "deben contener valores numéricos válidos.",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (LibroExcepcion ex) {

            avisarPersistencia(ex);
        }
    }

    private void actualizar() {

        String isbn = ventana.getTxtIsbn().getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Debe ingresar el ISBN del libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Libro libro = controlLibro.buscarLibro(isbn);

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "No existe un libro registrado con ese ISBN.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String titulo = ventana.getTxtTitulo().getText().trim();
        String autor = ventana.getTxtAutor().getText().trim();
        String anioTexto = ventana.getTxtAnio().getText().trim();
        String editorial = ventana.getTxtEditorial().getText().trim();
        String paginasTexto = ventana.getTxtPaginas().getText().trim();
        String precioTexto = ventana.getTxtPrecio().getText().trim();
        String ivaTexto = ventana.getTxtIva().getText().trim();
        String stockTexto = ventana.getTxtStock().getText().trim();
        String formato = (String) ventana.getCmbFormato().getSelectedItem();

        if (titulo.isEmpty() || autor.isEmpty() || anioTexto.isEmpty()
                || editorial.isEmpty() || paginasTexto.isEmpty()
                || precioTexto.isEmpty() || ivaTexto.isEmpty()
                || stockTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Todos los campos son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {

            int anio = Integer.parseInt(anioTexto);
            int paginas = Integer.parseInt(paginasTexto);
            double precio = Double.parseDouble(precioTexto);
            double iva = Double.parseDouble(ivaTexto);
            int stock = Integer.parseInt(stockTexto);

            if (anio < 1 || anio > 9999) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "El año de publicación debe tener un valor válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (paginas <= 0) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "El número de páginas debe ser mayor que 0.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (precio <= 0) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "El precio debe ser mayor que 0.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (iva != 5 && iva != 19) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "El IVA solamente puede ser 5% o 19%.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (stock < 0) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "La cantidad disponible no puede ser negativa.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean actualizado = controlLibro.actualizarLibro(
                    isbn,
                    titulo,
                    autor,
                    anio,
                    editorial,
                    paginas,
                    precio,
                    iva,
                    stock,
                    formato
            );

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Libro actualizado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                ventana.limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        ventana,
                        "No fue posible actualizar el libro.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Los campos numéricos deben contener valores válidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (LibroExcepcion e) {

            avisarPersistencia(e);
        }
    }
    private void eliminar() {

        String isbn = ventana.getTxtIsbn().getText().trim();
        String titulo = ventana.getTxtTitulo().getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Debe ingresar el ISBN del libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Debe ingresar el título del libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Libro libro = controlLibro.buscarLibro(isbn);

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "No existe un libro con el ISBN ingresado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!libro.getTitulo().equalsIgnoreCase(titulo)) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "El título no coincide con el libro registrado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                ventana,
                "¿Está seguro de eliminar el libro?\n\n"
                        + "ISBN: " + libro.getIsbn() + "\n"
                        + "Título: " + libro.getTitulo(),
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            try {

                boolean eliminado = controlLibro.eliminarLibro(isbn);

                if (eliminado) {

                    JOptionPane.showMessageDialog(
                            ventana,
                            "Libro eliminado correctamente.",
                            "Eliminación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    ventana.limpiarCampos();

                } else {

                    JOptionPane.showMessageDialog(
                            ventana,
                            "No fue posible eliminar el libro.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (LibroExcepcion e) {

                avisarPersistencia(e);
            }
        }
    }
    private void listar() {

        List<Libro> libros = new ArrayList<>(controlLibro.listarLibros());

        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "No hay libros registrados.",
                    "Listado de libros",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        libros.sort(Comparator.comparing(Libro::getIsbn));

        String[] columnas = {
            "ISBN",
            "Título",
            "Autor",
            "Precio",
            "Cantidad"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        for (Libro libro : libros) {

            Object[] fila = {
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                "$ " + libro.getPrecio(),
                libro.getStock()
            };

            modelo.addRow(fila);
        }

        JTable tabla = new JTable(modelo);

        tabla.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new java.awt.Dimension(750, 300));

        JOptionPane.showMessageDialog(
                ventana,
                scroll,
                "Listado de libros",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

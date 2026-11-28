package co.edu.uptc.tienda.gui;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.negocio.ControlCalificacion;
import co.edu.uptc.tienda.negocio.ControlLibro;
import co.edu.uptc.tienda.modelo.Calificacion;
import co.edu.uptc.tienda.modelo.Libro;


import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
    public final static String CALIFICAR = "Calificar";
    public final static String CALIFICACIONES = "Ver calificaciones";

    private VentanaLibro ventana;
    private ControlLibro controlLibro;
    private ControlCalificacion controlCalificacion;

    public EventosLibro(VentanaLibro ventana, ControlLibro controlLibro) {
        this.ventana = ventana;
        this.controlLibro = controlLibro;

        ventana.getBtnRegistrar().addActionListener(this);
        ventana.getBtnActualizar().addActionListener(this);
        ventana.getBtnEliminar().addActionListener(this);
        ventana.getBtnListar().addActionListener(this);
        ventana.getBtnLimpiar().addActionListener(this);
        ventana.getBtnCalificar().addActionListener(this);
        ventana.getBtnCalificaciones().addActionListener(this);

        if (controlLibro.getAvisoCarga() != null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    controlLibro.getAvisoCarga(),
                    "Libros",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        controlCalificacion = new ControlCalificacion(controlLibro);

        if (controlCalificacion.getAvisoCarga() != null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    controlCalificacion.getAvisoCarga(),
                    "Calificaciones",
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

        else if (evento.equals(CALIFICAR)) {
            calificar();
        }

        else if (evento.equals(CALIFICACIONES)) {
            verCalificaciones();
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

            String categoria = ventana.getCmbCategoria()
                    .getSelectedItem()
                    .toString();

            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()
                    || editorial.isEmpty() || anioTexto.isEmpty()
                    || paginasTexto.isEmpty() || precioTexto.isEmpty()
                    || ivaTexto.isEmpty() || stockTexto.isEmpty()) {

            	//textos que salen a datos erroneos o incompletos
            	
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
                    categoria,
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
        String categoria = (String) ventana.getCmbCategoria().getSelectedItem();

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
                    categoria,
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

        try {

            if (controlLibro.tieneVentas(isbn)) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "No se puede eliminar el libro porque tiene ventas asociadas.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

        } catch (LibroExcepcion e) {

            JOptionPane.showMessageDialog(
                    ventana,
                    e.getMessage(),
                    "Libros",
                    JOptionPane.WARNING_MESSAGE
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
            "Categoría",
            "Precio",
            "Cantidad",
            "Calificación"
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
                libro.getCategoria() == null ? "Sin categoría" : libro.getCategoria(),
                String.format("$%,.2f", libro.getPrecio()),
                libro.getStock(),
                textoCalificacion(libro.getIsbn())
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

    private String textoCalificacion(String isbn) {

        int cantidad = controlCalificacion.listarPorLibro(isbn).size();

        if (cantidad == 0) {
            return "Sin calificar";
        }

        return String.format("%.1f de 5 (%d)", controlCalificacion.calcularPromedio(isbn), cantidad);
    }

    private Libro libroDelFormulario() {

        String isbn = ventana.getTxtIsbn().getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Debe ingresar el ISBN del libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        Libro libro = controlLibro.buscarLibro(isbn);

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "No existe un libro registrado con ese ISBN.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return libro;
    }

    private void calificar() {

        Libro libro = libroDelFormulario();

        if (libro == null) {
            return;
        }

        JTextField txtCliente = new JTextField(12);
        JComboBox<Integer> cmbPuntaje = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
        JTextArea txtComentario = new JTextArea(4, 28);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);

        JPanel datos = new JPanel(new GridLayout(2, 2, 8, 8));
        datos.add(new JLabel("ID del cliente que compró el libro:"));
        datos.add(txtCliente);
        datos.add(new JLabel("Calificación (1 a 5):"));
        datos.add(cmbPuntaje);

        JPanel formulario = new JPanel(new BorderLayout(8, 8));
        formulario.add(new JLabel("Libro: " + libro.getTitulo()), BorderLayout.NORTH);
        formulario.add(datos, BorderLayout.CENTER);
        JPanel panelComentario = new JPanel(new BorderLayout(4, 4));
        panelComentario.add(new JLabel("Comentario (opcional, máximo "
                + ControlCalificacion.LARGO_COMENTARIO + " caracteres):"), BorderLayout.NORTH);
        panelComentario.add(new JScrollPane(txtComentario), BorderLayout.CENTER);
        formulario.add(panelComentario, BorderLayout.SOUTH);

        int respuesta = JOptionPane.showConfirmDialog(
                ventana,
                formulario,
                "Calificar libro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            controlCalificacion.calificar(
                    libro.getIsbn(),
                    txtCliente.getText().trim(),
                    (Integer) cmbPuntaje.getSelectedItem(),
                    txtComentario.getText()
            );

            JOptionPane.showMessageDialog(
                    ventana,
                    "Calificación guardada correctamente.",
                    "Calificaciones",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (LibroExcepcion e) {

            JOptionPane.showMessageDialog(
                    ventana,
                    e.getMessage(),
                    "Calificaciones",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void verCalificaciones() {

        Libro libro = libroDelFormulario();

        if (libro == null) {
            return;
        }

        List<Calificacion> calificaciones = controlCalificacion.listarPorLibro(libro.getIsbn());

        if (calificaciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "El libro \"" + libro.getTitulo() + "\" todavía no tiene calificaciones.",
                    "Calificaciones",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String[] columnas = {
            "Cliente",
            "Calificación",
            "Comentario",
            "Fecha"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        for (Calificacion calificacion : calificaciones) {

            modelo.addRow(new Object[]{
                calificacion.getIdCliente(),
                calificacion.getPuntaje() + " de 5",
                calificacion.getComentario(),
                calificacion.getFecha()
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(380);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new java.awt.Dimension(750, 250));

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.add(new JLabel(libro.getTitulo() + ": "
                + textoCalificacion(libro.getIsbn())), BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                ventana,
                contenido,
                "Calificaciones",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IGestionResena;
import co.edu.uptc.interfaces.IReviewRepository.Review;
import co.edu.uptc.negocio.GestionResena;

/**
 * PANEL DE RESEÑAS Y CALIFICACIONES DE UN LIBRO  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Muestra las reseñas de un libro, su promedio y permite crear, editar y
 * eliminar reseñas (RF19 y RF20).
 *
 * Es AUTÓNOMO: recibe IGestionResena por constructor y atiende sus propios
 * botones, por lo que no requiere modificar la clase Evento. Quien lo use
 * (VentanaPrincipal, un diálogo, etc.) solo debe llamar a
 * {@link #mostrarLibro(String, String, int)}.
 *
 * SOLID: SRP (solo interfaz de reseñas) y DIP (depende de IGestionResena).
 *
 * @author: Equipo de desarrollo grupo 7 UPTC
 */
public class PanelResenas extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient IGestionResena servicio;

    private final JLabel lblTitulo = Estilos.etiqueta("Seleccione un libro");
    private final JLabel lblPromedio = Estilos.etiqueta(" ");
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"Cliente", "Fecha", "Calificación", "Comentario"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final transient List<Review> mostradas = new ArrayList<>();

    private JButton btnEditar;
    private JButton btnEliminar;
    private String isbnActual;
    private int idClienteActual;

    public PanelResenas(IGestionResena servicio) {
        this.servicio = servicio;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));
        setBackground(Estilos.GRIS_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitulo.setFont(Estilos.FUENTE_TITULO);
        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.setOpaque(false);
        norte.add(lblTitulo);
        norte.add(lblPromedio);
        add(norte, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(Estilos.FUENTE);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(380);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        sur.setOpaque(false);
        sur.add(Estilos.boton("Nueva reseña", "RESENA_CREAR", e -> crear()));
        btnEditar = Estilos.boton("Editar", "RESENA_EDITAR", e -> editar());
        btnEliminar = Estilos.boton("Eliminar", "RESENA_ELIMINAR", e -> eliminar());
        sur.add(btnEditar);
        sur.add(btnEliminar);
        add(sur, BorderLayout.SOUTH);

        // Editar y Eliminar solo se muestran sobre una reseña propia.
        actualizarBotonesPropios();
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotonesPropios();
            }
        });
    }

    /** Muestra Editar/Eliminar solo si la fila seleccionada es del cliente en sesión. */
    private void actualizarBotonesPropios() {
        int fila = tabla.getSelectedRow();
        boolean propia = fila >= 0 && fila < mostradas.size()
                && mostradas.get(fila).idCliente() == idClienteActual;
        btnEditar.setVisible(propia);
        btnEliminar.setVisible(propia);
    }

    /** Carga las reseñas de un libro para el cliente con sesión activa. */
    public void mostrarLibro(String isbn, String titulo, int idCliente) {
        this.isbnActual = isbn;
        this.idClienteActual = idCliente;
        lblTitulo.setText("Reseñas de: " + titulo);
        recargar();
    }

    private void recargar() {
        modelo.setRowCount(0);
        mostradas.clear();
        actualizarBotonesPropios();
        if (isbnActual == null) {
            return;
        }
        try {
            mostradas.addAll(servicio.listarPorLibro(isbnActual));
            java.util.Map<Integer, String> nombres = servicio.nombresAutores(isbnActual);
            java.util.Map<Integer, java.time.LocalDateTime> fechas =
                    servicio.fechasResenas(isbnActual);
            // Más reciente primero; a igual fecha (o sin fecha) gana el id más alto.
            mostradas.sort(java.util.Comparator
                    .comparing((Review r) -> fechas.getOrDefault(r.id(),
                            java.time.LocalDateTime.MIN))
                    .thenComparingInt(Review::id).reversed());
            java.time.format.DateTimeFormatter formato =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Review r : mostradas) {
                java.time.LocalDateTime fecha = fechas.get(r.id());
                modelo.addRow(new Object[] {
                        nombres.getOrDefault(r.idCliente(), "Cliente #" + r.idCliente()),
                        fecha == null ? "-" : fecha.format(formato),
                        "★".repeat(r.calificacion())
                        + " (" + r.calificacion() + ")", r.comentario()});
            }
            double promedio = servicio.promedioCalificacion(isbnActual);
            lblPromedio.setText(mostradas.isEmpty() ? "Sin reseñas todavía"
                    : "Promedio: " + promedio + " / 5  (" + mostradas.size() + " reseñas)");
        } catch (ReglaNegocioException e) {
            error(e.getMessage());
        }
    }

    private void crear() {
        if (isbnActual == null) {
            error("Seleccione primero un libro.");
            return;
        }
        Object[] datos = pedirDatos(5, "");
        if (datos == null) {
            return;
        }
        try {
            servicio.crear(idClienteActual, isbnActual, (int) datos[0], (String) datos[1]);
            recargar();
        } catch (ReglaNegocioException e) {
            error(e.getMessage());
        }
    }

    private void editar() {
        Review r = seleccionada();
        if (r == null) {
            return;
        }
        Object[] datos = pedirDatos(r.calificacion(), r.comentario());
        if (datos == null) {
            return;
        }
        try {
            servicio.actualizar(idClienteActual, isbnActual, r.id(),
                    (int) datos[0], (String) datos[1]);
            recargar();
        } catch (ReglaNegocioException e) {
            error(e.getMessage());
        }
    }

    private void eliminar() {
        Review r = seleccionada();
        if (r == null) {
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la reseña seleccionada? Esta acción no se puede deshacer.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            servicio.eliminar(idClienteActual, isbnActual, r.id());
            recargar();
        } catch (ReglaNegocioException e) {
            error(e.getMessage());
        }
    }

    /** Formulario modal; devuelve {calificacion, comentario} o null si se cancela. */
    private Object[] pedirDatos(int calificacionInicial, String comentarioInicial) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(calificacionInicial,
                GestionResena.CALIFICACION_MIN, GestionResena.CALIFICACION_MAX, 1));
        JTextArea area = new JTextArea(comentarioInicial, 5, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JPanel form = new JPanel(new BorderLayout(5, 5));
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Calificación (1-5):"));
        fila.add(spinner);
        form.add(fila, BorderLayout.NORTH);
        form.add(new JScrollPane(area), BorderLayout.CENTER);
        int opcion = JOptionPane.showConfirmDialog(this, form, "Reseña",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }
        return new Object[] {(Integer) spinner.getValue(), area.getText()};
    }

    private Review seleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= mostradas.size()) {
            error("Seleccione una reseña de la tabla.");
            return null;
        }
        return mostradas.get(fila);
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Reseñas", JOptionPane.WARNING_MESSAGE);
    }
}

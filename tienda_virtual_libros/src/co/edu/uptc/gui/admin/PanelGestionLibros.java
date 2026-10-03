package co.edu.uptc.gui.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import co.edu.uptc.gui.eventos.admin.EventoAdmin;
import co.edu.uptc.modelo.dto.LibroDto;


/**
 * CLASE PanelGestionLibros  (paquete: gui)  extends JPanel
 * ---------------------------------------------------------------------------
 * Pantalla "Administración de Libros" (Manage Books) del prototipo:
 *   NORTE : caja "Búsqueda por ISBN o Título" + botón Buscar  (RF04)
 *   CENTRO: JTable con ISBN | Título | Autor | Precio | Stock | Acciones
 *           y en cada fila los botones "Editar" y "Eliminar" (RF02 / RF03).
 *
 * TRUCO DE LOS BOTONES EN LA TABLA: un JTable no contiene componentes reales,
 * solo "dibuja" cada celda con un renderer. Por eso (1) un renderer pinta un
 * JButton en las dos últimas columnas y (2) un MouseListener detecta en qué
 * fila/columna hizo clic el usuario y avisa al controlador.
 */
class PanelGestionLibros extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int COL_ISBN = 0;
    private static final int COL_EDITAR = 5;
    private static final int COL_ELIMINAR = 6;
    private static final String[] COLUMNAS =
            {"ISBN", "Título", "Autor", "Precio", "Stock", "Acciones", ""};

    private final CampoConHint campoBusqueda = new CampoConHint("Búsqueda por ISBN o Título", 30);
    private final JButton botonBuscar =
            Estilos.boton("Buscar", EventoAdmin.BUSCAR_LIBRO.name(), null);
    private final DefaultTableModel modelo;
    private final JTable tabla;

    /** Controlador al que se le avisa cuando se pulsa Editar/Eliminar en la tabla. */
    private ActionListener escuchador;

    PanelGestionLibros() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Estilos.GRIS_FONDO);

        // ----- Barra de búsqueda -----
        JPanel barra = new JPanel(new BorderLayout(8, 0));
        barra.setOpaque(false);
        barra.add(campoBusqueda, BorderLayout.CENTER);
        barra.add(botonBuscar, BorderLayout.EAST);
        add(barra, BorderLayout.NORTH);

        // ----- Tabla -----
        // El modelo se sobrescribe para que NINGUNA celda sea editable a mano.
        modelo = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(26);
        tabla.setFont(Estilos.FUENTE);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Las dos últimas columnas se dibujan como botones.
        tabla.getColumnModel().getColumn(COL_EDITAR).setCellRenderer(new RenderizadorBoton("Editar"));
        tabla.getColumnModel().getColumn(COL_ELIMINAR).setCellRenderer(new RenderizadorBoton("Eliminar"));
        // Anchos de columna: las de texto se reparten el espacio sobrante, las de botón son fijas.
        int[] anchos = {115, 190, 150, 95, 100, 80, 85};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        tabla.getColumnModel().getColumn(COL_EDITAR).setMaxWidth(80);
        tabla.getColumnModel().getColumn(COL_ELIMINAR).setMaxWidth(85);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alHacerClicEnTabla(e);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.GRIS_BORDE));
        add(scroll, BorderLayout.CENTER);
    }

    // ------------------------- API para la ventana principal -------------------------

    void registrarEscuchador(ActionListener nuevo) {
        this.escuchador = nuevo;
        botonBuscar.addActionListener(nuevo);
        campoBusqueda.setActionCommand(EventoAdmin.BUSCAR_LIBRO.name());
        campoBusqueda.addActionListener(nuevo);        // Enter en la caja también busca
    }

    String getTextoBusqueda() {
        return campoBusqueda.getText();
    }

    /** ISBN de la fila seleccionada (la que se pulsó), o null si no hay. */
    String getIsbnSeleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : String.valueOf(modelo.getValueAt(fila, COL_ISBN));
    }

    /** Reemplaza el contenido de la tabla por la lista recibida. */
    void mostrarLibros(List<LibroDto> libros) {
        modelo.setRowCount(0);
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        for (LibroDto l : libros) {
            // RF04: el stock en 0 se muestra como "Agotado"; con stock > 0 es "Disponible".
            String stock = l.getStock() > 0 ? String.valueOf(l.getStock()) : "0 (Agotado)";
            modelo.addRow(new Object[] {
                l.getIsbn(), l.getTitulo(), String.join(", ", l.getAutores()),
                moneda.format(l.getPrecioVenta()), stock, "Editar", "Eliminar"
            });
        }
    }

    // ------------------------- Manejo de clics en la tabla -------------------------

    private void alHacerClicEnTabla(MouseEvent e) {
        int fila = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());
        if (fila < 0 || escuchador == null) {
            return;
        }
        tabla.setRowSelectionInterval(fila, fila);       // así getIsbnSeleccionado() funciona
        String comando = null;
        if (columna == COL_EDITAR) {
            comando = EventoAdmin.EDITAR_LIBRO.name();
        } else if (columna == COL_ELIMINAR) {
            comando = EventoAdmin.ELIMINAR_LIBRO.name();
        }
        if (comando != null) {
            escuchador.actionPerformed(new ActionEvent(tabla, ActionEvent.ACTION_PERFORMED, comando));
        }
    }

    /** Renderer: devuelve un JButton para que la celda "parezca" un botón. */
    private static class RenderizadorBoton extends JButton implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        RenderizadorBoton(String texto) {
            super(texto);
            setBackground(Estilos.GRIS_BOTON);
            setFont(Estilos.FUENTE);
            setMargin(new java.awt.Insets(2, 2, 2, 2));   // sin relleno extra: el texto no se corta
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                                                       boolean foco, int fila, int columna) {
            return this;
        }
    }
}


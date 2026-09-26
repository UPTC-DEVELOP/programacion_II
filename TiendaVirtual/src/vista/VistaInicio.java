package vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Libro;
import modelo.Usuario;
import util.Estilos;
public class VistaInicio extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton botonAnadirLibro;
    private JButton botonQuitarLibro;
    private JButton botonGestionarClientes;
    private JButton botonCerrarSesion;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    public VistaInicio(Usuario usuario) {
        setTitle("Librería Digital");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Estilos.AZUL_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel titulo = new JLabel("LIBRERÍA DIGITAL");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        botonCerrarSesion = new JButton("Cerrar sesión");
        Estilos.botonSecundario(botonCerrarSesion);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrarSesion, BorderLayout.EAST);
        JLabel bienvenida = new JLabel(
                "Bienvenido, " + usuario.getNombre()
                        + " | Cliente " + usuario.getTipoCliente());
        bienvenida.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bienvenida.setForeground(Estilos.TEXTO);
        bienvenida.setBorder(BorderFactory.createEmptyBorder(14, 18, 8, 18));
        String[] columnas = { "Título", "Autor", "Formato", "Precio" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setRowHeight(25);
        tablaLibros.setForeground(Color.BLACK);
        tablaLibros.setBackground(Color.WHITE);
        tablaLibros.setSelectionBackground(Estilos.CELESTE);
        tablaLibros.setSelectionForeground(Color.BLACK);
        tablaLibros.getTableHeader().setBackground(Estilos.CELESTE);
        tablaLibros.getTableHeader().setForeground(Color.BLACK);
        tablaLibros.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));
        JScrollPane desplazamiento = new JScrollPane(tablaLibros);
        desplazamiento.setBorder(
                BorderFactory.createLineBorder(Estilos.AZUL_VIVO));
        botonAnadirLibro = new JButton("Añadir libro");
        botonQuitarLibro = new JButton("Quitar libro");
        botonGestionarClientes = new JButton("Gestionar clientes");
        Estilos.botonPrincipal(botonAnadirLibro);
        Estilos.botonSecundario(botonQuitarLibro);
        Estilos.botonSecundario(botonGestionarClientes);
        JPanel acciones = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 8));
        acciones.setBackground(Estilos.FONDO);
        acciones.add(botonAnadirLibro);
        acciones.add(botonQuitarLibro);
        acciones.add(botonGestionarClientes);
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Estilos.FONDO);
        centro.setBorder(
                BorderFactory.createEmptyBorder(0, 18, 14, 18));
        centro.add(bienvenida, BorderLayout.NORTH);
        centro.add(desplazamiento, BorderLayout.CENTER);
        centro.add(acciones, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }
    public JButton getBotonAnadirLibro() {
        return botonAnadirLibro;
    }
    public JButton getBotonQuitarLibro() {
        return botonQuitarLibro;
    }
    public JButton getBotonGestionarClientes() {
        return botonGestionarClientes;
    }
    public JButton getBotonCerrarSesion() {
        return botonCerrarSesion;
    }
    public int getFilaSeleccionada() {
        return tablaLibros.getSelectedRow();
    }
    public void limpiarLibros() {
        modeloTabla.setRowCount(0);
    }
    public void mostrarLibro(Libro libro) {
        modeloTabla.addRow(new Object[] {
                libro.getTitulo(),
                libro.getAutor(),
                libro.getFormato(),
                String.format("$%,d", libro.getPrecio())
                        .replace(',', '.')
        });
    }
}

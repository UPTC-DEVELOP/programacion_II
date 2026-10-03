package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import modelo.ItemCarrito;
import modelo.Libro;
import util.Estilos;

public class VistaCarrito extends JFrame {
    private static final long serialVersionUID = 1L;

    private JComboBox<Libro> comboLibro;
    private JTextField campoCantidad;
    private JButton botonAgregar;
    private JButton botonActualizar;
    private JButton botonEliminar;
    private JButton botonLimpiar;
    private JButton botonCerrar;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel etiquetaTotal;

    public VistaCarrito() {
        setTitle("Carrito de compras");
        setSize(760, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Estilos.AZUL_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("CARRITO DE COMPRAS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        botonCerrar = new JButton("Cerrar");
        Estilos.botonSecundario(botonCerrar);

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrar, BorderLayout.EAST);

        comboLibro = new JComboBox<Libro>();
        comboLibro.setRenderer((lista, valor, indice, seleccionado, foco) -> {
            JLabel etiqueta = new JLabel();
            etiqueta.setOpaque(true);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            if (valor != null) {
                etiqueta.setText(valor.getTitulo() + " - " + valor.getAutor());
            }
            if (seleccionado) {
                etiqueta.setBackground(Estilos.CELESTE);
                etiqueta.setForeground(Color.BLACK);
            } else {
                etiqueta.setBackground(Color.WHITE);
                etiqueta.setForeground(Estilos.TEXTO);
            }
            return etiqueta;
        });

        campoCantidad = new JTextField(10);
        Estilos.campoTexto(campoCantidad);

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));
        formulario.setBackground(Estilos.FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(18, 18, 10, 18));
        formulario.add(new JLabel("Libro:"));
        formulario.add(comboLibro);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(campoCantidad);

        botonAgregar = new JButton("Agregar");
        botonActualizar = new JButton("Actualizar");
        botonEliminar = new JButton("Eliminar");
        botonLimpiar = new JButton("Limpiar");

        Estilos.botonPrincipal(botonAgregar);
        Estilos.botonSecundario(botonActualizar);
        Estilos.botonSecundario(botonEliminar);
        Estilos.botonClaro(botonLimpiar);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        acciones.setBackground(Estilos.FONDO);
        acciones.add(botonAgregar);
        acciones.add(botonActualizar);
        acciones.add(botonEliminar);
        acciones.add(botonLimpiar);

        String[] columnas = { "Libro", "Cantidad", "Precio", "Subtotal" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setRowHeight(25);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.setSelectionBackground(Estilos.CELESTE);
        tablaCarrito.setSelectionForeground(Color.BLACK);
        tablaCarrito.getTableHeader().setBackground(Estilos.CELESTE);
        tablaCarrito.getTableHeader().setForeground(Color.BLACK);
        tablaCarrito.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane desplazamiento = new JScrollPane(tablaCarrito);
        desplazamiento.setBorder(BorderFactory.createLineBorder(Estilos.AZUL_VIVO));

        etiquetaTotal = new JLabel("Total: $0");
        etiquetaTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiquetaTotal.setForeground(Estilos.AZUL_MARINO);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Estilos.FONDO);
        pie.setBorder(BorderFactory.createEmptyBorder(10, 18, 16, 18));
        pie.add(acciones, BorderLayout.WEST);
        pie.add(etiquetaTotal, BorderLayout.EAST);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Estilos.FONDO);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(desplazamiento, BorderLayout.CENTER);
        centro.add(pie, BorderLayout.SOUTH);

        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    public void cargarLibros(Iterable<Libro> libros) {
        comboLibro.removeAllItems();
        for (Libro libro : libros) {
            comboLibro.addItem(libro);
        }
        comboLibro.setSelectedIndex(comboLibro.getItemCount() > 0 ? 0 : -1);
    }

    public Libro getLibroSeleccionado() {
        return (Libro) comboLibro.getSelectedItem();
    }

    public String getCantidadTexto() {
        return campoCantidad.getText().trim();
    }

    public int getFilaSeleccionada() {
        return tablaCarrito.getSelectedRow();
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void mostrarItem(ItemCarrito item) {
        modeloTabla.addRow(new Object[] {
                item.getLibro().getTitulo(),
                item.getCantidad(),
                formatearDinero(item.getLibro().getPrecio()),
                formatearDinero(item.getSubtotal())
        });
    }

    public void mostrarTotal(int total) {
        etiquetaTotal.setText("Total: " + formatearDinero(total));
    }

    public void limpiarCampos() {
        campoCantidad.setText("");
        tablaCarrito.clearSelection();
        if (comboLibro.getItemCount() > 0) {
            comboLibro.setSelectedIndex(0);
        }
        campoCantidad.requestFocusInWindow();
    }

    public void cargarCantidadSeleccionada() {
        int fila = getFilaSeleccionada();
        if (fila >= 0) {
            campoCantidad.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        }
    }

    public JButton getBotonAgregar() {
        return botonAgregar;
    }

    public JButton getBotonActualizar() {
        return botonActualizar;
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonCerrar() {
        return botonCerrar;
    }

    public JTable getTablaCarrito() {
        return tablaCarrito;
    }

    private String formatearDinero(int valor) {
        return String.format("$%,d", valor).replace(',', '.');
    }
}

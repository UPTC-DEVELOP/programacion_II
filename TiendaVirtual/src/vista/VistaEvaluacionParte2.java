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

import modelo.Pedido;
import modelo.Producto;
import util.Estilos;

public class VistaEvaluacionParte2 extends JFrame {
    private static final long serialVersionUID = 1L;

    private JComboBox<Producto> comboProductos;
    private JLabel valorPrecioBase;
    private JLabel valorStock;
    private JLabel valorDescuento;
    private JLabel valorIVA;
    private JLabel valorPrecioFinal;
    private JTextField campoCantidad;
    private JButton botonRegistrarPedido;
    private JButton botonCancelarPedido;
    private JButton botonCerrar;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public VistaEvaluacionParte2() {
        setTitle("Evaluación Parte 2 - Pedidos e inventario");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Estilos.AZUL_MARINO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("EVALUACIÓN PARTE 2 - GESTIÓN DE PEDIDOS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        botonCerrar = new JButton("Cerrar");
        Estilos.botonSecundario(botonCerrar);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrar, BorderLayout.EAST);

        comboProductos = new JComboBox<Producto>();
        valorPrecioBase = crearValor();
        valorStock = crearValor();
        valorDescuento = crearValor();
        valorIVA = crearValor();
        valorPrecioFinal = crearValor();
        campoCantidad = new JTextField(10);
        Estilos.campoTexto(campoCantidad);

        JPanel datos = new JPanel(new GridLayout(6, 2, 10, 9));
        datos.setBackground(Estilos.FONDO);
        datos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilos.AZUL_VIVO),
                "Producto seleccionado"));
        datos.add(new JLabel("Producto:"));
        datos.add(comboProductos);
        datos.add(new JLabel("Precio base:"));
        datos.add(valorPrecioBase);
        datos.add(new JLabel("Stock disponible:"));
        datos.add(valorStock);
        datos.add(new JLabel("Descuento:"));
        datos.add(valorDescuento);
        datos.add(new JLabel("IVA:"));
        datos.add(valorIVA);
        datos.add(new JLabel("Precio final unitario:"));
        datos.add(valorPrecioFinal);

        JPanel pedido = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pedido.setBackground(Estilos.FONDO);
        pedido.add(new JLabel("Cantidad:"));
        pedido.add(campoCantidad);

        botonRegistrarPedido = new JButton("Registrar Pedido");
        botonCancelarPedido = new JButton("Cancelar Pedido Seleccionado");
        Estilos.botonPrincipal(botonRegistrarPedido);
        Estilos.botonSecundario(botonCancelarPedido);
        pedido.add(botonRegistrarPedido);
        pedido.add(botonCancelarPedido);

        JPanel formulario = new JPanel(new BorderLayout(10, 10));
        formulario.setBackground(Estilos.FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(14, 18, 10, 18));
        formulario.add(datos, BorderLayout.CENTER);
        formulario.add(pedido, BorderLayout.SOUTH);

        String[] columnas = {
                "Producto", "Cantidad", "Precio final", "Total pagado"
        };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(25);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.setSelectionBackground(Estilos.CELESTE);
        tablaHistorial.setSelectionForeground(Color.BLACK);
        tablaHistorial.getTableHeader().setBackground(Estilos.CELESTE);
        tablaHistorial.getTableHeader().setForeground(Color.BLACK);
        tablaHistorial.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        JScrollPane desplazamiento = new JScrollPane(tablaHistorial);
        desplazamiento.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilos.AZUL_VIVO),
                "Historial de pedidos registrados"));

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(Estilos.FONDO);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(desplazamiento, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private JLabel crearValor() {
        JLabel etiqueta = new JLabel("-");
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setForeground(Estilos.AZUL_MARINO);
        return etiqueta;
    }

    public void cargarProductos(Iterable<Producto> productos) {
        comboProductos.removeAllItems();
        for (Producto producto : productos) {
            comboProductos.addItem(producto);
        }
        if (comboProductos.getItemCount() > 0) {
            comboProductos.setSelectedIndex(0);
        }
    }

    public Producto getProductoSeleccionado() {
        return (Producto) comboProductos.getSelectedItem();
    }

    public void seleccionarProducto(Producto producto) {
        comboProductos.setSelectedItem(producto);
    }

    public void mostrarDatosProducto(Producto producto) {
        if (producto == null) {
            valorPrecioBase.setText("-");
            valorStock.setText("-");
            valorDescuento.setText("-");
            valorIVA.setText("-");
            valorPrecioFinal.setText("-");
            return;
        }

        valorPrecioBase.setText(formatearDinero(producto.getPrecioBase()));
        valorStock.setText(String.valueOf(producto.getStock()));
        valorDescuento.setText(formatearPorcentaje(
                producto.getPorcentajeDescuento()));
        valorIVA.setText(formatearPorcentaje(producto.getImpuestoIVA()));
        valorPrecioFinal.setText(formatearDinero(
                producto.calcularPrecioFinal()));
    }

    public String getCantidadTexto() {
        return campoCantidad.getText().trim();
    }

    public void limpiarCantidad() {
        campoCantidad.setText("");
        campoCantidad.requestFocusInWindow();
    }

    public void agregarPedido(Pedido pedido) {
        modeloTabla.addRow(new Object[] {
                pedido.getProducto().getNombre(),
                pedido.getCantidad(),
                formatearDinero(pedido.getProducto().calcularPrecioFinal()),
                formatearDinero(pedido.getTotal())
        });
    }

    public int getFilaSeleccionada() {
        return tablaHistorial.getSelectedRow();
    }

    public void eliminarFila(int fila) {
        modeloTabla.removeRow(fila);
    }

    public JComboBox<Producto> getComboProductos() {
        return comboProductos;
    }

    public JButton getBotonRegistrarPedido() {
        return botonRegistrarPedido;
    }

    public JButton getBotonCancelarPedido() {
        return botonCancelarPedido;
    }

    public JButton getBotonCerrar() {
        return botonCerrar;
    }

    private String formatearDinero(double valor) {
        return String.format("$%,.0f", valor).replace(',', '.');
    }

    private String formatearPorcentaje(double valor) {
        return String.format("%.1f%%", valor);
    }
}

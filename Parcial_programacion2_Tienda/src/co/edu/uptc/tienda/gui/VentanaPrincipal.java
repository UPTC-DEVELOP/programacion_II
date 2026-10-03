package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.eventos.ActualizarCamposListener;
import co.edu.uptc.tienda.eventos.CancelarPedidoListener;
import co.edu.uptc.tienda.eventos.RegistrarPedidoListener;
import co.edu.uptc.tienda.modelo.Producto;

public class VentanaPrincipal extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JLabel lblPrecioBase, lblStock, lblDescuento, lblIVA;
    private JTextField txtCantidad;
    private JButton btnRegistrar, btnCancelar;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal() {
        setTitle("Sistema de Control de Inventario y Ventas - UPTC");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        configurarLayout();
        asignarEventos();
        cargarDatosPrueba();
    }

    private void inicializarComponentes() {
        comboProductos = new JComboBox<>();
        lblPrecioBase = new JLabel("$0.00");
        lblStock = new JLabel("0");
        lblDescuento = new JLabel("0%");
        lblIVA = new JLabel("0%");
        
        txtCantidad = new JTextField(10);
        btnRegistrar = new JButton("Registrar Pedido");
        btnCancelar = new JButton("Cancelar Pedido Seleccionado");

        String[] columnas = {"Producto", "Cantidad", "Total Pagado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(modeloTabla);
    }

    private void configurarLayout() {
        // Panel Superior: Selección y Datos del Producto
        JPanel panelSuperior = new JPanel(new GridLayout(2, 5, 10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        
        panelSuperior.add(new JLabel("Seleccionar Producto:"));
        panelSuperior.add(comboProductos);
        panelSuperior.add(new JLabel("Precio Base:"));
        panelSuperior.add(lblPrecioBase);
        panelSuperior.add(new JLabel("Stock Disponible:"));
        
        panelSuperior.add(new JLabel("Descuento:"));
        panelSuperior.add(lblDescuento);
        panelSuperior.add(new JLabel("IVA:"));
        panelSuperior.add(lblIVA);
        panelSuperior.add(lblStock);

        // Panel Central: Registro de Pedido
        JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Registrar Pedido"));
        panelCentral.add(new JLabel("Cantidad:"));
        panelCentral.add(txtCantidad);
        panelCentral.add(btnRegistrar);

        // Panel Inferior: Historial
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Historial de Transacciones"));
        panelInferior.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesInferior.add(btnCancelar);
        panelInferior.add(panelBotonesInferior, BorderLayout.SOUTH);

        // Ensamblaje final
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void asignarEventos() {
        // Módulo B: Evento de Selección Dinámica
        comboProductos.addItemListener(new ActualizarCamposListener(comboProductos, lblPrecioBase, lblStock, lblDescuento, lblIVA));
        
        // Módulo B: Evento de Procesamiento y Validación
        btnRegistrar.addActionListener(new RegistrarPedidoListener(comboProductos, txtCantidad, lblStock, modeloTabla));
        
        // Módulo C: Evento de Cancelación
        btnCancelar.addActionListener(new CancelarPedidoListener(tablaHistorial, modeloTabla, comboProductos));
    }

    private void cargarDatosPrueba() {
        comboProductos.addItem(new Producto("Laptop Gamer", 3500000, 10, 5.0, 19.0));
        comboProductos.addItem(new Producto("Mouse Inalámbrico", 85000, 50, 0.0, 19.0));
        comboProductos.addItem(new Producto("Teclado Mecánico", 250000, 20, 10.0, 19.0));
        
        // Disparar manualmente la actualización para el primer elemento
        if (comboProductos.getItemCount() > 0) {
            comboProductos.setSelectedIndex(0);
        }
    }

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
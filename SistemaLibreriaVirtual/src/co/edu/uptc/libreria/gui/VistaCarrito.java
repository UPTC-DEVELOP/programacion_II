package co.edu.uptc.libreria.gui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.libreria.modelo.ItemCarrito;

import java.util.List;

public class VistaCarrito extends JPanel{
	
	protected JLabel tituloVista;
	private JLabel seccion;
	protected JButton btnArchivo;
	protected JButton btnCatalogo;
	protected JButton btnPerfil;
	protected JButton btnCerrarSesion;
	protected JButton btnActualizarCantidad;
	protected JButton btnEliminarLibro;
	protected JButton btnSeguirComprando;
	protected JButton btnFinalizarCompra;
	
	private JTable tablaCarrito;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollTabla;
	
	private JLabel textoDescuento;
	private JLabel textoSubtotal;
	private JLabel textoIva;
	private JLabel textoTotal;
	
	
	
	public VistaCarrito() {
		
		this.setSize(700, 500);
		this.setLayout(null);
		
		inicializarCabecera();
		inicializarTabla();
		inicializarBotonesYtotales();
		
	}
	
	private void inicializarCabecera() {
		
		tituloVista = new JLabel("Tienda Libreria Virtual");
		tituloVista.setFont(new Font("Arial", Font.BOLD, 16));
		tituloVista.setBounds(30, 10, 300, 25);
		this.add(tituloVista);
		
		btnArchivo = new JButton("Archivo");
		btnCatalogo = new JButton("Catalogo");
		btnPerfil = new JButton("Mi perfil");
		btnCerrarSesion = new JButton("Cerrar Sesion");
		
		btnArchivo.setBounds(330, 10, 80, 25);
		btnCatalogo.setBounds(415, 10, 90, 25);
		btnPerfil.setBounds(510, 10, 85, 25);
		btnCerrarSesion.setBounds(600, 10, 70, 25);
		
		this.add(btnArchivo);
		this.add(btnCatalogo);
		this.add(btnPerfil);
		this.add(btnCerrarSesion);
		
		seccion = new JLabel("Carrito de compras");
		seccion.setFont(new Font("Arial", Font.BOLD, 14));
		seccion.setBounds(30, 45, 200, 25);
		this.add(seccion);
	}
		
	private void inicializarTabla() {
		String[] columnas = {"ISBN", "Titulo", "Precio Unitario", "Cantidad", "Sub Total"};
		
		modeloTabla = new DefaultTableModel(columnas, 0) {
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tablaCarrito = new JTable(modeloTabla);
		scrollTabla = new JScrollPane(tablaCarrito);
		scrollTabla.setBounds(30,75 ,640 ,230);
		this.add(scrollTabla);
	}
	
	private void inicializarBotonesYtotales() {
		btnEliminarLibro = new JButton("Eliminar");
		btnActualizarCantidad = new JButton("Actualizar Cantidad");
		
		btnEliminarLibro.setActionCommand(EventosGui.ELIMINAR);
		btnActualizarCantidad.setActionCommand(EventosGui.ACTUALIZAR);
		btnFinalizarCompra.setActionCommand(EventosGui.FINALIZAR_COMPRA);
		
		btnEliminarLibro.setBounds(30, 315, 100, 30);
		btnActualizarCantidad.setBounds(140, 315, 160, 30);
		
		this.add(btnEliminarLibro);
		this.add(btnActualizarCantidad);
		
		textoSubtotal = new JLabel("Subtotal: ");
		textoDescuento = new JLabel("Descuento(Clientes Premium): ");
		textoIva = new JLabel("Iva: 19%");
		textoTotal = new JLabel("Total: ");
		textoTotal.setFont(new Font("Arial", Font.BOLD, 12));
		
		textoSubtotal.setBounds(500, 290, 170, 20);
		textoDescuento.setBounds(500, 310, 170, 20);
		textoIva.setBounds(500, 330, 170, 20);
		textoTotal.setBounds(500, 350, 170, 20);
		
		this.add(textoSubtotal);
		this.add(textoDescuento);
		this.add(textoIva);
		this.add(textoTotal);
		
		btnSeguirComprando = new JButton("<-- Seguir Comprando");
		btnFinalizarCompra = new JButton("Finalizar Compra");
		
		btnSeguirComprando.setBounds(30, 400, 180, 35);
		btnFinalizarCompra.setBounds(510, 400, 160, 35);
		
		this.add(btnSeguirComprando);
		this.add(btnFinalizarCompra);
	}
	
	public void conectarControlador(EventosGui eventos) {
		btnArchivo.addActionListener(eventos);
		btnCatalogo.addActionListener(eventos);
		btnPerfil.addActionListener(eventos);
		btnCerrarSesion.addActionListener(eventos);
		btnActualizarCantidad.addActionListener(eventos);
		btnEliminarLibro.addActionListener(eventos);
		btnSeguirComprando.addActionListener(eventos);
		btnFinalizarCompra.addActionListener(eventos);
	}
	
	public String obtenerCodigoSeleccionado() {
		int filaSeleccionada = tablaCarrito.getSelectedRow();
		if (filaSeleccionada != -1) {
			return modeloTabla.getValueAt(filaSeleccionada, 0).toString();
		}
		return null;
	}
	
	public int obtenerNuevaCantidad() {
		String cantStr = JOptionPane.showInputDialog(this, "Ingresa la nueva cantidad: ");
		try {
			return Integer.parseInt(cantStr);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public void actualizarTabla(List<ItemCarrito> items) {
		modeloTabla.setRowCount(0);
		
		for (ItemCarrito item : items) {
			Object[] fila = new Object[] {
					item.getLibro().getCodigo(), item.getLibro().getTitulo(), item.getPrecioUnitario(), item.getCantidad(), item.getSubtotal()
			};
			modeloTabla.addRow(fila);
		}
	}
	
	public void mostrarTotales(double subtotal, double descuento, double iva, double total) {
		textoSubtotal.setText(String.format("Sub total: $%.2f", subtotal));
		textoDescuento.setText(String.format("Descuento: -$%.2f", descuento));
		textoIva.setText(String.format("Iva: $%.2f", iva));
		textoTotal.setText(String.format("Total: $%.2f", total));
	}
}

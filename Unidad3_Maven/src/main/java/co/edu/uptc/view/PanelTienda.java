package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.model.Libro;

// PanelTienda solo presenta libros y expone controles; los cálculos los hacen los servicios.
public class PanelTienda extends JPanel {
    private final JComboBox<Libro> cmbLibros=new JComboBox<>(); private final JLabel lblPrecio=new JLabel(),lblStock=new JLabel(),lblDescuento=new JLabel(),lblIva=new JLabel(),lblPrecioFinal=new JLabel(),lblTotal=new JLabel(); private final JTextField txtCantidad=new JTextField("1",5); private final JComboBox<String> cmbPago=new JComboBox<>(new String[]{"EFECTIVO","TARJETA_CREDITO","TARJETA_DEBITO","PSE"}); private final JButton btnAgregar=new JButton("Agregar al Carrito"),btnRegistrar=new JButton("Registrar Pedido");
    public PanelTienda(){setLayout(new BorderLayout(10,10)); JPanel datos=new JPanel(new GridLayout(0,2,6,6)); datos.setBorder(BorderFactory.createTitledBorder("Libro seleccionado"));datos.add(new JLabel("Libro:"));datos.add(cmbLibros);datos.add(new JLabel("Precio base:"));datos.add(lblPrecio);datos.add(new JLabel("Stock disponible:"));datos.add(lblStock);datos.add(new JLabel("Descuento:"));datos.add(lblDescuento);datos.add(new JLabel("IVA:"));datos.add(lblIva);datos.add(new JLabel("Precio final:"));datos.add(lblPrecioFinal);datos.add(new JLabel("Cantidad:"));datos.add(txtCantidad);datos.add(new JLabel("Método de pago:"));datos.add(cmbPago);datos.add(new JLabel("Total pedido rápido:"));datos.add(lblTotal);JPanel botones=new JPanel();btnAgregar.setActionCommand("AGREGAR_CARRITO");btnRegistrar.setActionCommand("REGISTRAR_PEDIDO");botones.add(btnAgregar);botones.add(btnRegistrar);add(datos,BorderLayout.NORTH);add(botones,BorderLayout.SOUTH);}
    public void pintarLibro(Libro libro){if(libro==null) {
		return;
	}lblPrecio.setText(String.valueOf(libro.getPrecioBase()));lblStock.setText(String.valueOf(libro.getStock()));lblDescuento.setText(String.valueOf(libro.getPorcentajeDescuento()));lblIva.setText(String.valueOf(libro.getImpuestoIVA()));lblPrecioFinal.setText(String.valueOf(libro.calcularPrecioFinal()));}
    public JComboBox<Libro> getCmbLibros(){return cmbLibros;} public JLabel getLblPrecio(){return lblPrecio;} public JLabel getLblStock(){return lblStock;} public JLabel getLblDescuento(){return lblDescuento;} public JLabel getLblIva(){return lblIva;} public JLabel getLblPrecioFinal(){return lblPrecioFinal;} public JLabel getLblTotal(){return lblTotal;} public JTextField getTxtCantidad(){return txtCantidad;} public JComboBox<String> getCmbPago(){return cmbPago;} public JButton getBtnAgregar(){return btnAgregar;} public JButton getBtnRegistrar(){return btnRegistrar;}
}

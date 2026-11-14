package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;

// PanelCarrito pinta líneas y totales; no modifica cantidades directamente en el modelo.
public class PanelCarrito extends JPanel {
    private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"ISBN","Libro","Precio","Cantidad","Subtotal","IVA"},0){@Override
	public boolean isCellEditable(int f,int c){return false;}}; private final JTable tabla=new JTable(modelo); private final JLabel lblSubtotal=new JLabel(),lblImpuestos=new JLabel(),lblDescuento=new JLabel(),lblTotal=new JLabel(); private final JButton btnAumentar=new JButton("+"),btnDisminuir=new JButton("−"),btnQuitar=new JButton("Quitar"),btnFinalizar=new JButton("Finalizar compra");
    public PanelCarrito(){setLayout(new BorderLayout(8,8));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);add(new JScrollPane(tabla),BorderLayout.CENTER);JPanel inferior=new JPanel(new GridLayout(2,4));inferior.add(new JLabel("Subtotal"));inferior.add(lblSubtotal);inferior.add(new JLabel("Impuestos"));inferior.add(lblImpuestos);inferior.add(new JLabel("Descuento"));inferior.add(lblDescuento);inferior.add(new JLabel("Total"));inferior.add(lblTotal);JPanel botones=new JPanel();btnAumentar.setActionCommand("AUMENTAR");btnDisminuir.setActionCommand("DISMINUIR");btnQuitar.setActionCommand("QUITAR");btnFinalizar.setActionCommand("FINALIZAR_COMPRA");botones.add(btnAumentar);botones.add(btnDisminuir);botones.add(btnQuitar);botones.add(btnFinalizar);JPanel sur=new JPanel(new BorderLayout());sur.add(inferior,BorderLayout.CENTER);sur.add(botones,BorderLayout.SOUTH);add(sur,BorderLayout.SOUTH);}
    public void pintar(Carrito carrito,double descuento){modelo.setRowCount(0);if(carrito!=null) {
		for(ItemCarrito i:carrito.getItems()) {
			modelo.addRow(new Object[]{i.getIsbn(),i.getTitulo(),i.calcularPrecioSinIva(),i.getCantidad(),i.calcularSubtotal(),i.calcularImpuesto()});
		}
	}if(carrito!=null){lblSubtotal.setText(String.valueOf(carrito.getSubtotalGeneral()));lblImpuestos.setText(String.valueOf(carrito.getTotalImpuestos()));lblDescuento.setText(String.valueOf(descuento));lblTotal.setText(String.valueOf(carrito.getTotalBruto()-descuento));}}
    public JTable getTabla(){return tabla;} public JButton getBtnAumentar(){return btnAumentar;} public JButton getBtnDisminuir(){return btnDisminuir;} public JButton getBtnQuitar(){return btnQuitar;} public JButton getBtnFinalizar(){return btnFinalizar;}
}

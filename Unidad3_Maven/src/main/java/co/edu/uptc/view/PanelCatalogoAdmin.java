package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.model.Libro;

// PanelCatalogoAdmin recoge datos del formulario y deja el CRUD al CatalogoController.
public class PanelCatalogoAdmin extends JPanel {
    private final JTextField txtIsbn=new JTextField(),txtTitulo=new JTextField(),txtPrecio=new JTextField(),txtStock=new JTextField(),txtIva=new JTextField("19"),txtDescuento=new JTextField("0"); private final JComboBox<String> cmbFormato=new JComboBox<>(new String[]{"FISICO","DIGITAL"}); private final JButton btnGuardar=new JButton("Guardar"),btnActualizar=new JButton("Actualizar"),btnEliminar=new JButton("Eliminar"),btnLimpiar=new JButton("Limpiar"); private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"ISBN","Título","Precio","Stock","IVA","Descuento","Formato"},0){@Override
	public boolean isCellEditable(int f,int c){return false;}}; private final JTable tabla=new JTable(modelo);
    public PanelCatalogoAdmin(){setLayout(new BorderLayout(8,8));JPanel form=new JPanel(new GridLayout(0,2,5,5));form.setBorder(BorderFactory.createTitledBorder("Datos del libro"));form.add(new JLabel("ISBN"));form.add(txtIsbn);form.add(new JLabel("Título"));form.add(txtTitulo);form.add(new JLabel("Precio base"));form.add(txtPrecio);form.add(new JLabel("Stock"));form.add(txtStock);form.add(new JLabel("IVA"));form.add(txtIva);form.add(new JLabel("Descuento"));form.add(txtDescuento);form.add(new JLabel("Formato"));form.add(cmbFormato);JPanel botones=new JPanel();btnGuardar.setActionCommand("GUARDAR_LIBRO");btnActualizar.setActionCommand("ACTUALIZAR_LIBRO");btnEliminar.setActionCommand("ELIMINAR_LIBRO");btnLimpiar.setActionCommand("LIMPIAR_FORMULARIO");botones.add(btnGuardar);botones.add(btnActualizar);botones.add(btnEliminar);botones.add(btnLimpiar);JPanel norte=new JPanel(new BorderLayout());norte.add(form,BorderLayout.CENTER);norte.add(botones,BorderLayout.SOUTH);add(norte,BorderLayout.NORTH);tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);add(new JScrollPane(tabla),BorderLayout.CENTER);}
    public void pintar(List<Libro> libros){modelo.setRowCount(0);for(Libro l:libros) {
		modelo.addRow(new Object[]{l.getIsbn(),l.getTitulo(),l.getPrecioBase(),l.getStock(),l.getImpuestoIVA(),l.getPorcentajeDescuento(),l.getFormato()});
	}}
    public void limpiar(){txtIsbn.setText("");txtTitulo.setText("");txtPrecio.setText("");txtStock.setText("");txtIva.setText("19");txtDescuento.setText("0");}
    public JTextField getTxtIsbn(){return txtIsbn;}public JTextField getTxtTitulo(){return txtTitulo;}public JTextField getTxtPrecio(){return txtPrecio;}public JTextField getTxtStock(){return txtStock;}public JTextField getTxtIva(){return txtIva;}public JTextField getTxtDescuento(){return txtDescuento;}public JComboBox<String> getCmbFormato(){return cmbFormato;}public JButton getBtnGuardar(){return btnGuardar;}public JButton getBtnActualizar(){return btnActualizar;}public JButton getBtnEliminar(){return btnEliminar;}public JButton getBtnLimpiar(){return btnLimpiar;}public JTable getTabla(){return tabla;}
}

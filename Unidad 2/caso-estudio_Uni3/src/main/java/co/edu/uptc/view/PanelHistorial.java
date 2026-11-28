package co.edu.uptc.view;

import co.edu.uptc.model.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// PanelHistorial solo muestra pedidos y habilita la acción de cancelar mediante el controlador.
public class PanelHistorial extends JPanel {
    private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"N°","Fecha","Cliente","Subtotal","IVA","Descuento","Total"},0){public boolean isCellEditable(int f,int c){return false;}}; private final JTable tabla=new JTable(modelo); private final JButton btnCancelar=new JButton("Cancelar Pedido Seleccionado");
    public PanelHistorial(){setLayout(new BorderLayout(8,8));tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);btnCancelar.setActionCommand("CANCELAR_PEDIDO");btnCancelar.setEnabled(false);add(new JScrollPane(tabla),BorderLayout.CENTER);add(btnCancelar,BorderLayout.SOUTH);}
    public void pintar(List<Pedido> pedidos){modelo.setRowCount(0);for(Pedido p:pedidos)modelo.addRow(new Object[]{p.getId(),p.getFecha(),p.getCorreoCliente(),p.getSubtotal(),p.getImpuestos(),p.getDescuento(),p.getTotal()});btnCancelar.setEnabled(false);}
    public JTable getTabla(){return tabla;} public JButton getBtnCancelar(){return btnCancelar;} public DefaultTableModel getModelo(){return modelo;}
}

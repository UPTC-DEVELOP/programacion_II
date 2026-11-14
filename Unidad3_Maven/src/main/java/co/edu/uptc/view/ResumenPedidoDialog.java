package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;

// ResumenPedidoDialog solo muestra el resumen y devuelve la decisión del cliente.
public class ResumenPedidoDialog extends JDialog {
    private final JButton btnConfirmar=new JButton("Confirmar"),btnCancelar=new JButton("Cancelar"); private boolean confirmado;
    public ResumenPedidoDialog(Frame padre,Carrito carrito,double descuento){super(padre,"Resumen del pedido",true);StringBuilder texto=new StringBuilder();for(ItemCarrito i:carrito.getItems()) {
		texto.append(i.getTitulo()).append(" x ").append(i.getCantidad()).append("\n");
	}texto.append("\nSubtotal: ").append(carrito.getSubtotalGeneral()).append("\nImpuestos: ").append(carrito.getTotalImpuestos()).append("\nDescuento: ").append(descuento).append("\nTotal: ").append(carrito.getTotalBruto()-descuento);JTextArea area=new JTextArea(texto.toString(),12,45);area.setEditable(false);btnConfirmar.setActionCommand("CONFIRMAR_RESUMEN");btnCancelar.setActionCommand("CANCELAR_RESUMEN");btnConfirmar.addActionListener(e->{confirmado=true;dispose();});btnCancelar.addActionListener(e->dispose());JPanel botones=new JPanel();botones.add(btnConfirmar);botones.add(btnCancelar);add(new JScrollPane(area),BorderLayout.CENTER);add(botones,BorderLayout.SOUTH);pack();setLocationRelativeTo(padre);}
    public boolean isConfirmado(){return confirmado;} public JButton getBtnConfirmar(){return btnConfirmar;}public JButton getBtnCancelar(){return btnCancelar;}
}

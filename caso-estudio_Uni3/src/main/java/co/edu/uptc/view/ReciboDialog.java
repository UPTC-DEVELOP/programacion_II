package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

// ReciboDialog presenta el texto generado por ReciboService sin modificarlo.
public class ReciboDialog extends JDialog {
    private final JTextArea area=new JTextArea(); private final JButton btnCerrar=new JButton("Cerrar");
    public ReciboDialog(Frame padre,String recibo){super(padre,"Recibo de compra",true);area.setText(recibo);area.setEditable(false);area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));btnCerrar.setActionCommand("CERRAR_RECIBO");btnCerrar.addActionListener(e->dispose());add(new JScrollPane(area),BorderLayout.CENTER);add(btnCerrar,BorderLayout.SOUTH);setSize(600,500);setLocationRelativeTo(padre);}
    public JTextArea getArea(){return area;} public JButton getBtnCerrar(){return btnCerrar;}
}

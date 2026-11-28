package co.edu.uptc.view;

import co.edu.uptc.model.Resena;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// PanelResenas presenta reseñas y recoge una calificación; ResenaService valida la compra.
public class PanelResenas extends JPanel {
    private final JTextField txtIsbn=new JTextField();private final JComboBox<Integer> cmbCalificacion=new JComboBox<>(new Integer[]{1,2,3,4,5});private final JTextArea txtComentario=new JTextArea(3,25);private final JButton btnGuardar=new JButton("Guardar reseña");private final DefaultTableModel modelo=new DefaultTableModel(new Object[]{"ISBN","Cliente","Calificación","Comentario","Fecha"},0){public boolean isCellEditable(int f,int c){return false;}};private final JTable tabla=new JTable(modelo);
    public PanelResenas(){setLayout(new BorderLayout(8,8));JPanel form=new JPanel(new GridLayout(0,2,5,5));form.add(new JLabel("ISBN:"));form.add(txtIsbn);form.add(new JLabel("Calificación:"));form.add(cmbCalificacion);form.add(new JLabel("Comentario:"));form.add(new JScrollPane(txtComentario));btnGuardar.setActionCommand("GUARDAR_RESENA");form.add(new JLabel());form.add(btnGuardar);add(form,BorderLayout.NORTH);add(new JScrollPane(tabla),BorderLayout.CENTER);}
    public void pintar(List<Resena> resenas){modelo.setRowCount(0);for(Resena r:resenas)modelo.addRow(new Object[]{r.getIsbn(),r.getCorreoCliente(),r.getCalificacion(),r.getComentario(),r.getFecha()});}
    public JTextField getTxtIsbn(){return txtIsbn;}public JComboBox<Integer> getCmbCalificacion(){return cmbCalificacion;}public JTextArea getTxtComentario(){return txtComentario;}public JButton getBtnGuardar(){return btnGuardar;}public JTable getTabla(){return tabla;}
}

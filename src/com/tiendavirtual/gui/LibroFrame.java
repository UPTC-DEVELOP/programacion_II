package com.tiendavirtual.gui;
import com.tiendavirtual.controlador.LibroController; import com.tiendavirtual.modelo.Libro;
import javax.swing.*; import javax.swing.table.DefaultTableModel; import java.awt.*;
public class LibroFrame extends JFrame {
 private final LibroController c; private final JTextField isbn=new JTextField(),titulo=new JTextField(),autor=new JTextField(),anio=new JTextField(),cat=new JTextField(),edit=new JTextField(),pag=new JTextField(),precio=new JTextField(),stock=new JTextField();
 private final JComboBox<String> formato=new JComboBox<>(new String[]{"Físico","Digital"}), iva=new JComboBox<>(new String[]{"19%","5%"});
 private final DefaultTableModel m=new DefaultTableModel(new Object[]{"ISBN","Título","Autor","Año","Categoría","Editorial","Páginas","Precio","Stock","Formato","IVA","Precio+IVA"},0){public boolean isCellEditable(int r,int col){return false;}};
 private final JTable tabla=new JTable(m);
 public LibroFrame(LibroController c){this.c=c;setTitle("Tienda Virtual - CRUD Libros");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1200,680);setLocationRelativeTo(null);build();listar();}
 private void build(){
  JPanel root=new JPanel(new BorderLayout(8,8));root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));JLabel h=new JLabel("CRUD DE LIBROS",SwingConstants.CENTER);h.setFont(new Font("Arial",Font.BOLD,22));root.add(h,BorderLayout.NORTH);
  JPanel f=new JPanel(new GridLayout(6,4,6,6));f.setBorder(BorderFactory.createTitledBorder("Datos del libro"));
  add(f,"ISBN",isbn);add(f,"Título",titulo);add(f,"Autor",autor);add(f,"Año",anio);add(f,"Categoría",cat);add(f,"Editorial",edit);add(f,"Páginas",pag);add(f,"Precio",precio);add(f,"Stock",stock);add(f,"Formato",formato);add(f,"IVA",iva);
  JButton calc=new JButton("Calcular precio + IVA");calc.addActionListener(e->calcular());f.add(calc);root.add(f,BorderLayout.NORTH);
  JPanel center=new JPanel(new BorderLayout());center.setBorder(BorderFactory.createTitledBorder("Libros"));center.add(new JScrollPane(tabla),BorderLayout.CENTER);
  JPanel b=new JPanel();String[] bs={"Registrar","Actualizar","Eliminar","Listar","Limpiar"};JButton[] j=new JButton[5];for(int i=0;i<5;i++){j[i]=new JButton(bs[i]);b.add(j[i]);}
  j[0].addActionListener(e->registrar());j[1].addActionListener(e->actualizar());j[2].addActionListener(e->eliminar());j[3].addActionListener(e->listar());j[4].addActionListener(e->limpiar());center.add(b,BorderLayout.SOUTH);root.add(center,BorderLayout.CENTER);
  tabla.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()&&tabla.getSelectedRow()>=0)cargar();});setContentPane(root);
 }
 private void add(JPanel p,String n,Component x){p.add(new JLabel(n+":"));p.add(x);}
 private Libro leer(){double iv="5%".equals(iva.getSelectedItem())?.05:.19;return new Libro(isbn.getText().trim(),titulo.getText().trim(),autor.getText().trim(),Integer.parseInt(anio.getText().trim()),cat.getText().trim(),edit.getText().trim(),Integer.parseInt(pag.getText().trim()),Double.parseDouble(precio.getText().trim().replace(",",".")),Integer.parseInt(stock.getText().trim()),(String)formato.getSelectedItem(),iv);}
 private void registrar(){try{c.registrar(leer());msg("Libro registrado.");listar();limpiar();}catch(Exception e){err(e.getMessage());}}
 private void actualizar(){try{c.actualizar(leer());msg("Libro actualizado.");listar();limpiar();}catch(Exception e){err(e.getMessage());}}
 private void eliminar(){try{c.eliminar(isbn.getText().trim());msg("Libro eliminado.");listar();limpiar();}catch(Exception e){err(e.getMessage());}}
 private void listar(){m.setRowCount(0);for(Libro l:c.listar())m.addRow(new Object[]{l.getIsbn(),l.getTitulo(),l.getAutor(),l.getAnio(),l.getCategoria(),l.getEditorial(),l.getPaginas(),String.format("%.2f",l.getPrecio()),l.getStock(),l.getFormato(),String.format("%.0f%%",l.getIva()*100),String.format("%.2f",l.calcularPrecioConIva())});}
 private void cargar(){int r=tabla.getSelectedRow();isbn.setText(""+m.getValueAt(r,0));titulo.setText(""+m.getValueAt(r,1));autor.setText(""+m.getValueAt(r,2));anio.setText(""+m.getValueAt(r,3));cat.setText(""+m.getValueAt(r,4));edit.setText(""+m.getValueAt(r,5));pag.setText(""+m.getValueAt(r,6));precio.setText(""+m.getValueAt(r,7));stock.setText(""+m.getValueAt(r,8));formato.setSelectedItem(m.getValueAt(r,9));iva.setSelectedItem(m.getValueAt(r,10));}
 private void calcular(){try{double p=Double.parseDouble(precio.getText().trim().replace(",","."));double i="5%".equals(iva.getSelectedItem())?.05:.19;msg(String.format("Precio con IVA: %.2f",c.precioFinal(p,i)));}catch(Exception e){err("Ingrese un precio válido.");}}
 private void limpiar(){isbn.setText("");titulo.setText("");autor.setText("");anio.setText("");cat.setText("");edit.setText("");pag.setText("");precio.setText("");stock.setText("");formato.setSelectedIndex(0);iva.setSelectedIndex(0);tabla.clearSelection();}
 private void msg(String s){JOptionPane.showMessageDialog(this,s,"Información",JOptionPane.INFORMATION_MESSAGE);}private void err(String s){JOptionPane.showMessageDialog(this,s,"Validación",JOptionPane.ERROR_MESSAGE);}
}

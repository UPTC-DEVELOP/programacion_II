package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

// VentanaPrincipal organiza las vistas por rol; el controlador decide qué pestañas mostrar.
public class VentanaPrincipal extends JFrame {
    private final JTabbedPane pestanas=new JTabbedPane(); private final JButton btnCerrarSesion=new JButton("Cerrar sesión"); private final JLabel lblUsuario=new JLabel(" ");
    public VentanaPrincipal(){setTitle("Tienda Virtual de Libros");setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);setMinimumSize(new Dimension(1000,650));setLocationRelativeTo(null);JPanel superior=new JPanel(new BorderLayout());superior.add(lblUsuario,BorderLayout.WEST);superior.add(btnCerrarSesion,BorderLayout.EAST);btnCerrarSesion.setActionCommand("CERRAR_SESION");add(superior,BorderLayout.NORTH);add(pestanas,BorderLayout.CENTER);}
    public JTabbedPane getPestanas(){return pestanas;} public JButton getBtnCerrarSesion(){return btnCerrarSesion;} public JLabel getLblUsuario(){return lblUsuario;}
    public void limpiarPestanas(){pestanas.removeAll();}
    public void agregarPestana(String titulo, Component componente){pestanas.addTab(titulo,componente);}
}

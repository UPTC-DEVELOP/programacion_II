package co.edu.uptc.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelCentral extends JFrame {

    private JButton botonRegistrar;
    private JButton botonActualizar;
    private JButton botonBuscar;
    private JButton botonEliminar;
    private JButton botonSalir;

  public PanelCentral() {

   setTitle("Tienda Virtual de Libros");
   setSize(500, 350);
   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   setLocationRelativeTo(null);
   setResizable(false);

   JPanel panelBotones = new JPanel(
             new GridLayout(5, 1, 10, 10)
  );

   botonRegistrar = new JButton("Registrar Cliente");
   botonActualizar = new JButton("Actualizar Cliente");
   botonBuscar = new JButton("Buscar Cliente");
   botonEliminar = new JButton("Eliminar Cliente");
   botonSalir = new JButton("Salir");

   panelBotones.add(botonRegistrar);
   panelBotones.add(botonActualizar);
   panelBotones.add(botonBuscar);
   panelBotones.add(botonEliminar);
   panelBotones.add(botonSalir);

    add(panelBotones);

   botonRegistrar.addActionListener(e -> {
      new PanelRegistrarCliente().setVisible(true);
       dispose();
      });
   
   botonActualizar.addActionListener(e -> {
           new PanelActualizarCliente().setVisible(true);
            dispose();
     });

   botonBuscar.addActionListener(e -> {
           new PanelBuscarCliente().setVisible(true);
            dispose();
   });

   botonEliminar.addActionListener(e -> {
           new PanelEliminarCliente().setVisible(true);
            dispose();
     });

   botonSalir.addActionListener(e -> {
           System.exit(0);
       });
    }
}
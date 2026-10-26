package co.edu.uptc.gui;

import java.awt.event.ActionListener;

import co.edu.uptc.util.Comandos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

<<<<<<< HEAD
    private JButton botonRegistrar;
    private JButton botonActualizar;
    private JButton botonBuscar;
    private JButton botonEliminar;
    private JButton botonSalir;
    private JButton botonCarrito;
=======
>>>>>>> 83e788452caffc8bf709ef6c08534ac89bd0f2c7

public abstract class PanelCentral<T> extends JPanel {
    protected JTable tabla;
    protected DefaultTableModel modelo;
    protected String tituloPanel;
    protected JButton btnEliminar;
    protected JButton btnActualizar;
    protected JButton btnCrear;
    protected JButton btnBuscar;
    protected JButton btnVer;
    protected JButton btnLimpiar;

    public PanelCentral(ActionListener evento) {
        JPanel pSuperior = new JPanel();
        pSuperior.setLayout(new BorderLayout());
        setLayout(new BorderLayout());

<<<<<<< HEAD
   JPanel panelBotones = new JPanel(
             new GridLayout(6, 1, 10, 10)
            
  );

   botonRegistrar = new JButton("Registrar Cliente");
   botonActualizar = new JButton("Actualizar Cliente");
   botonBuscar = new JButton("Buscar Cliente");
   botonEliminar = new JButton("Eliminar Cliente");
   botonSalir = new JButton("Salir");
   botonCarrito = new JButton("Carrito de compras");

   panelBotones.add(botonRegistrar);
   panelBotones.add(botonActualizar);
   panelBotones.add(botonBuscar);
   panelBotones.add(botonEliminar);
   panelBotones.add(botonSalir);
   panelBotones.add(botonCarrito);
=======
        // Panel título
        agregarTituloPanel();
        JPanel pTitulo = new JPanel();
        pTitulo.add(new JLabel(tituloPanel));

        // Panel botones CRUD
        JPanel pBtnFuncion = new JPanel();
        btnEliminar = new JButton(Comandos.ELIMINAR_LIBRO);
        btnActualizar = new JButton(Comandos.ACTUALIZAR_LIBRO);
        btnCrear = new JButton(Comandos.CREAR_LIBRO);
        btnVer = new JButton(Comandos.VER_LIBRO);
        btnLimpiar = new JButton(Comandos.LIMPIAR_LIBRO);

        // Asociar eventos
        btnEliminar.addActionListener(evento);
        btnActualizar.addActionListener(evento);
        btnCrear.addActionListener(evento);
        btnVer.addActionListener(evento);
        btnLimpiar.addActionListener(evento);
>>>>>>> 83e788452caffc8bf709ef6c08534ac89bd0f2c7

        // Agregar botones
        pBtnFuncion.add(btnEliminar);
        pBtnFuncion.add(btnActualizar);
        pBtnFuncion.add(btnCrear);
        pBtnFuncion.add(btnVer);
        pBtnFuncion.add(btnLimpiar);

        // Panel filtros (buscar por ISBN)
        JPanel pFiltros = new JPanel();
        JTextField txBuscar = new JTextField();
        txBuscar.setPreferredSize(new Dimension(145, 30));
        btnBuscar = new JButton(Comandos.BUSCAR_LIBRO);
        btnBuscar.addActionListener(evento);

        pFiltros.add(txBuscar);
        pFiltros.add(btnBuscar);

        // Panel superior
        pSuperior.add(pTitulo, BorderLayout.NORTH);
        pSuperior.add(pBtnFuncion, BorderLayout.CENTER);
        pSuperior.add(pFiltros, BorderLayout.SOUTH);

<<<<<<< HEAD
   botonSalir.addActionListener(e -> {
           System.exit(0);
       });
   botonCarrito.addActionListener(e-> {
	   new PanelCarrito().setVisible(true);
   });
=======
        // Tabla
        modelo = new DefaultTableModel();
        tabla = new JTable(modelo);
        agregarCabeceraTabla();

        JScrollPane spTabla = new JScrollPane(tabla);

        add(pSuperior, BorderLayout.NORTH);
        add(spTabla, BorderLayout.CENTER);

        agregarIdentificadorComandoBoton();
>>>>>>> 83e788452caffc8bf709ef6c08534ac89bd0f2c7
    }

    // Métodos abstractos que las clases hijas deben implementar
    public abstract void agregarTituloPanel();
    public abstract void agregarIdentificadorComandoBoton();
    public abstract void agregarCabeceraTabla();
    public abstract void poblarTabla(List<T> lista);
}
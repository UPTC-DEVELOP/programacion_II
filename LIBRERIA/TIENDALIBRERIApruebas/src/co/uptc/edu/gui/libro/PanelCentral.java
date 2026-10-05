package co.uptc.edu.gui.libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public abstract class PanelCentral extends JPanel {
    protected JTable tablaLibros;
    protected DefaultTableModel modelo;
    protected String tituloPanel;
    protected JButton btnEliminar;
    protected JButton btnActualizar;
    protected JButton btnCrear;
    protected JButton btnBuscar;
    protected JButton btnVer;
    protected JButton btnLimpiar;

    public PanelCentral(Evento evento) {
        JPanel pSuperior = new JPanel();
        pSuperior.setLayout(new BorderLayout());
        setLayout(new BorderLayout());

        // Panel título
        JPanel pTitulo = new JPanel();
        pTitulo.add(new JLabel(tituloPanel));

        // Panel botones CRUD
        JPanel pBtnFuncion = new JPanel();
        btnEliminar = new JButton(Evento.ELIMINAR_LIBRO);
        btnActualizar = new JButton(Evento.ACTUALIZAR_LIBRO);
        btnCrear = new JButton(Evento.CREAR_LIBRO);
        btnVer = new JButton(Evento.VER_LIBRO);
        btnLimpiar = new JButton(Evento.LIMPIAR_LIBRO);

        // Asociar eventos
        btnEliminar.addActionListener(evento);
        btnActualizar.addActionListener(evento);
        btnCrear.addActionListener(evento);
        btnVer.addActionListener(evento);
        btnLimpiar.addActionListener(evento);

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
        btnBuscar = new JButton(Evento.BUSCAR_LIBRO);
        btnBuscar.addActionListener(evento);

        pFiltros.add(txBuscar);
        pFiltros.add(btnBuscar);

        // Panel superior
        pSuperior.add(pTitulo, BorderLayout.NORTH);
        pSuperior.add(pBtnFuncion, BorderLayout.CENTER);
        pSuperior.add(pFiltros, BorderLayout.SOUTH);

        // Tabla
        modelo = new DefaultTableModel();
        tablaLibros = new JTable(modelo);
        agregarCabeceraTabla();

        JScrollPane spTabla = new JScrollPane(tablaLibros);

        add(pSuperior, BorderLayout.NORTH);
        add(spTabla, BorderLayout.CENTER);

        agregarIdentificadorComandoBoton();
    }

    // Métodos abstractos que las clases hijas deben implementar
    public abstract void agregarTituloPanel();
    public abstract void agregarIdentificadorComandoBoton();
    public abstract void agregarCabeceraTabla();
    public abstract void poblarTabla(List<?> listaLibros);
}
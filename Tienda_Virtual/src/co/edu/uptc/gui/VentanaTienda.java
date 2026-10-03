package co.edu.uptc.gui;

import co.edu.uptc.controller.TiendaController;
import javax.swing.*;
import java.awt.*;

public class VentanaTienda extends JFrame {
    private PanelTienda panelTienda;
    private PanelCatalogoLibros panelCatalogo;
    private Eventos oyenteEventos;
    private TiendaController controller;

    public VentanaTienda() {
        controller = new TiendaController();
        oyenteEventos = new Eventos(this);

        setTitle("Tienda Virtual de Libros - UPTC");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelTienda = new PanelTienda(oyenteEventos);
        panelCatalogo = new PanelCatalogoLibros(oyenteEventos);

        setLayout(new CardLayout());
        add(panelTienda, "PANEL_LOGIN");
        add(panelCatalogo, "PANEL_CATALOGO");
    }

    public void mostrarPanelCatalogo() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        refrescarCatalogo();
        cl.show(getContentPane(), "PANEL_CATALOGO");
    }

    public void refrescarCatalogo() {
        panelCatalogo.cargarDatosTabla(controller.listarLibros());
    }

    public void accionCancelar() {
        System.exit(0);
    }

    public TiendaController getController() { return controller; }
    public PanelCatalogoLibros getPanelCatalogo() { return panelCatalogo; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaTienda vt = new VentanaTienda();
            vt.setVisible(true);
        });
    }
}
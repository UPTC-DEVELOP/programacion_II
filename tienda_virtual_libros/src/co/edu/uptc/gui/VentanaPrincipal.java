package co.edu.uptc.tienda.gui;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.negocio.GestionCliente;
import co.edu.uptc.tienda.persistencia.LocalCliente;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema Tienda Virtual de Libros - Módulo Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //ventana en toda la pantalla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
      

        // persistencia
        IGestionCliente persistencia = new LocalCliente();
        
        GestionCliente negocio = new GestionCliente(persistencia);

        PanelCliente panelCliente = new PanelCliente(negocio);
        add(panelCliente);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
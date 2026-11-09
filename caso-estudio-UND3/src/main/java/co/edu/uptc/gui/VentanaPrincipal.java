package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private MenuPrincipal menuPrincipal;
    private JDesktopPane escritorio;
    private JLabel lblEstado;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Librería - Biblioteca Central UPTC");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        menuPrincipal = new MenuPrincipal();
        setJMenuBar(menuPrincipal);

        escritorio = new JDesktopPane();
        escritorio.setBackground(new Color(214, 217, 223));

        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        panelEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        lblEstado = new JLabel("Usuario: Invitado | Sin sesión | Versión 3.0.0");
        panelEstado.add(lblEstado);

        add(escritorio, BorderLayout.CENTER);
        add(panelEstado, BorderLayout.SOUTH);
    }

    public void agregarListenerMenu(ActionListener listener) {
        menuPrincipal.agregarListener(listener);
    }

    public void actualizarEstadoSesion(String texto) {
        lblEstado.setText(texto);
    }

    public void mostrarVentanaInterna(JInternalFrame ventana) {
        if (ventana.getParent() != escritorio) {
            escritorio.add(ventana);
        }
        ventana.setVisible(true);
        try {
            ventana.setIcon(false);
            ventana.setMaximum(true);
            ventana.setSelected(true);
        } catch (PropertyVetoException e) {
            ventana.setLocation(0, 0);
        }
        ventana.toFront();
    }
}


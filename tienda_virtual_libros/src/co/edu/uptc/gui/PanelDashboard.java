package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel DASHBOARD del administrador: punto de entrada a los módulos
 * de gestión (CRUD de libros y CRUD de clientes) y cierre de sesión.
 */
public class PanelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final String CMD_GESTIONAR_LIBROS = "GESTION_LIBROS";
    public static final String CMD_GESTIONAR_CLIENTES = "GESTION_CLIENTES";
    public static final String CMD_SALIR = "CERRAR_SESION";

    public PanelDashboard(ActionListener manejadorEventos) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(236, 240, 241)); // Color de fondo suave
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("PANEL DE ADMINISTRACIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(44, 62, 80));

        JLabel lblBienvenida = new JLabel("Bienvenido, administrador de la tienda virtual de libros", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblBienvenida.setForeground(new Color(100, 100, 100));

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
        panelTitulo.setOpaque(false);
        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblBienvenida);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 0, 18));
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        panelAcciones.add(botonModulo("Gestionar Libros (CRUD)", CMD_GESTIONAR_LIBROS, manejadorEventos));
        panelAcciones.add(botonModulo("Gestionar Clientes (CRUD)", CMD_GESTIONAR_CLIENTES, manejadorEventos));
        panelAcciones.add(botonModulo("Cerrar sesión", CMD_SALIR, manejadorEventos));
        add(panelAcciones, BorderLayout.CENTER);
    }

    private JButton botonModulo(String texto, String comando, ActionListener manejadorEventos) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.addActionListener(manejadorEventos);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(new Color(41, 128, 185));
        boton.setForeground(Color.WHITE);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}

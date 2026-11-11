package com.tiendavirtual.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.tiendavirtual.controlador.LibroController;
import com.tiendavirtual.negocio.LibroService;
import com.tiendavirtual.repositorio.LibroRepositoryMemoria;
import com.tiendavirtual.negocio.GestionCliente;
public class MenuPrincipal extends JFrame {
private GestionCliente gestionCliente;

public MenuPrincipal(GestionCliente gestionCliente) {
    this.gestionCliente = gestionCliente;
        setTitle("TIENDA VIRTUAL - MENÚ PRINCIPAL");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel titulo = new JLabel("MENÚ PRINCIPAL", JLabel.CENTER);

        JButton btnLibros = new JButton("GESTIÓN DE LIBROS");
        JButton btnClientes = new JButton("GESTIÓN DE CLIENTES");
        JButton btnVentas = new JButton("GESTIÓN DE VENTAS");
        JButton btnSalir = new JButton("CERRAR SESIÓN");
        btnLibros.addActionListener(e -> {
            LibroRepositoryMemoria repositorio = new LibroRepositoryMemoria();
            LibroService servicio = new LibroService(repositorio);
            LibroController controlador = new LibroController(servicio);

            LibroFrame ventana = new LibroFrame(controlador);
            ventana.setVisible(true);
        });

        btnClientes.addActionListener(e -> {
            GestionClientesFrame ventana =
                new GestionClientesFrame(gestionCliente);

            ventana.setVisible(true);
        });

        btnVentas.addActionListener(e -> {
            VentaFrame ventana = new VentaFrame();
            ventana.setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(titulo);
        add(btnLibros);
        add(btnClientes);
        add(btnVentas);
        add(btnSalir);
    }
}
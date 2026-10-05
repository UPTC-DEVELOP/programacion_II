package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class PantallaAdmin extends JPanel {

    public PantallaAdmin(Navegador nav) {
        super(new BorderLayout());
        add(EstiloUI.crearHeaderSuperior("Panel de Administración General"), BorderLayout.NORTH);

        JPanel menuGrid = new JPanel(new GridLayout(1, 3, 20, 20));
        menuGrid.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuGrid.setBackground(EstiloUI.FONDO_MENU);

        JButton btnGestionarInv = EstiloUI.crearCardBoton("Gestor de Inventario", "Alta, consulta y control de stock de libros", EstiloUI.PRIMARIO);
        JButton btnReportes = EstiloUI.crearCardBoton("Métricas de Ventas", "Visualiza simulaciones y reportes del sistema", EstiloUI.PRIMARIO);

        JButton btnUsuarios = EstiloUI.crearCardBoton("Gestión de Usuarios", "Registro, roles y datos de los usuarios", EstiloUI.PRIMARIO);

        btnGestionarInv.addActionListener(e -> nav.irA(Vista.INVENTARIO));
        btnUsuarios.addActionListener(e -> nav.irA(Vista.GESTION_USUARIOS));
        btnReportes.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Simulación de Reportes: No hay estadísticas registradas aún.",
                        "Información", JOptionPane.INFORMATION_MESSAGE));

        menuGrid.add(btnGestionarInv);
        menuGrid.add(btnUsuarios);
        menuGrid.add(btnReportes);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLogout = new JButton("Cerrar Sesión Admin");
        btnLogout.addActionListener(e -> nav.irA(Vista.LOGIN));
        footer.add(btnLogout);

        add(menuGrid, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
}

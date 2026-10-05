package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Panel principal del cliente. */
public class PantallaUsuario extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final JLabel lblUsuarioActual;

    public PantallaUsuario(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;

        JPanel header = EstiloUI.crearHeaderSuperior("Panel Principal de Cliente");
        lblUsuarioActual = new JLabel("Bienvenido, Cliente");
        lblUsuarioActual.setForeground(Color.WHITE);
        lblUsuarioActual.setFont(new Font("SansSerif", Font.ITALIC, 14));
        header.add(lblUsuarioActual, BorderLayout.EAST);

        JPanel menuGrid = new JPanel(new GridLayout(1, 2, 20, 20));
        menuGrid.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuGrid.setBackground(EstiloUI.FONDO_MENU);

        JButton btnCatalogo = EstiloUI.crearCardBoton("Explora el Catálogo", "Examina y compra libros disponibles", EstiloUI.PRIMARIO);
        JButton btnCarrito = EstiloUI.crearCardBoton("Carrito de Compras", "Revisa tus productos y finaliza la compra", EstiloUI.PRIMARIO);
        btnCatalogo.addActionListener(e -> nav.irA(Vista.CATALOGO));
        btnCarrito.addActionListener(e -> nav.irA(Vista.CARRITO));
        menuGrid.add(btnCatalogo);
        menuGrid.add(btnCarrito);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> nav.irA(Vista.LOGIN));
        footer.add(btnLogout);

        add(header, BorderLayout.NORTH);
        add(menuGrid, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        lblUsuarioActual.setText("Bienvenido, " + tienda.getUsuarioActual());
    }
}

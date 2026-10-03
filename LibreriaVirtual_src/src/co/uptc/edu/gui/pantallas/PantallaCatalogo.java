package co.uptc.edu.gui.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Libro;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;

/** Catálogo de libros disponibles para el cliente. */
public class PantallaCatalogo extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final JPanel panelCatalogoLibros;

    public PantallaCatalogo(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;

        add(EstiloUI.crearHeaderSuperior("Catálogo de Libros Disponible"), BorderLayout.NORTH);

        panelCatalogoLibros = new JPanel(new GridLayout(0, 3, 15, 15));
        panelCatalogoLibros.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCatalogoLibros.setBackground(EstiloUI.FONDO_LOGIN);

        JScrollPane scrollPane = new JScrollPane(panelCatalogoLibros);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel navBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Inicio");
        JButton btnIrCarrito = EstiloUI.crearBotonEstilizado("Ver Carrito", EstiloUI.PRIMARIO);
        btnVolver.addActionListener(e -> nav.irA(Vista.USUARIO_HOME));
        btnIrCarrito.addActionListener(e -> nav.irA(Vista.CARRITO));
        navBottom.add(btnVolver);
        navBottom.add(btnIrCarrito);

        add(scrollPane, BorderLayout.CENTER);
        add(navBottom, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        panelCatalogoLibros.removeAll();
        NumberFormat formatter = EstiloUI.formatoMoneda();

        for (Libro libro : tienda.getInventario()) {
            panelCatalogoLibros.add(crearTarjetaLibro(libro, formatter));
        }
        panelCatalogoLibros.revalidate();
        panelCatalogoLibros.repaint();
    }

    private JPanel crearTarjetaLibro(Libro libro, NumberFormat formatter) {
        JPanel cardLibro = new JPanel(new BorderLayout(5, 5));
        cardLibro.setBackground(Color.WHITE);
        cardLibro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.PRIMARIO, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel("<html><b>" + libro.getTitulo() + "</b></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblInfo = new JLabel("<html>Autor: " + libro.getAutor() + "<br>Categoría: " + libro.getCategoria()
                + "<br>Stock: " + libro.getStock() + " un.</html>");
        lblInfo.setForeground(EstiloUI.TEXTO_INFO);

        JLabel lblPrecio = new JLabel(formatter.format(libro.getPrecio()));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrecio.setForeground(EstiloUI.PRECIO);

        JButton btnAgregar = new JButton("Agregar 🛒");
        btnAgregar.setEnabled(libro.getStock() > 0);
        btnAgregar.addActionListener(e -> agregarAlCarrito(libro));

        JPanel bottomCard = new JPanel(new BorderLayout());
        bottomCard.setOpaque(false);
        bottomCard.add(lblPrecio, BorderLayout.WEST);
        bottomCard.add(btnAgregar, BorderLayout.EAST);

        cardLibro.add(lblTitulo, BorderLayout.NORTH);
        cardLibro.add(lblInfo, BorderLayout.CENTER);
        cardLibro.add(bottomCard, BorderLayout.SOUTH);
        return cardLibro;
    }

    private void agregarAlCarrito(Libro libro) {
        switch (tienda.agregarAlCarrito(libro)) {
            case CANTIDAD_INCREMENTADA:
                JOptionPane.showMessageDialog(this, "Se incrementó la cantidad de '" + libro.getTitulo() + "' en el carrito.");
                break;
            case SIN_STOCK:
                JOptionPane.showMessageDialog(this, "No hay suficiente stock disponible.", "Límite superado", JOptionPane.WARNING_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Libro '" + libro.getTitulo() + "' añadido al carrito.");
        }
    }
}

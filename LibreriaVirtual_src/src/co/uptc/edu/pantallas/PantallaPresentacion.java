package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Vista;

import javax.swing.*;
import java.awt.*;

/** Pantalla de bienvenida. */
public class PantallaPresentacion extends JPanel {

    public PantallaPresentacion(Navegador nav) {
        super(new BorderLayout());
        setBackground(EstiloUI.FONDO_PRESENTACION);

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(EstiloUI.PRIMARIO);
        panelHeader.setPreferredSize(new Dimension(0, 80));
        JLabel lblTitulo = new JLabel("BIBLIOTECA VIRTUAL");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel lblSub = new JLabel("Tu portal digital hacia el conocimiento y la lectura");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblSub.setForeground(EstiloUI.TEXTO_SUBTITULO);

        JLabel lblIcono = new JLabel("📖");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));

        JButton btnIngresar = EstiloUI.crearBotonEstilizado("Ingresar al Sistema", EstiloUI.INDIGO);
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnIngresar.setPreferredSize(new Dimension(220, 50));
        btnIngresar.addActionListener(e -> nav.irA(Vista.LOGIN));

        gbc.gridy = 0; panelCentro.add(lblIcono, gbc);
        gbc.gridy = 1; panelCentro.add(lblSub, gbc);
        gbc.gridy = 2; panelCentro.add(btnIngresar, gbc);

        add(panelHeader, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
    }
}

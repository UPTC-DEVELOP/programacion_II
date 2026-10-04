package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.CredencialDto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Pantalla de LOGIN de la tienda virtual.
 * Solo captura el correo y la contraseña y notifica a la capa de eventos.
 */
public class PanelLogin extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnRegistrarse;

    public static final String CMD_INGRESAR = "INICIAR_SESION";
    public static final String CMD_REGISTRARSE = "REGISTRAR_CLIENTE";

    public PanelLogin(ActionListener manejadorEventos) {
        inicializarComponentes();
        configurarLayout();
        registrarEventos(manejadorEventos);
    }

    private void inicializarComponentes() {
        txtCorreo = new CampoConHint("ejemplo@correo.com", 22);
        txtContrasena = new JPasswordField(22);

        btnIngresar = new JButton("Ingresar");
        btnRegistrarse = new JButton("Registrarse");
    }

    private void registrarEventos(ActionListener manejadorEventos) {
        btnIngresar.setActionCommand(CMD_INGRESAR);
        btnIngresar.addActionListener(manejadorEventos);

        btnRegistrarse.setActionCommand(CMD_REGISTRARSE);
        btnRegistrarse.addActionListener(manejadorEventos);

        // Enter en el campo de contraseña también dispara el login
        txtContrasena.addActionListener(ev -> manejadorEventos.actionPerformed(
                new java.awt.event.ActionEvent(btnIngresar, java.awt.event.ActionEvent.ACTION_PERFORMED, CMD_INGRESAR)));
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("TIENDA VIRTUAL DE LIBROS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));

        JLabel subtitulo = new JLabel("Inicio de sesión", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 100, 100));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridy = 1;
        add(subtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Correo Electrónico:"), gbc);

        gbc.gridx = 1;
        add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnIngresar, gbc);

        gbc.gridy = 5;
        add(btnRegistrarse, gbc);

        gbc.gridy = 6;
        add(new JLabel("Admin: admin@libros.com / admin123"), gbc);
    }

    /**
     * Método requerido por VentanaPrincipal para obtener los datos del login.
     */
    public CredencialDto getCredencial() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        return new CredencialDto(correo, contrasena);
    }

    /** Limpia los campos después de cerrar sesión. */
    public void limpiar() {
        txtCorreo.setText("");
        txtContrasena.setText("");
    }
}

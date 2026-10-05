package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Registro de nuevos clientes (RF-02). */
public class PantallaRegistro extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final Navegador nav;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();
    private final JPasswordField txtPass2 = new JPasswordField();

    public PantallaRegistro(Tienda tienda, Navegador nav) {
        super(new GridBagLayout());
        this.tienda = tienda;
        this.nav = nav;
        setBackground(EstiloUI.FONDO_LOGIN);

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.PRIMARIO, 1),
                new EmptyBorder(25, 30, 25, 30)
        ));

        JLabel lblTitle = new JLabel("Crear cuenta de cliente", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(EstiloUI.PRIMARIO);

        JPanel campos = new JPanel(new GridLayout(0, 2, 10, 5));
        campos.setOpaque(false);
        campos.add(conTitulo(txtNombre, "Nombre"));
        campos.add(conTitulo(txtApellido, "Apellido"));
        campos.add(conTitulo(txtCorreo, "Correo electrónico"));
        campos.add(conTitulo(txtTelefono, "Teléfono (7 a 10 dígitos)"));
        campos.add(conTitulo(txtDireccion, "Dirección"));
        campos.add(new JLabel(""));
        campos.add(conTitulo(txtPass, "Contraseña (mín. 8, letras y números)"));
        campos.add(conTitulo(txtPass2, "Confirmar contraseña"));

        JButton btnVolver = new JButton("Volver al login");
        btnVolver.setFocusPainted(false);
        JButton btnRegistrar = EstiloUI.crearBotonEstilizado("Registrarme", EstiloUI.PRIMARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setOpaque(false);
        acciones.add(btnVolver);
        acciones.add(btnRegistrar);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(campos, BorderLayout.CENTER);
        card.add(acciones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> nav.irA(Vista.LOGIN));
        btnRegistrar.addActionListener(e -> registrar());

        add(card);
    }

    @Override
    public void refrescar() {
        limpiar();
    }

    private void registrar() {
        String pass = new String(txtPass.getPassword());
        String pass2 = new String(txtPass2.getPassword());
        if (!pass.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            tienda.getGestionUsuarios().registrarCliente(txtNombre.getText(), txtApellido.getText(),
                    txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(), pass);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puedes iniciar sesión.",
                "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);
        limpiar();
        nav.irA(Vista.LOGIN);
    }

    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPass.setText("");
        txtPass2.setText("");
    }

    private static JComponent conTitulo(JComponent campo, String titulo) {
        campo.setBorder(BorderFactory.createTitledBorder(titulo));
        return campo;
    }
}

package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Actualización de datos personales del usuario en sesión (RF-11). */
public class PantallaPerfil extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final Navegador nav;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtRol = new JTextField();
    private final JPasswordField txtPassActual = new JPasswordField();
    private final JPasswordField txtPassNueva = new JPasswordField();
    private final JPasswordField txtPassNueva2 = new JPasswordField();

    public PantallaPerfil(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;
        this.nav = nav;

        add(EstiloUI.crearHeaderSuperior("Mi Perfil"), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(EstiloUI.FONDO_MENU);

        JPanel card = new JPanel(new GridLayout(0, 2, 10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.PRIMARIO, 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        txtRol.setEditable(false);

        card.add(conTitulo(txtNombre, "Nombre"));
        card.add(conTitulo(txtApellido, "Apellido"));
        card.add(conTitulo(txtCorreo, "Correo electrónico"));
        card.add(conTitulo(txtTelefono, "Teléfono (7 a 10 dígitos)"));
        card.add(conTitulo(txtDireccion, "Dirección"));
        card.add(conTitulo(txtRol, "Tipo de usuario"));
        card.add(conTitulo(txtPassActual, "Contraseña actual (solo si la cambias)"));
        card.add(new JLabel(""));
        card.add(conTitulo(txtPassNueva, "Nueva contraseña (opcional)"));
        card.add(conTitulo(txtPassNueva2, "Confirmar nueva contraseña"));
        centro.add(card);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Inicio");
        JButton btnGuardar = EstiloUI.crearBotonEstilizado("Guardar cambios", EstiloUI.PRIMARIO);
        btnVolver.addActionListener(e ->
                nav.irA(tienda.getRolActual() == Rol.ADMIN ? Vista.ADMIN_HOME : Vista.USUARIO_HOME));
        btnGuardar.addActionListener(e -> guardar());
        footer.add(btnVolver);
        footer.add(btnGuardar);

        add(centro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        Usuario u = tienda.getUsuarioSesion();
        if (u != null) {
            txtNombre.setText(u.getNombre());
            txtApellido.setText(u.getApellido());
            txtCorreo.setText(u.getCorreo());
            txtDireccion.setText(u.getDireccion());
            txtTelefono.setText(u.getTelefono());
            txtRol.setText(u.getTipoUsuario() == Rol.ADMIN ? "Administrador" : "Cliente");
        }
        txtPassActual.setText("");
        txtPassNueva.setText("");
        txtPassNueva2.setText("");
    }

    private void guardar() {
        Usuario u = tienda.getUsuarioSesion();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "No hay una sesión activa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nueva = new String(txtPassNueva.getPassword());
        if (!nueva.equals(new String(txtPassNueva2.getPassword()))) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña y su confirmación no coinciden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            tienda.getGestionUsuarios().actualizarPerfil(u.getCorreo(), txtNombre.getText(), txtApellido.getText(),
                    txtCorreo.getText(), txtDireccion.getText(), txtTelefono.getText(),
                    new String(txtPassActual.getPassword()), nueva);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refrescar();
        JOptionPane.showMessageDialog(this, "Tus datos se actualizaron correctamente.",
                "Perfil actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JComponent conTitulo(JComponent campo, String titulo) {
        campo.setBorder(BorderFactory.createTitledBorder(titulo));
        return campo;
    }
}

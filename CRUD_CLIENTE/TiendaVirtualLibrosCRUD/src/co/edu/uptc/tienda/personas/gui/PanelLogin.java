package co.edu.uptc.tienda.personas.gui;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import co.edu.uptc.tienda.gui.VentanaPrincipal;

public class PanelLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;

    public PanelLogin() {
        setTitle("Inicio de sesión - Tienda Virtual de Libros");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        crearFormulario();
    }

    private void crearFormulario() {
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField();

        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField();

        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblContrasena);
        panelFormulario.add(txtContrasena);

        btnIngresar = new JButton("Ingresar");

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnIngresar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarIngreso();
            }
        });
    }

    private void validarIngreso() {
        String usuario = txtUsuario.getText().trim();
        char[] contrasena = txtContrasena.getPassword();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Debe ingresar un usuario.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (contrasena.length < 8) {
            JOptionPane.showMessageDialog(
                this,
                "La contraseña debe tener mínimo 8 caracteres.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            this,
            "Inicio de sesión exitoso."
        );

        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PanelLogin login = new PanelLogin();
            login.setVisible(true);
        });
    }
}
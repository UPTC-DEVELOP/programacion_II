package co.edu.uptc.gui.cliente;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.interfaces.IClienteService;
import co.edu.uptc.negocio.ValidacionException;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JInternalFrame {
    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private JButton btnIniciarSesion;
    private JButton btnCancelar;

    private IClienteService clienteService;

    public VentanaLogin(IClienteService clienteService) {
        super("Autenticación - Iniciar Sesión", true, true, true, true);
        this.clienteService = clienteService;
        setSize(380, 220);
        initUI();
    }

    private void initUI() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lblCorreo, gbc);

        txtCorreo = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtCorreo, gbc);

        JLabel lblClave = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lblClave, gbc);

        txtClave = new JPasswordField(18);
        gbc.gridx = 1; gbc.gridy = 1;
        panelFormulario.add(txtClave, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnIniciarSesion = new JButton("Ingresar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        btnIniciarSesion.addActionListener(e -> ejecutarLogin());
        btnCancelar.addActionListener(e -> this.dispose());
    }

    private void ejecutarLogin() {
        String correo = txtCorreo.getText().trim();
        String clave = new String(txtClave.getPassword());

        try {
            // Se actualizó a iniciarSesion() en lugar de autenticar()
            Cliente cliente = clienteService.iniciarSesion(correo, clave);
            
            // Se actualizó a getNombreCompleto() (si tu clase Cliente usa solo getNombre(), cámbialo de vuelta)
            JOptionPane.showMessageDialog(this, "¡Bienvenido/a, " + cliente.getNombreCompleto() + "!", 
                    "Sesión Iniciada", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            
        } catch (ValidacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
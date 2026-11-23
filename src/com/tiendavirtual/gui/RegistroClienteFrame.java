package com.tiendavirtual.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.modelo.ClientePremium;
import com.tiendavirtual.negocio.GestionCliente;

public class RegistroClienteFrame extends JFrame {

    private GestionCliente gestionClientes;

    private JTextField txNombre;
    private JTextField txApellido;
    private JTextField txCorreo;
    private JPasswordField txContrasenia;
    private JTextField txTelefono;
    private JTextField txNumeroDocumento;

    private JComboBox<String> cbTipoDocumento;

    private JButton btRegistrar;
    private JButton btCancelar;

    public RegistroClienteFrame(GestionCliente gestionCliente) {

        this.gestionClientes = gestionCliente;

        setTitle("REGISTRO DE CLIENTE");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(8, 2, 5, 5));

        txNombre = new JTextField();
        txApellido = new JTextField();
        txCorreo = new JTextField();
        txContrasenia = new JPasswordField();
        txTelefono = new JTextField();
        txNumeroDocumento = new JTextField();

        cbTipoDocumento = new JComboBox<>(
            new String[]{"CC", "TI", "CE", "Pasaporte"}
        );

        btRegistrar = new JButton("REGISTRAR");

        btRegistrar.addActionListener(e -> {

            try {
                String nombre = txNombre.getText().trim();
                String apellido = txApellido.getText().trim();
                String correo = txCorreo.getText().trim();
                String contrasenia =
                    new String(txContrasenia.getPassword());
                String telefono = txTelefono.getText().trim();

                String tipoDocumento =
                    (String) cbTipoDocumento.getSelectedItem();

                int numeroDocumento = Integer.parseInt(
                    txNumeroDocumento.getText().trim()
                );

                Cliente cliente = new ClientePremium(
                    tipoDocumento,
                    numeroDocumento,
                    nombre,
                    apellido,
                    correo,
                    telefono,
                    contrasenia
                );

                boolean registrado =
                    gestionClientes.registrarCliente(cliente);

                if (registrado) {
                    JOptionPane.showMessageDialog(
                        this,
                        "CLIENTE REGISTRADO CORRECTAMENTE"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "NO SE PUDO REGISTRAR EL CLIENTE"
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "INGRESA UN NÚMERO DE DOCUMENTO VÁLIDO"
                );
            }
        });

        btCancelar = new JButton("VOLVER AL LOGIN");

        btCancelar.addActionListener(e -> {
            dispose();
        });

        add(new JLabel("NOMBRE:"));
        add(txNombre);

        add(new JLabel("APELLIDO:"));
        add(txApellido);

        add(new JLabel("CORREO:"));
        add(txCorreo);

        add(new JLabel("CONTRASEÑA:"));
        add(txContrasenia);

        add(new JLabel("TELÉFONO:"));
        add(txTelefono);

        add(new JLabel("TIPO DE DOCUMENTO:"));
        add(cbTipoDocumento);

        add(new JLabel("NÚMERO DE DOCUMENTO:"));
        add(txNumeroDocumento);

        add(btRegistrar);
        add(btCancelar);
    }
}
package com.tiendavirtual.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.negocio.GestionCliente;

public class ModificarClienteFrame extends JFrame {

    private JTextField txNombre;
    private JTextField txApellido;
    private JTextField txCorreo;
    private JTextField txTelefono;
    private JPasswordField txContrasenia;

    private JButton btGuardar;
    private JButton btCancelar;

    private GestionCliente gestionCliente;
    private Cliente cliente;

    public ModificarClienteFrame(GestionCliente gestionCliente, Cliente cliente) {

        this.gestionCliente = gestionCliente;
        this.cliente = cliente;

        setTitle("MODIFICAR DATOS DEL CLIENTE");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        txNombre = new JTextField(cliente.getNombre());
        txApellido = new JTextField(cliente.getApellido());
        txCorreo = new JTextField(cliente.getCorreo());
        txTelefono = new JTextField(cliente.getTelefono());
        txContrasenia = new JPasswordField();

        btGuardar = new JButton("GUARDAR CAMBIOS");
        btCancelar = new JButton("CANCELAR");

        btGuardar.addActionListener(e -> {

            String nombre = txNombre.getText();
            String apellido = txApellido.getText();
            String correo = txCorreo.getText();
            String telefono = txTelefono.getText();
            String contrasenia = new String(txContrasenia.getPassword());

            Cliente clienteActualizado = new Cliente(
                    cliente.getTipoDocumento(),
                    cliente.getNumeroDocumento(),
                    nombre,
                    apellido,
                    correo,
                    telefono,
                    contrasenia
            );

            boolean actualizado = gestionCliente.actualizarCliente(clienteActualizado);

            if (actualizado) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Datos actualizados correctamente"
                );
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "No se pudieron actualizar los datos"
                );
            }
        });

        btCancelar.addActionListener(e -> {
            dispose();
        });

        add(new JLabel("NOMBRE:"));
        add(txNombre);

        add(new JLabel("APELLIDO:"));
        add(txApellido);

        add(new JLabel("CORREO:"));
        add(txCorreo);

        add(new JLabel("TELÉFONO:"));
        add(txTelefono);

        add(new JLabel("NUEVA CONTRASEÑA:"));
        add(txContrasenia);

        add(btGuardar);
        add(btCancelar);
    }
}
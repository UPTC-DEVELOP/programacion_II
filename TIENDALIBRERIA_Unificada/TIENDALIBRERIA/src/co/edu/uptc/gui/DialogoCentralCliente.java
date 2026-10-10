package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.modelo.Cliente;
import co.uptc.edu.gui.libro.Evento;

public abstract class DialogoCentralCliente extends JDialog {

    protected boolean isCrear;
    protected String tituloDialogo;

    // Campos de cliente
    protected JTextField txCedula;
    protected JTextField txNombre;
    protected JTextField txApellido;
    protected JTextField txTelefono;
    protected JTextField txCorreo;
    protected JTextField txDireccion;
    protected JComboBox<String> cbxTipoCliente;

    // Botones
    protected JButton btnGuardar;
    protected JButton btnCerrar;

    public DialogoCentralCliente(Evento evento, String tituloDialogo, boolean isCrear) {
        this.isCrear = isCrear;
        this.tituloDialogo = tituloDialogo;

        setSize(420, 400);
        setTitle(tituloDialogo);
        setModal(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes(evento);
    }

    private void inicializarComponentes(Evento evento) {
        JPanel pCliente = new JPanel(new GridLayout(7, 2, 8, 8));
        pCliente.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txCedula = new JTextField();
        txNombre = new JTextField();
        txApellido = new JTextField();
        txTelefono = new JTextField();
        txCorreo = new JTextField();
        txDireccion = new JTextField();
        cbxTipoCliente = new JComboBox<>(new String[] {"Premium", "No Premium"});

        pCliente.add(new JLabel("Cédula:")); pCliente.add(txCedula);
        pCliente.add(new JLabel("Nombre:")); pCliente.add(txNombre);
        pCliente.add(new JLabel("Apellido:")); pCliente.add(txApellido);
        pCliente.add(new JLabel("Teléfono:")); pCliente.add(txTelefono);
        pCliente.add(new JLabel("Correo:")); pCliente.add(txCorreo);
        pCliente.add(new JLabel("Dirección:")); pCliente.add(txDireccion);
        pCliente.add(new JLabel("Tipo de cliente:")); pCliente.add(cbxTipoCliente);

        // Botones
        btnGuardar = new JButton(isCrear ? "Guardar" : "Actualizar");
        btnCerrar = new JButton("Cancelar");

        btnGuardar.addActionListener(evento);
        btnCerrar.addActionListener(evento);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBotones.add(btnCerrar);
        pBotones.add(btnGuardar);

        add(pCliente, BorderLayout.CENTER);
        add(pBotones, BorderLayout.SOUTH);

        asignarComandoBotones();
    }

    // Construye el cliente con lo escrito en el formulario (las reglas de validacion viven en el negocio)
    public Cliente capturarDatos() {
        return new Cliente(
            txCedula.getText().trim(),
            txNombre.getText().trim(),
            txApellido.getText().trim(),
            txTelefono.getText().trim(),
            txCorreo.getText().trim(),
            txDireccion.getText().trim(),
            cbxTipoCliente.getSelectedItem().toString()
        );
    }

    public abstract void asignarComandoBotones();
}

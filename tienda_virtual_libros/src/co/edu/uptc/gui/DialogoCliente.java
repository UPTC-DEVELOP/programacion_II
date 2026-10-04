package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para crear o editar un cliente.
 *
 * PRINCIPIO SOLID:
 * - SRP: Solo se encarga de mostrar el formulario, validar los datos de
 *   PRESENTACIÓN y devolver el DTO. No conoce la capa de negocio.
 *
 * El diálogo se cierra solo cuando el usuario pulsa "Guardar" (con datos
 * válidos) o "Cancelar"; el llamador consulta isGuardado() y getClienteDto().
 *
 * @author Grupo 7 - UPTC
 * @version 1.0
 */
public class DialogoCliente extends JDialog {

    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private JTextField txtPrimerNombre;
    private JTextField txtOtrosNombres;
    private JTextField txtPrimerApellido;
    private JTextField txtOtrosApellidos;
    private JComboBox<String> comboTipoIdentificacion;
    private JTextField txtIdentificacion;
    private JTextField txtCorreoElectronico;
    private JTextField txtCelular;
    private JTextField txtDireccion;
    private JPasswordField txtContrasenia;
    private JComboBox<TipoCliente> comboTipoCliente;
    private JComboBox<Rol> comboRol;
    private boolean guardado = false;

    // Botones
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final ClienteDto clienteEditar; // Null si es creación, con datos si es edición

    // Comandos de acción
    public static final String CMD_GUARDAR = "GUARDAR_CLIENTE_DIALOGO";
    public static final String CMD_CANCELAR = "CANCELAR_DIALOGO";

    /**
     * Constructor del diálogo.
     * @param padre ventana padre (para el centrado y la modalidad)
     * @param clienteEditar DTO del cliente a editar (null si es creación)
     */
    public DialogoCliente(Frame padre, ClienteDto clienteEditar) {
        super(padre, clienteEditar == null ? "Nuevo Cliente" : "Editar Cliente", true);

        this.clienteEditar = clienteEditar;

        setSize(520, 560);
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();
        configurarLayout();
        registrarEventos();

        // Si es edición, cargar los datos del cliente
        if (clienteEditar != null) {
            cargarDatos(clienteEditar);
        }
    }

    private void inicializarComponentes() {
        // Campos de texto
        txtPrimerNombre = new JTextField(20);
        txtOtrosNombres = new JTextField(20);
        txtPrimerApellido = new JTextField(20);
        txtOtrosApellidos = new JTextField(20);
        txtIdentificacion = new JTextField(15);
        txtCorreoElectronico = new JTextField(20);
        txtCelular = new JTextField(15);
        txtDireccion = new JTextField(30);
        txtContrasenia = new JPasswordField(20);

        // Combos
        comboTipoIdentificacion = new JComboBox<>(new String[]{"CC", "TI", "CE", "Pasaporte"});
        comboTipoCliente = new JComboBox<>(TipoCliente.values());
        comboRol = new JComboBox<>(Rol.values());

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
    }

    private void configurarLayout() {
        JPanel panelFormulario = new JPanel(new GridLayout(12, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelFormulario.add(new JLabel("Primer nombre: *"));
        panelFormulario.add(txtPrimerNombre);

        panelFormulario.add(new JLabel("Otros nombres:"));
        panelFormulario.add(txtOtrosNombres);

        panelFormulario.add(new JLabel("Primer apellido: *"));
        panelFormulario.add(txtPrimerApellido);

        panelFormulario.add(new JLabel("Otros apellidos:"));
        panelFormulario.add(txtOtrosApellidos);

        panelFormulario.add(new JLabel("Tipo identificación:"));
        panelFormulario.add(comboTipoIdentificacion);

        panelFormulario.add(new JLabel("Identificación: *"));
        panelFormulario.add(txtIdentificacion);

        panelFormulario.add(new JLabel("Correo electrónico: *"));
        panelFormulario.add(txtCorreoElectronico);

        panelFormulario.add(new JLabel("Celular: *"));
        panelFormulario.add(txtCelular);

        panelFormulario.add(new JLabel("Dirección: *"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Contraseña: *"));
        panelFormulario.add(txtContrasenia);

        panelFormulario.add(new JLabel("Tipo cliente: *"));
        panelFormulario.add(comboTipoCliente);

        panelFormulario.add(new JLabel("Rol: *"));
        panelFormulario.add(comboRol);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void registrarEventos() {
        btnGuardar.setActionCommand(CMD_GUARDAR);
        btnGuardar.addActionListener(e -> alGuardar());

        btnCancelar.setActionCommand(CMD_CANCELAR);
        btnCancelar.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(btnGuardar);
    }

    /**
     * Carga los datos del cliente en el formulario (modo edición).
     */
    private void cargarDatos(ClienteDto cliente) {
        txtPrimerNombre.setText(vacio(cliente.getPrimerNombre()));
        txtOtrosNombres.setText(vacio(cliente.getOtrosNombres()));
        txtPrimerApellido.setText(vacio(cliente.getPrimerApellido()));
        txtOtrosApellidos.setText(vacio(cliente.getOtrosApellidos()));
        if (cliente.getTipoIdentificacion() != null) {
            comboTipoIdentificacion.setSelectedItem(cliente.getTipoIdentificacion());
        }
        txtIdentificacion.setText(vacio(cliente.getIdentificacion()));
        txtCorreoElectronico.setText(vacio(cliente.getCorreoElectronico()));
        txtCelular.setText(vacio(cliente.getCelular()));
        txtDireccion.setText(vacio(cliente.getDireccion()));
        if (cliente.getTipoCliente() != null) {
            comboTipoCliente.setSelectedItem(cliente.getTipoCliente());
        }
        if (cliente.getRol() != null) {
            comboRol.setSelectedItem(cliente.getRol());
        }

        // En modo edición, la contraseña no se muestra (se conserva la actual)
        txtContrasenia.setEnabled(false);
        txtContrasenia.setToolTipText("Deje vacío para conservar la contraseña actual");
    }

    private String vacio(String valor) {
        return valor == null ? "" : valor;
    }

    /**
     * Valida la obligatoriedad (presentación) y arma el ClienteDto.
     * Si hay error muestra el mensaje y deja el diálogo abierto.
     */
    private void alGuardar() {
        try {
            String primerNombre = exigir(txtPrimerNombre, "Primer nombre");
            String primerApellido = exigir(txtPrimerApellido, "Primer apellido");
            String identificacion = exigir(txtIdentificacion, "Identificación");
            String correo = exigir(txtCorreoElectronico, "Correo electrónico");
            String celular = exigir(txtCelular, "Celular");
            String direccion = exigir(txtDireccion, "Dirección");
            if (!correo.contains("@")) {
                throw new IllegalArgumentException("El correo electrónico no es válido.");
            }

            String contrasenia = new String(txtContrasenia.getPassword()).trim();
            if (clienteEditar == null) {
                // En creación la contraseña es obligatoria
                if (contrasenia.isEmpty()) {
                    txtContrasenia.requestFocus();
                    throw new IllegalArgumentException("La contraseña es obligatoria.");
                }
            } else if (!contrasenia.isEmpty() && contrasenia.length() < 4) {
                txtContrasenia.requestFocus();
                throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
            }

            ClienteDto dto = new ClienteDto();
            dto.setPrimerNombre(primerNombre);
            dto.setOtrosNombres(txtOtrosNombres.getText().trim());
            dto.setPrimerApellido(primerApellido);
            dto.setOtrosApellidos(txtOtrosApellidos.getText().trim());
            dto.setTipoIdentificacion((String) comboTipoIdentificacion.getSelectedItem());
            dto.setIdentificacion(identificacion);
            dto.setCorreoElectronico(correo);
            dto.setCelular(celular);
            dto.setDireccion(direccion);
            dto.setTipoCliente((TipoCliente) comboTipoCliente.getSelectedItem());
            dto.setRol((Rol) comboRol.getSelectedItem());
            dto.setContrasenia(contrasenia);
            dto.setIdCliente(clienteEditar != null ? clienteEditar.getIdCliente() : 0);

            guardado = true;
            setVisible(false);
            dispose();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private String exigir(JTextField campo, String nombre) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) {
            campo.requestFocus();
            throw new IllegalArgumentException("El campo \"" + nombre + "\" es obligatorio.");
        }
        return valor;
    }

    /**
     * Muestra un mensaje de error.
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muesta un mensaje de éxito.
     */
    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isGuardado() {
        return guardado;
    }

    /**
     * Devuelve los datos capturados en el formulario.
     * PRINCIPIO: La GUI no crea objetos de negocio, solo DTOs.
     */
    public ClienteDto getClienteDto() {
        ClienteDto dto = new ClienteDto();
        dto.setPrimerNombre(txtPrimerNombre.getText().trim());
        dto.setOtrosNombres(txtOtrosNombres.getText().trim());
        dto.setPrimerApellido(txtPrimerApellido.getText().trim());
        dto.setOtrosApellidos(txtOtrosApellidos.getText().trim());
        dto.setTipoIdentificacion((String) comboTipoIdentificacion.getSelectedItem());
        dto.setIdentificacion(txtIdentificacion.getText().trim());
        dto.setCorreoElectronico(txtCorreoElectronico.getText().trim());
        dto.setCelular(txtCelular.getText().trim());
        dto.setDireccion(txtDireccion.getText().trim());
        dto.setTipoCliente((TipoCliente) comboTipoCliente.getSelectedItem());
        dto.setRol((Rol) comboRol.getSelectedItem());
        dto.setContrasenia(new String(txtContrasenia.getPassword()).trim());
        dto.setIdCliente(clienteEditar != null ? clienteEditar.getIdCliente() : 0);
        return dto;
    }
}

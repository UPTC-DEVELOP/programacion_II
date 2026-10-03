package co.edu.uptc.tienda.personas.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ClientePremium;
import co.edu.uptc.tienda.modelo.ClienteRegular;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.negocio.Configuracion;
import co.edu.uptc.tienda.negocio.GestionCliente;

/**
 * Diálogo modal para la creación y actualización de clientes.
 * Permite capturar los atributos personales y clasificar el tipo de cliente.
 */
public class DialogoCliente extends JDialog {

    private static final long serialVersionUID = 1L;

    // Campos de texto del formulario
    private JTextField txtPrimerNombre;
    private JTextField txtOtrosNombres;
    private JTextField txtPrimerApellido;
    private JTextField txtOtrosApellidos;
    private JTextField txtTipoIdentificacion;
    private JTextField txtIdentificacion;
    private JTextField txtCorreoElectronico;
    private JTextField txtCelular;
    private JTextField txtDireccion;
    private JPasswordField txtContrasenia;

    // Selector de tipo de cliente (Regular o Premium)
    private JComboBox<TipoCliente> comboTipoCliente;
    
    // Botones de acción
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    // Capa de negocio
    private GestionCliente gestionCliente;
    
    // Referencia al cliente si estamos en modo edición (null en caso de nuevo cliente)
    private Cliente clienteEdicion;
    
    // Bandera para indicar si se guardó exitosamente la información
    private boolean guardadoExitoso = false;

    /**
     * Constructor por defecto para crear un nuevo cliente.
     */
    public DialogoCliente() {
        this(null);
    }

    /**
     * Constructor que admite un cliente existente para modo actualización/edición.
     * 
     * @param clienteAEditar Cliente a modificar, o null para crear uno nuevo.
     */
    public DialogoCliente(Cliente clienteAEditar) {
        this.clienteEdicion = clienteAEditar;
        
        // Configuración básica de la ventana modal
        if (clienteAEditar == null) {
            setTitle("Registro de Nuevo Cliente");
        } else {
            setTitle("Actualizar Cliente - " + clienteAEditar.getIdentificacion());
        }
        
        setSize(520, 520);
        setLocationRelativeTo(null);
        setModal(true); // Bloquea la ventana principal hasta cerrar este diálogo
        
        // Obtener la instancia del gestor de clientes mediante la configuración central
        gestionCliente = Configuracion.getGestionCliente();

        // Construir la interfaz de usuario
        crearFormulario();
        
        // Si es modo edición, precargar los datos existentes
        if (clienteAEditar != null) {
            cargarDatosCliente(clienteAEditar);
        }
    }

    /**
     * Construye y distribuye los componentes del formulario en el diálogo.
     */
    private void crearFormulario() {
        JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 8, 8));

        txtPrimerNombre = new JTextField();
        txtOtrosNombres = new JTextField();
        txtPrimerApellido = new JTextField();
        txtOtrosApellidos = new JTextField();
        txtTipoIdentificacion = new JTextField();
        txtIdentificacion = new JTextField();
        txtCorreoElectronico = new JTextField();
        txtCelular = new JTextField();
        txtDireccion = new JTextField();
        txtContrasenia = new JPasswordField();

        comboTipoCliente = new JComboBox<>(TipoCliente.values());
        
        // Botones de acción
        btnGuardar = new JButton(clienteEdicion == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");
        
        // Agregar etiquetas y campos al panel del formulario
        panelFormulario.add(new JLabel("Primer nombre (*):"));
        panelFormulario.add(txtPrimerNombre);

        panelFormulario.add(new JLabel("Otros nombres:"));
        panelFormulario.add(txtOtrosNombres);

        panelFormulario.add(new JLabel("Primer apellido (*):"));
        panelFormulario.add(txtPrimerApellido);

        panelFormulario.add(new JLabel("Otros apellidos:"));
        panelFormulario.add(txtOtrosApellidos);

        panelFormulario.add(new JLabel("Tipo identificación (*):"));
        panelFormulario.add(txtTipoIdentificacion);

        panelFormulario.add(new JLabel("Identificación (*):"));
        panelFormulario.add(txtIdentificacion);

        panelFormulario.add(new JLabel("Correo electrónico (*):"));
        panelFormulario.add(txtCorreoElectronico);

        panelFormulario.add(new JLabel("Celular:"));
        panelFormulario.add(txtCelular);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Contraseña:"));
        panelFormulario.add(txtContrasenia);

        panelFormulario.add(new JLabel("Tipo cliente:"));
        panelFormulario.add(comboTipoCliente);

        // Margen y ubicación del formulario
        JPanel contenedorFormulario = new JPanel(new BorderLayout());
        contenedorFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedorFormulario.add(panelFormulario, BorderLayout.CENTER);
        add(contenedorFormulario, BorderLayout.CENTER);
        
        // Panel de botones en la parte inferior
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Listeners de eventos
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> procesarGuardado());
    }

    /**
     * Carga los datos de un cliente existente en los campos del formulario.
     * 
     * @param cliente Instancia del cliente cuyos datos se van a editar.
     */
    private void cargarDatosCliente(Cliente cliente) {
        txtPrimerNombre.setText(cliente.getPrimerNombre());
        txtOtrosNombres.setText(cliente.getOtrosNombres() != null ? cliente.getOtrosNombres() : "");
        txtPrimerApellido.setText(cliente.getPrimerApellido());
        txtOtrosApellidos.setText(cliente.getOtrosApellidos() != null ? cliente.getOtrosApellidos() : "");
        txtTipoIdentificacion.setText(cliente.getTipoIdentificacion());
        txtIdentificacion.setText(cliente.getIdentificacion());
        // Deshabilitar la edición de identificación para proteger la clave de búsqueda
        txtIdentificacion.setEditable(false);
        
        txtCorreoElectronico.setText(cliente.getCorreoElectronico());
        txtCelular.setText(cliente.getCelular() != null ? cliente.getCelular() : "");
        txtDireccion.setText(cliente.getDireccion() != null ? cliente.getDireccion() : "");
        txtContrasenia.setText(cliente.getContrasenia() != null ? cliente.getContrasenia() : "");
        
        if (cliente.getTipoCliente() != null) {
            comboTipoCliente.setSelectedItem(cliente.getTipoCliente());
        }
    }

    /**
     * Valida los datos ingresados y realiza la creación o actualización en el sistema.
     */
    private void procesarGuardado() {
        String primerNombre = txtPrimerNombre.getText().trim();
        String otrosNombres = txtOtrosNombres.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String otrosApellidos = txtOtrosApellidos.getText().trim();
        String tipoIdentificacion = txtTipoIdentificacion.getText().trim();
        String identificacion = txtIdentificacion.getText().trim();
        String correoElectronico = txtCorreoElectronico.getText().trim();
        String celular = txtCelular.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String contrasenia = new String(txtContrasenia.getPassword()).trim();
        TipoCliente tipoCliente = (TipoCliente) comboTipoCliente.getSelectedItem();
        
        // Validación de campos obligatorios
        if (primerNombre.isEmpty() || primerApellido.isEmpty() || tipoIdentificacion.isEmpty() || 
            identificacion.isEmpty() || correoElectronico.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos obligatorios (*).", 
                "Campos Incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Modo Creación: Verificar si la identificación ya se encuentra registrada
        if (clienteEdicion == null) {
            Cliente existente = gestionCliente.buscarCliente(identificacion);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un cliente registrado con la identificación: " + identificacion, 
                    "Cliente Duplicado", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Construir la instancia adecuada según el tipo de cliente (Polimorfismo)
        Cliente clienteProcesado;
        if (tipoCliente == TipoCliente.PREMIUM) {
            clienteProcesado = new ClientePremium(
                primerNombre, otrosNombres, primerApellido, otrosApellidos,
                tipoIdentificacion, identificacion, correoElectronico, celular, direccion,
                clienteEdicion != null ? clienteEdicion.getIdCliente() : 0,
                tipoCliente, contrasenia,
                clienteEdicion != null ? clienteEdicion.getFechaRegistro() : new Timestamp(System.currentTimeMillis()),
                clienteEdicion != null ? clienteEdicion.getIntentosFallidos() : 0
            );
        } else {
            clienteProcesado = new ClienteRegular(
                primerNombre, otrosNombres, primerApellido, otrosApellidos,
                tipoIdentificacion, identificacion, correoElectronico, celular, direccion,
                clienteEdicion != null ? clienteEdicion.getIdCliente() : 0,
                tipoCliente, contrasenia,
                clienteEdicion != null ? clienteEdicion.getFechaRegistro() : new Timestamp(System.currentTimeMillis()),
                clienteEdicion != null ? clienteEdicion.getIntentosFallidos() : 0
            );
        }

        // Ejecutar la persistencia según el modo
        if (clienteEdicion == null) {
            // Guardar nuevo cliente
            gestionCliente.agregarCliente(clienteProcesado);
            JOptionPane.showMessageDialog(this, 
                "Cliente registrado exitosamente.", 
                "Registro Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Actualizar cliente existente
            boolean actualizado = gestionCliente.actualizarCliente(clienteProcesado);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente actualizado exitosamente.", 
                    "Actualización Exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo actualizar la información del cliente.", 
                    "Error de Actualización", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Marcar operación exitosa y cerrar ventana
        guardadoExitoso = true;
        dispose();
    }

    /**
     * Retorna si la operación de guardado/actualización concluyó satisfactoriamente.
     * 
     * @return true si se guardaron cambios, false si se canceló la ventana.
     */
    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}
package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ClientePremium;
import co.edu.uptc.tienda.modelo.ClienteRegular;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

public class GestionCliente {

    private IGestionCliente persistenciaCliente;
    private int contadorId;

    //validar correos con @ y un dominio
    private static final String REGEX_CORREO = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

    public GestionCliente(IGestionCliente persistenciaCliente) {
        this.persistenciaCliente = persistenciaCliente;
        this.contadorId = 1;
    }

    

    private String validarCampos(String primerNombre, String primerApellido, String identificacion, 
                                 String correo, String celular, String direccion, String contrasenia, boolean esNuevo) {
        
        // campos obligatorios
        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            return "ERROR: El primer nombre es obligatorio";
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            return "ERROR: El primer apellido es obligatorio";
        }
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return "ERROR: La identificación es obligatoria";
        }
        if (celular == null || celular.trim().isEmpty()) {
            return "ERROR: El número de celular es obligatorio";
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            return "ERROR: La dirección es obligatoria";
        }

        //campo celular 10 dígitos
        if (!celular.trim().matches("\\d{10}")) {
            return "ERROR: El número de celular debe contener exactamente 10 digitos";
        }

        //valida formato de correo 
        if (correo == null || !Pattern.matches(REGEX_CORREO, correo.trim())) {
            return "ERROR: El correo no tiene un formato válido (debe contener '@' y un dominio )";
        }

        // validaciones para nuevos registros
        if (esNuevo) {
            if (contrasenia == null || contrasenia.trim().length() < 8) {
                return "ERROR: La contraseña debe tener al menos 8 caracteres";
            }
        }

        return null; 
    }

  //crear
    public String registrarCliente(String primerNombre, String otrosNombres, 
                                   String primerApellido, String otrosApellidos, 
                                   String tipoIdentificacion, String identificacion, 
                                   String correo, String celular, String direccion, 
                                   TipoCliente tipoCliente, String contrasenia) {

        String errorValidacion = validarCampos(primerNombre, primerApellido, identificacion, correo, celular, direccion, contrasenia, true);
        if (errorValidacion != null) {
            return errorValidacion;
        }


        if (persistenciaCliente.buscarPorIdentificacion(identificacion.trim()) != null) {
            return "ERROR: Ya existe un usuario registrado con la identificacion " + identificacion;
        }

        if (persistenciaCliente.buscarPorCorreo(correo.trim()) != null) {
            return "ERROR: El correo " + correo + " ya este registrado por otro cliente";
        }

        int nuevoId = contadorId++;
        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
        int intentosFallidosIniciales = 0;

        Cliente nuevoCliente;

        if (tipoCliente == TipoCliente.PREMIUM) {
            nuevoCliente = new ClientePremium(
                    primerNombre.trim(), otrosNombres.trim(), primerApellido.trim(), otrosApellidos.trim(),
                    tipoIdentificacion, identificacion.trim(), correo.trim(), celular.trim(), direccion.trim(),
                    nuevoId, tipoCliente, contrasenia, fechaActual, intentosFallidosIniciales
            );
        } else {
            nuevoCliente = new ClienteRegular(
                    primerNombre.trim(), otrosNombres.trim(), primerApellido.trim(), otrosApellidos.trim(),
                    tipoIdentificacion, identificacion.trim(), correo.trim(), celular.trim(), direccion.trim(),
                    nuevoId, tipoCliente, contrasenia, fechaActual, intentosFallidosIniciales
            );
        }

        if (persistenciaCliente.agregarCliente(nuevoCliente)) {
            return "Cliente registrado exitosamente con ID: " + nuevoId;
        }
        return "ERROR: No se pudo guardar el cliente";
    }

    //buscar
    public Cliente buscarPorIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return null;
        }
        return persistenciaCliente.buscarPorIdentificacion(identificacion.trim());
    }

    public List<Cliente> consultarTodosLosClientes() {
        return persistenciaCliente.obtenerTodosLosClientes();
    }

    // modificar
    public String modificarCliente(String identificacion, String primerNombre, String otrosNombres, 
                                   String primerApellido, String otrosApellidos, 
                                   String celular, String direccion, TipoCliente tipoCliente) {

        Cliente existente = persistenciaCliente.buscarPorIdentificacion(identificacion);
        if (existente == null) {
            return "ERROR: No se encontró el cliente a modificar.";
        }

  
        String errorValidacion = validarCampos(primerNombre, primerApellido, identificacion, existente.getCorreoElectronico(), celular, direccion, existente.getContrasenia(), false);
        if (errorValidacion != null) {
            return errorValidacion;
        }

        Cliente clienteActualizado;
        if (tipoCliente == TipoCliente.PREMIUM) {
            clienteActualizado = new ClientePremium(
                    primerNombre.trim(), otrosNombres.trim(), primerApellido.trim(), otrosApellidos.trim(),
                    existente.getTipoIdentificacion(), identificacion.trim(), existente.getCorreoElectronico(),
                    celular.trim(), direccion.trim(), existente.getIdCliente(), tipoCliente,
                    existente.getContrasenia(), existente.getFechaRegistro(), existente.getIntentosFallidos()
            );
        } else {
            clienteActualizado = new ClienteRegular(
                    primerNombre.trim(), otrosNombres.trim(), primerApellido.trim(), otrosApellidos.trim(),
                    existente.getTipoIdentificacion(), identificacion.trim(), existente.getCorreoElectronico(),
                    celular.trim(), direccion.trim(), existente.getIdCliente(), tipoCliente,
                    existente.getContrasenia(), existente.getFechaRegistro(), existente.getIntentosFallidos()
            );
        }

        if (persistenciaCliente.actualizarCliente(clienteActualizado)) {
            return " Cliente actualizado exitosamente";
        }
        return "ERROR: No se pudo actualizar el cliente";
    }

  
    public String eliminarCliente(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return "ERROR: Ingrese un numero de identificación valido";
        }
        if (persistenciaCliente.eliminarCliente(identificacion.trim())) {
            return "Cliente eliminado correctamente";
        }
        return "ERROR: No se encontro ningun cliente con ese documento" + identificacion;
    }
}
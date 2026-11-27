
package co.edu.uptc.tiendalibros.negocio;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCliente;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.excepciones.ReglaNegocioException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;
import co.edu.uptc.tiendalibros.negocio.seguridad.GestionSeguridad;

/// Gestiona las operaciones y reglas de negocio relacionadas con los clientes.
public class GestionCliente {

  /// Repositorio utilizado para almacenar y consultar los clientes.
  private final IRepositorioCliente repositorio;
  
  /// Repositorio de las cuentas de usuario. Un cliente que se registra necesita una cuenta.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Gestor de seguridad. Valida y protege la contraseña del cliente que se registra.
  private final GestionSeguridad gestorSeguridad;

  /// Crea la gestión de clientes utilizando el repositorio indicado.
  ///
  /// @param repositorio Repositorio encargado de almacenar los clientes.
  public GestionCliente(
    IRepositorioCliente repositorio,
    IRepositorioUsuario repositorioUsuario,
    GestionSeguridad gestorSeguridad
  ) {
    this.repositorio = repositorio;
    this.repositorioUsuario = repositorioUsuario;
    this.gestorSeguridad = gestorSeguridad;
  }
  
  

  /// Guarda un cliente después de validar sus datos.
  ///
  /// @param cliente Cliente que se desea guardar.
  /// @return Cliente guardado con su identificador.
  public Cliente guardar(Cliente cliente) {

    validar(cliente);
    
    if (cliente.getId() == null) {
      throw new ReglaNegocioException(
        "Un cliente nuevo se crea desde 'Registrarse', porque necesita una cuenta con contraseña."
      );
    }

    // Comprueba que el cliente exista cuando se está actualizando.
    if (cliente.getId() != null &&
      this.repositorio.buscarPorId(cliente.getId()).isEmpty()) {

      throw new ReglaNegocioException(
        "El cliente ya no existe."
      );
    }
    
 // Comprueba que no exista otro cliente con el mismo documento o correo.
    boolean clienteDuplicado = this.repositorio.listar().stream()
        .anyMatch(registrado ->
            !java.util.Objects.equals(cliente.getId(), registrado.getId()) &&
            (
                (cliente.getTipoDocumento().trim().equalsIgnoreCase(
                    registrado.getTipoDocumento().trim()) &&
                 cliente.getNumeroDocumento().trim().equals(
                    registrado.getNumeroDocumento().trim()))
                ||
                cliente.getCorreoElectronico().trim().equalsIgnoreCase(
                    registrado.getCorreoElectronico().trim())
            )
        );

    if (clienteDuplicado) {
        throw new ReglaNegocioException(
            "Ya existe un cliente con ese documento o correo electrónico."
        );
    }

    try {

        return this.repositorio.guardar(cliente);

    } catch (ViolacionElementoNoExisteException excepcion) {

        throw new ReglaNegocioException(excepcion.getMessage());
    }
}

public Cliente registrar(Cliente cliente, char[] contrasena, char[] confirmacion) {
  
  try {
    
    validar(cliente);
    
    // un cliente que se registra siempre empieza como REGULAR
    cliente.setTipoCliente("REGULAR");
    
    ResultadoValidacion erroresContrasena = this.gestorSeguridad.validarContrasena(contrasena);
    
    if (erroresContrasena.hayErrores()) {
      String error = erroresContrasena.getPorAtributo()
        .get("contrasena")
        .getFirst();
      throw new ReglaNegocioException("Error de contraseña: " + error);
    }
    
    if (!Arrays.equals(contrasena, confirmacion)) {
      throw new ReglaNegocioException("La confirmación no coincide con la contraseña.");
    }
    
    // Comprueba que no exista otro cliente con el mismo documento.
    for (Cliente registrado : this.repositorio.listar()) {
      
      if (cliente.getTipoDocumento()
        .trim()
        .equalsIgnoreCase(
          registrado.getTipoDocumento()
            .trim()
        ) &&
        cliente.getNumeroDocumento()
          .trim()
          .equals(
            registrado.getNumeroDocumento()
              .trim()
          )) {
        
        throw new ReglaNegocioException("Ya existe un cliente con ese documento.");
      }
    }
    
    // Comprueba que no exista una cuenta con el mismo correo.
    if (this.repositorioUsuario.buscarPorCorreo(
      cliente.getCorreoElectronico()
        .trim()
    )
      .isPresent()) {
      
      throw new ReglaNegocioException("Ya existe una cuenta con ese correo electrónico.");
    }
    
    Usuario cuenta = new Usuario(
      cliente.getCorreoElectronico()
        .trim(),
      this.gestorSeguridad.protegerContrasena(contrasena),
      true,
      EnumSet.of(Rol.CLIENTE)
    );
    
    this.repositorioUsuario.registrarCliente(cuenta, cliente);
    
    return cliente;
    
  } finally {
    
    // las contraseñas no deben quedar en la memoria
    Arrays.fill(contrasena, '\0');
    Arrays.fill(confirmacion, '\0');
  }
}

  /// Valida los datos obligatorios de un cliente.
  ///
  /// @param cliente Cliente cuyos datos se van a validar.
  public void validar(Cliente cliente) {

    if (cliente == null) {
      throw new ReglaNegocioException(
        "El cliente no puede ser nulo."
      );
    }

    if (cliente.getTipoDocumento() == null ||
      cliente.getTipoDocumento().isBlank()) {

      throw new ReglaNegocioException(
        "El tipo de documento no puede estar vacío."
      );
    }

    if (cliente.getNumeroDocumento() == null ||
      cliente.getNumeroDocumento().isBlank()) {

      throw new ReglaNegocioException(
        "El número de documento no puede estar vacío."
      );
    }

    if (cliente.getNombres() == null ||
      cliente.getNombres().isBlank()) {

      throw new ReglaNegocioException(
        "Los nombres del cliente no pueden estar vacíos."
      );
    }

    if (cliente.getApellidos() == null ||
      cliente.getApellidos().isBlank()) {

      throw new ReglaNegocioException(
        "Los apellidos del cliente no pueden estar vacíos."
      );
    }

    if (cliente.getCorreoElectronico() == null ||
      cliente.getCorreoElectronico().isBlank()) {

      throw new ReglaNegocioException(
        "El correo electrónico no puede estar vacío."
      );
    }

    if (cliente.getTelefono() == null ||
      cliente.getTelefono().isBlank()) {

      throw new ReglaNegocioException(
        "El teléfono no puede estar vacío."
      );
    }

    if (cliente.getDireccionEnvio() == null ||
      cliente.getDireccionEnvio().isBlank()) {

      throw new ReglaNegocioException(
        "La dirección de envío no puede estar vacía."
      );
    }

    if (cliente.getTipoCliente() == null ||
      !(cliente.getTipoCliente().equals("REGULAR") ||
        cliente.getTipoCliente().equals("PREMIUM"))) {

      throw new ReglaNegocioException(
        "El tipo de cliente debe ser REGULAR o PREMIUM."
      );
    }
  }

  /// Obtiene todos los clientes registrados.
  ///
  /// @return Lista de clientes almacenados.
  public List<Cliente> listar() {
    return this.repositorio.listar();
  }

  /// Busca un cliente utilizando su identificador.
  ///
  /// @param id Identificador del cliente.
  /// @return Cliente encontrado o un resultado vacío.
  public Optional<Cliente> buscarPorId(Long id) {
    return this.repositorio.buscarPorId(id);
  }

  /// Elimina un cliente utilizando su identificador.
  ///
  /// @param id Identificador del cliente que se desea eliminar.
  /// @return true si el cliente fue eliminado.
  public boolean eliminar(Long id) {
    return this.repositorio.eliminar(id);
  }
}

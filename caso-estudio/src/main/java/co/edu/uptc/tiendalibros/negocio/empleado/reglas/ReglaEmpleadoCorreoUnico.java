//
package co.edu.uptc.tiendalibros.negocio.empleado.reglas;

import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioAtributoException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Regla de validación para determinar si el correo electrónico de la cuenta del empleado es
/// único entre todas las cuentas de usuario (de empleados y de clientes).
///
/// Retorna una [ViolacionReglaNegocioAtributoException] si los datos violan la regla.
public class ReglaEmpleadoCorreoUnico implements IReglaValidacion<DatosEmpleado> {
  
  /// Repositorio de las cuentas de usuario.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorioEmpleado;
  
  /// Crea una nueva regla de correo de cuenta único.
  ///
  /// @param repositorioUsuario  Repositorio para saber si ya hay una cuenta con ese correo.
  /// @param repositorioEmpleado Repositorio de los empleados.
  public ReglaEmpleadoCorreoUnico(
    IRepositorioUsuario repositorioUsuario,
    IRepositorioEmpleado repositorioEmpleado
  ) {
    this.repositorioUsuario = repositorioUsuario;
    this.repositorioEmpleado = repositorioEmpleado;
  }
  
  @Override
  public boolean cumple(DatosEmpleado datos) {
    // si no hay correo, no hay nada que comparar: otra regla lo exige
    if ((datos.correoElectronico() == null) || datos.correoElectronico().isBlank()) {
      return true;
    }
    
    Optional<Usuario> existente = this.repositorioUsuario.buscarPorCorreo(datos.correoElectronico());
    
    // ninguna cuenta usa ese correo
    if (existente.isEmpty()) {
      return true;
    }
    
    // una cuenta usa ese correo y un empleado nuevo no puede repetirlo
    if (datos.id() == null) {
      return false;
    }
    
    // al editar, solo es válido si la cuenta que tiene ese correo es la del mismo empleado
    Optional<EmpleadoConUsuario> actual = this.repositorioEmpleado.buscarPorId(datos.id());
    
    return actual.isPresent()
      && actual.get().getUsuario().getId().equals(existente.get().getId());
  }
  
  @Override
  public Optional<ViolacionReglaNegocioException> validar(
    ResultadoValidacion errores,
    DatosEmpleado datos
  ) {
    // si el elemento cumple la regla, devolver vacío
    if (cumple(datos) == true) {
      return Optional.empty();
    }
    // aquí, el elemento viola la regla
    
    String mensajeError = String.format(
      "Ya existe una cuenta con el correo '%s'.",
      datos.correoElectronico()
    );
    
    // primero, reportar el error (atributo, mensajeError)
    errores.agregarAtributo("correoElectronico", mensajeError);
    
    // segundo, devolver la excepción de la regla de negocio respectiva
    return Optional.of(
      new ViolacionReglaNegocioAtributoException("correoElectronico", mensajeError)
    );
  }
}

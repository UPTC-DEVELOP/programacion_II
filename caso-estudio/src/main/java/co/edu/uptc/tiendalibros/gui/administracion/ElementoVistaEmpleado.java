//
package co.edu.uptc.tiendalibros.gui.administracion;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;

/// Representa los datos de un empleado adaptados para ser mostrados en la
/// interfaz gráfica.
public class ElementoVistaEmpleado extends ElementoVista<DatosEmpleado> {
  
  /// Crea un objeto [ElementoVistaEmpleado] con los datos de un empleado nuevo y sin
  /// errores.
  ///
  /// Un empleado nuevo empieza con la cuenta habilitada y sin rol elegido.
  public ElementoVistaEmpleado() {
    this(
      // (id, tipoDocumento, numeroDocumento, nombres, apellidos, correoElectronico, rol,
      // habilitado, contrasena, confirmacion)
      new DatosEmpleado(null, "CC", "", "", "", "", null, true, new char[0], new char[0]),
      new ResultadoValidacion()
    );
  }
  
  /// Crea un objeto [ElementoVistaEmpleado] con los valores especificados.
  ///
  /// @param datos   Datos del empleado que muestra la vista.
  /// @param errores Posibles errores de validación.
  public ElementoVistaEmpleado(
    DatosEmpleado datos,
    ResultadoValidacion errores
  ) {
    super(datos, errores);
  }
  
  /// Obtiene el rol del empleado, o `null` si no tiene un rol elegido.
  ///
  /// @return Rol del empleado.
  public Rol getRol() { return getElemento().rol(); }
}
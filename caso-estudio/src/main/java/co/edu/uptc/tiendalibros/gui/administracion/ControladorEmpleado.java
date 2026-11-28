//
package co.edu.uptc.tiendalibros.gui.administracion;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;
import co.edu.uptc.tiendalibros.negocio.empleado.GestionEmpleado;

/// Representa un controlador para los eventos y las solicitudes de los empleados.
public class ControladorEmpleado extends ControladorBase {
  
  /// Ruta para el evento de la pantalla de inicio de los empleados.
  public static final String EMPLEADO_INICIO = "empleado.inicio";
  
  /// Ruta para el evento de crear un empleado.
  public static final String EMPLEADO_CREAR = "empleado.nuevo";
  
  /// Ruta para el evento de editar un empleado.
  public static final String EMPLEADO_EDITAR = "empleado.editar";
  
  /// Ruta para el evento de guardar un empleado.
  public static final String EMPLEADO_GUARDAR = "empleado.guardar";
  
  /// Ruta para el evento de listar en una tabla de datos los empleados.
  public static final String EMPLEADO_LISTAR = "empleado.listar";
  
  /// Ruta para el evento de eliminar un empleado.
  public static final String EMPLEADO_ELIMINAR = "empleado.eliminar";
  
  /// Panel de empleados que este controlador controla.
  private PanelEmpleados panelEmpleados;
  
  /// Gestor de empleados.
  private GestionEmpleado gestorEmpleado;
  
  /// Crea un nuevo objeto [ControladorEmpleado].
  ///
  /// @param enrutador Enrutador para comunicar eventos.
  /// @param panel     Panel para la gestión de empleados.
  /// @param gestor    Gestor de las operaciones de los empleados.
  public ControladorEmpleado(
    EnrutadorEventos enrutador,
    PanelEmpleados panel,
    GestionEmpleado gestor
  ) {
    super(enrutador);
    this.panelEmpleados = panel;
    this.gestorEmpleado = gestor;
  }
  
  /// Muestra el panel de empleados.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de empleados y los visualiza en la tabla visual de datos.
  public void listar() {
    List<EmpleadoConUsuario> empleados = this.gestorEmpleado.listar();
    
    // un empleado se muestra con 6 atributos: (id, nombre, documento, correo, rol,
    // habilitado)
    int cantidadAtributos = 6;
    Object[][] filas = new Object[empleados.size()][cantidadAtributos];
    
    for (int i = 0; i < empleados.size(); i++) {
      EmpleadoConUsuario elemento = empleados.get(i);
      Empleado empleado = elemento.getEmpleado();
      
      filas[i] = new Object[] {
        elemento.getId(),
        empleado.getNombres() + " " + empleado.getApellidos(),
        empleado.getTipoDocumento() + " " + empleado.getNumeroDocumento(),
        elemento.getUsuario()
          .getCorreoElectronico(),
        describirRoles(elemento),
        elemento.getUsuario()
          .isHabilitado() ? "Sí" : "No"
      };
    }
    
    this.panelEmpleados.listar(filas);
  }
  
  /// Muestra el formulario para crear un empleado.
  public void crear() {
    this.panelEmpleados.mostrarFormulario(new ElementoVistaEmpleado());
  }
  
  /// Busca el empleado con el ID dado y prepara el formulario para editarlo.
  ///
  /// @param id ID del empleado que se quiere editar.
  public void editar(Long id) {
    if (id == null) {
      return;
    }
    
    Optional<EmpleadoConUsuario> empleado = this.gestorEmpleado.buscarPorId(id);
    
    // si no se encontró el empleado se debe avisar al usuario
    if (empleado.isEmpty()) {
      mostrarAviso(Aviso.advertencia("El empleado ya no existe en el repositorio de datos."));
      listar();
      return;
    }
    
    // al abrir el formulario no hay errores de validación
    this.panelEmpleados.mostrarFormulario(
      new ElementoVistaEmpleado(convertirADatos(empleado.get()), new ResultadoValidacion())
    );
  }
  
  /// Guarda el empleado y su cuenta de usuario.
  ///
  /// @param datos Datos del empleado que se quiere guardar.
  public void guardar(DatosEmpleado datos) {
    if (datos == null) {
      return;
    }
    
    String accion = datos.id() == null ? "creó" : "actualizó";
    String nombre = (datos.nombres() + " " + datos.apellidos()).trim();
    
    // el gestor borra las contraseñas de datos al terminar. Los campos contraseñas se verán
    // vacías
    Respuesta<EmpleadoConUsuario> respuesta = this.gestorEmpleado.guardar(datos);
    
    switch (respuesta) {
      case RespuestaExitosa<EmpleadoConUsuario> exitosa -> {
        this.panelEmpleados.cerrarFormulario();
        listar();
        mostrarAviso(Aviso.exito(String.format("Se %s el empleado '%s'.", accion, nombre)));
      }
      case RespuestaFallida<EmpleadoConUsuario> fallida -> {
        this.panelEmpleados.mostrarFormulario(new ElementoVistaEmpleado(datos, fallida.getErrores()));
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
      }
    }
  }
  
  /// Elimina el empleado con el ID especificado, junto con su cuenta de usuario.
  ///
  /// @param id ID del empleado que se quiere eliminar.
  public void eliminar(Long id) {
    if (id == null) {
      return;
    }
    
    Respuesta<EmpleadoConUsuario> respuesta = this.gestorEmpleado.eliminar(id);
    
    switch (respuesta) {
      case RespuestaExitosa<EmpleadoConUsuario> exitosa -> {
        String nombre = "";
        if (respuesta.getElemento()
          .isPresent()) {
          nombre = respuesta.getElemento()
            .get()
            .getEmpleado()
            .getNombres();
        }
        mostrarAviso(Aviso.exito(String.format("Se eliminó el empleado '%s' y su cuenta.", nombre)));
        this.panelEmpleados.cerrarFormulario();
        listar();
      }
      case RespuestaFallida<EmpleadoConUsuario> fallida -> {
        // se vuelve a mostrar el formulario con el mensaje del error; si el empleado ya no
        // existe, solo se avisa y se actualiza la lista
        Optional<EmpleadoConUsuario> existente = this.gestorEmpleado.buscarPorId(id);
        if (existente.isPresent()) {
          this.panelEmpleados.mostrarFormulario(
            new ElementoVistaEmpleado(convertirADatos(existente.get()), fallida.getErrores())
          );
        } else {
          this.panelEmpleados.cerrarFormulario();
          listar();
        }
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
      }
    }
  }
  
  @Override
  public void registrarEventos() {
    getEnrutador().registrarEvento(ControladorEmpleado.EMPLEADO_INICIO, (elemento) -> mostrarInicio());
    getEnrutador().registrarEvento(ControladorEmpleado.EMPLEADO_LISTAR, (elemento) -> listar());
    getEnrutador().registrarEvento(ControladorEmpleado.EMPLEADO_CREAR, (elemento) -> crear());
    getEnrutador().registrarEvento(ControladorEmpleado.EMPLEADO_EDITAR, (id) -> editar((Long) id));
    getEnrutador()
      .registrarEvento(ControladorEmpleado.EMPLEADO_GUARDAR, (datos) -> guardar((DatosEmpleado) datos));
    getEnrutador().registrarEvento(ControladorEmpleado.EMPLEADO_ELIMINAR, (id) -> eliminar((Long) id));
  }
  
  /// Convierte un empleado con su cuenta en los datos que muestra el formulario. Las
  /// contraseñas quedan vacías.
  ///
  /// @param elemento Empleado con su cuenta de usuario.
  /// @return Datos para el formulario.
  private DatosEmpleado convertirADatos(EmpleadoConUsuario elemento) {
    Empleado empleado = elemento.getEmpleado();
    
    return new DatosEmpleado(
      elemento.getId(),
      empleado.getTipoDocumento(),
      empleado.getNumeroDocumento(),
      empleado.getNombres(),
      empleado.getApellidos(),
      elemento.getUsuario()
        .getCorreoElectronico(),
      obtenerRol(elemento),
      elemento.getUsuario()
        .isHabilitado(),
      new char[0],
      new char[0]
    );
  }
  
  /// Obtiene el único rol de la cuenta del empleado, o `null` si no tiene ninguno.
  private Rol obtenerRol(EmpleadoConUsuario elemento) {
    for (Rol rol : elemento.getUsuario()
      .getRoles()) {
      return rol;
    }
    return null;
  }
  
  /// Describe el rol del empleado para mostrarlo en la tabla.
  private String describirRoles(EmpleadoConUsuario elemento) {
    Rol rol = obtenerRol(elemento);
    return rol == null ? "(sin rol)" : rol.getNombreVisible();
  }
}
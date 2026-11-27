
package co.edu.uptc.tiendalibros.gui.clientes;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.gui.login.ControladorLogin;
import co.edu.uptc.tiendalibros.gui.navegacion.ControladorNavegacion;
import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.negocio.GestionCliente;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ReglaNegocioException;

/// Controla las operaciones y eventos de la gestión de clientes.
public class ControladorCliente extends ControladorBase {

  /// Evento para iniciar la gestión de clientes.
  public static final String CLIENTE_INICIO = "cliente.inicio";

  /// Evento para crear un cliente nuevo.
  public static final String CLIENTE_NUEVO = "cliente.nuevo";

  /// Evento para editar un cliente existente.
  public static final String CLIENTE_EDITAR = "cliente.editar";

  /// Evento para guardar un cliente.
  public static final String CLIENTE_GUARDAR = "cliente.guardar";

  /// Evento para listar los clientes.
  public static final String CLIENTE_LISTAR = "cliente.listar";

  /// Evento para eliminar un cliente.
  public static final String CLIENTE_ELIMINAR = "cliente.eliminar";
  
  /// Evento para registrar un cliente nuevo con su cuenta.
  public static final String CLIENTE_REGISTRAR = "cliente.registrar";

  /// Panel principal de gestión de clientes.
  private PanelGestionClientes panelClientes;

  /// Gestor encargado de las reglas de negocio de clientes.
  private GestionCliente gestorCliente;

  /// Crea el controlador de clientes.
  ///
  /// @param enrutador Enrutador encargado de distribuir los eventos.
  /// @param panel Panel principal de clientes.
  /// @param gestor Gestor de operaciones de clientes.
  public ControladorCliente(
      EnrutadorEventos enrutador,
      PanelGestionClientes panel,
      GestionCliente gestor
  ) {
    super(enrutador);
    this.panelClientes = panel;
    this.gestorCliente = gestor;
  }

  /// Inicia la gestión mostrando los clientes registrados.
  public void mostrarInicio() {
    listar();
  }

  /// Consulta los clientes y los muestra en la tabla.
  public void listar() {

    List<Cliente> clientes = this.gestorCliente.listar();

    int cantidadAtributos = 9;

    Object[][] filas = new Object[clientes.size()][cantidadAtributos];

    for (int i = 0; i < clientes.size(); i++) {

      Cliente cliente = clientes.get(i);

      filas[i] = new Object[] {
        cliente.getId(),
        cliente.getTipoDocumento(),
        cliente.getNumeroDocumento(),
        cliente.getNombres(),
        cliente.getApellidos(),
        cliente.getCorreoElectronico(),
        cliente.getTelefono(),
        cliente.getDireccionEnvio(),
        cliente.getTipoCliente()
      };
    }

    this.panelClientes.listar(filas);
  }

  /// Abre el formulario para registrar un cliente nuevo.
  public void nuevo() {

    ElementoVistaCliente elementoVista = new ElementoVistaCliente();

    this.panelClientes.mostrarFormulario(elementoVista);
  }

  /// Busca un cliente y abre su formulario de edición.
  ///
  /// @param id Identificador del cliente seleccionado.
  public void editar(Object id) {

    if (id == null || !(id instanceof Long)) {
      return;
    }

    Long idCliente = (Long) id;

    Optional<Cliente> cliente = this.gestorCliente.buscarPorId(idCliente);

    if (cliente.isPresent()) {

      ElementoVistaCliente elementoVista = new ElementoVistaCliente(
        cliente.get(),
        new ResultadoValidacion()
      );

      this.panelClientes.mostrarFormulario(elementoVista);

    } else {

      mostrarAviso(
        Aviso.advertencia(
          "El cliente ya no existe en el repositorio de datos."
        )
      );

      listar();
    }
  }

  /// Guarda un cliente nuevo o actualiza uno existente.
  ///
  /// @param elemento Objeto cliente que se desea guardar.
  public void guardar(Object elemento) {

    if (elemento == null || !(elemento instanceof Cliente cliente)) {
      return;
    }

    String accion = cliente.getId() == null ? "creó" : "actualizó";

    try {

      this.gestorCliente.guardar(cliente);

      this.panelClientes.cerrarFormulario();

      listar();

      mostrarAviso(
        Aviso.exito(
          String.format(
            "Se %s el cliente '%s %s'.",
            accion,
            cliente.getNombres(),
            cliente.getApellidos()
          )
        )
      );

    } catch (ReglaNegocioException excepcion) {

      ResultadoValidacion errores = new ResultadoValidacion();

      errores.agregarGeneral(excepcion.getMessage());

      ElementoVistaCliente elementoVista = new ElementoVistaCliente(
        cliente,
        errores
      );

      this.panelClientes.mostrarFormulario(elementoVista);

      mostrarAviso(Aviso.advertencia(excepcion.getMessage()));
    }
  }
  
  /// Registra un cliente nuevo con su cuenta de usuario.
  ///
  /// @param elemento Datos del registro: el cliente y sus contraseñas.
  public void registrar(Object elemento) {
    
    if (elemento == null || !(elemento instanceof DatosRegistroCliente datos)) {
      return;
    }
    
    try {
      
      this.gestorCliente.registrar(datos.cliente(), datos.contrasena(), datos.confirmacion());
      
      this.panelClientes.cerrarFormulario();
      
      // vuelve a la pantalla de inicio de sesión
      getEnrutador().manejarEvento(ControladorNavegacion.NAVEGACION_INICIO_SESION);
      
      mostrarAviso(Aviso.exito("Te registraste correctamente. Ya puedes iniciar sesión."));
      
    } catch (ReglaNegocioException excepcion) {
      
      ResultadoValidacion errores = new ResultadoValidacion();
      
      errores.agregarGeneral(excepcion.getMessage());
      
      this.panelClientes.mostrarFormulario(new ElementoVistaCliente(datos.cliente(), errores));
      
      mostrarAviso(Aviso.advertencia(excepcion.getMessage()));
    }
  }

  /// Elimina un cliente utilizando su identificador.
  ///
  /// @param id Identificador del cliente que se desea eliminar.
  public void eliminar(Object id) {

    if (id == null || !(id instanceof Long idCliente)) {
      return;
    }

    boolean eliminado = this.gestorCliente.eliminar(idCliente);

    if (eliminado) {

      mostrarAviso(Aviso.exito("Se eliminó el cliente."));

    } else {

      mostrarAviso(Aviso.advertencia("No se pudo eliminar el cliente."));
    }

    this.panelClientes.cerrarFormulario();

    listar();
  }

  /// Registra los eventos relacionados con la gestión de clientes.
  @Override
  public void registrarEventos() {

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_INICIO,
      (elemento) -> mostrarInicio()
    );

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_LISTAR,
      (elemento) -> listar()
    );

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_NUEVO,
      (elemento) -> nuevo()
    );

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_EDITAR,
      (elemento) -> editar(elemento)
    );

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_GUARDAR,
      (cliente) -> guardar(cliente)
    );

    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_ELIMINAR,
      (elemento) -> eliminar(elemento)
    );
    
    getEnrutador().registrarEvento(
      ControladorLogin.LOGIN_REGISTRAR,
      (elemento) -> nuevo()
    );
    
    getEnrutador().registrarEvento(
      ControladorCliente.CLIENTE_REGISTRAR,
      (datos) -> registrar(datos)
    );
  }
}

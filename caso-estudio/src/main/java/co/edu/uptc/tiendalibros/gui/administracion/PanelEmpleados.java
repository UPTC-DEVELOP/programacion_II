//
package co.edu.uptc.tiendalibros.gui.administracion;

import javax.swing.JButton;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.InfoColumnaTabla;
import co.edu.uptc.tiendalibros.gui.componentes.PanelGestionElementoBase;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;

/// Representa un panel para la gestión de los empleados y sus cuentas de usuario.
public class PanelEmpleados extends PanelGestionElementoBase<ElementoVistaEmpleado> {
  
  /// Botón para crear un empleado.
  private JButton btnCrear;
  
  /// Botón para editar un empleado.
  private JButton btnEditar;
  
  /// Crea un nuevo objeto [PanelEmpleados] para la gestión de empleados.
  ///
  /// @param evento Objeto que maneja eventos.
  public PanelEmpleados(Evento evento) {
    super(evento);
    // asignar el índice de la columna ID en la tabla de datos
    setColumnaID(0);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente();
    // nota: no invocar setLayout() porque borra todo lo hecho por la clase padre, y no se
    // verá nada.
  }
  
  @Override
  public String getTituloDialogoFormulario(ElementoVistaEmpleado elementoVista) {
    return elementoVista.getElemento()
      .id() == null ? "Crear Empleado" : "Editar Empleado";
  }
  
  @Override
  public FormularioElementoBase<ElementoVistaEmpleado, DatosEmpleado> crearFormulario() {
    return new FormularioEmpleado(getEvento());
  }
  
  @Override
  public void configurarBotones() {
    // crear y configurar el botón crear y su evento para crear un empleado
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(ControladorEmpleado.EMPLEADO_CREAR);
    this.btnCrear.addActionListener(getEvento());
    FabricaEstilos.fabrica(this.btnCrear)
      .botonCrear()
      .tamanioPreferido();
    
    // editar necesita el ID como dato, por eso se usa listener en lugar de actioncommand
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);
    FabricaEstilos.fabrica(this.btnEditar)
      .botonEditar()
      .tamanioPreferido();
    
    this.btnEditar.addActionListener((e) -> {
      int indiceFila = getPanelTabla().getIndiceFilaSeleccionada();
      solicitarEdicion(indiceFila);
    });
    
    // agregar los botones de acción a la barra de acciones
    getBarraAcciones().agregarBotonAccion(this.btnCrear);
    getBarraAcciones().agregarBotonAccion(this.btnEditar);
    
    // asignar la función que manejará el evento cuando el usuario seleccione una fila en la
    // tabla visual
    getPanelTabla()
      .setFuncionFilaSeleccionada((indiceFila) -> {
        // índice >= 0: el usuario seleccionó una fila
        // índice < 0: el usuario no ha seleccionado ninguna fila
        this.btnEditar.setEnabled(indiceFila >= 0);
      });
  }
  
  @Override
  public String getRutaEventoSolicitarEdicion() {
    // devolver la ruta que el controlador usa para editar
    return ControladorEmpleado.EMPLEADO_EDITAR;
  }
  
  @Override
  public InfoColumnaTabla[] getInfoColumnas() {
    // crear la información de las columnas del modelo de tabla de datos
    InfoColumnaTabla[] infoColumnas = new InfoColumnaTabla[] {
      // Infocolumna: (indice, encabezado, identificador, esEditable, esVisible)
      new InfoColumnaTabla(0, "ID", "id", false, false),
      new InfoColumnaTabla(1, "Nombre", "nombre", false, true),
      new InfoColumnaTabla(2, "Documento", "documento", false, true),
      new InfoColumnaTabla(3, "Correo electrónico", "correoElectronico", false, true),
      new InfoColumnaTabla(4, "Rol", "rol", false, true),
      new InfoColumnaTabla(5, "Cuenta habilitada", "habilitado", false, true),
    };
    
    return infoColumnas;
  }
}
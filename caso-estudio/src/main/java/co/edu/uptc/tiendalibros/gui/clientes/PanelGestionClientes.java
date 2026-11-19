
package co.edu.uptc.tiendalibros.gui.clientes;

import javax.swing.JButton;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.InfoColumnaTabla;
import co.edu.uptc.tiendalibros.gui.componentes.PanelGestionElementoBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Cliente;

/// Representa el panel principal para la gestión de clientes.
public class PanelGestionClientes
    extends PanelGestionElementoBase<ElementoVistaCliente> {

  /// Botón utilizado para registrar nuevos clientes.
  private JButton btnCrear;

  /// Botón utilizado para editar clientes seleccionados.
  private JButton btnEditar;

  /// Crea el panel de gestión de clientes.
  ///
  /// @param evento Objeto encargado de manejar los eventos del panel.
  public PanelGestionClientes(Evento evento) {
    super(evento);
    setColumnaID(0);
  }

  /// Inicializa los componentes visuales del panel.
  @Override
  public void inicializarComponente() {
    super.inicializarComponente();
  }

  /// Obtiene el título del formulario según la operación realizada.
  ///
  /// @param elementoVista Elemento vista del cliente seleccionado.
  /// @return Título del formulario.
  @Override
  public String getTituloDialogoFormulario(
      ElementoVistaCliente elementoVista) {

    return elementoVista.getElemento().getId() == null
        ? "Crear Cliente"
        : "Editar Cliente";
  }

  /// Crea el formulario utilizado para registrar y editar clientes.
  ///
  /// @return Formulario de clientes.
  @Override
  public FormularioElementoBase<ElementoVistaCliente, ?> crearFormulario() {
      return new FormularioCliente(getEvento());
  }

  /// Configura los botones de las acciones disponibles en el panel.
  @Override
  public void configurarBotones() {

    // Botón para crear un nuevo cliente.
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(ControladorCliente.CLIENTE_NUEVO);
    this.btnCrear.addActionListener(getEvento());

    // Botón para editar el cliente seleccionado.
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);

    this.btnEditar.addActionListener((e) -> {
      int indiceFila = getPanelTabla().getIndiceFilaSeleccionada();
      solicitarEdicion(indiceFila);
    });

    // Agrega los botones a la barra de acciones.
    getBarraAcciones().agregarBotonAccion(this.btnCrear);
    getBarraAcciones().agregarBotonAccion(this.btnEditar);

    // Habilita el botón editar cuando se selecciona una fila.
    getPanelTabla().setFuncionFilaSeleccionada((indiceFila) -> {
      this.btnEditar.setEnabled(indiceFila >= 0);
    });
  }

  /// Obtiene la ruta del evento para solicitar la edición de un cliente.
  ///
  /// @return Ruta del evento de edición.
  @Override
  public String getRutaEventoSolicitarEdicion() {
    return ControladorCliente.CLIENTE_EDITAR;
  }

  /// Define las columnas que se mostrarán en la tabla de clientes.
  ///
  /// @return Información de las columnas de la tabla.
  @Override
  public InfoColumnaTabla[] getInfoColumnas() {

    return new InfoColumnaTabla[] {
      new InfoColumnaTabla(0, "ID", "id", false, true),
      new InfoColumnaTabla(1, "Tipo documento", "tipoDocumento", false, true),
      new InfoColumnaTabla(2, "Número documento", "numeroDocumento", false, true),
      new InfoColumnaTabla(3, "Nombres", "nombres", false, true),
      new InfoColumnaTabla(4, "Apellidos", "apellidos", false, true),
      new InfoColumnaTabla(5, "Correo electrónico", "correoElectronico", false, true),
      new InfoColumnaTabla(6, "Teléfono", "telefono", false, true),
      new InfoColumnaTabla(7, "Dirección", "direccionEnvio", false, true),
      new InfoColumnaTabla(8, "Tipo cliente", "tipoCliente", false, true)
    };
  }
}

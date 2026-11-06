package co.edu.uptc.tiendalibros.gui.categoria;

import javax.swing.JButton;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.InfoColumnaTabla;
import co.edu.uptc.tiendalibros.gui.componentes.PanelGestionElementoBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Categoria;

// ElementoVistaCategoria está en el mismo paquete; no requiere import adicional
public class PanelCategorias extends PanelGestionElementoBase<ElementoVistaCategoria> {
  
  private JButton btnCrear;
  private JButton btnEditar;
  
  public PanelCategorias(Evento evento) {
    super(evento);
    setColumnaID(0);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente();
  }
  
  @Override
  public String getTituloDialogoFormulario(ElementoVistaCategoria elementoVista) {
    return elementoVista.getElemento().getId() == null ? "Crear Categoría" : "Editar Categoría";
  }

  @Override
  public FormularioElementoBase<ElementoVistaCategoria, Categoria> crearFormulario() {
    return new FormularioCategoria(getEvento());
  }
  
  @Override
  public void configurarBotones() {
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(ControladorCategoria.CATEGORIA_NUEVO);
    this.btnCrear.addActionListener(getEvento());
    
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);
    
    this.btnEditar.addActionListener((e) -> {
      int indiceFila = getPanelTabla().getIndiceFilaSeleccionada();
      solicitarEdicion(indiceFila);
    });
    
    getBarraAcciones().agregarBotonAccion(this.btnCrear);
    getBarraAcciones().agregarBotonAccion(this.btnEditar);
    
    getPanelTabla()
      .setFuncionFilaSeleccionada((indiceFila) -> {
        this.btnEditar.setEnabled(indiceFila >= 0);
      });
  }
  
  @Override
  public String getRutaEventoSolicitarEdicion() { return ControladorCategoria.CATEGORIA_EDITAR; }
  
  @Override
  public InfoColumnaTabla[] getInfoColumnas() {
    return new InfoColumnaTabla[] {
      new InfoColumnaTabla(0, "ID", "id", false, true),
      new InfoColumnaTabla(1, "Nombre", "nombre", false, true),
      new InfoColumnaTabla(2, "Descripción", "descripcion", false, true),
      new InfoColumnaTabla(3, "Código", "codigo", false, true),
    };
  }
}
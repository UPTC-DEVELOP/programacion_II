package main.co.edu.uptc.fesad.tpsi.tienda.gui.categoria;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.DialogoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.InfoColumnaTabla;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.ModeloTablaLista;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelLista;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;

public class PanelCategorias extends PanelBase {
  
  private static final int COLUMNA_ID = 0;
  
  private JButton btnCrear;
  private JButton btnEditar;
  
  private PanelLista panelLista;
  
  DialogoBase dialogo;
  
  public PanelCategorias(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    
    setLayout(new BorderLayout());
    
    JPanel barraAcciones = new JPanel();
    barraAcciones.setLayout(
      new FlowLayout(FlowLayout.LEFT, 4, 8)
    );
    
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(
      ControladorCategoria.CATEGORIA_NUEVO
    );
    this.btnCrear.addActionListener(getEvento());
    
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);
    this.btnEditar.addActionListener(
      (e) -> solicitarEdicion()
    );
    
    barraAcciones.add(this.btnCrear);
    barraAcciones.add(this.btnEditar);
    
    add(barraAcciones, BorderLayout.NORTH);
    
    InfoColumnaTabla[] infoColumnas = new InfoColumnaTabla[] {
      
      new InfoColumnaTabla(
        0, "ID", "id", false, true
      ),
      
      new InfoColumnaTabla(
        1, "Nombre", "nombre", false, true
      ),
      
      new InfoColumnaTabla(
        2, "Descripción", "descripcion", false, true
      ),
      
      new InfoColumnaTabla(
        3, "Código", "codigo", false, true
      )
    };
    
    ModeloTablaLista modeloTabla = new ModeloTablaLista(infoColumnas);
    
    this.panelLista = (PanelLista) new PanelLista(modeloTabla).construir();
    
    this.panelLista.setFuncionFilaSeleccionada(
      (indiceFila) -> {
        this.btnEditar.setEnabled(indiceFila >= 0);
      }
    );
    
    this.panelLista.setFuncionFilaActivada(
      (indiceFila) -> {
        solicitarEdicion();
      }
    );
    
    add(this.panelLista, BorderLayout.CENTER);
  }
  
  public void solicitarEdicion() {
    
    Object id = this.panelLista.getValorCeldaSeleccionada(
      PanelCategorias.COLUMNA_ID
    );
    
    if (id != null) {
      getEvento().manejar(
        ControladorCategoria.CATEGORIA_EDITAR,
        id
      );
    }
  }
  
  public void listar(Object[][] datos) {
    this.panelLista.listar(datos);
  }
  
  public void mostrarFormulario(Categoria categoria) {
    
    String titulo = categoria.getId() == null
      ? "Crear Categoría"
      : "Editar Categoría";
    
    Window propietario = SwingUtilities.getWindowAncestor(this);
    
    this.dialogo = new DialogoFormularioCategoria(
      propietario,
      titulo,
      categoria,
      getEvento()
    ).construir();
    
    this.dialogo.mostrar();
    
    this.dialogo = null;
  }
  
  public void cerrarFormulario() {
    
    if (this.dialogo != null) {
      this.dialogo.cerrar();
    }
  }
  
  public void mostrarError(String mensaje) {
    
    Component padre = (this.dialogo != null)
      ? this.dialogo
      : this;
    
    JOptionPane.showMessageDialog(
      padre,
      mensaje,
      "Aviso",
      JOptionPane.WARNING_MESSAGE
    );
  }
  
  public void mostrarDialogo(
    String titulo,
    Object elemento
  ) throws Exception {
    
    throw new Exception(
      "mostrar dialogo no implementado"
    );
  }
}
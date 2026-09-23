//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.util.LinkedHashMap;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/// Representa la tabla visual de datos con la cual el usuario interactúa directamente.
public class TablaBase extends JTable {
  
  // indica si el panel está inicializado completamente
  private boolean inicializado = false;
  
  public LinkedHashMap<Integer, InfoColumnaTabla> infoColumnas;
  
  public TablaBase() {
    super();
  }
  
  public TablaBase(ModeloTablaLista modeloTabla) {
    super(modeloTabla);
    this.infoColumnas = modeloTabla.getInfoColumnas();
  }
  
  protected void inicializarComponente() {
    // inicializar el componente
    InfoColumnaTabla[] infoColumnasArray = this.infoColumnas.values()
      .toArray(InfoColumnaTabla[]::new);
    setInfoColumnas(infoColumnasArray);
    
    setRowHeight(30);
  }
  
  public void setInfoColumnas(InfoColumnaTabla[] infoColumnas) {
    
    for (InfoColumnaTabla infoColumna : infoColumnas) {
      this.infoColumnas.put(infoColumna.indice(), infoColumna);
      // asignar los identificadores de las columnas
      getColumnModel().getColumn(infoColumna.indice())
        .setIdentifier(infoColumna.identificador());
    }
    
    // ocultar las columnas de la vista de usuario
    for (InfoColumnaTabla infoColumna : infoColumnas) {
      if (infoColumna.esVisible() == false) {
        TableColumn columnaOculta = this.getColumn(infoColumna.identificador());
        removeColumn(columnaOculta);
      }
    }
    
  }
  
  public InfoColumnaTabla getInfoColumna(int indiceColumna) {
    return this.infoColumnas.get(indiceColumna);
  }
  
  /// Termina la inicialización del componente. Invoca al método inicializarComponente().
  public final TablaBase construir() {
    
    if (!this.inicializado) {
      this.inicializado = true;
      inicializarComponente();
    }
    return this;
  }
}

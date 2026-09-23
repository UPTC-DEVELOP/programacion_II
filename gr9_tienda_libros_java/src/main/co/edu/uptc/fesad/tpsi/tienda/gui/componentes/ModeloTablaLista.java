//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

/// Representa el modelo lógico de datos de una tabla.
public class ModeloTablaLista extends DefaultTableModel {
  
  /// Guarda en forma ordenada la información de cómo configurar las columnas del modelo
  /// de tabla.
  public LinkedHashMap<Integer, InfoColumnaTabla> infoColumnas;
  
  private EsCeldaEditable funcionEsCeldaEditable;
  
  public ModeloTablaLista() {
    this(new InfoColumnaTabla[] {});
  }
  
  public ModeloTablaLista(InfoColumnaTabla[] infoColumnas) {
    super();
    this.infoColumnas = new LinkedHashMap<Integer, InfoColumnaTabla>();
    setInfoColumnas(infoColumnas);
    
    // esta es la función que decide si una celda es editable
    // de manera predeterminada se asigna si es editable dependiendo del info de la columna
    this.funcionEsCeldaEditable = (fila, columna) -> {
      return getInfoColumna(columna).esEditable();
    };
    
  }
  
  public LinkedHashMap<Integer, InfoColumnaTabla> getInfoColumnas() { return this.infoColumnas; }
  
  /// @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
  @Override
  public boolean isCellEditable(int row, int column) {
    // invocar la función que decide si la celda es editable
    return this.funcionEsCeldaEditable.evaluar(row, column);
  }
  
  public void setFuncionEsCeldaEditable(EsCeldaEditable funcion) {
    // Asignar la función dada si no es nula
    if (funcion != null) {
      this.funcionEsCeldaEditable = funcion;
    }
    
    // Notificar el cambio que es obligatorio según las reglas de un TableModel
    fireTableStructureChanged();
  }
  
  public void setInfoColumnas(InfoColumnaTabla[] infoColumnas) {
    String[] encabezadosColumna = new String[infoColumnas.length];
    
    for (InfoColumnaTabla infoColumna : infoColumnas) {
      this.infoColumnas.put(infoColumna.indice(), infoColumna);
      encabezadosColumna[infoColumna.indice()] = infoColumna.encabezado();
    }
    
    setColumnIdentifiers(encabezadosColumna);
    // Notificar el cambio que es obligatorio según las reglas de un TableModel
    fireTableStructureChanged();
  }
  
  public InfoColumnaTabla getInfoColumna(int indiceColumna) {
    return this.infoColumnas.get(indiceColumna);
  }
  
  /// Establece los datos que el modelo lógico de la tabla debe manejar.
  /// 
  /// @param datos Datos para el modelo lógico de la tabla.
  public void setDatos(Object[][] datos) {
    // primero, descartar todas las filas que existen previamente
    setRowCount(0);
    
    // el array de datos es un array de dos dimensiones:
    // - dimensión 0: los índices
    // - dimensión 1: un array de atributos
    
    // segundo, agregar fila por fila. Cada fila es un array de atributos
    for (Object[] fila : datos) {
      addRow(fila);
    }
  }
  
  public Object[] getEncabezadosColumnas() {
    Object[] encabezadosColumna = new Object[this.infoColumnas.values()
      .size()];
    int i = 0;
    for (InfoColumnaTabla infoColumna : this.infoColumnas.values()) {
      encabezadosColumna[i] = infoColumna.encabezado();
      i++;
    }
    
    return encabezadosColumna;
  }
  
}

package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

// Documentación de GridBagConstraints:
// https://docs.oracle.com/en/java/javase/25/docs/api/java.desktop/java/awt/GridBagConstraints.html

/// Representa un constructor de [GridBagConstraints] usando el patrón de diseño builder.
///
/// @author Andres Avila
public class RestriccionesCelda {
  /// Columna en la cuadrícula (eje X).
  public int gridx;
  
  /// Fila en la cuadrícula (eje Y).
  public int gridy;
  
  /// Ancho de columna: Las columnas que el componente ocupa.
  public int gridwidth;
  
  /// Alto de fila: Las filas que el componente ocupa.
  public int gridheight;
  
  /// Peso horizontal: factor para distribuir el espacio horizontal. 0.0 = no crece.
  public double weightx;
  
  /// Peso vertical: factor para distribuir el espacio verticial. 0.0 = no crece.
  public double weighty;
  
  /// Ancla: posición del componente dentro de su celda.
  public int anchor;
  
  /// Llenado o expansión: la forma como el componente llena su celda.
  public int fill;
  
  /// Márgenes externos (top, left, bottom, right).
  public Insets insets;
  
  /// Márgen interna horizontal.
  public int ipadx;
  
  /// Márgen interna vertical.
  public int ipady;
  
  /// Crea un nuevo objeto de [RestriccionesCelda] con los valores especificados.
  RestriccionesCelda(
    int gridx,
    int gridy,
    int gridwidth,
    int gridheight,
    double weightx,
    double weighty,
    int anchor,
    int fill,
    Insets insets,
    int ipadx,
    int ipady
  ) {
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridheight;
    this.weightx = weightx;
    this.weighty = weighty;
    this.anchor = anchor;
    this.fill = fill;
    this.insets = insets;
    this.ipadx = ipadx;
    this.ipady = ipady;
  }
  
  /// Crea un nuevo objeto de [RestriccionesCelda] con valores predeterminados.
  public RestriccionesCelda() {
    this.gridx = GridBagConstraints.RELATIVE;
    this.gridy = GridBagConstraints.RELATIVE;
    this.gridwidth = 1;
    this.gridheight = 1;
    this.weightx = 0.0d;
    this.weighty = 0.0d;
    this.anchor = GridBagConstraints.CENTER;
    this.fill = GridBagConstraints.NONE;
    this.insets = new Insets(0, 0, 0, 0);
    this.ipadx = 0;
    this.ipady = 0;
  }
  
  /// Columna en la cuadrícula (eje X).
  public RestriccionesCelda gridx(int gridx) {
    this.gridx = gridx;
    return this;
  }
  
  /// Fila en la cuadrícula (eje Y).
  public RestriccionesCelda gridy(int gridy) {
    this.gridy = gridy;
    return this;
  }
  
  /// Ancho de columna: Las columnas que el componente ocupa.
  public RestriccionesCelda gridwidth(int gridwidth) {
    this.gridwidth = gridwidth;
    return this;
  }
  
  /// Alto de fila: Las filas que el componente ocupa.
  public RestriccionesCelda gridheight(int gridheight) {
    this.gridheight = gridheight;
    return this;
  }
  
  /// Peso horizontal: factor para distribuir el espacio horizontal. 0.0 = no crece.
  public RestriccionesCelda weightx(double weightx) {
    this.weightx = weightx;
    return this;
  }
  
  /// Peso vertical: factor para distribuir el espacio verticial. 0.0 = no crece.
  public RestriccionesCelda weighty(double weighty) {
    this.weighty = weighty;
    return this;
  }
  
  /// Ancla: posición del componente dentro de su celda.
  ///
  /// Usar constantes de [GridBagConstraints].
  public RestriccionesCelda anchor(int anchor) {
    this.anchor = anchor;
    return this;
  }
  
  /// Llenado o expansión: la forma como el componente llena su celda.
  ///
  /// Usar constantes de [GridBagConstraints].
  public RestriccionesCelda fill(int fill) {
    this.fill = fill;
    return this;
  }
  
  /// Márgenes externos con un objeto [Insets] (top, left, bottom, right).
  public RestriccionesCelda insets(Insets insets) {
    this.insets = insets;
    return this;
  }
  
  /// Márgenes externos con valores (top, left, bottom, right).
  public RestriccionesCelda insets(int top, int left, int bottom, int right) {
    this.insets = new Insets(top, left, bottom, right);
    return this;
  }
  
  /// Márgen interna horizontal.
  public RestriccionesCelda ipadx(int ipadx) {
    this.ipadx = ipadx;
    return this;
  }
  
  /// Márgen interna vertical.
  public RestriccionesCelda ipady(int ipady) {
    this.ipady = ipady;
    return this;
  }
  
  /// Convierte este objeto a un objeto [GridBagConstraints] para ser usando por
  /// un [GridBagLayout].
  public GridBagConstraints aGridBagConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = this.gridx;
    gbc.gridy = this.gridy;
    gbc.gridwidth = this.gridwidth;
    gbc.gridheight = this.gridheight;
    gbc.weightx = this.weightx;
    gbc.weighty = this.weighty;
    gbc.anchor = this.anchor;
    gbc.fill = this.fill;
    gbc.insets = this.insets;
    gbc.ipadx = this.ipadx;
    gbc.ipady = this.ipady;
    return gbc;
  }
  
  /// Convierte este objeto a un objeto [GridBagConstraints] para ser usando por
  /// un [GridBagLayout].
  public GridBagConstraints aGBC() {
    return aGridBagConstraints();
  }
  
  /// Construe un un nuevo objeto [RestriccionesCelda] a a partir de los valores de
  /// este objeto.
  public RestriccionesCelda clonar() {
    return new RestriccionesCelda(
      this.gridx, this.gridy, this.gridwidth, this.gridheight, this.weightx, this.weighty, this.anchor, this.fill,
      this.insets, this.ipadx, this.ipady
    );
  }
  
  // estos métodos los estoy probando a ver si funcionan
  
  /// Hace que el componente se expanda horizontalmente.
  ///
  /// Equivale a `fill = HORIZONTAL, weightx = 1.0`.
  public RestriccionesCelda expandirHorizontal() {
    this.fill = GridBagConstraints.HORIZONTAL;
    this.weightx = 1.0d;
    return this;
  }
  
  /// Hace que el componente se expanda en ambas direcciones.
  ///
  /// Equivale a `(fill =BOTH, weight = 1.0)`.
  public RestriccionesCelda expandir() {
    this.fill = GridBagConstraints.BOTH;
    this.weightx = 1.0d;
    this.weighty = 1.0d;
    return this;
  }
  
  /// Ocupa el que el componente ocupe el resto de columnas disponibles en la fila.
  ///
  /// Equivale a `gridwidth = REMAINDER`.
  public RestriccionesCelda ocuparFila() {
    this.gridwidth = GridBagConstraints.REMAINDER;
    return this;
  }
  
  /// Ancla al oeste o izquierda.
  ///
  /// Equivale a `anchor = GridBagConstraints.WEST`.
  public RestriccionesCelda anclarIzquierda() {
    this.anchor = GridBagConstraints.WEST;
    return this;
  }
  
}

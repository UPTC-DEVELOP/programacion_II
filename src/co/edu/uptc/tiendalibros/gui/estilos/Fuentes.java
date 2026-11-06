//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Font;

//
public enum Fuentes {
  FUENTE_BASE("arvo", "Arvo-Bold.ttf", Font.PLAIN, 13f),
  FUENTE_ETIQUETA_CAMPO("arvo", "Arvo-Bold.ttf", Font.PLAIN, 12f),
  FUENTE_CAMPO("arvo", "Arvo-Bold.ttf", Font.PLAIN, 12f),
  FUENTE_BOTON("arvo", "Arvo-Bold.ttf", Font.BOLD, 13f),
  FUENTE_PESTANA("arvo", "Arvo-Bold.ttf", Font.BOLD, 14f),
  FUENTE_MENSAJE_ERROR("arvo", "Arvo-Bold.ttf", Font.BOLD, 13f);
  
  private String nombreDirectorio;
  private String nombreArchivo;
  private int estilo;
  private float tamano;
  
  private Font fuente;
  
  private Fuentes(
    String nombreDirectorio,
    String nombreArchivo,
    int estilo,
    float tamano
  ) {
    this.nombreDirectorio = nombreDirectorio;
    this.nombreArchivo = nombreArchivo;
    this.estilo = estilo;
    this.tamano = tamano;
  }
  
  public Font fuente() {
    if (this.fuente == null) {
      this.fuente = ManejadorRecursos.obtenerFuente(
        this.nombreDirectorio,
        this.nombreArchivo,
        this.estilo,
        this.tamano
      );
    }
    return this.fuente;
  }
  
  public static void precargar() {
    for (Fuentes fuente : Fuentes.values()) {
      fuente.fuente();
    }
  }
}

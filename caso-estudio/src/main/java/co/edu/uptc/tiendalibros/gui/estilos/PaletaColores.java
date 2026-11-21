//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Color;

/// Representa una paleta de colores. Hay colores según su propósito.
///
public enum PaletaColores {
  
  FONDO_VENTANA(0xF8F8F8),
  BORDE_SECCION(0xCDD0D8),
  TEXTO_PRINCIPAL(0x1A1F36),
  TEXTO_SECUNDARIO(0x5A6070),
  PRIMARIO(0xfdfdfd),
  CAMPO_BORDE(0xB0B8C8),
  PELIGRO(0xB03A2E),
  ERROR_TEXTO(0x922B21),
  ERROR_FONDO(0xFDF0EF),
  ERROR_BORDE(0xC0392B),
  MODULO_ACTIVO_FONDO(0xD6E6E6),
  MODULO_ACTIVO_ACENTO(0x588B8B);
  
  private Color color;
  
  private PaletaColores(int valor) {
    this.color = new Color(valor);
  }
  
  public Color color() {
    return this.color;
  }
}

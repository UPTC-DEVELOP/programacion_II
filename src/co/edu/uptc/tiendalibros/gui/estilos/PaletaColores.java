//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Color;

// estamos usando esta paleta de colores (usar sólo los primeros seis dígitos
// hexadecimales)
// https://coolors.co/588b8b-ffffff-ffd5c2-f28f3b-c8553d-2d3047-93b7be
// --dark-cyan: #588b8bff;
// --white: #fdfdfdff;
// --peach-fuzz: #ffd5c2ff;
// --tiger-orange: #f28f3bff;
// --rosy-copper: #c8553dff;
// --space-indigo: #2d3047ff;
// --light-blue: #93b7beff;

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

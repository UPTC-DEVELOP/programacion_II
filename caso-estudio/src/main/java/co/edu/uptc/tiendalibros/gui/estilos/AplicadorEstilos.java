//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

/// Aplica estilos visuales a un componente Swing. Cada método modifica de inmediato el
/// componente.
///
/// Los estilos se pueden encadenar uno tras otro. El orden de aplicar los estilos sí
/// importa.
///
/// Cada estilo ignora los componentes que no son del tipo que le corresponde.
public class AplicadorEstilos {
  
  /// Alto predeterminado de algunos componentes.
  private static final int ALTO_CAMPO = 30;
  
  /// Alto predeterminado para los botones.
  private static final int ALTO_BOTON = 42;
  
  /// Lado, en píxeles, de los íconos de los botones del menú lateral.
  private static final int LADO_ICONO_MENU_NAVEGACION = 24;
  
  /// Componente al que se le aplican los estilos visuales.
  private final JComponent componente;
  
  /// Crea un objeto [AplicadorEstilos] con el componente especificado.
  ///
  /// @param componente Componente al que se le aplican los estilos visuales.
  public AplicadorEstilos(JComponent componente) {
    this.componente = componente;
  }
  
  /// Aplica el estilo de una etiqueta de campo normal.
  public AplicadorEstilos etiqueta() {
    if (this.componente instanceof JLabel etiqueta) {
      etiqueta.setFont(Fuentes.FUENTE_ETIQUETA_CAMPO.fuente());
      etiqueta.setForeground(PaletaColores.TEXTO_SECUNDARIO.color());
      etiqueta.setPreferredSize(new Dimension(0, AplicadorEstilos.ALTO_CAMPO));
    }
    return this;
  }
  
  /// Aplica el estilo de un campo de texto con el número de columnas indicado.
  public AplicadorEstilos campoTexto(int columnas) {
    if (this.componente instanceof JTextField campo) {
      campo.setColumns(columnas);
      campo.setFont(Fuentes.FUENTE_CAMPO.fuente());
      campo.setForeground(PaletaColores.TEXTO_PRINCIPAL.color());
      campo.setBackground(Color.WHITE);
      campo.setMinimumSize(new Dimension(80, AplicadorEstilos.ALTO_CAMPO));
      campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, AplicadorEstilos.ALTO_CAMPO));
      bordeCampoNormal();
    }
    return this;
  }
  
  /// Aplica el estilo de un botón común.
  public AplicadorEstilos boton() {
    if (this.componente instanceof JButton boton) {
      boton.setFont(Fuentes.FUENTE_BOTON.fuente());
      boton.setForeground(PaletaColores.TEXTO_PRINCIPAL.color());
      boton.setBackground(PaletaColores.PRIMARIO.color());
    }
    return this;
  }
  
  /// Aplica estilo de botón con el ícono de crear.
  public AplicadorEstilos botonCrear() {
    boton();
    if (this.componente instanceof JButton boton) {
      if (IconoImagen.CREAR.icono()
        .isPresent()) {
        boton.setIcon(
          IconoImagen.CREAR.icono()
            .get()
        );
      }
    }
    return this;
  }
  
  /// Aplica estilo de botón con el ícono de editar.
  public AplicadorEstilos botonEditar() {
    boton();
    if (this.componente instanceof JButton boton) {
      if (IconoImagen.EDITAR.icono()
        .isPresent()) {
        boton.setIcon(
          IconoImagen.EDITAR.icono()
            .get()
        );
      }
    }
    return this;
  }
  
  /// Aplica estilo de botón con el ícono de guardar.
  public AplicadorEstilos botonGuardar() {
    boton();
    if (this.componente instanceof JButton boton) {
      if (IconoImagen.GUARDAR.icono()
        .isPresent()) {
        boton.setIcon(
          IconoImagen.GUARDAR.icono()
            .get()
        );
      }
    }
    return this;
  }
  
  /// Aplica estilo de botón más el ícono de eliminar.
  public AplicadorEstilos botonEliminar() {
    boton();
    if (this.componente instanceof JButton boton) {
      if (IconoImagen.ELIMINAR.icono()
        .isPresent()) {
        boton.setIcon(
          IconoImagen.ELIMINAR.icono()
            .get()
        );
      }
    }
    return this;
  }
  
  /// Aplica el estilo de un botón del menú lateral de navegación: el texto alineado a la
  /// izquierda y, junto a él, el ícono del módulo.
  ///
  /// El tamaño del botón se define por aparte después de aplicar este estilo.
  ///
  /// Si el ícono no se pudo cargar, el botón queda solo con texto.
  ///
  /// @param icono Ícono del módulo al que lleva el botón.
  public AplicadorEstilos botonMenuNavegacion(IconoImagen icono) {
    boton();
    if (this.componente instanceof JButton boton) {
      boton.setHorizontalAlignment(SwingConstants.LEFT);
      
      // obtener el ícono y asignarlo si fue cargado exitosamente
      Optional<ImageIcon> imagen = icono.icono(AplicadorEstilos.LADO_ICONO_MENU_NAVEGACION);
      
      if (imagen.isPresent()) {
        boton.setIcon(imagen.get());
      }
    }
    // asegurarse de que el botón de navegación tenga un estilo normal
    quitarModuloActivo();
    
    return this;
  }
  
  /// Aplica el estilo de una etiqueta de mensaje de error.
  public AplicadorEstilos notaError() {
    if (this.componente instanceof JLabel nota) {
      nota.setFont(Fuentes.FUENTE_MENSAJE_ERROR.fuente());
      nota.setForeground(PaletaColores.ERROR_TEXTO.color());
    }
    return this;
  }
  
  /// Aplica el estilo de un combo box.
  public AplicadorEstilos comboBox() {
    if (this.componente instanceof JComboBox<?> combo) {
      combo.setFont(Fuentes.FUENTE_CAMPO.fuente());
      combo.setForeground(PaletaColores.TEXTO_PRINCIPAL.color());
      combo.setBackground(Color.WHITE);
      combo.setMinimumSize(new Dimension(80, AplicadorEstilos.ALTO_CAMPO));
      combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, AplicadorEstilos.ALTO_CAMPO));
    }
    return this;
  }
  
  /// Hace que el componente de texto sea de solo lectura.
  public AplicadorEstilos soloLectura() {
    if (this.componente instanceof JTextComponent campo) {
      campo.setEditable(false);
    }
    return this;
  }
  
  /// Aplica el estilo visual de error: fondo y borde rojos.
  public AplicadorEstilos error() {
    this.componente.setBackground(PaletaColores.ERROR_FONDO.color());
    this.componente.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PaletaColores.ERROR_BORDE.color(), 1),
        BorderFactory.createEmptyBorder(2, 6, 2, 6)
      )
    );
    return this;
  }
  
  /// Quita el estilo de error y restaura el borde y fondo normales de un campo.
  public AplicadorEstilos quitarError() {
    this.componente.setBackground(Color.WHITE);
    bordeCampoNormal();
    return this;
  }
  
  /// Aplica negrita a la fuente actual del componente.
  ///
  /// Precondición: la fuente del componente ya fue establecida.
  public AplicadorEstilos enfasis() {
    Font fuenteActual = this.componente.getFont();
    if (fuenteActual != null) {
      this.componente.setFont(fuenteActual.deriveFont(Font.BOLD));
    }
    return this;
  }
  
  /// Aplica transparencia al fondo del componente con el valor alfa transparencia indicado
  /// (0 = invisible, 255 = opaco).
  ///
  /// Precondición: el color de fuente del componente ya fue establecido.
  public AplicadorEstilos transparente(int alfa) {
    Color fondo = this.componente.getBackground();
    this.componente.setOpaque(false);
    this.componente.setBackground(
      new Color(fondo.getRed(), fondo.getGreen(), fondo.getBlue(), alfa)
    );
    return this;
  }
  
  /// Aplica un borde normal a un campo.
  public AplicadorEstilos bordeCampoNormal() {
    Border borde = BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(PaletaColores.CAMPO_BORDE.color(), 1),
      BorderFactory.createEmptyBorder(2, 6, 2, 6)
    );
    this.componente.setBorder(borde);
    return this;
  }
  
  public AplicadorEstilos bordeSeccion(String titulo) {
    TitledBorder borde = BorderFactory
      .createTitledBorder(BorderFactory.createLineBorder(PaletaColores.BORDE_SECCION.color(), 1), titulo);
    
    borde.setTitleFont(
      Fuentes.FUENTE_BASE.fuente()
        .deriveFont(Font.BOLD, 13f)
    );
    
    borde.setTitleColor(PaletaColores.TEXTO_PRINCIPAL.color());
    
    Border bordeCompuesto = new CompoundBorder(borde, BorderFactory.createEmptyBorder(4, 8, 8, 8));
    
    this.componente.setBorder(bordeCompuesto);
    return this;
  }
  
  /// Establece el tamaño preferido del componente.
  ///
  /// Debe ser el último estilo, después de aplicar la fuente y el ícono.
  ///
  /// El ancho se calcula con el contenido del componente en este momento y queda fijo.
  /// Tampoco se vuelve calcular después de cambiar el texto o el íconop.
  public AplicadorEstilos tamanioPreferido() {
    int alto;
    
    if (this.componente instanceof JButton) {
      alto = AplicadorEstilos.ALTO_BOTON;
    } else {
      alto = AplicadorEstilos.ALTO_CAMPO;
    }
    this.componente.setPreferredSize(new Dimension(this.componente.getPreferredSize().width, alto));
    return this;
  }
  
  /// Estilo del conjunto de pestañas: fuente y colores de texto.
  public AplicadorEstilos pestanas() {
  this.componente.setFont(Fuentes.FUENTE_PESTANA.fuente());
  this.componente.setForeground(PaletaColores.TEXTO_PRINCIPAL.color());
  
  return this;
}
  
  /// Marca el botón de navegación como el módulo activo que se está visualizando.
  public AplicadorEstilos moduloActivo() {
    this.componente.setBackground(PaletaColores.MODULO_ACTIVO_FONDO.color());
    Border bordeModuloActivo = BorderFactory.createMatteBorder(0, 5, 0, 0, PaletaColores.MODULO_ACTIVO_ACENTO.color());
    this.componente.setBorder(crearBordeModuloBoton(bordeModuloActivo));
    return this;
  }
  
  /// Devuelve el botón de navegación a su apariencia normal porque es módulo que no
  /// está activo.
  public AplicadorEstilos quitarModuloActivo() {
    this.componente.setBackground(PaletaColores.PRIMARIO.color());
    Border bordeModuloNormalBorder = crearBordeModuloBoton(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    this.componente.setBorder(bordeModuloNormalBorder);
    return this;
  }
  
  /// Crea el borde de un botón del panel de navegación.
  private Border crearBordeModuloBoton(Border barra) {
    return BorderFactory.createCompoundBorder(
      barra,
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PaletaColores.CAMPO_BORDE.color(), 1),
        BorderFactory.createEmptyBorder(0, 10, 0, 10)
      )
    );
  }

  
}

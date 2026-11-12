//
package co.edu.uptc.tiendalibros.gui.libro;

import co.edu.uptc.tiendalibros.modelo.Dinero;
import co.edu.uptc.tiendalibros.modelo.EstadoEnCatalogo;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibroBuilder;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.TipoIVA;

/// Representa los datos de un formato de libro que un usuario ingresó en un formulario.
///
/// Estos datos es para la comunicación de la interfaz gráfica hacia el controlador.
public record DatosEntradaFormatoLibro(
  Long id,
  Long idLibro,
  TipoFormatoLibro tipoFormato,
  Integer inventario,
  Integer cantidadPaginas,
  EstadoEnCatalogo estadoLibroCatalogo,
  Dinero precioBase,
  TipoIVA tipoIVA
) {
  
  /// Convierte estos datos de entrada en un formato de libro.
  ///
  /// @return Un [FormatoLibro] con los mismos datos de este objeto.
  public FormatoLibro aFormatoLibro() {
    return new FormatoLibroBuilder()
      .id(this.id)
      .idLibro(this.idLibro)
      .tipoFormatoLibro(this.tipoFormato)
      .precioBase(this.precioBase)
      .tipoIVA(this.tipoIVA)
      .inventario(this.inventario)
      .cantidadPaginas(this.cantidadPaginas)
      .estadoEnCatalogo(this.estadoLibroCatalogo)
      .construir();
  }
  
  public static Builder builder() {
    return new Builder();
  }
  

  public static Builder aBuilder(DatosEntradaFormatoLibro otro) {
    return new Builder()
      .id(otro.id())
      .idLibro(otro.idLibro())
      .tipoFormato(otro.tipoFormato())
      .inventario(otro.inventario())
      .cantidadPaginas(otro.cantidadPaginas())
      .estadoLibroCatalogo(otro.estadoLibroCatalogo())
      .precioBase(otro.precioBase())
      .tipoIVA(otro.tipoIVA());
  }
  
  public static class Builder {
    private Long id;
    private Long idLibro;
    private TipoFormatoLibro tipoFormato;
    private Integer inventario;
    private Integer cantidadPaginas;
    private EstadoEnCatalogo estadoLibroCatalogo;
    private Dinero precioBase;
    private TipoIVA tipoIVA;
    
    private Builder() {
    }
    
    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder idLibro(Long idLibro) {
      this.idLibro = idLibro;
      return this;
    }
    
    public Builder tipoFormato(TipoFormatoLibro tipoFormato) {
      this.tipoFormato = tipoFormato;
      return this;
    }
    
    public Builder inventario(Integer inventario) {
      this.inventario = inventario;
      return this;
    }
    
    public Builder cantidadPaginas(Integer cantidadPaginas) {
      this.cantidadPaginas = cantidadPaginas;
      return this;
    }
    
    public Builder estadoLibroCatalogo(EstadoEnCatalogo estadoLibroCatalogo) {
      this.estadoLibroCatalogo = estadoLibroCatalogo;
      return this;
    }
    
    public Builder precioBase(Dinero precioBase) {
      this.precioBase = precioBase;
      return this;
    }
    
    public Builder tipoIVA(TipoIVA tipoIVA) {
      this.tipoIVA = tipoIVA;
      return this;
    }
    
    public DatosEntradaFormatoLibro construir() {
      return new DatosEntradaFormatoLibro(
        this.id,
        this.idLibro,
        this.tipoFormato,
        this.inventario,
        this.cantidadPaginas,
        this.estadoLibroCatalogo,
        this.precioBase,
        this.tipoIVA
      );
    }
  }
}

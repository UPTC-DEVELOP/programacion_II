//
package co.edu.uptc.tiendalibros.negocio.editorial;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.editorial.reglas.ReglaEditorialExiste;
import co.edu.uptc.tiendalibros.negocio.editorial.reglas.ReglaEditorialNombreLongitudMaxima;
import co.edu.uptc.tiendalibros.negocio.editorial.reglas.ReglaEditorialNombreNoVacio;
import co.edu.uptc.tiendalibros.negocio.editorial.reglas.ReglaEditorialNombreUnico;
import co.edu.uptc.tiendalibros.negocio.editorial.reglas.ReglaEditorialSinLibrosAsociados;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un gestor de la lógica de negocio para las editoriales.
public class GestionEditorial {
  
  /// Repositorio de datos de las editoriales.
  private final IRepositorioEditorial repositorioEditorial;
  
  /// Repositorio de datos de los libros.
  private final IRepositorioLibro repositorioLibro;
  
  /// Lista de reglas de validación para guardar una editorial
  List<IReglaValidacion<Editorial>> reglasValidacionGuardar;
  
  /// Lista de reglas de validación para eliminar una editorial
  List<IReglaValidacion<Editorial>> reglasValidacionEliminar;
  
  /// Crea un nuevo objeto [GestionEditorial].
  ///
  /// @param repositorio Repositorio de datos de la editorial.
  public GestionEditorial(
    IRepositorioEditorial repositorioEditorial,
    IRepositorioLibro repositorioLibro
  ) {
    this.repositorioEditorial = repositorioEditorial;
    this.repositorioLibro = repositorioLibro;
    
    // agregar las reglas de validación para guardar una editorial
    this.reglasValidacionGuardar = new ArrayList<>(
      (List.of(
        new ReglaEditorialNombreNoVacio(),
        new ReglaEditorialNombreLongitudMaxima(),
        new ReglaEditorialNombreUnico(this.repositorioEditorial)
      ))
    );
    
    // agregar las reglas de validación para eliminar una editorial
    this.reglasValidacionEliminar = new ArrayList<>(
      List.of(
        new ReglaEditorialSinLibrosAsociados(this.repositorioLibro)
      )
    );
  }
  
  /// Guarda la editorial especificada en el repositorio de datos.
  ///
  /// @param editorial Editorial que se quiere guardar.
  /// @return Respuesta con el resultado de la operación de guardar.
  public Respuesta<Editorial> guardar(Editorial editorial) {
    // objeto para notificar los errores
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // copiar la lista de reglas de validación para guardar
    List<IReglaValidacion<Editorial>> reglasParaValidar = new ArrayList<>(this.reglasValidacionGuardar);
    
    // si se quiere guardar o editar una editorial ya existente (ID != null), se debe agregar
    // la precondición de que la editorial existe en el repositorioEditorial
    if (editorial.getId() != null) {
      reglasParaValidar.addFirst(new ReglaEditorialExiste(this.repositorioEditorial));
    }
    
    // preparar la editorial antes de validar y guardar
    Editorial unaEditorial = prepararParaGuardar(editorial);
    
    // primero, verificar si no hubo errores de validación de datos
    for (IReglaValidacion<Editorial> regla : reglasParaValidar) {
      regla.validar(errores, unaEditorial);
    }
    
    if (errores.hayErrores()) {
      // devolver una respuesta fallida por errores de validación
      return new RespuestaFallida<Editorial>(
        Optional.of(unaEditorial), "Hay errores en los datos ingresados para la editorial.", errores
      );
    }
    
    // intentar guardar la editorial en el repositorioEditorial
    try {
      Editorial editorialGuardada = this.repositorioEditorial.guardar(editorial);
      
      // devolver una respuesta exitosa de la operación guardar
      return new RespuestaExitosa<Editorial>(Optional.of(editorialGuardada), "Se guardó la editorial.");
    } catch (ViolacionElementoNoExisteException excepcion) {
      // aquí, el error de la operación de guardar se agrega como un error de tipo general
      errores = new ResultadoValidacion();
      errores.agregarGeneral(excepcion.getMessage());
      
      // devolver una respuesta fallida de la operación guardar
      return new RespuestaFallida<Editorial>(
        Optional.of(editorial), String.format("No se pudo guardar la editorial '%s'", editorial.getNombre()), errores
      );
    }
    
  }
  
  /// Elimina la editorial con el ID especificado.
  ///
  /// @param id ID de la editorial que se quiere eliminar.
  /// @return Respuesta con el resultado de la operación de eliminar.
  public Respuesta<Editorial> eliminar(Long id) {
    // resultado de la validación de errores
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // buscar la editorial con el ID dado
    Optional<Editorial> editorial = this.repositorioEditorial.buscarPorId(id);
    
    // devolver una respuesta fallida si no se encontró el la editorial
    if (editorial.isEmpty()) {
      
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("La editorial ya no existe en el repositorio de datos.")
      );
      
      return new RespuestaFallida<Editorial>(
        Optional.empty(),
        "No se pudo eliminar la editorial.", errores
      );
    }
    
    // verificar si no hubo errores de validación de reglas de negocio
    for (IReglaValidacion<Editorial> regla : this.reglasValidacionEliminar) {
      regla.validar(errores, editorial.get());
    }
    
    // mostrar los errores generales
    if (errores.hayErrores()) {
      return new RespuestaFallida<>(
        editorial, String.format(
          "No se puede eliminar la editorial '%s'.",
          editorial.get()
            .getNombre()
        ), errores
      );
    }
    
    // aquí, procede a eliminar la editorial
    this.repositorioEditorial.eliminar(id);
    
    // devolver una respuesta exitosa de la operación guardar
    return new RespuestaExitosa<Editorial>(editorial, "Se eliminó la editorial.");
  }
  
  /// Carga las editoriales del repositorio de datos.
  ///
  /// @return Lista con las editoriales cargadas del repositorio.
  public List<Editorial> listar() {
    return this.repositorioEditorial.listar();
  }
  
  /// Busca la editorial con el ID especificado.
  ///
  /// @param id ID de la editorial que se quiere buscar.
  /// @return La editorial con el ID especificado.
  public Optional<Editorial> buscarPorId(Long id) {
    return this.repositorioEditorial.buscarPorId(id);
  }
  
  /// Prepara la editorial para ser guardada: limpia espacios en los atributos de texto y
  /// normaliza los datos.
  ///
  /// @param editorial Editorial que se desea preparar.
  /// @return La misma editorial con sus atributos normalizados.
  public Editorial prepararParaGuardar(Editorial editorial) {
    // limpiar el atributo 'nombre'
    editorial.setNombre(
      editorial.getNombre()
        .trim()
    );
    
    return editorial;
  }
  
}

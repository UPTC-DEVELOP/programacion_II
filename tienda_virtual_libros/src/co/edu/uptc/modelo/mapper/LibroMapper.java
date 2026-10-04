package co.edu.uptc.modelo.mapper;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.LibroDto;
import java.util.ArrayList;
import java.util.List;

public class LibroMapper {

    // Convierte DTO (viene de la GUI) a Entidad (va a Persistencia)
    public static Libro toEntity(LibroDto dto) {
        if (dto == null) return null;
        return new Libro(
                dto.getIsbn(), dto.getTitulo(), dto.getAutores(), dto.getAnioPublicacion(),
                dto.getEditorial(), dto.getNumPaginas(), dto.getPrecioVenta(), dto.getStock(),
                dto.getFormato(), dto.getCategoria()
        );
    }

    // Convierte Entidad (viene de Persistencia) a DTO (va a la GUI)
    public static LibroDto toDto(Libro entity) {
        if (entity == null) return null;
        return new LibroDto(
                entity.getIsbn(), entity.getTitulo(), entity.getAutores(), entity.getAnioPublicacion(),
                entity.getEditorial(), entity.getNumPaginas(), entity.getPrecioVenta(), entity.getStock(),
                entity.getFormato(), entity.getCategoria()
        );
    }

    // Convierte listas
    public static List<LibroDto> toDtoList(List<Libro> entidades) {
        List<LibroDto> dtos = new ArrayList<>();
        for (Libro entity : entidades) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }
}
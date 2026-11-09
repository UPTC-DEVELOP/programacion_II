package co.edu.uptc.persistencia;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IVentaRepository;

/**
 * Fábrica de repositorios de libros.
 *
 * Permite cambiar entre la implementación en memoria (LocalLibro) y la
 * implementación JSON (LibroJsonRepository) sin modificar la capa de negocio ni
 * la de presentación. El punto de composición (AppLibros) elige el tipo.
 *
 * Aplica: OCP (Open/Closed) y DIP (Dependency Inversion): las capas altas
 * dependen de ILibroRepositorio, y esta fábrica es el único lugar que conoce
 * las clases concretas.
 */
public final class LibroRepositoryFactory {

    /**
     * Tipos de almacenamiento soportados.
     */
    public enum Tipo {
        /** Lista en RAM con datos de ejemplo. */
        MEMORIA,
        /** Archivo data/libros.json. */
        JSON
    }

    /**
     * Constructor privado: la clase es de utilidad y no debe instanciarse.
     */
    private LibroRepositoryFactory() {
    }

    /**
     * Crea una implementación de ILibroRepositorio según el tipo indicado.
     *
     * @param tipo            Tipo de almacenamiento deseado
     * @param ventaRepository Repositorio de ventas usado por la implementación
     *                        JSON para validar RF03 (puede ser null)
     * @return Implementación concreta del repositorio de libros
     */
    public static ILibroRepositorio crear(Tipo tipo, IVentaRepository ventaRepository) {
        if (tipo == Tipo.MEMORIA) {
            return new LocalLibro();
        }
        return new LibroJsonRepository(ventaRepository);
    }

    /**
     * Atajo para crear la implementación JSON.
     *
     * @param ventaRepository Repositorio de ventas usado para validar RF03
     * @return Repositorio de libros persistido en data/libros.json
     */
    public static ILibroRepositorio crearJson(IVentaRepository ventaRepository) {
        return new LibroJsonRepository(ventaRepository);
    }
}

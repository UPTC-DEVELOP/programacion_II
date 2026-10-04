package com.tiendavirtual.repositorio;
import com.tiendavirtual.modelo.Libro; import java.util.List;
public interface LibroRepository { void guardar(Libro l); void actualizar(Libro l); void eliminar(String isbn); Libro buscarPorIsbn(String isbn); List<Libro> listar(); }

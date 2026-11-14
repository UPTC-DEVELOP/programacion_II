package co.uptc.edu.interfaces;

import java.util.List;

import co.uptc.edu.model.Libro;

//Contrato de persistencia del inventario de libros (memoria o base de datos)
public interface IGestionLibro {

    void guardar(Libro libro);

    //  Deja guardado el stock actual del libro (se usa al finalizar una compra)
    void actualizarStock(String isbn, int stock);

    List<Libro> listar();
}

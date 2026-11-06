package co.edu.uptc.tiendalibros.interfaces;

import java.util.List;

import co.edu.uptc.tiendalibros.modelo.Categoria;

public interface IRepositorioCategoria {
  
  Categoria guardar(Categoria categoria);
  
  boolean eliminar(Long id);
  
  List<Categoria> buscarPorId(Long id);
  
  List<Categoria> listar();
}
package main.co.edu.uptc.fesad.tpsi.tienda.interfaces;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;

public interface IRepositorioCategoria {
  
  Categoria guardar(Categoria categoria);
  
  boolean eliminar(Long id);
  
  List<Categoria> buscarPorId(Long id);
  
  List<Categoria> listar();
}
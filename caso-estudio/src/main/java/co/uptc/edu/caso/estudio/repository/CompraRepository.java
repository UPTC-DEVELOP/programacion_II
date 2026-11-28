package co.uptc.edu.caso.estudio.repository;

import java.util.List;
import co.uptc.edu.caso.estudio.negocio.Compra;

public interface CompraRepository {

    void guardar(Compra compra);

    List<Compra> listar();

    Compra buscarPorId(int idCompra);

    void actualizar(Compra compra);

    void eliminar(int idCompra);
}
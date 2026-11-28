package persistencia;

import java.util.ArrayList;
import java.util.List;

public interface IRepositorioConsulta<T> {

    List<T> listar();

    default List<T> listarUltimos(int limite) {
        List<T> todos = listar();
        if (limite <= 0 || todos.isEmpty()) {
            return new ArrayList<T>();
        }
        int desde = Math.max(0, todos.size() - limite);
        return new ArrayList<T>(todos.subList(desde, todos.size()));
    }

    default List<T> listarPagina(int inicio, int cantidad) {
        List<T> todos = listar();
        int desde = Math.max(0, inicio);
        int hasta = Math.min(todos.size(), desde + Math.max(0, cantidad));
        if (desde >= hasta) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(todos.subList(desde, hasta));
    }

    default int contar() {
        return listar().size();
    }
}

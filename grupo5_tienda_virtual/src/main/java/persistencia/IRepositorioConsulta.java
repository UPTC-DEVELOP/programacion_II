package persistencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IRepositorioConsulta<T> {

    /**
     * @deprecated usar listarUltimos() o listarPagina().
     */
    @Deprecated
    List<T> listar();

    /**
     * Devuelve los ultimos N registros, del mas reciente al mas antiguo.
     * El orden de referencia es el de listar(): insercion para
     * Clientes/Libros y fecha/id descendente para Compras.
     */
    @SuppressWarnings("deprecation")
    default List<T> listarUltimos(int limite) {
        List<T> todos = listar();
        if (limite <= 0 || todos.isEmpty()) {
            return new ArrayList<T>();
        }
        int desde = Math.max(0, todos.size() - limite);
        List<T> ultimos = new ArrayList<T>(todos.subList(desde, todos.size()));
        Collections.reverse(ultimos);
        return ultimos;
    }

    @SuppressWarnings("deprecation")
    default List<T> listarPagina(int inicio, int cantidad) {
        List<T> todos = listar();
        int desde = Math.max(0, inicio);
        int hasta = Math.min(todos.size(), desde + Math.max(0, cantidad));
        if (desde >= hasta) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(todos.subList(desde, hasta));
    }

    @SuppressWarnings("deprecation")
    default int contar() {
        return listar().size();
    }

    default List<T> buscar(String criterio) {
        return new ArrayList<T>();
    }
}

package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import excepciones.ExcepcionValidacion;
import persistencia.PersistenciaCarrito;

public class RepositorioCarrito {
    private final ArrayList<ItemCarrito> items = new ArrayList<>();
    private final PersistenciaCarrito persistencia = new PersistenciaCarrito();
    private final persistencia.PersistenciaCarritoJdbc jdbc;
    private final boolean modoJdbc = configuracion.ConfiguracionBD.carritoEnJdbc();
    private boolean cargado;

    public RepositorioCarrito() {
        this("local");
    }

    public RepositorioCarrito(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Correo obligatorio");
        }
        String id = "usuario:" +
            correo.trim().toLowerCase(java.util.Locale.ROOT);
        this.jdbc = new persistencia.PersistenciaCarritoJdbc(id);
    }

    public void cargar(Iterable<Libro> catalogo) throws IOException {
        if (cargado) return;
        List<PersistenciaCarrito.Registro> registros = modoJdbc ? jdbc.leer() : persistencia.leer();
        ArrayList<ItemCarrito> recuperados = new ArrayList<>();
        for (PersistenciaCarrito.Registro r : registros) {
            Libro encontrado = null;
            for (Libro l : catalogo) {
                if (mismoLibro(l, r.titulo, r.autor, r.formato, r.precio)) {
                    encontrado = l;
                    break;
                }
            }
            if (encontrado != null && r.cantidad > 0) {
                try {
                    if (indice(recuperados, encontrado) < 0) recuperados.add(new ItemCarrito(encontrado, r.cantidad));
                } catch (ExcepcionValidacion e) { throw new IOException("Carrito guardado inválido", e); }
            }
        }
        items.clear();
        items.addAll(recuperados);
        cargado = true;
    }

    public void agregar(Libro libro, int cantidad) throws ExcepcionValidacion, IOException {
        if (libro == null) throw new ExcepcionValidacion("Debe seleccionar un libro.");
        if (indice(items, libro) >= 0) throw new ExcepcionValidacion("El libro ya está en el carrito. Use Actualizar.");
        ItemCarrito item = new ItemCarrito(libro, cantidad);
        items.add(item);
        try { guardar(); } catch (IOException e) { items.remove(item); throw e; }
    }

    public ArrayList<ItemCarrito> listar() { return new ArrayList<>(items); }

    public void actualizarCantidad(int posicion, int cantidad) throws ExcepcionValidacion, IOException {
        validarPosicion(posicion);
        int anterior = items.get(posicion).getCantidad();
        items.get(posicion).setCantidad(cantidad);
        try { guardar(); } catch (IOException e) { items.get(posicion).setCantidad(anterior); throw e; }
    }

    public void eliminar(int posicion) throws ExcepcionValidacion, IOException {
        validarPosicion(posicion);
        ItemCarrito eliminado = items.remove(posicion);
        try { guardar(); } catch (IOException e) { items.add(posicion, eliminado); throw e; }
    }

    public void vaciar() throws IOException {
        ArrayList<ItemCarrito> respaldo = new ArrayList<>(items);
        items.clear();
        try { guardar(); } catch (IOException e) { items.addAll(respaldo); throw e; }
    }

    public long calcularTotal() {
        long total = 0;
        for (ItemCarrito i : items) total += (long)i.getLibro().getPrecio() * i.getCantidad();
        return total;
    }

    private void guardar() throws IOException { if (modoJdbc) jdbc.guardar(items); else persistencia.guardar(items); }

    private void validarPosicion(int posicion) throws ExcepcionValidacion {
        if (posicion < 0 || posicion >= items.size()) throw new ExcepcionValidacion("Seleccione un elemento válido del carrito.");
    }

    private static int indice(List<ItemCarrito> lista, Libro libro) {
        for (int i = 0; i < lista.size(); i++) if (lista.get(i).getLibro() == libro) return i;
        return -1;
    }

    private static boolean mismoLibro(Libro l, String titulo, String autor, String formato, int precio) {
        return l.getTitulo().equals(titulo) && l.getAutor().equals(autor)
            && l.getFormato().equals(formato) && l.getPrecio() == precio;
    }
}

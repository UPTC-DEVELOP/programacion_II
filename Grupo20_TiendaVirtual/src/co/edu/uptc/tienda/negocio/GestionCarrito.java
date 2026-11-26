package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.interfaz.IPersistenciaCarrito;
import co.edu.uptc.tienda.modelo.Carrito;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.RegistroCarrito;

public class GestionCarrito implements IGestionCarrito {

    private final Carrito carrito;
    private final List<IPersistenciaCarrito> persistencias;
    private boolean recuperado;

    public GestionCarrito(Carrito carrito, List<IPersistenciaCarrito> persistencias) {
        this.carrito = carrito;
        this.persistencias = persistencias;
        this.recuperado = false;
    }

    @Override
    public void agregarLibro(Libro libro) throws CarritoExcepcion {
        agregarLibro(libro, 1);
    }

    @Override
    public void agregarLibro(Libro libro, int cantidad) throws CarritoExcepcion {
        if (libro == null) {
            throw new CarritoExcepcion("Debe seleccionar un libro.");
        }
        if (cantidad <= 0) {
            throw new CarritoExcepcion("La cantidad debe ser mayor que 0.");
        }
        ItemCarrito existente = carrito.buscarItem(libro.getIsbn());
        int enCarrito = (existente == null) ? 0 : existente.getCantidad();
        int totalSolicitado = enCarrito + cantidad;
        if (totalSolicitado > libro.getStock()) {
            throw new CarritoExcepcion("No hay inventario suficiente. Solicitado: " + totalSolicitado
                    + ", disponible: " + libro.getStock() + ".");
        }
        List<ItemCarrito> itemsAntes = copiarItems();
        String clienteAntes = carrito.getIdCliente();
        if (existente == null) {
            carrito.agregarItem(new ItemCarrito(libro, cantidad));
        } else {
            existente.setCantidad(totalSolicitado);
        }
        guardarODeshacer(itemsAntes, clienteAntes);
    }

    @Override
    public void cambiarCantidad(String isbn, int nuevaCantidad) throws CarritoExcepcion {
        ItemCarrito item = carrito.buscarItem(isbn);
        if (item == null) {
            throw new CarritoExcepcion("El libro con ISBN " + isbn + " no está en el carrito.");
        }
        if (nuevaCantidad <= 0) {
            throw new CarritoExcepcion("La cantidad debe ser mayor que 0. Para quitar el libro use Eliminar.");
        }
        if (nuevaCantidad == item.getCantidad()) {
            throw new CarritoExcepcion("La nueva cantidad debe ser diferente a la actual.");
        }
        if (nuevaCantidad > item.getLibro().getStock()) {
            throw new CarritoExcepcion("No hay inventario suficiente. Solicitado: " + nuevaCantidad
                    + ", disponible: " + item.getLibro().getStock() + ".");
        }
        List<ItemCarrito> itemsAntes = copiarItems();
        String clienteAntes = carrito.getIdCliente();
        item.setCantidad(nuevaCantidad);
        guardarODeshacer(itemsAntes, clienteAntes);
    }

    @Override
    public void eliminarLibro(String isbn) throws CarritoExcepcion {
        ItemCarrito item = carrito.buscarItem(isbn);
        if (item == null) {
            throw new CarritoExcepcion("El libro con ISBN " + isbn + " no está en el carrito.");
        }
        List<ItemCarrito> itemsAntes = copiarItems();
        String clienteAntes = carrito.getIdCliente();
        carrito.quitarItem(item);
        guardarODeshacer(itemsAntes, clienteAntes);
    }

    @Override
    public void vaciar() throws CarritoExcepcion {
        List<ItemCarrito> itemsAntes = copiarItems();
        String clienteAntes = carrito.getIdCliente();
        carrito.vaciar();
        guardarODeshacer(itemsAntes, clienteAntes);
    }

    @Override
    public void asignarCliente(String idCliente) throws CarritoExcepcion {
        String actual = carrito.getIdCliente();
        boolean igual = (actual == null) ? idCliente == null : actual.equals(idCliente);
        if (igual) {
            return;
        }
        List<ItemCarrito> itemsAntes = copiarItems();
        carrito.setIdCliente(idCliente);
        guardarODeshacer(itemsAntes, actual);
    }

    @Override
    public String obtenerIdCliente() {
        return carrito.getIdCliente();
    }

    @Override
    public List<String> sincronizarConCatalogo(List<Libro> catalogo) throws CarritoExcepcion {
        List<String> avisos = new ArrayList<>();
        boolean cambio = false;
        if (!recuperado && !catalogo.isEmpty()) {
            recuperado = true;
            cambio = recuperar(catalogo, avisos);
        }
        for (ItemCarrito item : new ArrayList<>(carrito.getItems())) {
            Libro libro = item.getLibro();
            if (!catalogo.contains(libro)) {
                carrito.quitarItem(item);
                avisos.add("El libro \"" + libro.getTitulo() + "\" ya no está en el catálogo y se quitó del carrito.");
                cambio = true;
            } else if (libro.getStock() <= 0) {
                carrito.quitarItem(item);
                avisos.add("El libro \"" + libro.getTitulo() + "\" se quedó sin inventario y se quitó del carrito.");
                cambio = true;
            } else if (item.getCantidad() > libro.getStock()) {
                item.setCantidad(libro.getStock());
                avisos.add("La cantidad de \"" + libro.getTitulo() + "\" se ajustó a " + libro.getStock()
                        + " por el inventario disponible.");
                cambio = true;
            }
        }
        if (cambio) {
            try {
                guardar();
            } catch (CarritoExcepcion e) {
                avisos.add(e.getMessage());
            }
        }
        return avisos;
    }

    private List<RegistroCarrito> leerGuardado(List<String> avisos) {
        for (IPersistenciaCarrito persistencia : persistencias) {
            try {
                return persistencia.leer();
            } catch (CarritoExcepcion e) {
                avisos.add(e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private boolean recuperar(List<Libro> catalogo, List<String> avisos) {
        boolean descartado = false;
        for (RegistroCarrito registro : leerGuardado(avisos)) {
            Libro libro = buscarEnCatalogo(catalogo, registro.getIsbn());
            if (libro == null) {
                avisos.add("El libro con ISBN " + registro.getIsbn()
                        + " del carrito guardado no está en el catálogo y se descartó.");
                descartado = true;
            } else if (registro.getCantidad() <= 0 || carrito.buscarItem(libro.getIsbn()) != null) {
                avisos.add("Se descartó un registro repetido o sin cantidad del carrito guardado (ISBN "
                        + registro.getIsbn() + ").");
                descartado = true;
            } else {
                if (carrito.estaVacio()) {
                    carrito.setIdCliente(registro.getIdCliente());
                }
                carrito.agregarItem(new ItemCarrito(libro, registro.getCantidad()));
            }
        }
        return descartado;
    }

    private Libro buscarEnCatalogo(List<Libro> catalogo, String isbn) {
        for (Libro libro : catalogo) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    private void guardar() throws CarritoExcepcion {
        List<CarritoExcepcion> errores = guardarEnPersistencias();
        if (!errores.isEmpty()) {
            throw errores.get(0);
        }
    }

    private void guardarODeshacer(List<ItemCarrito> itemsAntes, String clienteAntes) throws CarritoExcepcion {
        List<CarritoExcepcion> errores = guardarEnPersistencias();
        if (errores.isEmpty()) {
            return;
        }
        if (errores.size() == persistencias.size()) {
            restaurar(itemsAntes, clienteAntes);
            throw new CarritoExcepcion("El carrito no se pudo guardar en ninguna parte y el cambio se deshizo.\n"
                    + errores.get(0).getMessage());
        }
        throw errores.get(0);
    }

    private List<CarritoExcepcion> guardarEnPersistencias() {
        List<CarritoExcepcion> errores = new ArrayList<>();
        if (!recuperado) {
            return errores;
        }
        List<RegistroCarrito> registros = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            registros.add(new RegistroCarrito(carrito.getIdCliente(), item.getLibro().getIsbn(), item.getCantidad()));
        }
        for (IPersistenciaCarrito persistencia : persistencias) {
            try {
                persistencia.guardar(registros);
            } catch (CarritoExcepcion e) {
                errores.add(e);
            }
        }
        return errores;
    }

    private List<ItemCarrito> copiarItems() {
        List<ItemCarrito> copia = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            copia.add(new ItemCarrito(item.getLibro(), item.getCantidad()));
        }
        return copia;
    }

    private void restaurar(List<ItemCarrito> items, String idCliente) {
        carrito.vaciar();
        for (ItemCarrito item : items) {
            carrito.agregarItem(item);
        }
        carrito.setIdCliente(idCliente);
    }

    @Override
    public List<ItemCarrito> obtenerItems() {
        return carrito.getItems();
    }

    @Override
    public double calcularSubtotal() {
        double suma = 0;
        for (ItemCarrito item : carrito.getItems()) {
            suma += item.obtenerSubtotalItem();
        }
        return suma;
    }

    @Override
    public double calcularImpuestos() {
        double suma = 0;
        for (ItemCarrito item : carrito.getItems()) {
            suma += item.obtenerImpuestoItem();
        }
        return suma;
    }

    @Override
    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuestos();
    }

    @Override
    public double calcularTotal(double porcentajeDescuento) {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        return calcularTotal() * (1 - porcentajeDescuento / 100.0);
    }
}

package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.IPersistenciaCarrito;
import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;
import co.edu.uptc.tienda.modelo.Carrito;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.RegistroCarrito;

public class GestionCarrito implements IGestionCarrito {

    private final Carrito carrito;
    private final List<IPersistenciaCarrito> persistencias;
    private final ControlLibro controlLibro;
    private final IRegistroOperaciones registro;
    private final IGestionCompra gestionCompra;
    private final List<RegistroCarrito> otrosClientes = new ArrayList<>();
    private List<Libro> catalogo = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private boolean recuperado;

    private String avisoCompra;

    public GestionCarrito(Carrito carrito, List<IPersistenciaCarrito> persistencias, ControlLibro controlLibro,
            IGestionCompra gestionCompra, IRegistroOperaciones registro) {
        this.carrito = carrito;
        this.persistencias = persistencias;
        this.controlLibro = controlLibro;
        this.gestionCompra = gestionCompra;
        this.registro = registro;
        this.recuperado = false;
    }

    @Override
    public void agregarLibro(Libro libro) throws CarritoExcepcion {
        agregarLibro(libro, 1);
    }

    @Override
    public void agregarLibro(Libro libro, int cantidad) throws CarritoExcepcion {
        if (carrito.getIdCliente() == null) {
            throw new CarritoExcepcion("Seleccione un cliente antes de agregar libros.");
        }
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
        guardarODeshacer(itemsAntes, clienteAntes, "Agregar libro",
                "Cliente " + clienteAntes + ", ISBN " + libro.getIsbn() + ", cantidad " + cantidad);
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
        guardarODeshacer(itemsAntes, clienteAntes, "Cambiar cantidad",
                "Cliente " + clienteAntes + ", ISBN " + isbn + ", nueva cantidad " + nuevaCantidad);
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
        guardarODeshacer(itemsAntes, clienteAntes, "Eliminar libro", "Cliente " + clienteAntes + ", ISBN " + isbn);
    }

    @Override
    public void vaciar() throws CarritoExcepcion {
        List<ItemCarrito> itemsAntes = copiarItems();
        String clienteAntes = carrito.getIdCliente();
        carrito.vaciar();
        guardarODeshacer(itemsAntes, clienteAntes, "Vaciar carrito", "Cliente " + clienteAntes);
    }

    @Override
    public Compra finalizarCompra(Cliente clienteConSesion, String metodoPago) throws CarritoExcepcion {
        avisoCompra = null;
        if (carrito.estaVacio()) {
            throw new CarritoExcepcion("El carrito está vacío.");
        }
        if (carrito.getIdCliente() == null) {
            throw new CarritoExcepcion("Debe seleccionar un cliente para finalizar la compra.");
        }
        if (clienteConSesion == null || !clienteConSesion.getId().equals(carrito.getIdCliente())) {
            throw new CarritoExcepcion("El cliente debe iniciar sesión para finalizar la compra.");
        }
        for (ItemCarrito item : carrito.getItems()) {
            Libro libro = item.getLibro();
            if (item.getCantidad() > libro.getStock()) {
                throw new CarritoExcepcion("No hay inventario suficiente de \"" + libro.getTitulo() + "\". Solicitado: "
                        + item.getCantidad() + ", disponible: " + libro.getStock() + ".");
            }
        }
        Compra compra;
        try {
            compra = gestionCompra.registrarCompra(clienteConSesion, metodoPago,
                    calcularPorcentajeDescuento(clienteConSesion), carrito.getItems());
        } catch (CompraExcepcion e) {
            throw new CarritoExcepcion(e.getMessage());
        }
        List<String> avisos = new ArrayList<>();
        if (gestionCompra.getAvisoGuardado() != null) {
            avisos.add(gestionCompra.getAvisoGuardado());
        }
        for (ItemCarrito item : carrito.getItems()) {
            try {
                controlLibro.descontarStock(item.getLibro().getIsbn(), item.getCantidad());
            } catch (LibroExcepcion e) {
                if (!avisos.contains(e.getMessage())) {
                    avisos.add(e.getMessage());
                }
            }
        }
        carrito.vaciar();
        try {
            guardar();
        } catch (CarritoExcepcion e) {
            avisos.add(e.getMessage());
        }
        if (!avisos.isEmpty()) {
            avisoCompra = "La compra se finalizó, pero hubo problemas al guardar:\n" + String.join("\n", avisos);
        }
        return compra;
    }

    @Override
    public String obtenerAvisoCompra() {
        return avisoCompra;
    }

    @Override
    public void asignarCliente(String idCliente) throws CarritoExcepcion {
        String actual = carrito.getIdCliente();
        boolean igual = (actual == null) ? idCliente == null : actual.equals(idCliente);
        if (igual) {
            return;
        }
        cargarCliente(idCliente);
        List<String> avisos = new ArrayList<>();
        if (revisarItems(avisos)) {
            try {
                guardar();
            } catch (CarritoExcepcion e) {
                avisos.add(e.getMessage());
            }
        }
        if (!avisos.isEmpty()) {
            throw new CarritoExcepcion(String.join("\n", avisos));
        }
    }

    private void cargarCliente(String idCliente) {
        if (carrito.getIdCliente() != null) {
            for (ItemCarrito item : carrito.getItems()) {
                otrosClientes.add(new RegistroCarrito(carrito.getIdCliente(), item.getLibro().getIsbn(),
                        item.getCantidad()));
            }
        }
        carrito.vaciar();
        carrito.setIdCliente(idCliente);
        if (idCliente == null) {
            return;
        }
        for (RegistroCarrito registro : new ArrayList<>(otrosClientes)) {
            if (registro.getIdCliente().equals(idCliente)) {
                otrosClientes.remove(registro);
                Libro libro = buscarEnCatalogo(catalogo, registro.getIsbn());
                if (libro != null) {
                    carrito.agregarItem(new ItemCarrito(libro, registro.getCantidad()));
                }
            }
        }
    }

    @Override
    public String obtenerIdCliente() {
        return carrito.getIdCliente();
    }

    @Override
    public List<String> sincronizarConCatalogo(List<Libro> catalogo, List<Cliente> clientes)
            throws CarritoExcepcion {
        List<String> avisos = new ArrayList<>();
        boolean cambio = false;
        this.catalogo = catalogo;
        this.clientes = clientes;
        if (!recuperado && !catalogo.isEmpty()) {
            recuperado = true;
            boolean descartado = recuperar(catalogo, avisos);
            cambio = descartado || !carrito.estaVacio() || !otrosClientes.isEmpty();
        }
        int quitados = otrosClientes.size();
        otrosClientes.removeIf(registro -> buscarEnCatalogo(catalogo, registro.getIsbn()) == null);
        quitados -= otrosClientes.size();
        if (quitados > 0) {
            avisos.add("De los carritos de otros clientes se quitaron libros que ya no están en el catálogo: "
                    + quitados + ".");
            cambio = true;
        }
        int sinCliente = otrosClientes.size();
        otrosClientes.removeIf(registro -> !existeCliente(registro.getIdCliente()));
        sinCliente -= otrosClientes.size();
        if (carrito.getIdCliente() != null && !existeCliente(carrito.getIdCliente())) {
            sinCliente += carrito.getItems().size();
            carrito.vaciar();
            carrito.setIdCliente(null);
        }
        if (sinCliente > 0) {
            avisos.add("Se borraron los carritos de los clientes que ya no existen.");
            cambio = true;
        }
        if (revisarItems(avisos)) {
            cambio = true;
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

    private boolean revisarItems(List<String> avisos) {
        boolean cambio = false;
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
        return cambio;
    }

    private List<RegistroCarrito> leerGuardado(List<String> avisos) {
        for (IPersistenciaCarrito persistencia : persistencias) {
            try {
                List<RegistroCarrito> registros = persistencia.leer();
                if (!registros.isEmpty()) {
                    return registros;
                }
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
            } else if (registro.getIdCliente() == null || registro.getCantidad() <= 0
                    || buscarRegistro(registro.getIdCliente(), registro.getIsbn()) != null) {
                avisos.add("Se descartó un registro repetido, sin cliente o sin cantidad del carrito guardado (ISBN "
                        + registro.getIsbn() + ").");
                descartado = true;
            } else if (!existeCliente(registro.getIdCliente())) {
                avisos.add("El cliente " + registro.getIdCliente()
                        + " del carrito guardado ya no existe y se descartó su libro (ISBN " + registro.getIsbn() + ").");
                descartado = true;
            } else {
                otrosClientes.add(registro);
            }
        }
        if (!otrosClientes.isEmpty()) {
            String idCliente = carrito.getIdCliente();
            carrito.setIdCliente(null);
            cargarCliente(idCliente != null ? idCliente : otrosClientes.get(0).getIdCliente());
        }
        return descartado;
    }

    private RegistroCarrito buscarRegistro(String idCliente, String isbn) {
        for (RegistroCarrito registro : otrosClientes) {
            if (registro.getIdCliente().equals(idCliente) && registro.getIsbn().equals(isbn)) {
                return registro;
            }
        }
        return null;
    }

    private boolean existeCliente(String idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(idCliente)) {
                return true;
            }
        }
        return false;
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
        if (errores.isEmpty()) {
            return;
        }
        if (errores.size() == persistencias.size()) {
            throw errores.get(0);
        }
        throw avisoGuardadoParcial(errores);
    }

    private CarritoExcepcion avisoGuardadoParcial(List<CarritoExcepcion> errores) {
        StringBuilder mensaje = new StringBuilder("El cambio se guardó, pero no en todas partes:");
        for (CarritoExcepcion error : errores) {
            mensaje.append("\n").append(error.getMessage());
        }
        return new CarritoExcepcion(mensaje.toString());
    }

    private void guardarODeshacer(List<ItemCarrito> itemsAntes, String clienteAntes, String operacion,
            String detalle) throws CarritoExcepcion {
        List<CarritoExcepcion> errores = guardarEnPersistencias();
        if (!errores.isEmpty() && errores.size() == persistencias.size()) {
            restaurar(itemsAntes, clienteAntes);
            throw new CarritoExcepcion("El carrito no se pudo guardar en ninguna parte y el cambio se deshizo.\n"
                    + errores.get(0).getMessage());
        }
        registro.registrar("Carrito", operacion, detalle);
        if (!errores.isEmpty()) {
            throw avisoGuardadoParcial(errores);
        }
    }

    private List<CarritoExcepcion> guardarEnPersistencias() {
        List<CarritoExcepcion> errores = new ArrayList<>();
        if (!recuperado) {
            return errores;
        }
        List<RegistroCarrito> registros = new ArrayList<>(otrosClientes);
        if (carrito.getIdCliente() != null) {
            for (ItemCarrito item : carrito.getItems()) {
                registros.add(new RegistroCarrito(carrito.getIdCliente(), item.getLibro().getIsbn(),
                        item.getCantidad()));
            }
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

    @Override
    public double calcularPorcentajeDescuento(Cliente cliente) {
        if (cliente == null) {
            return 0;
        }
        return cliente.obtenerPorcentajeDescuento() + gestionCompra.obtenerDescuentoFrecuente(cliente.getId());
    }
}

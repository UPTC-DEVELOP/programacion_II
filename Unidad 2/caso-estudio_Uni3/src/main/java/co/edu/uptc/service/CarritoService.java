package co.edu.uptc.service;

import co.edu.uptc.exception.StockInsuficienteException;
import co.edu.uptc.exception.ValidacionException;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import co.edu.uptc.persistence.CarritoRepository;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.RegistroOperaciones;

// CarritoService conecta el modelo del carrito con el catálogo y su almacenamiento.
public class CarritoService {
    private final LibroRepository libros; private final CarritoRepository carritos; private final RegistroOperaciones registro;
    public CarritoService(LibroRepository libros, CarritoRepository carritos, RegistroOperaciones registro) { this.libros=libros; this.carritos=carritos; this.registro=registro; }
    public Carrito cargar(String correo) { return carritos.buscarPorCliente(correo).orElseGet(() -> new Carrito(correo)); }
    public void agregar(Carrito carrito, String isbn, int cantidad) {
        if (cantidad <= 0) throw new ValidacionException("La cantidad debe ser positiva."); Libro libro=libros.buscarPorIsbn(isbn).orElseThrow(() -> new ValidacionException("El libro no existe.")); ItemCarrito item=carrito.buscar(isbn); int nueva=cantidad+(item==null?0:item.getCantidad());
        if (nueva > libro.getStock()) throw new StockInsuficienteException("No hay stock suficiente para el libro seleccionado.");
        if (item == null) carrito.agregar(new ItemCarrito(libro.getIsbn(),libro.getTitulo(),libro.getPrecioBase(),libro.getPorcentajeDescuento(),libro.getImpuestoIVA(),cantidad)); else for(int i=0;i<cantidad;i++) item.aumentar(); guardar(carrito); registro.registrar("INFO", carrito.getCorreoCliente(), "Cambio en carrito");
    }
    public void aumentar(Carrito carrito, String isbn) { agregar(carrito, isbn, 1); }
    public void disminuir(Carrito carrito, String isbn) { ItemCarrito item=carrito.buscar(isbn); if(item==null) throw new ValidacionException("El libro no está en el carrito."); item.disminuir(); guardar(carrito); }
    public void eliminar(Carrito carrito, String isbn) { carrito.eliminar(isbn); guardar(carrito); }
    public void vaciar(Carrito carrito) { carrito.vaciar(); guardar(carrito); }
    public void guardar(Carrito carrito) { if(carrito.getItems().isEmpty()) carritos.eliminar(carrito.getCorreoCliente()); else carritos.guardar(carrito); }
}

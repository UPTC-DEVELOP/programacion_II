package co.edu.uptc.service;

import co.edu.uptc.exception.SesionNoIniciadaException;
import co.edu.uptc.exception.StockInsuficienteException;
import co.edu.uptc.exception.ValidacionException;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.LineaPedido;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.MetodoPago;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.persistence.CarritoRepository;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.PedidoRepository;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.util.FechaUtil;
import java.util.ArrayList;
import java.util.List;

// CompraService coordina una compra completa usando solo interfaces y políticas inyectadas.
public class CompraService {
    private final LibroRepository libros; private final PedidoRepository pedidos; private final CarritoRepository carritos; private final RegistroOperaciones registro; private final ReciboService recibos; private final Sesion sesion; private final PoliticaDescuentoAdicional politica;
    public CompraService(LibroRepository libros, PedidoRepository pedidos, CarritoRepository carritos, RegistroOperaciones registro, ReciboService recibos, Sesion sesion, PoliticaDescuentoAdicional politica){this.libros=libros;this.pedidos=pedidos;this.carritos=carritos;this.registro=registro;this.recibos=recibos;this.sesion=sesion;this.politica=politica;}
    public Pedido finalizarCompra(Carrito carrito, MetodoPago metodo){ Cliente cliente=clienteActual(); if(carrito==null||carrito.getItems().isEmpty()) throw new ValidacionException("El carrito está vacío."); if(metodo==null) throw new ValidacionException("Seleccione un método de pago."); return procesar(cliente,carrito,metodo); }
    public Pedido registrarPedidoRapido(String isbn,int cantidad,MetodoPago metodo){ Cliente cliente=clienteActual(); Libro libro=libros.buscarPorIsbn(isbn).orElseThrow(()->new ValidacionException("El libro no existe.")); if(cantidad<=0) throw new ValidacionException("La cantidad debe ser positiva."); Carrito carrito=new Carrito(cliente.getCorreo()); carrito.agregar(new ItemCarrito(libro.getIsbn(),libro.getTitulo(),libro.getPrecioBase(),libro.getPorcentajeDescuento(),libro.getImpuestoIVA(),cantidad)); return procesar(cliente,carrito,metodo); }
    public List<Pedido> historialCliente(){ return pedidos.listarPorCliente(clienteActual().getCorreo()); }
    public List<Pedido> historialGlobal(){ return pedidos.listar(); }
    public void cancelarPedido(long id){ Pedido pedido=null; for(Pedido p:pedidos.listar()) if(p.getId()==id) pedido=p; if(pedido==null) throw new ValidacionException("El pedido no existe."); for(LineaPedido l:pedido.getLineas()) libros.buscarPorIsbn(l.getIsbn()).ifPresent(libro->{libro.reponerStock(l.getCantidad());libros.guardar(libro);}); pedidos.eliminar(id); registro.registrar("INFO",pedido.getCorreoCliente(),"Pedido cancelado #"+id); }
    private Pedido procesar(Cliente cliente,Carrito carrito,MetodoPago metodo){ ArrayList<LineaPedido> lineas=new ArrayList<LineaPedido>(); double subtotal=0; double impuestos=0; for(ItemCarrito item:carrito.getItems()){ Libro libro=libros.buscarPorIsbn(item.getIsbn()).orElseThrow(()->new ValidacionException("El libro ya no está disponible.")); if(item.getCantidad()>libro.getStock()) throw new StockInsuficienteException("Stock insuficiente para "+libro.getTitulo()); LineaPedido linea=new LineaPedido(libro.getIsbn(),libro.getTitulo(),libro.getPrecioBase(),libro.getPorcentajeDescuento(),libro.getImpuestoIVA(),item.getCantidad()); lineas.add(linea); subtotal+=linea.calcularSubtotal(); impuestos+=linea.calcularImpuesto(); }
        double bruto=redondear(subtotal+impuestos); double descuento=redondear(cliente.calcularDescuento(bruto)+politica.calcularDescuento(cliente,bruto,pedidos.listarPorCliente(cliente.getCorreo()).size())); double total=redondear(bruto-descuento); Pedido pedido=new Pedido(pedidos.siguienteId(),FechaUtil.ahora(),cliente.getCorreo(),lineas,redondear(subtotal),redondear(impuestos),descuento,total,metodo);
        for(LineaPedido linea:lineas){ Libro libro=libros.buscarPorIsbn(linea.getIsbn()).get(); libro.descontarStock(linea.getCantidad()); libros.guardar(libro); }
        pedidos.guardar(pedido); carritos.eliminar(cliente.getCorreo()); carrito.vaciar(); registro.registrar("INFO",cliente.getCorreo(),"Pedido #"+pedido.getId()+" registrado por "+total); recibos.generar(pedido); return pedido; }
    private Cliente clienteActual(){ if(!sesion.estaLogueado()||!(sesion.getUsuarioActual() instanceof Cliente)) throw new SesionNoIniciadaException("Debe iniciar sesión como cliente para comprar."); return (Cliente)sesion.getUsuarioActual(); }
    private double redondear(double valor){return Math.round(valor);}
}

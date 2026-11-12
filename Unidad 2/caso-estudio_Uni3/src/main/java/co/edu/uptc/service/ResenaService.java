package co.edu.uptc.service;

import co.edu.uptc.exception.ValidacionException;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.model.Resena;
import co.edu.uptc.persistence.PedidoRepository;
import co.edu.uptc.persistence.ResenaRepository;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.util.FechaUtil;

// ResenaService verifica que el cliente haya comprado el libro antes de guardar su opinión.
public class ResenaService {
    private final ResenaRepository resenas; private final PedidoRepository pedidos; private final RegistroOperaciones registro;
    public ResenaService(ResenaRepository resenas, PedidoRepository pedidos, RegistroOperaciones registro){this.resenas=resenas;this.pedidos=pedidos;this.registro=registro;}
    public Resena registrar(String isbn, String correo, int calificacion, String comentario){
        if(calificacion<1||calificacion>5) throw new ValidacionException("La calificación debe estar entre 1 y 5.");
        boolean comprado=false; for(Pedido p:pedidos.listarPorCliente(correo)) for(co.edu.uptc.model.LineaPedido l:p.getLineas()) if(l.getIsbn().equals(isbn)) comprado=true;
        if(!comprado) throw new ValidacionException("Solo puede reseñar libros que haya comprado.");
        if(resenas.existeParaCliente(isbn,correo)) throw new ValidacionException("Ya existe una reseña para este libro.");
        Resena resena=new Resena(isbn,correo,calificacion,comentario,FechaUtil.ahora()); resenas.guardar(resena); registro.registrar("INFO",correo,"Reseña registrada"); return resena;
    }
}

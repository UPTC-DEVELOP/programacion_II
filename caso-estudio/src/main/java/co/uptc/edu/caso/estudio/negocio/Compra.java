package co.uptc.edu.caso.estudio.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compra {

    // Atributos-
    private int idCompra;
    private Date fecha;
    private double total;
    private String estado;
    private List<DetalleCompra> detalles;

    // Lista estática para simular el almacenamiento CRUD en memoria
    private static List<Compra> listaCompras = new ArrayList<>();

    //  Constructores
    public Compra() {
        this.fecha = new Date();
        this.estado = "Pendiente";
        this.detalles = new ArrayList<>();
    }

    public Compra(int idCompra, String estado) {
        this();
        this.idCompra = idCompra;
        this.estado = estado;
    }

    // Métodos de lógica interna
    public void agregarDetalle(DetalleCompra detalle) {
        this.detalles.add(detalle);
        calcularTotal();
    }

    public double calcularTotal() {
        double acum = 0.0;
        for (DetalleCompra d : detalles) {
            acum += d.calcularSubtotal();
        }
        this.total = acum;
        return this.total;
    }

    public String generarResumen() {
        return "ID: " + idCompra + " | Estado: " + estado + " | Total: $" + total + " | Fecha: " + fecha;
    }

    public void confirmarCompra() {
        this.estado = "Confirmada";
        System.out.println("La compra #" + idCompra + " fue confirmada en el sistema.");
    }

    // CRUD 
    
    // C - CREATE: Registra el objeto actual en la lista
    public boolean guardar() {
        if (buscarPorId(this.idCompra) != null) {
            return false; // El ID ya existe
        }
        calcularTotal();
        return listaCompras.add(this);
    }

    // R - READ: Retorna todas las compras registradas
    public static List<Compra> obtenerTodas() {
        return listaCompras;
    }

    // R - READ: Busca una compra específica por su ID
    public static Compra buscarPorId(int id) {
        for (Compra c : listaCompras) {
            if (c.getIdCompra() == id) {
                return c;
            }
        }
        return null;
    }
    // U - UPDATE: Modifica el estado de una compra existente
    public static boolean actualizarEstado(int id, String nuevoEstado) {
        Compra c = buscarPorId(id);
        if (c != null) {
            c.setEstado(nuevoEstado);
            return true;
        }
        return false;
    }
    // D - DELETE: Elimina la compra de la lista
    public static boolean eliminar(int id) {
        Compra c = buscarPorId(id);
        if (c != null) {
            return listaCompras.remove(c);
        }
        return false;
    }
    // --- Getters y Setters ---
    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public Date getFecha() { return fecha; }

    public double getTotal() { return total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }

    public List<DetalleCompra> getDetalles() { return detalles; }
}

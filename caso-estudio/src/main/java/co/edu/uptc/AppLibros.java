package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.interfaces.ICarritoService;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IGestionResena;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.negocio.GestionResena;
import co.edu.uptc.persistencia.ReviewJdbcRepository;
import co.edu.uptc.negocio.CarritoServiceImpl;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionCompra;
import co.edu.uptc.negocio.GestionDashboard;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionReporte;
import co.edu.uptc.negocio.GestionSeguridad;
import co.edu.uptc.negocio.ValidadorDatos;
import co.edu.uptc.persistencia.AuditoriaCompuesta;
import co.edu.uptc.persistencia.AuditoriaRepository;
import co.edu.uptc.persistencia.AuditoriaTxtRepository;
import co.edu.uptc.persistencia.CarritoJdbcRepository;
import co.edu.uptc.persistencia.ClienteJdbcRepository;
import co.edu.uptc.persistencia.ClienteJsonRepository;
import co.edu.uptc.persistencia.FacturaJsonRepository;
import co.edu.uptc.persistencia.LibroJdbcRepository;
import co.edu.uptc.persistencia.VentaJdbcRepository;


/**
 * Punto de entrada de la Tienda Virtual de Libros.
 * Arquitectura multicapa con inyección de dependencias:
 *

 * 
 *   Persistencia  -> ClienteJsonRepository (espejo de data/clientes.json) /

 * 
 *   Persistencia  -> repositorios JDBC sobre PostgreSQL (libros, carrito,
 *                    ventas, reseñas) /
 *                    FacturaJsonRepository (data/facturas.json) /

 *                    AuditoriaRepository (data/auditoria.json)
 *   Validación    -> ValidadorDatos
 *   Negocio       -> GestionSeguridad / GestionCliente / GestionLibro /
 *                    GestionDashboard / CarritoServiceImpl (ICarritoService) /
 *                    GestionReporte
 *   Presentación  -> VentanaPrincipal (Swing)
 * 
 *

 * El CRUD de libros y el carrito se persisten únicamente en PostgreSQL.
 * La capa de negocio depende únicamente de interfaces (DIP).

 */
public class AppLibros {
    public static void main(String[] args) {
        // 1. Capa de Persistencia (se inyecta en la capa de negocio)
        /*modificacion:Brayan Panqueva*/
        // PostgreSQL es la implementación activa para clientes, libros y carrito.
        IClienteRepositorio repositorioCliente = new ClienteJdbcRepository();
        IClienteRepositorio repositorioClienteJson = new ClienteJsonRepository();
        VentaJdbcRepository repositorioVentaJdbc = new VentaJdbcRepository();
        IVentaRepository repositorioVenta = repositorioVentaJdbc;
        ICarritoRepository repositorioCarrito = new CarritoJdbcRepository();
        FacturaJsonRepository repositorioFacturaJson = new FacturaJsonRepository();

        // Auditoría de operaciones del administrador (RF17/RF18): el TXT es el
        // destino obligatorio y el JSON se conserva como espejo adicional.
        IAuditoria auditoria = new AuditoriaCompuesta(
                new AuditoriaTxtRepository(), new AuditoriaRepository());

        // Libros se persisten únicamente en PostgreSQL.
        ILibroRepositorio repositorioLibro = new LibroJdbcRepository(repositorioVenta);

        // 2. Capa de Validación
        ValidadorDatos validador = new ValidadorDatos();

        // 3. Capa de Negocio (Inyección de Dependencias)
        GestionSeguridad gestionSeguridad = new GestionSeguridad(repositorioCliente);
        GestionLibro gestionLibro = new GestionLibro(
                repositorioLibro, validador, auditoria);
        GestionCliente gestionCliente = new GestionCliente(repositorioCliente, validador,
                repositorioClienteJson, auditoria);
        // GestionDashboard inyecta DOS repositorios: libros (stock) y ventas (KPIs)
        GestionDashboard gestionDashboard = new GestionDashboard(repositorioLibro, repositorioVenta);

        // Servicio del carrito (RF10-RF13). Inyecta 3 abstracciones:
        // ICarritoRepository (persistencia JDBC), ILibroRepositorio (catálogo) e
        // IGestionCliente (validación de sesión + tipo de cliente para descuento).
        ICarritoService carritoService = new CarritoServiceImpl(
                repositorioCarrito, repositorioLibro, gestionCliente);

        // Servicio de reportes del administrador. Inyecta 3 abstracciones:
        // ICarritoRepository (carritos activos), IGestionLibro (títulos/precios)
        // e IClienteRepositorio (nombres de clientes).
        GestionReporte gestionReporte = new GestionReporte(
                repositorioCarrito, gestionLibro, repositorioCliente);
        /*modificacion:Brayan Panqueva*/
        // Servicio de gestión de compras. Inyecta 3 abstracciones:
        GestionCompra gestionCompra = new GestionCompra(
                repositorioCarrito, repositorioCliente, repositorioVentaJdbc);

        // Reseñas y calificaciones (RF19/RF20): repositorio JDBC + servicio de negocio.
        IGestionResena gestionResena = new GestionResena(
                new ReviewJdbcRepository(), repositorioLibro, repositorioCliente);

        // 4. Capa de Presentación (GUI)
        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(
                    gestionSeguridad, gestionCliente, gestionLibro, gestionDashboard,
                    carritoService, gestionReporte, gestionCompra, repositorioFacturaJson::guardar,
                    gestionResena);
            ventana.setVisible(true);
        });
    }
}

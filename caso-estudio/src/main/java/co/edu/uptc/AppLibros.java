package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.ICarritoService;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.negocio.CarritoServiceImpl;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionDashboard;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionReporte;
import co.edu.uptc.negocio.GestionSeguridad;
import co.edu.uptc.negocio.ValidadorDatos;
import co.edu.uptc.persistencia.AuditoriaRepository;
import co.edu.uptc.persistencia.CarritoJsonRepository;
import co.edu.uptc.persistencia.LibroRepositoryFactory;
import co.edu.uptc.persistencia.ClienteJsonRepository;
import co.edu.uptc.persistencia.LocalVenta;

/**
 * Punto de entrada de la Tienda Virtual de Libros.
 * Arquitectura multicapa con inyección de dependencias:
 *
 * <pre>
 *   Persistencia  -> ClienteJsonRepository (data/clientes.json) /
 *                    LibroJsonRepository (data/libros.json) /
 *                    LocalVenta / CarritoJsonRepository (data/carritos.json) /
 *                    AuditoriaRepository (data/auditoria.json)
 *   Validación    -> ValidadorDatos
 *   Negocio       -> GestionSeguridad / GestionCliente / GestionLibro /
 *                    GestionDashboard / CarritoServiceImpl (ICarritoService) /
 *                    GestionReporte
 *   Presentación  -> VentanaPrincipal (Swing)
 * </pre>
 *
 * <p>La persistencia de libros se selecciona a través de
 * {@link LibroRepositoryFactory}: con Tipo.JSON se usa data/libros.json y con
 * Tipo.MEMORIA se conserva el comportamiento histórico de LocalLibro. La capa
 * de negocio sigue dependiendo únicamente de ILibroRepositorio (DIP).</p>
 */
public class AppLibros {
    public static void main(String[] args) {
        // 1. Capa de Persistencia (se inyecta en la capa de negocio)
        // Clientes persistidos en data/clientes.json (contraseñas cifradas)
        IClienteRepositorio repositorioCliente = new ClienteJsonRepository();
        LocalVenta repositorioVenta = new LocalVenta(); // Datos semilla para el dashboard
        CarritoJsonRepository repositorioCarrito = new CarritoJsonRepository();

        // Auditoría de operaciones del administrador (RF17/RF18)
        IAuditoria auditoria = new AuditoriaRepository();

        // Repositorio de libros persistido en data/libros.json. Se inyecta el
        // repositorio de ventas para validar que un libro no tenga ventas (RF03).
        ILibroRepositorio repositorioLibro = LibroRepositoryFactory.crear(
                LibroRepositoryFactory.Tipo.JSON, repositorioVenta);

        // 2. Capa de Validación
        ValidadorDatos validador = new ValidadorDatos();

        // 3. Capa de Negocio (Inyección de Dependencias)
        GestionSeguridad gestionSeguridad = new GestionSeguridad(repositorioCliente);
        GestionLibro gestionLibro = new GestionLibro(repositorioLibro, validador, auditoria);
        GestionCliente gestionCliente = new GestionCliente(repositorioCliente, validador);
        // GestionDashboard inyecta DOS repositorios: libros (stock) y ventas (KPIs)
        GestionDashboard gestionDashboard = new GestionDashboard(repositorioLibro, repositorioVenta);

        // Servicio del carrito (RF10-RF13). Inyecta 3 abstracciones:
        // ICarritoRepository (persistencia JSON), ILibroRepositorio (catálogo) e
        // IGestionCliente (validación de sesión + tipo de cliente para descuento).
        ICarritoService carritoService = new CarritoServiceImpl(
                repositorioCarrito, repositorioLibro, gestionCliente);

        // Servicio de reportes del administrador. Inyecta 3 abstracciones:
        // ICarritoRepository (carritos activos), IGestionLibro (títulos/precios)
        // e IClienteRepositorio (nombres de clientes).
        GestionReporte gestionReporte = new GestionReporte(
                repositorioCarrito, gestionLibro, repositorioCliente);

        // 4. Capa de Presentación (GUI)
        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(
                    gestionSeguridad, gestionCliente, gestionLibro, gestionDashboard,
                    carritoService, gestionReporte);
            ventana.setVisible(true);
        });
    }
}

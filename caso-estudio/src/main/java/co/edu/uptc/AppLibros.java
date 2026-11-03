package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.interfaces.ICarritoService;
import co.edu.uptc.negocio.CarritoServiceImpl;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionDashboard;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionReporte;
import co.edu.uptc.negocio.GestionSeguridad;
import co.edu.uptc.negocio.ValidadorDatos;
import co.edu.uptc.persistencia.CarritoRepositoryEnMemoria;
import co.edu.uptc.persistencia.LocalCliente;
import co.edu.uptc.persistencia.LocalLibro;
import co.edu.uptc.persistencia.LocalVenta;

/**
 * Punto de entrada de la Tienda Virtual de Libros.
 * Arquitectura multicapa con inyección de dependencias:
 *
 *   Persistencia  -> LocalCliente / LocalLibro / LocalVenta / CarritoRepositoryEnMemoria
 *   Validación    -> ValidadorDatos
 *   Negocio       -> GestionSeguridad / GestionCliente / GestionLibro /
 *                    GestionDashboard / CarritoServiceImpl (ICarritoService) /
 *                    GestionReporte
 *   Presentación  -> VentanaPrincipal (Swing)
 *
 *  se agregan CarritoServiceImpl (RF10-RF13) y GestionReporte
 * (reportes del administrador), inyectados por constructor en la GUI.
 */
public class AppLibros {
    public static void main(String[] args) {
        // 1. Capa de Persistencia (se inyecta en la capa de negocio)
        LocalLibro repositorioLibro = new LocalLibro();
        LocalCliente repositorioCliente = new LocalCliente();
        LocalVenta repositorioVenta = new LocalVenta(); // Datos semilla para el dashboard
        CarritoRepositoryEnMemoria repositorioCarrito = new CarritoRepositoryEnMemoria();

        // 2. Capa de Validación
        ValidadorDatos validador = new ValidadorDatos();

        // 3. Capa de Negocio (Inyección de Dependencias)
        GestionSeguridad gestionSeguridad = new GestionSeguridad(repositorioCliente);
        GestionLibro gestionLibro = new GestionLibro(repositorioLibro, validador);
        GestionCliente gestionCliente = new GestionCliente(repositorioCliente, validador);
        // GestionDashboard inyecta DOS repositorios: libros (stock) y ventas (KPIs)
        GestionDashboard gestionDashboard = new GestionDashboard(repositorioLibro, repositorioVenta);

        // Servicio del carrito (RF10-RF13). Inyecta 3 abstracciones:
        // ICarritoRepository (persistencia), IGestionLibro (catálogo) e
        // IGestionCliente (validación de sesión + tipo de cliente para descuento).
        ICarritoService carritoService = new CarritoServiceImpl(
                repositorioCarrito, gestionLibro, gestionCliente);

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

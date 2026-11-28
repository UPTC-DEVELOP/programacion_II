package co.edu.uptc.gui;

import co.edu.uptc.negocio.BitacoraRepositorio;
import co.edu.uptc.negocio.BitacoraRepositorioTxt;
import co.edu.uptc.negocio.CarritoRepositorio;
import co.edu.uptc.negocio.CarritoRepositorioJson;
import co.edu.uptc.negocio.CarritoService;
import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.ClienteRepositorio;
import co.edu.uptc.negocio.ClienteRepositorioJson;
import co.edu.uptc.negocio.ClienteService;
import co.edu.uptc.negocio.CompraRepositorio;
import co.edu.uptc.negocio.CompraRepositorioJson;
import co.edu.uptc.negocio.CompraService;
import co.edu.uptc.negocio.LibroRepositorio;
import co.edu.uptc.negocio.LibroRepositorioJson;
import co.edu.uptc.negocio.PedidoRepositorio;
import co.edu.uptc.negocio.PedidoRepositorioJson;
import co.edu.uptc.negocio.PedidoService;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.ReciboService;
import co.edu.uptc.negocio.SesionService;
import co.edu.uptc.negocio.VentaRepositorio;
import co.edu.uptc.negocio.VentaRepositorioJson;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Runner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // Se conserva el look and feel por defecto si el sistema no lo permite.
            }

            try {
                Path carpetaDatos = Paths.get("data");

                // Persistencia existente de V2.
                LibroRepositorio libros = new LibroRepositorioJson(carpetaDatos.resolve("libros.json"));
                VentaRepositorio ventas = new VentaRepositorioJson(carpetaDatos.resolve("ventas.json"));
                PedidoRepositorio pedidos = new PedidoRepositorioJson(carpetaDatos.resolve("ventas.json"));
                BitacoraRepositorio bitacora = new BitacoraRepositorioTxt(carpetaDatos.resolve("bitacora.txt"));

                // Nuevos repositorios V3.
                ClienteRepositorio clientes = new ClienteRepositorioJson(carpetaDatos.resolve("clientes.json"));
                CarritoRepositorio carritos = new CarritoRepositorioJson(carpetaDatos.resolve("carritos.json"));
                CompraRepositorio compras = new CompraRepositorioJson(carpetaDatos.resolve("compras.json"));
                ReciboService recibos = new ReciboService(carpetaDatos.resolve("recibos"));

                // Servicios de negocio.
                CatalogoService servicioCatalogo = new CatalogoService(libros, ventas, bitacora);
                PedidoService servicioPedidos = new PedidoService(libros, pedidos, bitacora);
                ClienteService servicioClientes = new ClienteService(clientes, bitacora);
                CarritoService servicioCarrito = new CarritoService(libros, carritos, bitacora);
                SesionService sesion = new SesionService();
                CompraService servicioCompras = new CompraService(libros, compras, carritos, recibos, bitacora);

                // Vistas.
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                VentanaLibro ventanaLibro = new VentanaLibro();
                VentanaPedido ventanaPedido = new VentanaPedido();
                VentanaCliente ventanaCliente = new VentanaCliente();
                VentanaTienda ventanaTienda = new VentanaTienda();
                VentanaHistorialCompras ventanaHistorial = new VentanaHistorialCompras();

                // Controladores.
                ControladorGUI controladorCatalogo =
                        new ControladorGUI(ventanaPrincipal, ventanaLibro, servicioCatalogo);
                ControladorPedido controladorPedido =
                        new ControladorPedido(ventanaPrincipal, ventanaPedido, servicioPedidos);
                ControladorCliente controladorCliente =
                        new ControladorCliente(ventanaPrincipal, ventanaCliente, servicioClientes, sesion);
                ControladorTienda controladorTienda =
                        new ControladorTienda(ventanaPrincipal, ventanaTienda, ventanaHistorial,
                                servicioCatalogo, servicioCarrito, servicioCompras, sesion);

                // Sincronización entre módulos sin acoplar las capas de negocio.
                controladorCatalogo.setAlModificarCatalogo(() -> {
                    controladorPedido.recargarProductos();
                    controladorTienda.refrescarCatalogo();
                });
                controladorPedido.setAlModificarInventario(() -> {
                    controladorCatalogo.refrescarCatalogo();
                    controladorTienda.refrescarCatalogo();
                });
                controladorCliente.setAlCambiarSesion(controladorTienda::actualizarSesion);
                controladorTienda.setAlSolicitarClientes(controladorCliente::mostrarVentana);
                controladorTienda.setAlModificarInventario(controladorCatalogo::refrescarCatalogo);

                // Inicio de cada módulo.
                controladorPedido.iniciar();
                controladorTienda.iniciar();
                controladorCatalogo.iniciar();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error al iniciar", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}

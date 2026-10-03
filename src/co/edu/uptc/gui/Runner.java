package co.edu.uptc.gui;

import co.edu.uptc.negocio.BitacoraRepositorio;
import co.edu.uptc.negocio.BitacoraRepositorioTxt;
import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.LibroRepositorio;
import co.edu.uptc.negocio.LibroRepositorioJson;
import co.edu.uptc.negocio.PedidoRepositorio;
import co.edu.uptc.negocio.PedidoRepositorioJson;
import co.edu.uptc.negocio.PedidoService;
import co.edu.uptc.negocio.PersistenciaException;
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
               
            }

            try {
                
                Path carpetaDatos = Paths.get("data");
                LibroRepositorio libros = new LibroRepositorioJson(carpetaDatos.resolve("libros.json"));
                // Los pedidos se guardan en ventas.json: así el catálogo sabe qué libros tienen ventas
                VentaRepositorio ventas = new VentaRepositorioJson(carpetaDatos.resolve("ventas.json"));
                PedidoRepositorio pedidos = new PedidoRepositorioJson(carpetaDatos.resolve("ventas.json"));
                BitacoraRepositorio bitacora = new BitacoraRepositorioTxt(carpetaDatos.resolve("bitacora.txt"));

                // Negocio
                CatalogoService servicio = new CatalogoService(libros, ventas, bitacora);
                PedidoService servicioPedidos = new PedidoService(libros, pedidos, bitacora);

                // Interfaz gráfica + controlador
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                VentanaLibro ventanaLibro = new VentanaLibro();
                VentanaPedido ventanaPedido = new VentanaPedido();
                ControladorGUI controlador = new ControladorGUI(ventanaPrincipal, ventanaLibro, servicio);
                ControladorPedido controladorPedido =
                        new ControladorPedido(ventanaPrincipal, ventanaPedido, servicioPedidos);

                // Sincronización entre ventanas: cada controlador avisa al otro cuando algo cambia
                controlador.setAlModificarCatalogo(controladorPedido::recargarProductos);
                controladorPedido.setAlModificarInventario(controlador::refrescarCatalogo);

                controladorPedido.iniciar();
                controlador.iniciar();

            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error al iniciar", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}

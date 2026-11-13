package co.edu.uptc;

import javax.swing.SwingUtilities;

import co.edu.uptc.controller.ControladorPrincipal;
import co.edu.uptc.controller.LoginController;
import co.edu.uptc.persistence.CarritoRepository;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.PedidoRepository;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.persistence.ResenaRepository;
import co.edu.uptc.persistence.UsuarioRepository;
import co.edu.uptc.persistence.json.CarritoJsonRepository;
import co.edu.uptc.persistence.json.LibroJsonRepository;
import co.edu.uptc.persistence.json.PedidoJsonRepository;
import co.edu.uptc.persistence.json.ResenaJsonRepository;
import co.edu.uptc.persistence.json.UsuarioJsonRepository;
import co.edu.uptc.persistence.txt.RegistroOperacionesTxt;
import co.edu.uptc.service.AutenticacionService;
import co.edu.uptc.service.CarritoService;
import co.edu.uptc.service.CatalogoService;
import co.edu.uptc.service.CompraService;
import co.edu.uptc.service.DescuentoClienteFrecuente;
import co.edu.uptc.service.ReciboService;
import co.edu.uptc.service.ResenaService;
import co.edu.uptc.service.Sesion;
import co.edu.uptc.util.Rutas;
import co.edu.uptc.view.LoginFrame;
import co.edu.uptc.view.VentanaPrincipal;

// Main es la raíz de composición: aquí se conectan implementaciones y contratos.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibroRepository libros=new LibroJsonRepository(Rutas.libros()); UsuarioRepository usuarios=new UsuarioJsonRepository(Rutas.clientes()); PedidoRepository pedidos=new PedidoJsonRepository(Rutas.pedidos()); CarritoRepository carritos=new CarritoJsonRepository(Rutas.carritos()); ResenaRepository resenasRepo=new ResenaJsonRepository(Rutas.resenas()); RegistroOperaciones registro=new RegistroOperacionesTxt(Rutas.registroOperaciones());
            Sesion sesion=new Sesion(); AutenticacionService autenticacion=new AutenticacionService(usuarios,registro); CatalogoService catalogo=new CatalogoService(libros,pedidos,registro); CarritoService carrito=new CarritoService(libros,carritos,registro); ReciboService recibos=new ReciboService(Rutas.recibos()); CompraService compras=new CompraService(libros,pedidos,carritos,registro,recibos,sesion,new DescuentoClienteFrecuente()); ResenaService resenas=new ResenaService(resenasRepo,pedidos,registro);
            LoginFrame login=new LoginFrame(); new LoginController(login,autenticacion,sesion,()->new ControladorPrincipal(new VentanaPrincipal(),sesion.getUsuarioActual(),sesion,autenticacion,catalogo,carrito,compras,resenas,resenasRepo,libros)); login.setVisible(true);
        });
    }
}

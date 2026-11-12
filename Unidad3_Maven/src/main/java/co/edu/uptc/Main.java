package co.edu.uptc;

import co.edu.uptc.controller.ControladorPrincipal;
import co.edu.uptc.controller.LoginController;
import co.edu.uptc.persistence.*;
import co.edu.uptc.persistence.json.*;
import co.edu.uptc.persistence.txt.RegistroOperacionesTxt;
import co.edu.uptc.service.*;
import co.edu.uptc.util.Rutas;
import co.edu.uptc.view.LoginFrame;
import co.edu.uptc.view.VentanaPrincipal;
import javax.swing.SwingUtilities;

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

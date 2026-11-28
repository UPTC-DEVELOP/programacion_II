package co.edu.uptc.controller;

import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.Rol;
import co.edu.uptc.model.Usuario;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.ResenaRepository;
import co.edu.uptc.service.*;
import co.edu.uptc.view.*;

// ControladorPrincipal arma las pantallas del usuario autenticado y coordina sus refrescos.
public class ControladorPrincipal {
    private final VentanaPrincipal ventana; private final Usuario usuario; private final Sesion sesion; private final AutenticacionService autenticacion; private final CatalogoService catalogo; private final CarritoService carrito; private final CompraService compras; private final ResenaService resenas; private final ResenaRepository repositorioResenas; private final LibroRepository libros;
    public ControladorPrincipal(VentanaPrincipal ventana,Usuario usuario,Sesion sesion,AutenticacionService autenticacion,CatalogoService catalogo,CarritoService carrito,CompraService compras,ResenaService resenas,ResenaRepository repositorioResenas,LibroRepository libros){this.ventana=ventana;this.usuario=usuario;this.sesion=sesion;this.autenticacion=autenticacion;this.catalogo=catalogo;this.carrito=carrito;this.compras=compras;this.resenas=resenas;this.repositorioResenas=repositorioResenas;this.libros=libros;configurar();}
    private void configurar(){ventana.getLblUsuario().setText(usuario.getNombreCompleto()+" - "+usuario.getCorreo());ventana.getBtnCerrarSesion().addActionListener(e->{sesion.cerrar();ventana.dispose();});if(usuario.getRol()==Rol.ADMINISTRADOR){PanelCatalogoAdmin panel=new PanelCatalogoAdmin();new CatalogoController(panel,catalogo);ventana.agregarPestana("Gestión de Libros",panel);PanelHistorial historial=new PanelHistorial();new HistorialController(historial,compras,true);ventana.agregarPestana("Historial de Ventas",historial);}else{Cliente cliente=(Cliente)usuario;PanelTienda tienda=new PanelTienda();TiendaController tiendaController=new TiendaController(tienda,carrito,compras,cliente);tiendaController.cargarLibros(libros.listar());ventana.agregarPestana("Tienda",tienda);PanelCarrito panelCarrito=new PanelCarrito();new CarritoController(panelCarrito,carrito,compras,cliente);ventana.agregarPestana("Carrito",panelCarrito);PanelHistorial historial=new PanelHistorial();new HistorialController(historial,compras);ventana.agregarPestana("Mi Historial",historial);PanelPerfil perfil=new PanelPerfil();new PerfilController(perfil,autenticacion,cliente);ventana.agregarPestana("Mi Cuenta",perfil);PanelResenas panelResenas=new PanelResenas();new ResenaController(panelResenas,resenas,repositorioResenas,cliente);ventana.agregarPestana("Reseñas",panelResenas);}ventana.pack();ventana.setLocationRelativeTo(null);ventana.setVisible(true);}
}

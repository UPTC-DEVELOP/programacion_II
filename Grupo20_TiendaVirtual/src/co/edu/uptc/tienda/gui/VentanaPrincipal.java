package co.edu.uptc.tienda.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.interfaz.IPersistenciaCarrito;
import co.edu.uptc.tienda.modelo.Carrito;
import co.edu.uptc.tienda.negocio.ClienteConfig;
import co.edu.uptc.tienda.negocio.ControlLibro;
import co.edu.uptc.tienda.negocio.GestionCarrito;
import co.edu.uptc.tienda.persistencia.CarritoBD;
import co.edu.uptc.tienda.persistencia.CarritoJson;

public class VentanaPrincipal extends JFrame implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private ControlLibro controlLibro;
    private ClienteConfig clienteConfig;
    private JTabbedPane pestanas;
    private PanelCliente panelCliente;
    private PanelCarrito panelCarrito;
    private EventosCarrito eventosCarrito;

    public VentanaPrincipal() {

        setTitle("Tienda Virtual de Libros");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controlLibro = new ControlLibro();
        clienteConfig = new ClienteConfig();

        panelCliente = new PanelCliente(null);
        panelCliente.setEventos(new EventosCliente(panelCliente, clienteConfig));

        List<IPersistenciaCarrito> persistencias = new ArrayList<>();
        persistencias.add(new CarritoBD());
        persistencias.add(new CarritoJson("datos/carrito.json"));
        IGestionCarrito gestion = new GestionCarrito(new Carrito(), persistencias);
        panelCarrito = new PanelCarrito();
        eventosCarrito = new EventosCarrito(panelCarrito, gestion);
        panelCarrito.setEventos(eventosCarrito);

        pestanas = new JTabbedPane();
        pestanas.addTab("Libros", new VentanaLibro(controlLibro).getContentPane());
        pestanas.addTab("Clientes", panelCliente);
        pestanas.addTab("Carrito", panelCarrito);
        pestanas.addChangeListener(this);

        add(pestanas);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (pestanas.getSelectedComponent() == panelCarrito) {
            eventosCarrito.actualizarDatos(controlLibro.listarLibros(), clienteConfig.listar());
        }
    }

    public static void main(String[] args) {

        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}

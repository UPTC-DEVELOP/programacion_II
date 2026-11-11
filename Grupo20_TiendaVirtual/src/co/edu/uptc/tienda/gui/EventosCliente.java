package co.edu.uptc.tienda.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.ClienteConfig;

public class EventosCliente implements ActionListener {

    public final static String GUARDAR = "Guardar";
    public final static String BUSCAR = "Buscar";
    public final static String ACTUALIZAR = "Actualizar";
    public final static String ELIMINAR = "Eliminar";
    public final static String LIMPIAR = "Limpiar";

    private PanelCliente panelCliente;
    private ClienteConfig clienteConfig;

    public EventosCliente(PanelCliente panelCliente) {
        this(panelCliente, new ClienteConfig());
    }

    public EventosCliente(PanelCliente panelCliente, ClienteConfig clienteConfig) {
        this.panelCliente = panelCliente;
        this.clienteConfig = clienteConfig;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ejecutar(e);
        } catch (IllegalArgumentException ex) {
            panelCliente.mostrarAlerta("Clientes", ex.getMessage());
        }
    }

    private void ejecutar(ActionEvent e) {

        String evento = e.getActionCommand();

        if (evento.equals(GUARDAR)) {

            Cliente cliente = new Cliente(
                    panelCliente.getId(),
                    panelCliente.getNombre(),
                    panelCliente.getCorreo(),
                    panelCliente.getDireccion(),
                    panelCliente.getCelular(),
                    panelCliente.getTipoCliente()
            );

            clienteConfig.guardar(cliente);

            System.out.println("Cliente guardado");
        }

        else if (evento.equals(BUSCAR)) {

            Cliente cliente = clienteConfig.buscarPorId(panelCliente.getId());

            if (cliente != null) {
                panelCliente.mostrarCliente(cliente);
                System.out.println("Cliente encontrado");
                System.out.println("Nombre: " + cliente.getNombreCompleto());
                System.out.println("Correo: " + cliente.getCorreoElectronico());
            } else {
                System.out.println("Cliente no encontrado");
                panelCliente.mostrarAlerta("Clientes", "Cliente no encontrado.");
            }
        }

        else if (evento.equals(ACTUALIZAR)) {

            Cliente cliente = new Cliente(
                    panelCliente.getId(),
                    panelCliente.getNombre(),
                    panelCliente.getCorreo(),
                    panelCliente.getDireccion(),
                    panelCliente.getCelular(),
                    panelCliente.getTipoCliente()
            );

            clienteConfig.actualizar(cliente);

            System.out.println("Cliente actualizado");
        }

        else if (evento.equals(LIMPIAR)) {

            panelCliente.limpiarCampos();
        }

        else if (evento.equals(ELIMINAR)) {

            clienteConfig.eliminar(panelCliente.getId());

            System.out.println("Cliente eliminado");
        }
    }
}

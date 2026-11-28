package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.negocio.ClienteConfig;

public class EventosCliente implements ActionListener {

	//texto de los botones
	
    public final static String GUARDAR = "Añadir";
    public final static String BUSCAR = "Buscar";
    public final static String ACTUALIZAR = "Cambiar";
    public final static String ELIMINAR = "Borrar";
    public final static String LISTAR = "Leer";
    public final static String LIMPIAR = "Limpiar";
    public final static String INICIAR_SESION = "Iniciar sesión";
    public final static String CERRAR_SESION = "Cerrar sesión";
    public final static String HISTORIAL = "Historial de compras";

    private PanelCliente panelCliente;
    private ClienteConfig clienteConfig;

    public EventosCliente(PanelCliente panelCliente) {
        this(panelCliente, new ClienteConfig());
    }

    public EventosCliente(PanelCliente panelCliente, ClienteConfig clienteConfig) {
        this.panelCliente = panelCliente;
        this.clienteConfig = clienteConfig;
        if (clienteConfig.getAvisoCarga() != null) {
            panelCliente.mostrarAlerta("Clientes", clienteConfig.getAvisoCarga());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ejecutar(e);
        } catch (IllegalArgumentException ex) {
            panelCliente.mostrarAlerta("Clientes", ex.getMessage());
        }
        actualizarSesion();
    }

    public void actualizarSesion() {
        panelCliente.mostrarSesion(clienteConfig.getClienteLogueado());
    }

    private void ejecutar(ActionEvent e) {

        String evento = e.getActionCommand();

        if (evento.equals(GUARDAR)) {

            Cliente cliente = new Cliente(
                    panelCliente.getId().trim(),
                    panelCliente.getNombre().trim(),
                    panelCliente.getCorreo().trim(),
                    panelCliente.getDireccion().trim(),
                    panelCliente.getCelular().trim(),
                    panelCliente.getTipoCliente()
            );

            clienteConfig.guardar(cliente);

            //textos a eventos completados
            
            panelCliente.mostrarMensajeExito("Registro exitoso", "Cliente registrado correctamente.");
            panelCliente.limpiarCampos();
        }

        else if (evento.equals(BUSCAR)) {

            String id = panelCliente.getId().trim();
            if (id.isEmpty()) {
                panelCliente.mostrarAlerta("Validación", "Debe ingresar el ID del cliente para buscar.");
                return;
            }

            Cliente cliente = clienteConfig.buscarPorId(id);

            if (cliente != null) {
                panelCliente.mostrarCliente(cliente);
                panelCliente.mostrarMensajeExito("Búsqueda exitosa", "Cliente encontrado.");
            } else {
                panelCliente.mostrarAlerta("Clientes", "No existe un cliente registrado con ese ID.");
            }
        }

        else if (evento.equals(ACTUALIZAR)) {

            Cliente cliente = new Cliente(
                    panelCliente.getId().trim(),
                    panelCliente.getNombre().trim(),
                    panelCliente.getCorreo().trim(),
                    panelCliente.getDireccion().trim(),
                    panelCliente.getCelular().trim(),
                    panelCliente.getTipoCliente()
            );

            clienteConfig.actualizar(cliente);

            panelCliente.mostrarMensajeExito("Actualización exitosa", "Cliente actualizado correctamente.");
            panelCliente.limpiarCampos();
        }

        else if (evento.equals(ELIMINAR)) {

            String id = panelCliente.getId().trim();
            if (id.isEmpty()) {
                panelCliente.mostrarAlerta("Validación", "Debe ingresar el ID del cliente para eliminar.");
                return;
            }

            Cliente cliente = clienteConfig.buscarPorId(id);
            if (cliente == null) {
                panelCliente.mostrarAlerta("Clientes", "No existe un cliente con el ID ingresado.");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    panelCliente,
                    "¿Está seguro de eliminar el cliente?\n\n"
                            + "ID: " + cliente.getId() + "\n"
                            + "Nombre: " + cliente.getNombreCompleto(),
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                clienteConfig.eliminar(id);
                panelCliente.mostrarMensajeExito("Eliminación exitosa", "Cliente eliminado correctamente.");
                panelCliente.limpiarCampos();
            }
        }

        else if (evento.equals(LISTAR)) {
            listar();
        }

        else if (evento.equals(LIMPIAR)) {
            panelCliente.limpiarCampos();
        }

        else if (evento.equals(INICIAR_SESION)) {
            iniciarSesion();
        }

        else if (evento.equals(CERRAR_SESION)) {
            cerrarSesion();
        }

        else if (evento.equals(HISTORIAL)) {
            mostrarHistorial();
        }
    }

    private void iniciarSesion() {

        String correo = panelCliente.pedirCorreo();

        if (correo == null) {
            return;
        }

        if (correo.trim().isEmpty()) {
            panelCliente.mostrarAlerta("Validación", "Debe escribir el correo para iniciar sesión.");
            return;
        }

        if (clienteConfig.iniciarSesion(correo.trim())) {
            Cliente cliente = clienteConfig.getClienteLogueado();
            panelCliente.mostrarMensajeExito("Iniciar sesión", "Bienvenido(a), " + cliente.getNombreCompleto() + ".");
        } else {
            panelCliente.mostrarAlerta("Iniciar sesión", "No hay un cliente registrado con el correo " + correo.trim() + ".");
        }
    }

    private void cerrarSesion() {

        Cliente cliente = clienteConfig.getClienteLogueado();

        if (cliente == null) {
            panelCliente.mostrarAlerta("Cerrar sesión", "No hay ninguna sesión iniciada.");
            return;
        }

        clienteConfig.cerrarSesion();
        panelCliente.mostrarMensajeExito("Cerrar sesión", "Se cerró la sesión de " + cliente.getNombreCompleto() + ".");
    }

    private void mostrarHistorial() {

        Cliente cliente = clienteConfig.getClienteLogueado();

        if (cliente == null) {
            panelCliente.mostrarAlerta("Historial de compras", "Debe iniciar sesión para ver el historial de compras.");
            return;
        }

        List<Compra> compras;

        try {
            compras = clienteConfig.listarCompras(cliente.getId());
        } catch (CompraExcepcion ex) {
            panelCliente.mostrarAlerta("Historial de compras", ex.getMessage());
            return;
        }

        if (compras.isEmpty()) {
            panelCliente.mostrarMensajeExito("Historial de compras", cliente.getNombreCompleto() + " todavía no tiene compras.");
            return;
        }

        String[] columnas = {
            "N.º",
            "Fecha",
            "Método de pago",
            "Libros",
            "Subtotal",
            "IVA",
            "Descuento",
            "Total"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        double gastado = 0;

        for (Compra compra : compras) {
            Object[] fila = {
                compra.getNumero(),
                compra.getFecha(),
                compra.getMetodoPago(),
                compra.obtenerCantidadLibros(),
                String.format("$%,.2f", compra.getSubtotal()),
                String.format("$%,.2f", compra.getImpuestos()),
                String.format("$%,.2f (%.0f %%)", compra.getDescuento(), compra.getPorcentajeDescuento()),
                String.format("$%,.2f", compra.getTotal())
            };
            modelo.addRow(fila);
            gastado += compra.getTotal();
        }

        JTable tabla = new JTable(modelo);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(850, 250));

        double frecuente = clienteConfig.obtenerDescuentoFrecuente(cliente.getId());
        String resumen = cliente.getNombreCompleto() + ": " + compras.size() + " compra(s), total gastado "
                + String.format("$%,.2f", gastado) + ". "
                + (frecuente > 0 ? String.format("Cliente frecuente: %.0f %% de descuento en la próxima compra.", frecuente)
                        : "Aún no tiene descuento por cliente frecuente.");

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.add(new JLabel(resumen), BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                panelCliente,
                contenido,
                "Historial de compras",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void listar() {

        List<Cliente> clientes = new ArrayList<>(clienteConfig.listar());

        if (clientes.isEmpty()) {
            panelCliente.mostrarMensajeExito("Listado de clientes", "No hay clientes registrados.");
            return;
        }

        clientes.sort(Comparator.comparing(Cliente::getId));

        String[] columnas = {
            "ID",
            "Nombre Completo",
            "Correo",
            "Dirección",
            "Celular",
            "Tipo"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        for (Cliente cliente : clientes) {
            Object[] fila = {
                cliente.getId(),
                cliente.getNombreCompleto(),
                cliente.getCorreoElectronico(),
                cliente.getDireccionEnvio(),
                cliente.getCelular(),
                cliente.getTipoCliente()
            };
            modelo.addRow(fila);
        }

        JTable tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(750, 300));

        JOptionPane.showMessageDialog(
                panelCliente,
                scroll,
                "Listado de clientes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

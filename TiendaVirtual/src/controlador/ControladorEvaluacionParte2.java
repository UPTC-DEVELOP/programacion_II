package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import excepciones.ExcepcionValidacion;
import modelo.Pedido;
import modelo.Producto;
import modelo.RepositorioProductos;
import vista.VistaEvaluacionParte2;

public class ControladorEvaluacionParte2 implements ActionListener {
    private VistaEvaluacionParte2 vista;
    private RepositorioProductos repositorioProductos;
    private ArrayList<Pedido> historialPedidos;

    public ControladorEvaluacionParte2(VistaEvaluacionParte2 vista,
            RepositorioProductos repositorioProductos) {
        this.vista = vista;
        this.repositorioProductos = repositorioProductos;
        this.historialPedidos = new ArrayList<Pedido>();

        vista.cargarProductos(repositorioProductos.getProductos());
        vista.getComboProductos().addActionListener(this);
        vista.getBotonRegistrarPedido().addActionListener(this);
        vista.getBotonCancelarPedido().addActionListener(this);
        vista.getBotonCerrar().addActionListener(this);

        actualizarDatosProducto();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();

        if (origen == vista.getComboProductos()) {
            actualizarDatosProducto();
        } else if (origen == vista.getBotonRegistrarPedido()) {
            registrarPedido();
        } else if (origen == vista.getBotonCancelarPedido()) {
            cancelarPedidoSeleccionado();
        } else if (origen == vista.getBotonCerrar()) {
            vista.dispose();
        }
    }

    private void actualizarDatosProducto() {
        vista.mostrarDatosProducto(vista.getProductoSeleccionado());
    }

    private void registrarPedido() {
        try {
            Producto producto = vista.getProductoSeleccionado();
            if (producto == null) {
                throw new ExcepcionValidacion("Seleccione un producto.");
            }

            String textoCantidad = vista.getCantidadTexto();
            if (textoCantidad.isEmpty()) {
                throw new ExcepcionValidacion("Ingrese la cantidad del pedido.");
            }

            int cantidad = Integer.parseInt(textoCantidad);
            producto.descontarStock(cantidad);

            Pedido pedido = new Pedido(producto, cantidad);
            historialPedidos.add(pedido);
            vista.agregarPedido(pedido);
            vista.mostrarDatosProducto(producto);
            vista.limpiarCantidad();

            JOptionPane.showMessageDialog(
                    vista,
                    "Pedido registrado correctamente.\nTotal a pagar: "
                            + String.format("$%,.0f", pedido.getTotal())
                                    .replace(',', '.'),
                    "Pedido registrado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            mostrarAlerta("La cantidad debe ser un número entero válido.");
        } catch (ExcepcionValidacion e) {
            mostrarAlerta(e.getMessage());
        }
    }

    private void cancelarPedidoSeleccionado() {
        try {
            int fila = vista.getFilaSeleccionada();
            if (fila < 0 || fila >= historialPedidos.size()) {
                throw new ExcepcionValidacion(
                        "Seleccione un pedido de la tabla para cancelarlo.");
            }

            Pedido pedido = historialPedidos.get(fila);
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Cancelar el pedido seleccionado y devolver "
                            + pedido.getCantidad() + " unidad(es) al stock?",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION);

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }

            pedido.getProducto().reponerStock(pedido.getCantidad());
            historialPedidos.remove(fila);
            vista.eliminarFila(fila);
            vista.seleccionarProducto(pedido.getProducto());
            vista.mostrarDatosProducto(pedido.getProducto());

            JOptionPane.showMessageDialog(
                    vista,
                    "Pedido cancelado. El stock fue restablecido.",
                    "Pedido cancelado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ExcepcionValidacion e) {
            mostrarAlerta(e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        JOptionPane.showMessageDialog(
                vista,
                mensaje,
                "Revise los datos",
                JOptionPane.WARNING_MESSAGE);
    }
}

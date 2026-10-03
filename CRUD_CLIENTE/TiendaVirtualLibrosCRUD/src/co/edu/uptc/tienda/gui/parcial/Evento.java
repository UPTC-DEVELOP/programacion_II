package co.edu.uptc.tienda.gui.parcial;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import co.edu.uptc.negocio.parcial.Producto;

public class Evento
        extends MouseAdapter
        implements ActionListener {

    private PanelExpresion panel;

    private VentanaPrincipal_Parcial ventana;

    public Evento(
            PanelExpresion panel,
            VentanaPrincipal_Parcial ventana) {

        this.panel = panel;
        this.ventana = ventana;
    }

    /**
     * Detecta el clic sobre Producto.
     */
    @Override
    public void mouseClicked(
            MouseEvent e) {

        if (e.getSource() ==
                panel.getComboProductos()) {

            solicitarProducto();
        }
    }

    /**
     * Solicita el nombre del producto.
     */
    private void solicitarProducto() {

        String nombreProducto =
                JOptionPane.showInputDialog(
                        panel,
                        "Ingrese el nombre del producto:",
                        "Nuevo Producto",
                        JOptionPane.QUESTION_MESSAGE);

        /*
         * Si presiona Cancelar.
         */
        if (nombreProducto == null) {

            return;
        }

        nombreProducto =
                nombreProducto.trim();

        /*
         * Validar nombre.
         */
        if (nombreProducto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    panel,
                    "Debe ingresar un nombre de producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Verificar si el producto
         * ya existe.
         */
        Producto productoExistente =
                ventana.buscarProducto(
                        nombreProducto);

        if (productoExistente != null) {

            panel.mostrarProductoSeleccionado(
                    productoExistente);

            mostrarInformacionProducto();

            return;
        }

        /*
         * Solicitar precio.
         */
        Double precio =
                solicitarDouble(
                        "Ingrese el precio base:");

        if (precio == null) {

            return;
        }

        /*
         * Solicitar stock.
         */
        Integer stock =
                solicitarEntero(
                        "Ingrese el stock disponible:");

        if (stock == null) {

            return;
        }

        /*
         * Solicitar descuento.
         */
        Double descuento =
                solicitarDouble(
                        "Ingrese el porcentaje de descuento:");

        if (descuento == null) {

            return;
        }

        /*
         * Solicitar IVA.
         */
        Double iva =
                solicitarDouble(
                        "Ingrese el porcentaje de IVA:");

        if (iva == null) {

            return;
        }

        /*
         * Crear producto.
         */
        Producto producto =
                new Producto(
                        nombreProducto,
                        precio,
                        stock,
                        descuento,
                        iva);

        /*
         * Guardar producto.
         */
        ventana.agregarProducto(
                producto);

        /*
         * Mostrar producto.
         */
        panel.mostrarProductoSeleccionado(
                producto);

        /*
         * Actualizar información.
         */
        mostrarInformacionProducto();
    }

    /**
     * Solicita un número decimal.
     */
    private Double solicitarDouble(
            String mensaje) {

        while (true) {

            String texto =
                    JOptionPane.showInputDialog(
                            panel,
                            mensaje,
                            "Datos del Producto",
                            JOptionPane.QUESTION_MESSAGE);

            /*
             * Cancelar.
             */
            if (texto == null) {

                return null;
            }

            try {

                double valor =
                        Double.parseDouble(
                                texto.trim());

                /*
                 * No aceptar negativos.
                 */
                if (valor < 0) {

                    JOptionPane.showMessageDialog(
                            panel,
                            "El valor no puede ser negativo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    continue;
                }

                return valor;

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        panel,
                        "Debe ingresar un número válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Solicita un número entero.
     */
    private Integer solicitarEntero(
            String mensaje) {

        while (true) {

            String texto =
                    JOptionPane.showInputDialog(
                            panel,
                            mensaje,
                            "Datos del Producto",
                            JOptionPane.QUESTION_MESSAGE);

            /*
             * Cancelar.
             */
            if (texto == null) {

                return null;
            }

            try {

                int valor =
                        Integer.parseInt(
                                texto.trim());

                /*
                 * No aceptar negativos.
                 */
                if (valor < 0) {

                    JOptionPane.showMessageDialog(
                            panel,
                            "El valor no puede ser negativo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    continue;
                }

                return valor;

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        panel,
                        "Debe ingresar un número entero válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra la información
     * del producto seleccionado.
     */
    private void mostrarInformacionProducto() {

        Producto producto =
                (Producto) panel
                        .getComboProductos()
                        .getSelectedItem();

        if (producto == null) {

            limpiarInformacion();

            return;
        }

        /*
         * Precio.
         */
        panel.getLblPrecio()
                .setText(
                        "$"
                        + panel
                                .getFormatoDinero()
                                .format(
                                        producto.getPrecio()));

        /*
         * Stock.
         */
        panel.getLblStock()
                .setText(
                        String.valueOf(
                                producto.getStock()));

        /*
         * Descuento.
         */
        panel.getLblDescuento()
                .setText(
                        producto
                                .getPorcentajeDescuento()
                                + "%");

        /*
         * IVA.
         */
        panel.getLblIVA()
                .setText(
                        producto
                                .getImpuestoIVA()
                                + "%");

        /*
         * Total inicial.
         */
        panel.getLblTotal()
                .setText("$0.00");
    }

    /**
     * Limpia los datos mostrados.
     */
    private void limpiarInformacion() {

        panel.getLblPrecio()
                .setText("$0.00");

        panel.getLblStock()
                .setText("0");

        panel.getLblDescuento()
                .setText("0%");

        panel.getLblIVA()
                .setText("0%");

        panel.getLblTotal()
                .setText("$0.00");
    }

    /**
     * Registrar pedido.
     */
    private void registrarPedido() {

        Producto producto =
                (Producto) panel
                        .getComboProductos()
                        .getSelectedItem();

        /*
         * Verificar producto.
         */
        if (producto == null) {

            JOptionPane.showMessageDialog(
                    panel,
                    "Debe ingresar un producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        int cantidad;

        /*
         * Obtener cantidad.
         */
        try {

            cantidad =
                    Integer.parseInt(
                            panel
                                    .getTxtCantidad()
                                    .getText()
                                    .trim());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    panel,
                    "La cantidad debe ser numérica.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Validar cantidad.
         */
        if (cantidad <= 0) {

            JOptionPane.showMessageDialog(
                    panel,
                    "La cantidad debe ser mayor que cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Validar stock.
         */
        if (cantidad > producto.getStock()) {

            JOptionPane.showMessageDialog(
                    panel,
                    "La cantidad solicitada supera "
                    + "el stock disponible.",
                    "Stock insuficiente",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Calcular precio final.
         */
        double precioFinal =
                producto.calcularPrecioFinal();

        /*
         * Calcular total.
         */
        double total =
                precioFinal * cantidad;

        /*
         * Restar stock.
         */
        producto.setStock(
                producto.getStock()
                - cantidad);

        /*
         * Agregar pedido a la tabla.
         */
        panel.getModeloTabla()
                .addRow(
                        new Object[] {
                                producto.getNombre(),
                                cantidad,
                                "$"
                                + panel
                                        .getFormatoDinero()
                                        .format(
                                                precioFinal),
                                "$"
                                + panel
                                        .getFormatoDinero()
                                        .format(
                                                total)
                        });

        /*
         * Actualizar stock.
         */
        panel.getLblStock()
                .setText(
                        String.valueOf(
                                producto.getStock()));

        /*
         * Mostrar total.
         */
        panel.getLblTotal()
                .setText(
                        "$"
                        + panel
                                .getFormatoDinero()
                                .format(
                                        total));

        /*
         * Limpiar cantidad.
         */
        panel.getTxtCantidad()
                .setText("");

        JOptionPane.showMessageDialog(
                panel,
                "Pedido registrado correctamente.",
                "Pedido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cancelar pedido seleccionado.
     */
    private void cancelarPedido() {

        int fila =
                panel.getTablaPedidos()
                        .getSelectedRow();

        /*
         * Verificar selección.
         */
        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    panel,
                    "Debe seleccionar un pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Obtener nombre.
         */
        String nombreProducto =
                panel
                        .getModeloTabla()
                        .getValueAt(
                                fila,
                                0)
                        .toString();

        /*
         * Obtener cantidad.
         */
        int cantidad =
                Integer.parseInt(
                        panel
                                .getModeloTabla()
                                .getValueAt(
                                        fila,
                                        1)
                                .toString());

        /*
         * Buscar producto.
         */
        Producto producto =
                ventana.buscarProducto(
                        nombreProducto);

        if (producto == null) {

            JOptionPane.showMessageDialog(
                    panel,
                    "No se encontró el producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        /*
         * Devolver stock.
         */
        producto.setStock(
                producto.getStock()
                + cantidad);

        /*
         * Eliminar pedido.
         */
        panel.getModeloTabla()
                .removeRow(fila);

        /*
         * Actualizar stock.
         */
        panel.getLblStock()
                .setText(
                        String.valueOf(
                                producto.getStock()));

        /*
         * Reiniciar total.
         */
        panel.getLblTotal()
                .setText("$0.00");

        JOptionPane.showMessageDialog(
                panel,
                "Pedido cancelado correctamente.",
                "Cancelación",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Eventos de los botones.
     */
    @Override
    public void actionPerformed(
            ActionEvent e) {

        if (e.getSource() ==
                panel.getBtnRegistrarPedido()) {

            registrarPedido();

        } else if (e.getSource() ==
                panel.getBtnCancelarPedido()) {

            cancelarPedido();
        }
    }
}
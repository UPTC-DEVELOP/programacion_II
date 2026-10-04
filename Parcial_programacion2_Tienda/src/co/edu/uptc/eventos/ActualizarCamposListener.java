package co.edu.uptc.eventos;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import co.edu.uptc.negocio.Producto;

public class ActualizarCamposListener implements ItemListener {
    private JComboBox<Producto> comboProductos;
    private JLabel lblPrecioBase;
    private JLabel lblStock;
    private JLabel lblDescuento;
    private JLabel lblIVA;

    public ActualizarCamposListener(JComboBox<Producto> comboProductos, JLabel lblPrecioBase,
                                    JLabel lblStock, JLabel lblDescuento, JLabel lblIVA) {
        this.comboProductos = comboProductos;
        this.lblPrecioBase = lblPrecioBase;
        this.lblStock = lblStock;
        this.lblDescuento = lblDescuento;
        this.lblIVA = lblIVA;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Producto prod = (Producto) comboProductos.getSelectedItem();
            if (prod != null) {
                lblPrecioBase.setText(String.format("$%.2f", prod.getPrecioBase()));
                lblStock.setText(String.valueOf(prod.getStock()));
                lblDescuento.setText(prod.getPorcentajeDescuento() + "%");
                lblIVA.setText(prod.getImpuestoIVA() + "%");
            }
        }
    }
}
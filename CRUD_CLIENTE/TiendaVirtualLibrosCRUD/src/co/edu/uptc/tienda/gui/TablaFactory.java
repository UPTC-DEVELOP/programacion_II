package co.edu.uptc.tienda.gui;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class TablaFactory {

	public static JScrollPane configurarTabla(String[] columnas, JTable tablaDestino,
			DefaultTableModel[] modeloContenedor) {

		DefaultTableModel modeloNoEditable = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		modeloContenedor[0] = modeloNoEditable;

		tablaDestino.setModel(modeloNoEditable);
		tablaDestino.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaDestino.getTableHeader().setReorderingAllowed(false);

		return new JScrollPane(tablaDestino);
	}

}

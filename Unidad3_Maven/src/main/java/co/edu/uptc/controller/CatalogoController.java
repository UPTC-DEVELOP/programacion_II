package co.edu.uptc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import co.edu.uptc.exception.TiendaException;
import co.edu.uptc.model.Categoria;
import co.edu.uptc.model.FormatoLibro;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.LibroFactory;
import co.edu.uptc.service.CatalogoService;
import co.edu.uptc.view.Mensajes;
import co.edu.uptc.view.PanelCatalogoAdmin;

// CatalogoController convierte eventos del formulario en operaciones de CatalogoService.
public class CatalogoController implements ActionListener {
    private final PanelCatalogoAdmin vista; private final CatalogoService catalogo;
    public CatalogoController(PanelCatalogoAdmin vista,CatalogoService catalogo){this.vista=vista;this.catalogo=catalogo;vista.getBtnGuardar().addActionListener(this);vista.getBtnActualizar().addActionListener(this);vista.getBtnEliminar().addActionListener(this);vista.getBtnLimpiar().addActionListener(this);refrescar();}
    @Override public void actionPerformed(ActionEvent e){try{switch(e.getActionCommand()){case "GUARDAR_LIBRO":catalogo.registrar(crearLibro());break;case "ACTUALIZAR_LIBRO":catalogo.actualizar(crearLibro());break;case "ELIMINAR_LIBRO":catalogo.eliminar(vista.getTxtIsbn().getText());break;case "LIMPIAR_FORMULARIO":vista.limpiar();return;default:return;}refrescar();Mensajes.informacion(vista,"Operación realizada correctamente.");}catch(TiendaException|NumberFormatException ex){Mensajes.error(vista,ex.getMessage());}}
    private Libro crearLibro(){FormatoLibro formato=FormatoLibro.valueOf((String)vista.getCmbFormato().getSelectedItem());ArrayList<String> autores=new ArrayList<>();autores.add("Autor de ejemplo");return LibroFactory.crear(formato,vista.getTxtIsbn().getText(),vista.getTxtTitulo().getText(),autores,2020,Categoria.OTRA,"Editorial",100,Double.parseDouble(vista.getTxtPrecio().getText()),Integer.parseInt(vista.getTxtStock().getText()),Double.parseDouble(vista.getTxtDescuento().getText()),Double.parseDouble(vista.getTxtIva().getText()));}
    public void refrescar(){vista.pintar(catalogo.listar());}
}

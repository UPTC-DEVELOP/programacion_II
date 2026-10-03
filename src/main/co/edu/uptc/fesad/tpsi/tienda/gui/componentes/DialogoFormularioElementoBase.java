/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

/// Representa una ventana de diálogo con un formulario para editar un elemento.
public abstract class DialogoFormularioElementoBase extends DialogoBase {
  
  private Object elemento;
  
  private FormularioElementoBase formulario;
  
  private JButton btnGuardar;
  
  private JButton btnEliminar;
  
  protected DialogoFormularioElementoBase(
    Window propietario,
    String titulo,
    Object elemento,
    Evento evento
  ) {
    super(propietario, titulo, evento);
    this.elemento = elemento;
  }
  
  protected abstract FormularioElementoBase crearFormulario();
  
  protected abstract String getEventoGuardar();
  
  protected abstract String getEventoEliminar();
  
  protected abstract String describirElemento();
  
@Override
protected void inicializarComponente() {
  setLayout(new BorderLayout(5, 5));
  
  // crear el formulario
  this.formulario = crearFormulario();
  // preparar el formulario con el elemento dato
  this.formulario.preparar(this.elemento);
  // agregar el formulario
  add(this.formulario, BorderLayout.CENTER);
  
  // crear un panel para los botones de acción
  JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
  // crear el botón eliminar
  this.btnEliminar = new JButton("Eliminar");
  // crear el botón guardar
  this.btnGuardar = new JButton("Guardar");
  
  // agregar cada botón de acción al panel de botones
  panelBotones.add(this.btnEliminar);
  panelBotones.add(this.btnGuardar);
  
  // agregar el panel de botones
  add(panelBotones, BorderLayout.SOUTH);
  
  asignarComandoBotones();
  // establecer que la acción predeterminada es el botón guardar, creo que es el Enter
  getRootPane().setDefaultButton(this.btnGuardar);
  
}

@Override
protected void asignarComandoBotones() {
  
  // asignar el evento para guardar un elemento
  this.btnGuardar.addActionListener(
    (e) -> {
      // pedir que se guarde el elemento con los datos ingresados por el usuario
      getEvento().manejar(getEventoGuardar(), this.formulario.capturarDatos());
    }
  );
  
  // habilitar el botón eliminar sólo si el elemento ya existe en el repositorio de datos
  // porque tiene ID no nulo
  this.btnEliminar.setEnabled(this.formulario.getIDElemento() != null);
  
  // asignar el evento para eliminar un elemento
  this.btnEliminar.addActionListener((e) -> {
    if (confirmarEliminacion()) {
      // pedir que se elimine el elemento con el ID
      getEvento().manejar(getEventoEliminar(), this.formulario.getIDElemento());
    }
  });
}

private boolean confirmarEliminacion() {
  // a Sí le toca el valor 0. A No le toca el valor 1
  String[] opciones = { "Sí", "No" };
  // preguntar al usuario si quiere eliminar el elemento
  int respuesta = JOptionPane.showOptionDialog(
    this, // este diálogo será el padre del JOption
    String.format("¿Desea eliminar %s ?", describirElemento()),
    "Confirmar Eliminación", // título del mensaje
    JOptionPane.YES_NO_OPTION, // botones: Sí o No
    JOptionPane.WARNING_MESSAGE, // dice cuál es el ícono del mensaje
    null, // no usar un ícono personalizado, solo usar el ícono predeterminado
    opciones, // array con el texto de las opciones que el usuario verá
    opciones[1] // de manera predeterminada, se activa el botón 1 = No
  );
  // si el usuario presiona el botón Sí, el JOptionPane devuelve el valor 0.
  // por lo que la confirmación es comparar si respuesta = 0
  return respuesta == 0;
}
  
protected Object getElemento() { return this.elemento; }

  
}

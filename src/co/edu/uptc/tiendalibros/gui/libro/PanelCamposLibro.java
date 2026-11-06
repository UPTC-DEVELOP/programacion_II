//
package co.edu.uptc.tiendalibros.gui.libro;

import java.awt.GridBagLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario;
import co.edu.uptc.tiendalibros.gui.componentes.PanelCamposBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.estilos.AlmacenImagenesCubiertas;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

//
public class PanelCamposLibro extends PanelCamposBase<ElementoVistaLibroConFormatos, Libro> {
  
  /// Campo de texto del título
  private JTextField txtTitulo;
  
  /// Campo de texto del ISBN
  private JTextField txtIsbn;
  
  /// Campo de selección de la categoría
  private JComboBox<Categoria> cbxCategoria;
  
  /// Campo de selección de la editorial
  private JComboBox<Editorial> cbxEditorial;
  
  /// Campo del año de publicación
  private JTextField txtAnioPublicacion;
  
  /// Campo de la imagen de la cubierta del libro
  private JTextField txtImagenLibro;
  
  /// Diálogo para seleccionar la imagen de la cubierta del libro
  private JFileChooser dialogoImagenLibro;
  
  /// Componente para mostrar errores del título
  private JLabel notaErrorTitulo;
  
  /// Componente para mostrar errores de la categoría
  private JLabel notaErrorCategoria;
  
  /// Componente para mostrar errores de la editorial
  private JLabel notaErrorEditorial;
  
  /// Componente para mostrar errores del año de publicación
  private JLabel notaErrorAnioPublicacion;
  
  /// Componente para mostrar errores del ISBN
  private JLabel notaErrorIsbn;
  
  /// Componente para mostrar errores de la imagen de la cubierta del libro
  private JLabel notaErrorImagenLibro;
  
  /// Crea un nuevo objeto [PanelCamposLibro].
  public PanelCamposLibro(AsociacionCamposFormulario asociacionCampos) {
    super(asociacionCampos);
  }
  
  @Override
  protected void inicializarComponente() {
    FabricaEstilos.fabrica(this)
      .bordeSeccion("Datos del libro");
    setBackground(null);
    setLayout(new GridBagLayout());
    
    // crear campo de título
    this.txtTitulo = new JTextField();
    FabricaEstilos.fabrica(this.txtTitulo)
      .campoTexto(30);
    
    // crear el campo de selección de categoría
    this.cbxCategoria = new JComboBox<>();
    FabricaEstilos.fabrica(this.cbxCategoria)
      .comboBox();
    
    // crear el campo de selección de editorial
    this.cbxEditorial = new JComboBox<>();
    FabricaEstilos.fabrica(this.cbxEditorial)
      .comboBox();
    
    // crear el campo del año de publicación
    this.txtAnioPublicacion = new JTextField();
    FabricaEstilos.fabrica(this.txtAnioPublicacion)
      .campoTexto(6);
    
    // crear el campo del ISBN
    this.txtIsbn = new JTextField();
    FabricaEstilos.fabrica(this.txtIsbn)
      .campoTexto(17);
    
    // crear el campo de imagen del libro
    this.txtImagenLibro = new JTextField();
    FabricaEstilos.fabrica(this.txtImagenLibro)
      .campoTexto(10);
    
    // crear las notas de errores para cada campo
    this.notaErrorTitulo = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorTitulo)
      .notaError();
    this.notaErrorCategoria = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorCategoria)
      .notaError();
    this.notaErrorEditorial = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorEditorial)
      .notaError();
    this.notaErrorAnioPublicacion = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorAnioPublicacion)
      .notaError();
    this.notaErrorImagenLibro = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorImagenLibro)
      .notaError();
    this.notaErrorIsbn = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorIsbn)
      .notaError();
    
    // asociar de manera lógica los campos con sus notas de error
    asociarCampo("titulo", this.txtTitulo, this.notaErrorTitulo);
    asociarCampo("categoria", this.cbxCategoria, this.notaErrorCategoria);
    asociarCampo("editorial", this.cbxEditorial, this.notaErrorEditorial);
    asociarCampo("anioPublicacion", this.txtAnioPublicacion, this.notaErrorAnioPublicacion);
    asociarCampo("isbn", this.txtIsbn, this.notaErrorIsbn);
    asociarCampo("imagenLibro", this.txtImagenLibro, this.notaErrorImagenLibro);
    
    ubicarComponentes();
    
    asignarRendererComboBoxCategoria();
    asignarRendererComboBoxEditorial();
  }
  
  @Override
  public void prepararCampos(ElementoVistaLibroConFormatos elementoVista) {
    setElementoVista(elementoVista);
    
    // llenar los campos de selección que vienen con el elmento vista
    llenarCategorias(elementoVista.getCategorias());
    llenarEditoriales(elementoVista.getEditoriales());
    
    // obtener el objeto libro que es el elemento principal que maneja el panel
    Libro libro = elementoVista.getElemento()
      .getLibro();
    
    // Mostrar los atributos del libro
    this.txtTitulo.setText(libro.getTitulo());
    this.txtAnioPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
    this.txtIsbn.setText(libro.getIsbn());
    this.txtImagenLibro.setText(libro.getImagenLibro());
    
    seleccionarCategoria(libro);
    seleccionarEditorial(libro);
  }
  
  private void ubicarComponentes() {
    // crear las etiquetas de los campos
    JLabel lblTitulo = new JLabel("Título:");
    JLabel lblCategoria = new JLabel("Categoría:");
    JLabel lblEditorial = new JLabel("Editorial:");
    JLabel lblAnioPublicacion = new JLabel("Año Publicación:");
    JLabel lblIsbn = new JLabel("ISBN:");
    JLabel lblImagenLibro = new JLabel("Imagen Libro:");
    
    // agregar el botón que abre el diálogo para seleccionar la imagen del libro
    JButton btnDialogoImagenLibro = new JButton("Abrir");
    btnDialogoImagenLibro.addActionListener((e) -> seleccionarImagenLibro());
    
    // crear una restricción base para todos los componentes del formulario
    RestriccionesCelda base = new RestriccionesCelda().insets(4, 4, 0, 4)
      .anclarIzquierda();
    
    // ubicar la etiqueta título
    agregar(
      lblTitulo,
      base.clonar()
        .gridy(0)
        .gridx(0)
    );
    // ubicar el campo titulo
    agregar(
      this.txtTitulo,
      base.clonar()
        .gridy(0)
        .gridx(1)
        .gridwidth(3)
        .expandirHorizontal()
    );
    // ubicar la nota de error del título
    agregar(
      this.notaErrorTitulo,
      base.clonar()
        .gridy(1)
        .gridx(1)
        .gridwidth(3)
        .expandirHorizontal()
    );
    
    // agregar la etiqueta de la categoría
    agregar(
      lblCategoria,
      base.clonar()
        .gridy(2)
        .gridx(0)
    );
    // agregar el campo de la categoría
    agregar(
      this.cbxCategoria,
      base.clonar()
        .gridy(2)
        .gridx(1)
        .expandirHorizontal()
    );
    
    // agregar la etiqueta de la editorial
    agregar(
      lblEditorial,
      base.clonar()
        .gridy(2)
        .gridx(2)
    );
    // agregar el campo de la editorial
    agregar(
      this.cbxEditorial,
      base.clonar()
        .gridy(2)
        .gridx(3)
        .expandirHorizontal()
    );
    
    // agregar la nota de error de la categoría
    agregar(
      this.notaErrorCategoria,
      base.clonar()
        .gridy(3)
        .gridx(1)
        .expandirHorizontal()
    );
    
    // agregar la nota de error de la editorial
    agregar(
      this.notaErrorEditorial,
      base.clonar()
        .gridy(3)
        .gridx(3)
        .expandirHorizontal()
    );
    
    // agregar la etiqueta del año de publicación
    agregar(
      lblAnioPublicacion,
      base.clonar()
        .gridy(4)
        .gridx(0)
    );
    // agregar el campo del año de publicación
    agregar(
      this.txtAnioPublicacion,
      base.clonar()
        .gridy(4)
        .gridx(1)
    );
    // agregar la nota de error del año de publicación
    agregar(
      this.notaErrorAnioPublicacion,
      base.clonar()
        .gridy(5)
        .gridx(1)
        .expandirHorizontal()
    );
    
    // agregar la etiqueta del ISBN
    agregar(
      lblIsbn,
      base.clonar()
        .gridy(4)
        .gridx(2)
    );
    // agregar el campo del ISBN
    agregar(
      this.txtIsbn,
      base.clonar()
        .gridy(4)
        .gridx(3)
        .expandirHorizontal()
    );
    // agregar la nota de error del ISBN
    agregar(
      this.notaErrorIsbn,
      base.clonar()
        .gridy(5)
        .gridx(3)
        .expandirHorizontal()
    );
    
    // agregar la etiqueta de la imagen del libro
    agregar(
      lblImagenLibro,
      base.clonar()
        .gridy(6)
        .gridx(0)
    );
    // agregar el campo de la imagen del libro
    agregar(
      this.txtImagenLibro,
      base.clonar()
        .gridy(6)
        .gridx(1)
    );
    // agregar el botón para el diálogo de la imagen del libro
    agregar(
      btnDialogoImagenLibro,
      base.clonar()
        .gridy(6)
        .gridx(2)
    );
    // agregar la nota de error de la imagen del libro
    agregar(
      this.notaErrorImagenLibro,
      base.clonar()
        .gridy(7)
        .gridx(1)
        .gridwidth(3)
        .expandirHorizontal()
    );
    
  }
  
  @Override
  public ResultadoValidacion validarCampos() {
    return new ValidadorFormulario()
      // .textoNoVacio("titulo", "título", this.txtTitulo)
      // .textoNoVacio("isbn", "ISBN", this.txtIsbn)
      .unaOpcionSeleccionada("categoria", this.cbxCategoria, "Debe seleccionar una categoría para el libro.")
      .unaOpcionSeleccionada("editorial", this.cbxEditorial, "Debe seleccionar una editorial para el libro.")
      .numeroEntero("anioPublicacion", "año publicación", this.txtAnioPublicacion)
      .nombreImagenValido("imagenLibro", "imagen del libro", this.txtImagenLibro)
      .getErrores();
  }
  
  @Override
  public Libro capturarDatos() {
    
    // getElemento() devuelve LibroConFormatos; el id del libro vive viene en
    // LibroConFormatos.getId()
    LibroConFormatos libroConFormatos = getElementoVista().getElemento();
    Long idLibro = libroConFormatos.getId();
    
    // obtener el id de la categoría seleccionada en el JComboBox
    Categoria categoria = (Categoria) this.cbxCategoria.getSelectedItem();
    Long idCategoria = categoria.getId();
    
    // obtener el id de la editorial seleccionada en el JComboBox
    Editorial editorial = (Editorial) this.cbxEditorial.getSelectedItem();
    Long idEditorial = editorial.getId();
    
    // obtener el título
    String titulo = this.txtTitulo.getText()
      .trim();
    
    // obtener el año de publicación
    int anioPublicacion = Integer.parseInt(
      this.txtAnioPublicacion.getText()
        .trim()
    );
    
    // obtener el ISBN
    String isbn = this.txtIsbn.getText()
      .trim();
    
    // obtener la imagen del libro
    String imagenLibro = this.txtImagenLibro.getText()
      .trim();
    
    // crear un nuevo objeto libro a partir de sus atributos
    return new LibroBuilder()
      .id(idLibro)
      .isbn(isbn)
      .titulo(titulo)
      .idCategoria(idCategoria)
      .idEditorial(idEditorial)
      .anioPublicacion(anioPublicacion)
      .imagenLibro(imagenLibro)
      .construir();
  }
  
  private void asignarRendererComboBoxCategoria() {
    // se debe configurar un objeto ListCellRenderer personalizado para mostrar bien las
    // opciones del combobox.
    this.cbxCategoria.setRenderer((list, categoria, index, selected, focused) -> {
      // una etiqueta es suficiente para mostrar el texto de la opción
      JLabel etiqueta = new JLabel();
      // el primer valor del modelo de datos del combobox es nulo
      if (categoria == null) {
        // obviamente, null no es una selección con index válido, por lo que se le pide al usuario
        // seleccionar un valor de la lista
        etiqueta.setText(index == -1 ? "" : "— Seleccione una categoría —");
      } else {
        // para cada opción de categoría, mostrar su nombre
        etiqueta.setText(categoria.getNombre());
      }
      // retornar la etiqueta que muestra el texto de la opción
      return etiqueta;
    });
  }
  
  /// Llena el JComboBox con la lista de categorías recibida. Agrega una opción vacía al
  /// inicio para que el usuario haga una selección consciente.
  ///
  /// @param categorias Lista de las categorías disponibles que se deben mostrar.
  private void llenarCategorias(List<Categoria> categorias) {
    // primero, crear el modelo de datos del combobox. El modelo de datos representa el
    // conjunto de opciones para seleccionar una opción
    DefaultComboBoxModel<Categoria> modelo = new DefaultComboBoxModel<>();
    // se agrega el elemento nulo que indica que el usuario todavía no ha seleccionado una
    // editorial de la lista
    modelo.addElement(null);
    
    // luego, se agrega cada editorial al modelo de datos del combobox
    for (Categoria categoria : categorias) {
      modelo.addElement(categoria);
    }
    
    // ahora se agrega el modelo de datos al combobox
    this.cbxCategoria.setModel(modelo);
  }
  
  /// Selecciona en el JComboBox la categoría del libro. Si el ID de la categoría es nulo o
  /// no se encuentra, deja la opción vacía seleccionada .
  ///
  /// @param libro Libro del cual se quiere seleccionar la categoría.
  private void seleccionarCategoria(Libro libro) {
    // obtiene el id de la categoría
    Long idCategoria = libro.getIdCategoria();
    // si el usuario no ha escogido una opción de categoría.
    // o también, por alguna extraña razón, el libro tiene una categoría nula
    if (idCategoria == null) {
      // se activa la selección vacía "Seleccione una categoría"
      this.cbxCategoria.setSelectedIndex(0);
      return;
    }
    
    // recorrer la lista de opciones hasta encontrar la opción que corresponda con la
    // categoría del libro
    for (int i = 0; i < this.cbxCategoria.getItemCount(); i++) {
      // obtener la categoríaa actual
      Categoria categoria = this.cbxCategoria.getItemAt(i);
      // si la opción categoria coincide con el id categoría del libro
      if (categoria != null && categoria.getId()
        .equals(idCategoria)) {
        // seleccionar la opción categoría y terminar de buscar
        this.cbxCategoria.setSelectedIndex(i);
        return;
      }
    }
    
    // no se encontró la categoría: dejar la opción vacía seleccionada
    this.cbxCategoria.setSelectedIndex(0);
  }
  
  private void asignarRendererComboBoxEditorial() {
    // se debe configurar un objeto ListCellRenderer personalizado para mostrar bien las
    // opciones del combobox.
    this.cbxEditorial.setRenderer((list, editorial, index, selected, focused) -> {
      // una etiqueta es suficiente para mostrar el texto de la opción
      JLabel etiqueta = new JLabel();
      // el primer valor del modelo de datos del combobox es nulo
      if (editorial == null) {
        // obviamente, null no es una selección con index válido, por lo que se le pide al usuario
        // seleccionar un valor de la lista
        etiqueta.setText(index == -1 ? "" : "— Seleccione una editorial —");
      } else {
        // para cada opción de editorial, mostrar su nombre
        etiqueta.setText(editorial.getNombre());
      }
      // retornar la etiqueta que muestra el texto de la opción
      return etiqueta;
    });
  }
  
  /// Selecciona en el JComboBox la editorial del libro. Si el ID de la editorial es nulo o
  /// no se encuentra, deja la opción vacía seleccionada .
  ///
  /// @param libro Libro del cual se quiere seleccionar la editorial.
  private void seleccionarEditorial(Libro libro) {
    // obtiene el id de la editorial
    Long idEditorial = libro.getIdEditorial();
    // si el usuario no ha escogido una opción de editorial.
    // o también, por alguna extraña razón, el libro tiene una editorial nula
    if (idEditorial == null) {
      // se activa la selección vacía "Seleccione una editorial"
      this.cbxEditorial.setSelectedIndex(0);
      return;
    }
    
    // recorrer la lista de opciones hasta encontrar la opción que corresponda con la
    // editorial del libro
    for (int i = 0; i < this.cbxEditorial.getItemCount(); i++) {
      // obtener la editorial actual
      Editorial editorial = this.cbxEditorial.getItemAt(i);
      // si la opción editorial coincide con el id editorial del libro
      if (editorial != null && editorial.getId()
        .equals(idEditorial)) {
        // seleccionar la opción editorial y terminar de buscar
        this.cbxEditorial.setSelectedIndex(i);
        return;
      }
    }
    
    // no se encontró la editorial: dejar la opción vacía seleccionada
    this.cbxEditorial.setSelectedIndex(0);
  }
  
  /// Llena el JComboBox con la lista de editoriales recibida. Agrega una opción vacía al
  /// inicio para que el usuario haga una selección consciente.
  ///
  /// @param editoriales Lista de las editoriales disponibles que se deben mostrar.
  private void llenarEditoriales(List<Editorial> editoriales) {
    // primero, crear el modelo de datos del combobox. El modelo de datos representa el
    // conjunto de opciones para seleccionar una opción
    DefaultComboBoxModel<Editorial> modelo = new DefaultComboBoxModel<>();
    // se agrega el elemento nulo que indica que el usuario todavía no ha seleccionado una
    // editorial de la lista
    modelo.addElement(null);
    
    // luego, se agrega cada editorial al modelo de datos del combobox
    for (Editorial editorial : editoriales) {
      modelo.addElement(editorial);
    }
    
    // ahora se agrega el modelo de datos al combobox
    this.cbxEditorial.setModel(modelo);
  }
  
  /// Abre un ventana de diálogo para seleccionar la imagen del libro, empezando en el
  /// directorio de las cubiertas. Luego, muestra el nombre del archivo elegido en el campo
  /// de imagen.
  private void seleccionarImagenLibro() {
    Optional<Path> directorioCubiertas = AlmacenImagenesCubiertas.obtenerDirectorioCubiertas();
    
    // si no se encontró el directorio de las imágenes de cubiertas se le avisa al usuario
    if (directorioCubiertas.isEmpty()) {
      JOptionPane.showMessageDialog(
        this,
        "No se pudo acceder a la carpeta de cubiertas.",
        "Imagen del libro",
        JOptionPane.ERROR_MESSAGE
      );
      return;
    }
    
    // Abrir la ventana de diálogo en el directorio de imágenes de las cubiertas
    this.dialogoImagenLibro = new JFileChooser(
      directorioCubiertas.get()
        .toFile()
    );
    
    // esperar si el usuario seleccionó el botón aceptar
    int resultado = this.dialogoImagenLibro.showOpenDialog(this);
    if (resultado != JFileChooser.APPROVE_OPTION) {
      // el usuario no seleccionó nada. salir
      return;
    }
    
    // obtener el nombre del archivo de la imagen seleccionada por el usuario
    File archivoImagen = this.dialogoImagenLibro.getSelectedFile();
    
    // establecer el nombre del archivo de la imagen de la cubierta
    this.txtImagenLibro.setText(archivoImagen.getName());
  }
  
}

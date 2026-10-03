package co.uptc.edu.gui;

/** Permite a las pantallas cambiar de vista sin conocer la ventana principal. */
public interface Navegador {
    void irA(Vista vista);
}

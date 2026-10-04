package co.edu.uptc.tienda.gui;

public interface Evento {

    public static final String CANCELAR = "Cancelar";
    public static final String LOGIN = "Login";
    public static final String ELIMINAR = "Eliminar";
    public static final String VER = "Ver";
    public static final String ACTUALIZAR = "Actualizar";
    public static final String CREAR = "Nuevo";
    public static final String BUSCAR = "Buscar";
    public static final String LIMPIAR = "Limpiar";

    public static final String GUARDAR = "Guardar";
    public static final String EDITAR = "Editar";

    public static final String ACTUALIZAR_TABLA = "ACTUALIZAR_TABLA";

    void ejecutarEvento(String evento);
}


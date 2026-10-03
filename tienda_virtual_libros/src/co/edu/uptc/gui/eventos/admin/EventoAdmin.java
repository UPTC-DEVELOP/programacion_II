package co.edu.uptc.gui.eventos.admin;

/**
 * ENUMERACIÓN EventoAdmin  (paquete: eventos)
 * ---------------------------------------------------------------------------
 * Lista de TODAS las acciones que el usuario puede disparar en el módulo de
 * administración. Cada botón de la GUI se identifica con una de estas
 * constantes mediante setActionCommand(EventoAdmin.X.name()).
 *
 * Ventajas frente a usar Strings sueltos ("btnGuardar"...):
 *  - Sin errores de digitación: el compilador valida los nombres.
 *  - El controlador usa un switch limpio y fácil de explicar.
 *  - Para agregar una acción nueva se agrega UNA constante aquí (OCP).
 */


public enum EventoAdmin {
	 // Menú lateral (prototipo: Dashboard, Manage Books, Reportes, Registro libros, Clientes)
    IR_DASHBOARD,
    IR_GESTION_LIBROS,
    IR_REPORTES,
    REGISTRAR_LIBRO,
    IR_CLIENTES,

    // Pantalla "Administración de Libros"
    BUSCAR_LIBRO,
    EDITAR_LIBRO,
    ELIMINAR_LIBRO,

    // Pantalla "Reportes"
    GENERAR_REPORTE
}



package co.edu.uptc.gui.interfaz.admin;

import co.edu.uptc.negocio.admin.ReglaNegocioException;
import co.edu.uptc.negocio.admin.dto.LibroDto;


/**
 * INTERFAZ IValidadorLibro  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Aísla las REGLAS DE VALIDACIÓN de RF01 y RF02 en su propio componente
 * (SRP: GestionLibro coordina el CRUD, el validador solo valida).
 * OCP: si mañana hay reglas nuevas se crea otro validador sin tocar GestionLibro.
 */
public interface IValidadorLibro {

    /** Valida los campos de un libro nuevo (no incluye la unicidad del ISBN). */
    void validarDatos(LibroDto libro) throws ReglaNegocioException;
}

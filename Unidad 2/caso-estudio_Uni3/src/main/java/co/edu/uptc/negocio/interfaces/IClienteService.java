package co.edu.uptc.negocio.interfaces;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ValidacionException;

public interface IClienteService {
    Cliente iniciarSesion(String correo, String clave) throws ValidacionException;
}

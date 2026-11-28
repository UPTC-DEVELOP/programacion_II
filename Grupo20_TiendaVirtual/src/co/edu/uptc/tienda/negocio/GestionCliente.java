package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCliente;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.persistencia.LocalCliente;
import co.edu.uptc.tienda.persistencia.RegistroOperacionesTxt;

public class GestionCliente implements IGestionCliente {

    private LocalCliente localCliente;
    private IGestionCompra compras;
    private IRegistroOperaciones registro;
    private Cliente clienteLogueado;

    public GestionCliente() {
        this(new LocalCliente(), new GestionCompra(), new RegistroOperacionesTxt("datos/operaciones.txt"));
    }

    public GestionCliente(LocalCliente localCliente, IGestionCompra compras, IRegistroOperaciones registro) {
        this.localCliente = localCliente;
        this.compras = compras;
        this.registro = registro;
        clienteLogueado = null;
    }

    public String getAvisoCarga() {
        return localCliente.getAvisoCarga();
    }

    @Override
    public void guardar(Cliente cliente) {
        // Revisa que todos los campos estén llenos y con datos validos
        validar(cliente);
        
        // Revisa que el cliente recien registrado no tenga un ID igual a otro
        if (localCliente.buscarPorId(cliente.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el ID " + cliente.getId() + ".");
        }
        validarCorreoLibre(cliente);
        
        // Persistencia, guarda los datos con JSON
        localCliente.guardar(cliente);
        registro.registrar("Clientes", "Registrar cliente", "ID " + cliente.getId() + ", " + cliente.getNombreCompleto());
    }

    @Override
    public ArrayList<Cliente> listar() {
        // Pide y devuelve los datos recién guardados en JSON
        return localCliente.listar();
    }

    @Override
    public Cliente buscarPorId(String id) {
        // busca el cliente por ID, pero esto lo hace la persistencia JSON
        return localCliente.buscarPorId(id);
    }


    @Override
    public void actualizar(Cliente cliente) {
        validar(cliente);
        if (localCliente.buscarPorId(cliente.getId()) == null) {
            throw new IllegalArgumentException("No existe un cliente con el ID " + cliente.getId() + ".");
        }
        validarCorreoLibre(cliente);
        localCliente.actualizar(cliente);
        if (clienteLogueado != null && clienteLogueado.getId().equals(cliente.getId())) {
            clienteLogueado = cliente;
        }
        registro.registrar("Clientes", "Actualizar cliente", "ID " + cliente.getId() + ", " + cliente.getNombreCompleto());
    }

    @Override
    public void eliminar(String id) {
        if (localCliente.buscarPorId(id) == null) {
            throw new IllegalArgumentException("No existe un cliente con el ID " + id + ".");
        }
        localCliente.eliminar(id);
        if (clienteLogueado != null && clienteLogueado.getId().equals(id)) {
            clienteLogueado = null;
        }
        registro.registrar("Clientes", "Eliminar cliente", "ID " + id);
    }

    public boolean iniciarSesion(String correo) {
        if (esVacio(correo)) {
            return false;
        }
        for (Cliente cliente : listar()) {
            if (cliente.getCorreoElectronico().equalsIgnoreCase(correo.trim())) {
                clienteLogueado = cliente;
                registro.registrar("Clientes", "Iniciar sesión", "ID " + cliente.getId() + ", " + cliente.getCorreoElectronico());
                return true;
            }
        }
        return false;
    }

    public void cerrarSesion() {
        if (clienteLogueado != null) {
            registro.registrar("Clientes", "Cerrar sesión", "ID " + clienteLogueado.getId());
        }
        clienteLogueado = null;
    }

    public List<Compra> listarCompras(String id) throws CompraExcepcion {
        return compras.listarComprasCliente(id);
    }

    public double obtenerDescuentoFrecuente(String id) {
        return compras.obtenerDescuentoFrecuente(id);
    }

    public Cliente getClienteLogueado() {
        return clienteLogueado;
    }

    public boolean esClientePremium() {
        if (clienteLogueado != null) {
            return "Premium".equalsIgnoreCase(clienteLogueado.getTipoCliente());
        }
        return false;
    }

    private void validar(Cliente cliente) {
        // Verifica que el identificador único no esté vacío
        if (esVacio(cliente.getId())) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        
        // Revisa que se haya escrito el nombre completo
        if (esVacio(cliente.getNombreCompleto())) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        
        // Revisa que se haya escrito un correo valido,y que no esté vacio
        if (esVacio(cliente.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (!cliente.getCorreoElectronico().trim().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido, por ejemplo nombre@dominio.com.");
        }
        
        // Revisa que se haya escrito una direccion, no correcta sino que se haya llenado el espacio
        if (esVacio(cliente.getDireccionEnvio())) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        
        // Revisa que se haya llenado el espacio de telefono
        if (esVacio(cliente.getCelular())) {
            throw new IllegalArgumentException("El celular es obligatorio.");
        }
        if (!cliente.getCelular().trim().matches("\\d{7,10}")) {
            throw new IllegalArgumentException("El celular solo puede tener números, entre 7 y 10 dígitos.");
        }
    }


    private void validarCorreoLibre(Cliente cliente) {
        for (Cliente otro : localCliente.listar()) {
            if (!otro.getId().equals(cliente.getId())
                    && otro.getCorreoElectronico().equalsIgnoreCase(cliente.getCorreoElectronico().trim())) {
                throw new IllegalArgumentException("El correo " + cliente.getCorreoElectronico().trim()
                        + " ya está registrado con otro cliente.");
            }
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}

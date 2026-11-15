package persistencia;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Compra;

public class RepositorioComprasArchivoPlano implements IRepositorio<Compra> {

    private static final String RUTA_POR_DEFECTO = "datos/compras.txt";

    private final Path archivo;
    private final List<Compra> compras;

    public RepositorioComprasArchivoPlano() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioComprasArchivoPlano(String ruta) {
        this.archivo = Path.of(ruta);
        this.compras = new ArrayList<Compra>();
        cargar();
    }

    @Override
    public List<Compra> listar() {
        return compras;
    }

    @Override
    public void agregar(Compra compra) {
        compras.add(compra);
    }

    @Override
    public boolean eliminar(Compra compra) {
        return compras.remove(compra);
    }

    @Override
    public void cargar() {
    }

    @Override
    public void guardar() {
    }

    public Path getArchivo() {
        return archivo;
    }
}

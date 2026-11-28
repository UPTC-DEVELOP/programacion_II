package co.edu.uptc.tienda.persistencia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaRecibo;

public class ReciboTxt implements IPersistenciaRecibo {

    private final File carpeta;

    public ReciboTxt(String rutaCarpeta) {
        carpeta = new File(rutaCarpeta);
    }

    @Override
    public String guardar(int numeroCompra, String texto) throws CompraExcepcion {
        carpeta.mkdirs();
        File archivo = new File(carpeta, String.format("recibo-%04d.txt", numeroCompra));
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(texto);
        } catch (IOException e) {
            throw new CompraExcepcion("No se pudo guardar el recibo en " + archivo.getPath() + ".");
        }
        return archivo.getPath();
    }
}

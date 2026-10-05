package co.uptc.edu.libro.persitencia;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import co.uptc.edu.libro.modelo.Libro;

public class LibroDAO {

	public void guardarLibros(List<Libro> libros, String archivo) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
			oos.writeObject(libros);
		}
	}

	public List<Libro> cargarLibros(String archivo) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
			return (List<Libro>) ois.readObject();
		}
	}
}

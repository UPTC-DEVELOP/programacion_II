package co.edu.uptc.libreria.modelo.enums;

public enum TipoCliente {
	
	REGULAR("Regular"), PREMIUM("Premium");

	private final String etiqueta;

	private TipoCliente(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	@Override
	public String toString() {
		return etiqueta;
	}
}

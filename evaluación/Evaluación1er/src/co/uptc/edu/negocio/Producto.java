package co.uptc.edu.negocio;

public class Producto {


private final int id;
private final String nombre;
private final double precioBase;
private int stock;
private double porcentajeDescuento;
private double impuestoIVA;

public Producto(
        int id,
        String nombre,
        double precioBase,
        int stock,
        double porcentajeDescuento,
        double impuestoIVA
) {
    if (precioBase < 0) {
        throw new IllegalArgumentException(
                "El precio base no puede ser negativo."
        );
    }

    if (stock < 0) {
        throw new IllegalArgumentException(
                "El stock no puede ser negativo."
        );
    }

    if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
        throw new IllegalArgumentException(
                "El descuento debe estar entre 0 y 100."
        );
    }

    if (impuestoIVA < 0) {
        throw new IllegalArgumentException(
                "El IVA no puede ser negativo."
        );
    }

    this.id = id;
    this.nombre = nombre;
    this.precioBase = precioBase;
    this.stock = stock;
    this.porcentajeDescuento = porcentajeDescuento;
    this.impuestoIVA = impuestoIVA;
}

public double calcularPrecioFinal() {
    double valorDescuento =
            precioBase * porcentajeDescuento / 100.0;

    double precioConDescuento =
            precioBase - valorDescuento;

    double valorIVA =
            precioConDescuento * impuestoIVA / 100.0;

    return precioConDescuento + valorIVA;
}

public void disminuirStock(int cantidad) {
    validarCantidad(cantidad);

    if (cantidad > stock) {
        throw new IllegalArgumentException(
                "La cantidad solicitada supera el stock disponible."
        );
    }

    stock -= cantidad;
}

public void aumentarStock(int cantidad) {
    validarCantidad(cantidad);
    stock += cantidad;
}

private void validarCantidad(int cantidad) {
    if (cantidad <= 0) {
        throw new IllegalArgumentException(
                "La cantidad debe ser mayor que cero."
        );
    }
}

public int getId() {
    return id;
}

public String getNombre() {
    return nombre;
}

public double getPrecioBase() {
    return precioBase;
}

public int getStock() {
    return stock;
}

public double getPorcentajeDescuento() {
    return porcentajeDescuento;
}

public double getImpuestoIVA() {
    return impuestoIVA;
}

public void setPorcentajeDescuento(double porcentajeDescuento) {
    if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
        throw new IllegalArgumentException(
                "El descuento debe estar entre 0 y 100."
        );
    }

    this.porcentajeDescuento = porcentajeDescuento;
}

public void setImpuestoIVA(double impuestoIVA) {
    if (impuestoIVA < 0) {
        throw new IllegalArgumentException(
                "El IVA no puede ser negativo."
        );
    }

    this.impuestoIVA = impuestoIVA;
}

@Override
public String toString() {
    return nombre;
}
}
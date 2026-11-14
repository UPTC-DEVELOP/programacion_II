package modelo;

import excepciones.ExcepcionValidacion;

public class Producto {
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    public Producto(String nombre, double precioBase, int stock,
            double porcentajeDescuento, double impuestoIVA)
            throws ExcepcionValidacion {
        setNombre(nombre);
        setPrecioBase(precioBase);
        setStock(stock);
        setPorcentajeDescuento(porcentajeDescuento);
        setImpuestoIVA(impuestoIVA);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ExcepcionValidacion {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionValidacion("El nombre del producto es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) throws ExcepcionValidacion {
        if (precioBase <= 0) {
            throw new ExcepcionValidacion("El precio base debe ser mayor que cero.");
        }
        this.precioBase = precioBase;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws ExcepcionValidacion {
        if (stock < 0) {
            throw new ExcepcionValidacion("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento)
            throws ExcepcionValidacion {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new ExcepcionValidacion(
                    "El descuento debe estar entre 0 y 100 por ciento.");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(double impuestoIVA)
            throws ExcepcionValidacion {
        if (impuestoIVA < 0 || impuestoIVA > 100) {
            throw new ExcepcionValidacion(
                    "El IVA debe estar entre 0 y 100 por ciento.");
        }
        this.impuestoIVA = impuestoIVA;
    }

    public double calcularPrecioFinal() {
        double precioConDescuento = precioBase
                * (1 - porcentajeDescuento / 100.0);
        return precioConDescuento * (1 + impuestoIVA / 100.0);
    }

    public void descontarStock(int cantidad) throws ExcepcionValidacion {
        if (cantidad <= 0) {
            throw new ExcepcionValidacion("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > stock) {
            throw new ExcepcionValidacion(
                    "Stock insuficiente. Disponible: " + stock + ".");
        }
        stock -= cantidad;
    }

    public void reponerStock(int cantidad) throws ExcepcionValidacion {
        if (cantidad <= 0) {
            throw new ExcepcionValidacion("La cantidad a reponer debe ser mayor que cero.");
        }
        stock += cantidad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

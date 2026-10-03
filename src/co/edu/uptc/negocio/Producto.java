package co.edu.uptc.negocio;

public abstract class Producto {

    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;

    protected Producto(String codigo, String nombre, double precioBase, int stock,
                       double porcentajeDescuento, double impuestoIVA) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
        this.impuestoIVA = impuestoIVA;
    }

    // ===================== Lógica de precios =====================

    public double calcularValorDescuento() {
        return redondear(precioBase * (porcentajeDescuento / 100.0));
    }

    public double calcularPrecioFinal() {
        double precioConDescuento = precioBase * (1 - porcentajeDescuento / 100.0);
        double precioConIva = precioConDescuento * (1 + impuestoIVA / 100.0);
        return redondear(precioConIva);
    }

    // ===================== Lógica de inventario =====================

    public void disminuirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente: solo hay " + stock + " unidades.");
        }
        stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        stock += cantidad;
    }

    protected static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    // ===================== Getters y setters =====================

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public double getImpuestoIVA() { return impuestoIVA; }
    public void setImpuestoIVA(double impuestoIVA) { this.impuestoIVA = impuestoIVA; }
}


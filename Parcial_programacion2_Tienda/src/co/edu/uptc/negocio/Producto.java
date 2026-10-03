package co.edu.uptc.negocio;



public class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private int stock;
    
    
    private double porcentajeDescuento; // Atributo para el estudiante A:brayan
    
    // ATRIBUTOS PARA EL PARCIAL:
    // Estudiante A (Brayan): private double porcentajeDescuento;
    // Estudiante B : private double impuestoIVA;

    public Producto(String id, String nombre, double precioBase, int stock, double porcentajeDescuento) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
        this.porcentajeDescuento = porcentajeDescuento;
    }

//METODO REALIZADO POR EL ESTUDIANTE A: BRAYAN
    public double calcularPrecioFinal() {
  double descuento =precioBase * (porcentajeDescuento / 100.0);
  return precioBase - descuento;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre; 
    }
}

package modelo;

import java.util.ArrayList;
import excepciones.ProductoNoEncontradoException;

public class Ticket {
    private int numeroTicket;
    private ArrayList<Producto> productos;  // Uso de colecciones obligatorias [cite: 13, 54]
    private float total;
    private String observaciones;

    public Ticket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
        this.productos = new ArrayList<>();
        this.total = 0.0f;
        this.observaciones = "";
    }

    public int getNumeroTicket() { return numeroTicket; }
    public ArrayList<Producto> getProductos() { return productos; }
    public float getTotal() { return total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public void añadirProducto(Producto p) {
        if (p != null) {
            productos.add(p);
            calcularTotal();
        }
    }

    public void eliminarProducto(Producto p) throws ProductoNoEncontradoException {
        if (!productos.contains(p)) {
            throw new ProductoNoEncontradoException("El producto " + p.getNombre() + " no está en este ticket.");
        }
        productos.remove(p);
        calcularTotal();
    }

    public float calcularTotal() {
        // Uso de Streams y Programación funcional (Lambda) para el cálculo eficiente [cite: 23, 31]
        this.total = (float) productos.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
        return this.total;
    }

    public void aplicarDescuento(float porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            calcularTotal();
            this.total -= (this.total * (porcentaje / 100.0f));
        }
    }

    public float dividirCuenta(int personas) {
        if (personas <= 0) return this.total;
        return this.total / personas;
    }

    public void cobrar() { 
        System.out.println("Ticket Nº " + numeroTicket + " cobrado con un total de: " + this.total + "€");
    }

    @Override
    public String toString() {
        // Uso obligatorio de StringBuilder para optimizar concatenaciones en cadenas de texto [cite: 21, 54]
        StringBuilder sb = new StringBuilder();
        sb.append("=== TICKET Nº ").append(numeroTicket).append(" ===\n");

        // Lambda para recorrer y anexar de forma elegante
        productos.forEach(p -> sb.append("- ").append(p.getNombre())
                .append(" : ").append(p.getPrecio()).append("€\n"));

        sb.append("---------------------\n");
        sb.append("TOTAL: ").append(total).append("€\n");
        if (!observaciones.isEmpty()) {
            sb.append("Obs: ").append(observaciones).append("\n");
        }
        return sb.toString();
    }
}
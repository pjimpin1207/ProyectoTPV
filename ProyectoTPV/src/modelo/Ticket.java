package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import excepciones.ProductoNoEncontradoException;

@Entity
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int numeroTicket;

    // Almacenamos la lista de productos directamente como objetos dentro del Ticket en la BBDD orientada a objetos
    private ArrayList<Producto> productos;
    private float total;
    private String observaciones;

    public Ticket() {
        // POJO
    }

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
        StringBuilder sb = new StringBuilder();
        sb.append("=== TICKET Nº ").append(numeroTicket).append(" ===\n");
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
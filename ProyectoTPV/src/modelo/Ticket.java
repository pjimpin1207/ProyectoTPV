package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long idBD;

    private int numeroTicket;
    private ArrayList<Producto> productos = new ArrayList<>();
    private float total;
    private String observaciones;
    private Date fecha;

    private HashSet<String> camareros = new HashSet<>();

    public Ticket() {}

    public Ticket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
        this.total = 0.0f;
        this.observaciones = "";
        this.fecha = new Date();
    }

    public int getNumeroTicket() { return numeroTicket; }
    public ArrayList<Producto> getProductos() { return productos; }
    public float getTotal() { return total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Date getFecha() { return fecha; }

    public void añadirCamarero(String nombre) {
        if (this.camareros == null) {
            this.camareros = new HashSet<>();
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.camareros.add(nombre);
        }
    }

    public String getNombresCamareros() {
        if (this.camareros == null || this.camareros.isEmpty()) {
            return "Ninguno";
        }
        return String.join(", ", this.camareros);
    }

    public void añadirProducto(Producto p) {
        if (p != null) {
            if (this.productos == null) this.productos = new ArrayList<>();
            productos.add(p);
            calcularTotal();
        }
    }

    public float calcularTotal() {
        if (this.productos == null) return 0.0f;
        this.total = (float) productos.stream().mapToDouble(Producto::getPrecio).sum();
        return this.total;
    }

    public void cobrar() {
        System.out.println("Ticket Nº " + numeroTicket + " cobrado.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TICKET MESA Nº ").append(numeroTicket).append(" ===\n");
        sb.append("ATENDIDO POR: ").append(getNombresCamareros()).append("\n");
        sb.append("--------------------------------\n");
        if (productos != null) {
            productos.forEach(p -> sb.append("- ").append(p.getNombre()).append(" : ").append(String.format("%.2f", p.getPrecio())).append("€\n"));
        }
        sb.append("---------------------\nTOTAL: ").append(String.format("%.2f", total)).append("€\n");
        return sb.toString();
    }
}
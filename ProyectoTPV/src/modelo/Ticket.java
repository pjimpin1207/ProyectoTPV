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

    // NUEVO: Colección para guardar a los camareros sin que se repitan
    private HashSet<String> camareros = new HashSet<>();
    public Ticket() {}

    public Ticket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
        this.productos = new ArrayList<>();
        this.total = 0.0f;
        this.observaciones = "";
        this.fecha = new Date();
        this.camareros = new HashSet<>(); // Inicializamos la lista
    }

    public int getNumeroTicket() { return numeroTicket; }
    public ArrayList<Producto> getProductos() { return productos; }
    public float getTotal() { return total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Date getFecha() { return fecha; }

    // NUEVO: Métodos para gestionar camareros
    public void añadirCamarero(String nombre) {
        // PARACAÍDAS: Si la base de datos la dejó en null, la creamos al vuelo
        if (this.camareros == null) {
            this.camareros = new HashSet<>();
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.camareros.add(nombre);
        }
    }

    public String getNombresCamareros() {
        // PARACAÍDAS: Comprobamos si es null ANTES de preguntar si está vacía
        if (this.camareros == null || this.camareros.isEmpty()) {
            return "Ninguno";
        }
        return String.join(", ", this.camareros);
    }

    public void añadirProducto(Producto p) {
        if (p != null) {
            productos.add(p);
            calcularTotal();
        }
    }

    public float calcularTotal() {
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
        productos.forEach(p -> sb.append("- ").append(p.getNombre()).append(" : ").append(String.format("%.2f", p.getPrecio())).append("€\n"));
        sb.append("---------------------\nTOTAL: ").append(String.format("%.2f", total)).append("€\n");
        return sb.toString();
    }
}
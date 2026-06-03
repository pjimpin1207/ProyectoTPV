package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import modelo.Mesa;
import modelo.Ticket;

public class SistemaTPV {
    private Date fechaSesion;
    private float totalCaja;
    private int numeroTickets;

    // Colección avanzada tipo Map para indexar mesas por su identificador único [cite: 13, 54]
    private Map<Integer, Mesa> mapaMesas;

    public SistemaTPV() {
        this.fechaSesion = new Date();
        this.totalCaja = 0.0f;
        this.numeroTickets = 0;
        this.mapaMesas = new HashMap<>();
        inicializarMesas();
    }

    private void inicializarMesas() {
        // Se preconfiguran 12 mesas de acuerdo al boceto de distribución espacial [cite: 141, 154]
        for (int i = 1; i <= 12; i++) {
            mapaMesas.put(i, new Mesa(i));
        }
    }

    public void iniciarSesion(Date fecha) {
        this.fechaSesion = fecha;
        this.totalCaja = 0.0f;
        System.out.println("Sesión de TPV iniciada con fecha: " + fechaSesion);
    }

    public void registrarTicket(Ticket t) {
        if (t != null) {
            this.totalCaja += t.getTotal();
            this.numeroTickets++;
            System.out.println("Ticket registrado con éxito en la caja.");
        }
    }

    public void mostrarResumenCaja() {
        System.out.println("====== RESUMEN DE CIERRE ======");
        System.out.println("Fecha de Sesión: " + fechaSesion);
        System.out.println("Tickets emitidos: " + numeroTickets);
        System.out.println("Recaudación Total: " + totalCaja + "€");
        System.out.println("===============================");
    }

    public void cerrarCaja() {
        mostrarResumenCaja();
        // Restablece contadores de sesión
        this.numeroTickets = 0;
        this.totalCaja = 0.0f;
    }

    public Mesa getMesa(int numero) {
        return mapaMesas.get(numero);
    }

    public float getTotalCaja() { return totalCaja; }
}
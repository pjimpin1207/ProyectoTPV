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
    private Map<Integer, Mesa> mapaMesas;

    public SistemaTPV() {
        this.fechaSesion = new Date();
        this.totalCaja = 0.0f;
        this.numeroTickets = 0;
        this.mapaMesas = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            mapaMesas.put(i, new Mesa(i));
        }
    }

    public void iniciarSesion(Date fecha) {
        this.fechaSesion = fecha;
        this.totalCaja = 0.0f;
    }

    public void registrarTicket(Ticket t) {
        if (t != null) {
            this.totalCaja += t.getTotal();
            this.numeroTickets++;
        }
    }

    public Mesa getMesa(int numero) {
        return mapaMesas.get(numero);
    }
}
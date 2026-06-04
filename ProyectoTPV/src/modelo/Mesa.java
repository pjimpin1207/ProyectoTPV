package modelo;

import excepciones.MesaException;

public class Mesa {
    private int numero;
    private EstadoMesa estado;
    private Ticket ticketActivo;

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.LIBRE;
        this.ticketActivo = new Ticket(numero);
    }

    public int getNumero() { return numero; }
    public EstadoMesa getEstado() { return estado; }
    public void cambiarEstado(EstadoMesa nuevoEstado) { this.estado = nuevoEstado; }
    public Ticket getTicketActivo() { return ticketActivo; }

    public void liberarMesa() throws MesaException {
        if (this.estado == EstadoMesa.PENDIENTE_PAGO) {
            throw new MesaException("La mesa tiene pagos pendientes.");
        }
        this.estado = EstadoMesa.LIBRE;
        this.ticketActivo = new Ticket(numero);
    }
}
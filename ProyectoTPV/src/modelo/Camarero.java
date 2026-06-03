package modelo;

import excepciones.MesaException;

public class Camarero extends Usuario {

    public Camarero(int id, String nombre, String password) {
        super(id, nombre, password);
    }

    public void seleccionarMesa(Mesa m) throws MesaException {
        if (m.getEstado() == EstadoMesa.RESERVADA) {
            throw new MesaException("La mesa número " + m.getNumero() + " está reservada.");
        }
        m.cambiarEstado(EstadoMesa.OCUPADA);
    }

    public Ticket crearTicket(Mesa m, int numeroTicket) throws MesaException {
        if (m.getEstado() != EstadoMesa.OCUPADA) {
            throw new MesaException("No se puede crear un ticket si la mesa no está ocupada.");
        }
        return new Ticket(numeroTicket);
    }
}
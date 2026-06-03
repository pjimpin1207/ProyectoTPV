package modelo;


import excepciones.MesaException;

public class Mesa {
    private int numero;
    private EstadoMesa estado;

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.LIBRE; // Estado por defecto
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public EstadoMesa getEstado() { return estado; }

    public void cambiarEstado(EstadoMesa nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void liberarMesa() throws MesaException {
        if (this.estado == EstadoMesa.PENDIENTE_PAGO) {
            throw new MesaException("No se puede liberar la mesa " + numero + " porque tiene pagos pendientes.");
        }
        this.estado = EstadoMesa.LIBRE;
    }
}
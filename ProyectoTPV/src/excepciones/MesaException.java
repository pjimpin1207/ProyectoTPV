package excepciones;

public class MesaException extends Exception {
    private static final long serialVersionUID = 1L;
    public MesaException(String mensaje) { super(mensaje); }
}
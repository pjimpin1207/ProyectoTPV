package modelo;


public class Administrator extends Usuario {

    public Administrator(int id, String nombre, String password) {
        super(id, nombre, password);
    }

    public void gestionarProducto() {
        // Se conectará con el módulo de la vista e interfaz de administración
    }

    public void verCierreCaja(float totalCaja) {
        System.out.println("El total de caja acumulado en la sesión actual es: " + totalCaja + "€");
    }
}
package main;

import java.util.Date;
import controlador.SistemaTPV;
import modelo.Categoria;
import modelo.Producto;
import modelo.Ticket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema TPV");

        // Instanciación del controlador
        SistemaTPV tpv = new SistemaTPV();
        iniciarSesion(new Date());

        // crear ticket
        Ticket testTicket = new Ticket(1);
        testTicket.añadirProducto(new Producto(101, "Cerveza", Categoria.BEBIDA, 2.50f));
        testTicket.añadirProducto(new Producto(204, "Patatas", Categoria.COMIDA, 4.50f));

        System.out.println(testTicket);

        registrarTicket(testTicket);
        mostrarResumenCaja();
    }
}
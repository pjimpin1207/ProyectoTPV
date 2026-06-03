package main; // Cambia esto si tu clase Main está en otro paquete o bórralo si está suelta en src

import ui.VentanaLogin;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo especial de eventos de Swing (buenas prácticas)
        SwingUtilities.invokeLater(() -> {
            VentanaLogin ventana = new VentanaLogin();
            ventana.setVisible(true);
        });
    }
}
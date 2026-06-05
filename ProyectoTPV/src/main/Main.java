package main;

import ui.VentanaLogin;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Ejecutamos la ventana en el hilo de eventos de AWT
        java.awt.EventQueue.invokeLater(() -> {
            VentanaLogin ventana = new VentanaLogin();
            ventana.setVisible(true);
        });
    }
}
package ui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema TPV - Panel Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usamos un JTabbedPane para poder cambiar entre la vista de mesas y la de administrador
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Mesas / Comedor", new VentanaMesas(new java.util.Date()));
        pestañas.addTab("Administración", new VentanaAdministrador());

        add(pestañas, BorderLayout.CENTER);
    }
}
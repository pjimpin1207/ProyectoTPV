package ui;

import javax.swing.*;
import java.awt.*;

public class PanelAdministrador extends JPanel {

    public PanelAdministrador() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Carta y Caja", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        JButton btnAñadir = new JButton("Añadir Producto");
        JButton btnVerCaja = new JButton("Cierre de Caja");

        panelBotones.add(btnAñadir);
        panelBotones.add(btnVerCaja);

        add(panelBotones, BorderLayout.CENTER);

        // Aquí irían los ActionListeners para conectar con la base de datos
        btnAñadir.addActionListener(e -> JOptionPane.showMessageDialog(this, "Aquí se abriría el formulario para añadir un nuevo producto."));
        btnVerCaja.addActionListener(e -> JOptionPane.showMessageDialog(this, "Total recaudado en la sesión: 0.00€"));
    }
}
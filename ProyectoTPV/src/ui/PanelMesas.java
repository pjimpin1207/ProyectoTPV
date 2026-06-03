package ui;

import javax.swing.*;
import java.awt.*;
import modelo.EstadoMesa;
import modelo.Mesa;
import modelo.Ticket;

public class PanelMesas extends JPanel {

    public PanelMesas() {
        setLayout(new GridLayout(3, 4, 15, 15)); // Cuadrícula para 12 mesas
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Generamos los botones para las 12 mesas
        for (int i = 1; i <= 12; i++) {
            Mesa mesaModelo = new Mesa(i);
            JButton btnMesa = new JButton("Mesa " + i);
            btnMesa.setFont(new Font("Arial", Font.BOLD, 14));

            // Color inicial verde (LIBRE)
            btnMesa.setBackground(Color.GREEN);

            btnMesa.addActionListener(e -> abrirMesa(mesaModelo, btnMesa));
            add(btnMesa);
        }
    }

    private void abrirMesa(Mesa mesa, JButton boton) {
        if (mesa.getEstado() == EstadoMesa.LIBRE) {
            mesa.cambiarEstado(EstadoMesa.OCUPADA);
            boton.setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "Mesa " + mesa.getNumero() + " abierta. Generando ticket...");
            // Aquí llamarías a tu ventana de añadir productos al ticket
        } else {
            // Simulamos la recuperación del ticket activo de esta mesa
            Ticket ticketSimulado = new Ticket(mesa.getNumero());

            // Ahora pasamos los 3 parámetros (Mesa, JButton y Ticket) a DialogoCobro
            new DialogoCobro(mesa, boton, ticketSimulado).setVisible(true);
        }
    }
}
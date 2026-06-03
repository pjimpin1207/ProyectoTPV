package ui;

import javax.swing.*;
import java.awt.*;
import modelo.EstadoMesa;
import modelo.Mesa;
import excepciones.MesaException;

public class DialogoCobro extends JDialog {

    public DialogoCobro(Mesa mesa, JButton botonMesa) {
        setTitle("Cobrar Mesa " + mesa.getNumero());
        setSize(300, 200);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTotal = new JLabel("Total a cobrar: 45.50€", SwingConstants.CENTER); // Simulado
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTotal, BorderLayout.CENTER);

        JButton btnCobrar = new JButton("Cobrar y Liberar");
        btnCobrar.addActionListener(e -> {
            try {
                // Simulamos el pago
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                mesa.cambiarEstado(EstadoMesa.LIBRE); // Se liberaría tras pagar
                botonMesa.setBackground(Color.GREEN); // Vuelve a estar verde
                JOptionPane.showMessageDialog(this, "Cobro realizado con éxito.");
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al liberar la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnCobrar, BorderLayout.SOUTH);
    }
}
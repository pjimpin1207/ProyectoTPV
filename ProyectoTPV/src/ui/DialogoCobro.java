package ui;

import javax.swing.*;
import java.awt.*;
import modelo.Mesa;
import modelo.Ticket;
import modelo.EstadoMesa;
import dao.TicketDAO;
import dao.TicketObjectDBDAO;

public class DialogoCobro extends JDialog {
    public DialogoCobro(JFrame parent, Mesa mesa, String camareroActual, Ticket ticketActivo) {
        super(parent, "Cobro", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentro = new JPanel(new GridLayout(3, 1));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ACTUALIZADO: Obtenemos toda la lista de camareros del ticket
        JLabel lblInfo = new JLabel("MESA Nº " + mesa.getNumero() + " | " + ticketActivo.getNombresCamareros(), SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14)); // Letra un poco más pequeña por si son muchos

        JLabel lblResumen = new JLabel(ticketActivo.getProductos().size() + " productos consumidos", SwingConstants.CENTER);
        JLabel lblMonto = new JLabel("MONTO COBRADO: " + String.format("%.2f", ticketActivo.getTotal()) + "€", SwingConstants.CENTER);
        lblMonto.setFont(new Font("Arial", Font.BOLD, 20));
        lblMonto.setForeground(Color.RED);

        panelCentro.add(lblInfo);
        panelCentro.add(lblResumen);
        panelCentro.add(lblMonto);
        add(panelCentro, BorderLayout.CENTER);

        JButton btnRegresar = new JButton("ACEPTAR Y REGRESAR");
        btnRegresar.addActionListener(e -> {
            try {
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                ticketActivo.cobrar();

                TicketDAO relacionalDAO = new TicketDAO();
                relacionalDAO.guardarTicket(ticketActivo);

                TicketObjectDBDAO oodbDAO = new TicketObjectDBDAO();
                oodbDAO.guardarTicketObjeto(ticketActivo);

                mesa.cambiarEstado(EstadoMesa.OCUPADA);
                mesa.liberarMesa();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en BD: " + ex.getMessage());
            }

            new VentanaMesas(new java.util.Date()).setVisible(true);
            parent.dispose();
            this.dispose();
        });

        add(btnRegresar, BorderLayout.SOUTH);
    }
}
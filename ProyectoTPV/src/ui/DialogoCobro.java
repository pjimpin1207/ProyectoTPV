package ui;

import javax.swing.*;
import java.awt.*;
import modelo.EstadoMesa;
import modelo.Mesa;
import dao.TicketDAO;
import dao.TicketObjectDBDAO;
import modelo.Ticket;

public class DialogoCobro extends JDialog {

    public DialogoCobro(Mesa mesa, JButton botonMesa, Ticket ticketActivo) {
        setTitle("Cobrar Mesa " + mesa.getNumero());
        setSize(300, 200);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTotal = new JLabel("Total a cobrar: " + ticketActivo.getTotal() + "€", SwingConstants.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTotal, BorderLayout.CENTER);

        JButton btnCobrar = new JButton("Cobrar y Liberar");
        btnCobrar.addActionListener(e -> {
            try {
                // 1. Cambiar estados de negocio
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                ticketActivo.cobrar();

                // 2. Persistencia en Base de Datos Relacional (MariaDB)
                TicketDAO relacionalDAO = new TicketDAO();
                relacionalDAO.guardarTicket(ticketActivo);

                // 3. Persistencia en Base de Datos Orientada a Objetos (ObjectDB)
                TicketObjectDBDAO oodbDAO = new TicketObjectDBDAO();
                oodbDAO.guardarTicketObjeto(ticketActivo);

                // 4. Liberación de la interfaz gráfica
                mesa.liberarMesa();
                botonMesa.setBackground(Color.GREEN);

                JOptionPane.showMessageDialog(this, "Cobro registrado con éxito en ambos sistemas de datos.");
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en el proceso de cobro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnCobrar, BorderLayout.SOUTH);
    }
}
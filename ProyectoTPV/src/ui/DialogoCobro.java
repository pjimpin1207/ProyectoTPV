package ui;

import java.awt.*;
import java.awt.event.*;
import modelo.Mesa;
import modelo.Ticket;
import modelo.EstadoMesa;
import dao.TicketDAO;
import dao.TicketObjectDBDAO;

public class DialogoCobro extends Dialog {
    public DialogoCobro(Frame parent, Mesa mesa, String camarero, Ticket ticketActivo) {
        super(parent, "Cobro", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Panel panelCentro = new Panel(new GridLayout(3, 1));

        Label lblInfo = new Label("MESA Nº " + mesa.getNumero() + " | " + ticketActivo.getNombresCamareros(), Label.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        Label lblResumen = new Label(ticketActivo.getProductos().size() + " productos consumidos", Label.CENTER);
        Label lblMonto = new Label("MONTO COBRADO: " + String.format("%.2f", ticketActivo.getTotal()) + "€", Label.CENTER);
        lblMonto.setFont(new Font("Arial", Font.BOLD, 20));
        lblMonto.setForeground(Color.RED);

        panelCentro.add(lblInfo);
        panelCentro.add(lblResumen);
        panelCentro.add(lblMonto);
        add(panelCentro, BorderLayout.CENTER);

        Button btnRegresar = new Button("ACEPTAR PAGO Y REGRESAR");
        btnRegresar.addActionListener(e -> {
            try {
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                ticketActivo.cobrar();
                new TicketDAO().guardarTicket(ticketActivo);
                new TicketObjectDBDAO().guardarTicketObjeto(ticketActivo);
                mesa.cambiarEstado(EstadoMesa.OCUPADA);
                mesa.liberarMesa();
            } catch (Exception ex) {
                MensajesAWT.mostrarMensaje(parent, "Error en BD: " + ex.getMessage(), "Error");
            }
            new VentanaMesas(new java.util.Date()).setVisible(true);
            parent.dispose();
            this.dispose();
        });

        add(btnRegresar, BorderLayout.SOUTH);
    }
}
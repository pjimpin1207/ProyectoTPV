package ui;

import java.awt.*;
<<<<<<< Updated upstream
=======
import java.awt.event.*;
import modelo.Mesa;
import modelo.Ticket;
>>>>>>> Stashed changes
import modelo.EstadoMesa;
import modelo.Mesa;
import dao.TicketDAO;
import dao.TicketObjectDBDAO;
import modelo.Ticket;

<<<<<<< Updated upstream
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
=======
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
>>>>>>> Stashed changes
            try {
                // 1. Cambiar estados de negocio
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                ticketActivo.cobrar();
<<<<<<< Updated upstream

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
=======
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
>>>>>>> Stashed changes
        });
        add(btnCobrar, BorderLayout.SOUTH);
    }
}
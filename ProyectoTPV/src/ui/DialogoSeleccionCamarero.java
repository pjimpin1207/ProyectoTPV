package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import modelo.Mesa;
import dao.UsuarioDAO;

public class DialogoSeleccionCamarero extends JDialog {
    public DialogoSeleccionCamarero(JFrame parent, Mesa mesa) {
        super(parent, "Selección de Camarero", true);
        setSize(300, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("SELECCIÓN DE CAMARERO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        UsuarioDAO uDao = new UsuarioDAO();
        List<String> camareros = uDao.obtenerNombresCamareros();

        JPanel panelBotones = new JPanel(new GridLayout(Math.max(4, camareros.size()), 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        if (camareros.isEmpty()) {
            panelBotones.add(new JLabel("No hay camareros. Crea uno en ADMIN.", SwingConstants.CENTER));
        } else {
            for (String nombreCamarero : camareros) {
                JButton btnCamarero = new JButton(nombreCamarero);
                btnCamarero.addActionListener(e -> {
                    // NUEVO: Ya no creamos ticket nuevo. Cogemos el que tiene la mesa en el Gestor.
                    new VentanaComanda(mesa, nombreCamarero, mesa.getTicketActivo()).setVisible(true);
                    parent.dispose();
                    this.dispose();
                });
                panelBotones.add(btnCamarero);
            }
        }
        add(new JScrollPane(panelBotones), BorderLayout.CENTER);
    }
}
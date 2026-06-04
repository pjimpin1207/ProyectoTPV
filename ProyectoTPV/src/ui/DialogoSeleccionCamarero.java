package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import modelo.Mesa;
import dao.UsuarioDAO;

public class DialogoSeleccionCamarero extends Dialog {
    public DialogoSeleccionCamarero(Frame parent, Mesa mesa) {
        super(parent, "Selección de Camarero", true);
        setSize(300, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Label titulo = new Label("SELECCIÓN DE CAMARERO", Label.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(titulo, BorderLayout.NORTH);

        UsuarioDAO uDao = new UsuarioDAO();
        List<String> camareros = uDao.obtenerNombresCamareros();

        Panel panelBotones = new Panel(new GridLayout(Math.max(4, camareros.size()), 1, 10, 10));

        if (camareros.isEmpty()) {
            panelBotones.add(new Label("No hay camareros.", Label.CENTER));
        } else {
            for (String nombreCamarero : camareros) {
                Button btnCamarero = new Button(nombreCamarero);
                btnCamarero.addActionListener(e -> {
                    new VentanaComanda(mesa, nombreCamarero, mesa.getTicketActivo()).setVisible(true);
                    parent.dispose();
                    this.dispose();
                });
                panelBotones.add(btnCamarero);
            }
        }
        ScrollPane scroll = new ScrollPane();
        scroll.add(panelBotones);
        add(scroll, BorderLayout.CENTER);
    }
}
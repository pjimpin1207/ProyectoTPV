package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import modelo.Mesa;
import modelo.Usuario;
import modelo.EstadoMesa;
import dao.UsuarioDAO;
import controlador.GestorMesas;

public class VentanaMesas extends Frame {
    public VentanaMesas(Date fechaSesion) {
        setTitle("Sistema TPV - Sala");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        Panel panelCuadricula = new Panel(new GridLayout(3, 4, 15, 15));

        Button btnAdmin = new Button("ADMIN");
        btnAdmin.setBackground(Color.DARK_GRAY);
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdmin.addActionListener(e -> {
            String pass = MensajesAWT.pedirPassword(this, "Contraseña (admin / 1234):");
            if (pass != null) {
                UsuarioDAO dao = new UsuarioDAO();
                Usuario admin = dao.validarLogin("admin", pass);

                if (admin != null) {
                    new VentanaAdministrador().setVisible(true);
                    this.dispose();
                } else {
                    MensajesAWT.mostrarMensaje(this, "Contraseña incorrecta.", "Error");
                }
            }
        });
        panelCuadricula.add(btnAdmin);

        for (int i = 1; i <= 11; i++) {
            Mesa mesaModelo = GestorMesas.getInstancia().getMesa(i);
            Button btnMesa = new Button("Mesa " + i);
            btnMesa.setFont(new Font("Arial", Font.BOLD, 14));

            if (mesaModelo.getEstado() == EstadoMesa.LIBRE) {
                btnMesa.setBackground(Color.decode("#87CEFA"));
                btnMesa.setForeground(Color.BLACK);
            } else {
                btnMesa.setBackground(Color.decode("#D32F2F"));
                btnMesa.setForeground(Color.WHITE);
            }

            btnMesa.addActionListener(e -> {
                DialogoSeleccionCamarero d = new DialogoSeleccionCamarero(this, mesaModelo);
                d.setVisible(true);
            });
            panelCuadricula.add(btnMesa);
        }

        Panel margen = new Panel(new BorderLayout());
        margen.add(panelCuadricula, BorderLayout.CENTER);
        add(margen, BorderLayout.CENTER);
    }
}
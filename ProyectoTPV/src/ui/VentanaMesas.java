package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import modelo.Mesa;
import modelo.Usuario;
import modelo.EstadoMesa;
import dao.UsuarioDAO;
import controlador.GestorMesas;

public class VentanaMesas extends JFrame {
    public VentanaMesas(Date fechaSesion) {
        setTitle("Sistema TPV - Sala (Fecha: " + fechaSesion.toString() + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelCuadricula = new JPanel(new GridLayout(3, 4, 15, 15));
        panelCuadricula.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAdmin = new JButton("ADMIN");
        btnAdmin.setBackground(Color.DARK_GRAY);
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdmin.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(this, pf, "Contraseña de Administrador (admin / 1234):", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okCxl == JOptionPane.OK_OPTION) {
                String password = new String(pf.getPassword());
                UsuarioDAO dao = new UsuarioDAO();
                Usuario admin = dao.validarLogin("admin", password);

                if (admin != null) {
                    new VentanaAdministrador().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelCuadricula.add(btnAdmin);

        for (int i = 1; i <= 11; i++) {
            // NUEVO: Pedimos la mesa al Gestor en lugar de crearla
            Mesa mesaModelo = GestorMesas.getInstancia().getMesa(i);

            JButton btnMesa = new JButton("Mesa " + String.format("%03d", i));
            btnMesa.setFont(new Font("Arial", Font.BOLD, 14));

            // NUEVO: Pinta en Rojo si está ocupada, o Verde si está libre
            if (mesaModelo.getEstado() == EstadoMesa.LIBRE) {
                btnMesa.setBackground(Color.GREEN);
            } else {
                btnMesa.setBackground(Color.RED);
            }

            btnMesa.addActionListener(e -> new DialogoSeleccionCamarero(this, mesaModelo).setVisible(true));
            panelCuadricula.add(btnMesa);
        }
        add(panelCuadricula, BorderLayout.CENTER);
    }
}
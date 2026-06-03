package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class VentanaLogin extends JFrame {
    public VentanaLogin() {
        setTitle("Bienvenido al Sistema TPV");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("BIENVENIDO AL SISTEMA TPV", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinnerFecha = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerFecha, "dd / MM / yyyy");
        spinnerFecha.setEditor(editor);
        spinnerFecha.setFont(new Font("Arial", Font.PLAIN, 16));

        panelCentro.add(new JLabel("SELECCIONE UNA FECHA:", SwingConstants.CENTER));
        panelCentro.add(spinnerFecha);
        add(panelCentro, BorderLayout.CENTER);

        JButton btnContinuar = new JButton("CONTINUAR --->");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.addActionListener(e -> {
            Date fechaSeleccionada = (Date) spinnerFecha.getValue();
            new VentanaMesas(fechaSeleccionada).setVisible(true);
            this.dispose();
        });

        JPanel panelSur = new JPanel();
        panelSur.add(btnContinuar);
        add(panelSur, BorderLayout.SOUTH);
    }
}
package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class VentanaLogin extends Frame {

    public VentanaLogin() {
        setTitle("Bienvenido al Sistema TPV");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        Label lblTitulo = new Label("BIENVENIDO AL SISTEMA TPV", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        Panel panelCentro = new Panel(new GridLayout(2, 1, 10, 10));
        panelCentro.add(new Label("SISTEMA INICIADO:", Label.CENTER));

        TextField txtFecha = new TextField(new Date().toString());
        txtFecha.setEditable(false);
        Panel pTxt = new Panel();
        pTxt.add(txtFecha);
        panelCentro.add(pTxt);

        add(panelCentro, BorderLayout.CENTER);

        Button btnContinuar = new Button("CONTINUAR --->");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.addActionListener(e -> {
            new VentanaMesas(new Date()).setVisible(true);
            this.dispose();
        });

        Panel panelSur = new Panel();
        panelSur.add(btnContinuar);
        add(panelSur, BorderLayout.SOUTH);
    }
}
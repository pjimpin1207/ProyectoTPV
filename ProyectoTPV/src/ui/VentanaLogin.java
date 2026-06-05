package ui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
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

        Panel panelCentro = new Panel(new GridLayout(3, 1, 10, 10));
        panelCentro.add(new Label("FECHA DE SESIÓN (dd/MM/yyyy):", Label.CENTER));

        // Creamos un formateador para mostrar y leer la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        TextField txtFecha = new TextField(sdf.format(new Date()));
        txtFecha.setEditable(true); // ¡Ahora es editable!

        Panel pTxt = new Panel();
        pTxt.add(txtFecha);
        panelCentro.add(pTxt);

        add(panelCentro, BorderLayout.CENTER);

        Button btnContinuar = new Button("CONTINUAR --->");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.addActionListener(e -> {
            try {
                // Intentamos convertir el texto ingresado en un objeto Date
                Date fechaSeleccionada = sdf.parse(txtFecha.getText());
                new VentanaMesas(fechaSeleccionada).setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                // Si el usuario escribe letras o un formato raro, salta este error
                MensajesAWT.mostrarMensaje(this, "Formato de fecha incorrecto. Usa dd/MM/yyyy", "Error");
            }
        });

        Panel panelSur = new Panel();
        panelSur.add(btnContinuar);
        add(panelSur, BorderLayout.SOUTH);
    }
}
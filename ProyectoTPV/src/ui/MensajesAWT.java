package ui;

import java.awt.*;
import java.awt.event.*;

public class MensajesAWT {

    public static void mostrarMensaje(Window parent, String mensaje, String titulo) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout(10, 10));
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.CENTER);
        Button btn = new Button("Aceptar");
        btn.addActionListener(e -> d.dispose());
        Panel p = new Panel(); p.add(btn);
        d.add(p, BorderLayout.SOUTH);
        d.setSize(350, 120);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
    }

    public static String pedirInput(Window parent, String mensaje, String titulo, String valorInicial) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new GridLayout(3, 1));
        d.add(new Label(mensaje, Label.CENTER));
        TextField txt = new TextField(valorInicial != null ? valorInicial : "");
        Panel pTxt = new Panel(); pTxt.add(txt);
        d.add(pTxt);

        Panel pBotones = new Panel();
        Button btnAceptar = new Button("Aceptar");
        Button btnCancelar = new Button("Cancelar");
        String[] result = new String[1];

        btnAceptar.addActionListener(e -> { result[0] = txt.getText(); d.dispose(); });
        btnCancelar.addActionListener(e -> d.dispose());

        pBotones.add(btnAceptar); pBotones.add(btnCancelar);
        d.add(pBotones);

        d.setSize(350, 150);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
        return result[0];
    }

    public static boolean pedirConfirmacion(Window parent, String mensaje, String titulo) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout(10, 10));
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.CENTER);

        Panel pBotones = new Panel();
        Button btnSi = new Button("Sí");
        Button btnNo = new Button("No");
        boolean[] result = new boolean[1];

        btnSi.addActionListener(e -> { result[0] = true; d.dispose(); });
        btnNo.addActionListener(e -> { result[0] = false; d.dispose(); });

        pBotones.add(btnSi); pBotones.add(btnNo);
        d.add(pBotones, BorderLayout.SOUTH);

        d.setSize(400, 120);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
        return result[0];
    }

    public static String pedirPassword(Window parent, String mensaje) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, "Seguridad", true);
        d.setLayout(new GridLayout(3, 1));
        d.add(new Label(mensaje, Label.CENTER));
        TextField txt = new TextField(15);
        txt.setEchoChar('*'); // Esto oculta el texto como contraseña
        Panel pTxt = new Panel(); pTxt.add(txt);
        d.add(pTxt);

        Panel pBotones = new Panel();
        Button btnOk = new Button("Entrar");
        Button btnCancelar = new Button("Cancelar");
        String[] result = new String[1];

        btnOk.addActionListener(e -> { result[0] = txt.getText(); d.dispose(); });
        btnCancelar.addActionListener(e -> d.dispose());

        pBotones.add(btnOk); pBotones.add(btnCancelar);
        d.add(pBotones);

        d.setSize(350, 150);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
        return result[0];
    }

    public static String pedirOpcion(Window parent, String mensaje, String titulo, String[] opciones) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout());
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.NORTH);

        java.awt.List lista = new java.awt.List();
        for(String o : opciones) lista.add(o);
        d.add(lista, BorderLayout.CENTER);

        Panel pBotones = new Panel();
        Button btnSel = new Button("Seleccionar");
        Button btnCancel = new Button("Cancelar");
        String[] result = new String[1];

        btnSel.addActionListener(e -> {
            if(lista.getSelectedItem() != null) { result[0] = lista.getSelectedItem(); d.dispose(); }
        });
        btnCancel.addActionListener(e -> d.dispose());

        pBotones.add(btnSel); pBotones.add(btnCancel);
        d.add(pBotones, BorderLayout.SOUTH);

        d.setSize(300, 200);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
        return result[0];
    }
}
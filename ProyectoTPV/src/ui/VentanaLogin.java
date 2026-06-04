package ui;

import java.awt.*;
<<<<<<< Updated upstream
import dao.UsuarioDAO;
import modelo.Usuario;

public class VentanaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public VentanaLogin() {
        setTitle("Sistema TPV - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
=======
import java.awt.event.*;
import java.util.Date;

public class VentanaLogin extends Frame {
    public VentanaLogin() {
        setTitle("Bienvenido al Sistema TPV");
        setSize(400, 300);
>>>>>>> Stashed changes
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

<<<<<<< Updated upstream
        add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnLogin = new JButton("Entrar");
        add(new JLabel()); // Espacio vacío
        add(btnLogin);

        btnLogin.addActionListener(e -> hacerLogin());
    }

    private void hacerLogin() {
        String user = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario validado = dao.validarLogin(user, pass);

        if (validado != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + validado.getNombre());
            new VentanaPrincipal().setVisible(true);
            this.dispose(); // Cierra el login
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
=======
        // Evento vital en AWT para poder cerrar el programa
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
        Panel pTxt = new Panel(); pTxt.add(txtFecha);
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
>>>>>>> Stashed changes
    }
}
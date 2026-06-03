package ui;

import javax.swing.*;
import java.awt.*;
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
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

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
    }
}
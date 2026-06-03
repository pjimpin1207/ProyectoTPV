package dao;

import java.sql.*;
import modelo.Camarero;
import modelo.Usuario;

public class UsuarioDAO {

    public Usuario validarLogin(String nombre, String password) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Por simplicidad, retornamos un Camarero genérico si el login es correcto
                return new Camarero(rs.getInt("id"), rs.getString("nombre"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
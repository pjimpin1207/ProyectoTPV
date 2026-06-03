package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Administrador;
import modelo.Camarero;
import modelo.Usuario;

public class UsuarioDAO {
    public Usuario validarLogin(String nombre, String password) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if(rs.getString("rol").equals("ADMINISTRADOR")) {
                    return new Administrador(rs.getInt("id"), rs.getString("nombre"), rs.getString("password"));
                }
                return new Camarero(rs.getInt("id"), rs.getString("nombre"), "");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void insertarCamarero(String nombre) {
        String sql = "INSERT INTO usuarios (nombre, password, rol) VALUES (?, '', 'CAMARERO')";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarCamarero(String nombre) {
        String sql = "DELETE FROM usuarios WHERE nombre = ? AND rol = 'CAMARERO'";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<String> obtenerNombresCamareros() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM usuarios WHERE rol = 'CAMARERO'";
        try (Connection conn = ConexionDB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) nombres.add(rs.getString("nombre"));
        } catch (SQLException e) { e.printStackTrace(); }
        return nombres;
    }
}
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Producto;

public class ProductoDAO {

    public void insertarProducto(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setFloat(3, p.getPrecio());
            pstmt.executeUpdate();
            System.out.println("Producto guardado en MariaDB.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        Categoria.valueOf(rs.getString("categoria")),
                        rs.getFloat("precio")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
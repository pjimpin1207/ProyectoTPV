package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Producto;

public class ProductoDAO {
    public void insertarProducto(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setFloat(3, p.getPrecio());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void modificarProducto(String nombreViejo, float nuevoPrecio) {
        String sql = "UPDATE productos SET precio = ? WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, nuevoPrecio);
            pstmt.setString(2, nombreViejo);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void modificarNombreProducto(String nombreViejo, String nombreNuevo) {
        String sql = "UPDATE productos SET nombre = ? WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo);
            pstmt.setString(2, nombreViejo);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarProducto(String nombre) {
        String sql = "DELETE FROM productos WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";
        try (Connection conn = ConexionDB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Producto(rs.getInt("id"), rs.getString("nombre"), Categoria.valueOf(rs.getString("categoria")), rs.getFloat("precio")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
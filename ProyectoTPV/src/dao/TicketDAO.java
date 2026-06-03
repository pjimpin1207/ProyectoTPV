package dao;
import java.sql.*;
import modelo.Ticket;

public class TicketDAO {

    public void guardarTicket(Ticket t) {
        String sql = "INSERT INTO tickets (total, observaciones) VALUES (?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setFloat(1, t.getTotal());
            pstmt.setString(2, t.getObservaciones());
            pstmt.executeUpdate();
            System.out.println("Ticket registrado en la base de datos.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
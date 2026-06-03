package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import modelo.Ticket;

public class TicketObjectDBDAO {

    public void guardarTicketObjeto(Ticket t) {
        EntityManager em = ConexionObjectDB.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t); // Guarda el objeto completo de forma nativa
            tx.commit();
            System.out.println("Ticket completo guardado de forma nativa en ObjectDB.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Ticket obtenerTicketObjeto(int numeroTicket) {
        EntityManager em = ConexionObjectDB.getEntityManager();
        try {
            return em.find(Ticket.class, numeroTicket);
        } finally {
            em.close();
        }
    }

    public List<Ticket> obtenerTodosLosTicketsObjetos() {
        EntityManager em = ConexionObjectDB.getEntityManager();
        try {
            // Consulta de objetos nativos utilizando JPQL
            TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
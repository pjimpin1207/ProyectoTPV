package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo.Ticket;

public class TicketObjectDBDAO {
    public void guardarTicketObjeto(Ticket t) {
        EntityManager em = ConexionObjectDB.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Ticket> obtenerTicketsHoy() {
        EntityManager em = ConexionObjectDB.getEntityManager();
        List<Ticket> ticketsDeHoy = new ArrayList<>();
        try {
            TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
            List<Ticket> todos = query.getResultList();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String hoyStr = sdf.format(new Date());

            for (Ticket t : todos) {
                if (t.getFecha() != null && sdf.format(t.getFecha()).equals(hoyStr)) {
                    ticketsDeHoy.add(t);
                }
            }
        } finally {
            em.close();
        }
        return ticketsDeHoy;
    }
}
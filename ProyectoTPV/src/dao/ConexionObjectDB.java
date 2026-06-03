package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionObjectDB {
    private static EntityManagerFactory emf;

    static {
        // Crea o abre el archivo de la base de datos orientada a objetos
        emf = Persistence.createEntityManagerFactory("db/tickets_historial.odb");
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
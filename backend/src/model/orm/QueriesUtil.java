package model.orm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class QueriesUtil {

    @SuppressWarnings("unchecked")
    public static List<String> getAllPlaneIDs()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<String> query = session.createQuery("SELECT planeID FROM Airplane");
        List<String> list = query.list();
        session.close();
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Airplane getPlaneData(String planeID)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Airplane> query = session.createQuery("FROM Airplane WHERE planeID = :plane_id");
        query.setParameter("plane_id", planeID);
        Airplane airplane = query.uniqueResult();
        session.close();
        return airplane;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getFlightIDs(String planeID)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Integer> query = session.createQuery("SELECT flightID FROM Flight WHERE planeID = :plane_id");
        query.setParameter("plane_id", planeID);
        List<Integer> list = query.list();
        session.close();
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Flight getFlightDetails(int flightID)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Flight> query = session.createQuery("FROM Flight WHERE flightID = :flight_id");
        query.setParameter("flight_id", flightID);
        Flight flight = query.uniqueResult();
        session.close();
        return flight;
    }

    public static void saveFlight(Flight flight)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        session.save(flight);
        t.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    public static int updatePlaneDetails(String planeID, String lastPosition, float lastHeading, float lastAltitude)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Airplane> query = session.createQuery("UPDATE Airplane set lastPosition = :last_position, lastHeading = :last_heading, lastAltitude = :last_altitude WHERE id = :plane_id");
        query.setParameter("last_position", lastPosition);
        query.setParameter("last_heading", lastHeading);
        query.setParameter("last_altitude", lastAltitude);
        query.setParameter("plane_id", planeID);
        Transaction t = session.beginTransaction();
        int rowsAffected = query.executeUpdate();
        t.commit();
        session.close();
        return rowsAffected;
    }
}
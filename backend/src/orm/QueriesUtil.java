package orm;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class QueriesUtil {
    private SessionFactory sessionFactory;

    public List<String> getAllPlaneIDs()
    {
        return null;
    }

    public Airplane getPlaneData(String planeID)
    {

        return null;
    }

    public List<Integer> getFlightIDs(String planeID,String fromDate)
    {

        return null;
    }

    public Flight getFlightDetails(int flightID)
    {

        return null;
    }

    public void saveFlight(Flight flight)
    {

    }

    public int updatePlaneDetails(float lastHeading,float lastAltitude)
    {

        return 0;
    }
}

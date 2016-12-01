package condorcet.appinfo3.groupe4.enigwall;

import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import condorcet.appinfo3.groupe4.enigwall.DAO.EnigmeDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;

public class EnigmeTestApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        EnigmeDAO enigmeDAO = new EnigmeDAO();

        // Lecture multiple
        try {
            ArrayList<Enigme> liste = enigmeDAO.readAll("1");
            System.out.println(liste);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

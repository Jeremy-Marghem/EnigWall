package condorcet.appinfo3.groupe4.enigwall.TestApex;

import com.sun.jersey.api.client.WebResource;
import condorcet.appinfo3.groupe4.enigwall.DAO.ParcoursDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;

public class ParcoursTestApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        ParcoursDAO parcoursDAO = new ParcoursDAO();

        // Lecture avec id de la ville
        System.out.println("-- LECTURE --");
        try {
            Parcours par = parcoursDAO.read("1");
            System.out.println("Ville lu : " +par);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Parcours parcours = new Parcours(1, "Test parcours");
        System.out.println("-- CREATION --");
        int r = 0;
        try {
            r = parcoursDAO.create(parcours);
            System.out.println("Le parcours a bien été créé !");
            System.out.println("Son id -> "+r);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        parcours.setId_parcours(r);
        parcours.setId_ville(2);
        System.out.println("-- UPDATE --");
        try {
            parcoursDAO.update(parcours);
            System.out.println("Parcours mis à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            parcoursDAO.delete(parcours);
            System.out.println("Parcours supprimé !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

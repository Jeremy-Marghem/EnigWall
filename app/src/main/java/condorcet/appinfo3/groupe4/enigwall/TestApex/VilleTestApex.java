package condorcet.appinfo3.groupe4.enigwall.TestApex;

import condorcet.appinfo3.groupe4.enigwall.DAO.VilleDAO;
import com.sun.jersey.api.client.WebResource;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class VilleTestApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        VilleDAO villeDAO = new VilleDAO();

        // Lecture avec id
        System.out.println("-- LECTURE ID --");
        try {
            Ville ville = villeDAO.read("1");
            System.out.println("Ville lue : " +ville);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Lecture avec nom de la ville
        System.out.println("-- LECTURE NOM --");
        try {
            Ville vl = new Ville("Tournai");
            Ville ville = villeDAO.read(vl);
            System.out.println("Ville lue : " +ville);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Création d'une nouvelle ville si on reçoit son ID c'est ok si on reçoit 0 c'est que la ville existe déjà
        Ville ville = new Ville("ChiconCity");
        System.out.println("-- CREATION --");
        int r = 0;
        try {
            r = villeDAO.create(ville);
            System.out.println("La ville a bien été créée !");
            System.out.println("Son id -> "+r);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        ville.setId_ville(r);
        ville.setNomville("Tomatecity");
        System.out.println("-- UPDATE --");
        try {
            villeDAO.update(ville);
            System.out.println("Ville mise à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            villeDAO.delete(ville);
            System.out.println("Ville supprimée !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

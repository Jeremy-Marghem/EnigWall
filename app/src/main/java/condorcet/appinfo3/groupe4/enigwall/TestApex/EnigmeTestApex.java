package condorcet.appinfo3.groupe4.enigwall.TestApex;

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


        // Lecture simple
        System.out.println("-- LECTURE SIMPLE --");
        try {
            Enigme enigme = enigmeDAO.read("1");
            System.out.println("Enigme lue : " +enigme);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Lecture multiple
        System.out.println("-- LECTURE MULTIPLE --");
        try {
            ArrayList<Enigme> liste = enigmeDAO.readAll("1");
            System.out.println(liste);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        // Lecture multiple
        System.out.println("-- LECTURE MULTIPLE SPEC --");
        try {
            ArrayList<Enigme> liste = enigmeDAO.readSpec("1", "2");
            System.out.println(liste);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        // Création d'une nouvelle énigme si on reçoit son ID c'est ok si on reçoit 0 c'est que l'énigme existe déjà
        Enigme enigme = new Enigme(1, "Test test", "0.0", "0.0", "Blabla", "blabla", "blabla", "bloblo", "test.jpg");

        System.out.println("-- CREATION --");
        int r = 0;
        try {
            r = enigmeDAO.create(enigme);
            System.out.println("L'énigme a bien été créée !");
            System.out.println("Son id -> "+r);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        enigme.setId_enigme(r);
        enigme.setId_enigme_suite(2);
        System.out.println("-- UPDATE --");
        try {
            enigmeDAO.update(enigme);
            System.out.println("Enigme mise à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            enigmeDAO.delete(enigme);
            System.out.println("Enigme supprimée !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

package condorcet.appinfo3.groupe4.enigwall.TestApex;

import com.sun.jersey.api.client.WebResource;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.DAO.VoterDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Voter;

public class VoterTestApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        VoterDAO voterDAO = new VoterDAO();

        // Lecture simple
        System.out.println("-- LECTURE --");
        try {
            Voter voter = voterDAO.read("1", "1");
            System.out.println("Info lue : " +voter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Création
        Voter voter = new Voter(1, 2, 3);
        System.out.println("-- CREATION --");
        try {
            voterDAO.create(voter);
            System.out.println("La ligne a bien été créée !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        voter.setNbrvote(4);
        System.out.println("-- UPDATE --");
        try {
            voterDAO.update(voter);
            System.out.println("Ligne mise à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            voterDAO.delete(voter);
            System.out.println("Ligne supprimée !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

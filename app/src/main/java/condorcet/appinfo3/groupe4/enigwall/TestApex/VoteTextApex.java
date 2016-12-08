package condorcet.appinfo3.groupe4.enigwall.TestApex;

import com.sun.jersey.api.client.WebResource;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.DAO.VoteDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Vote;

public class VoteTextApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        VoteDAO voteDAO = new VoteDAO();

        // Lecture simple
        System.out.println("-- LECTURE --");
        try {
            Vote vote = voteDAO.read("1");
            System.out.println("Vote lu : " +vote);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Lecture via la note
        System.out.println("-- LECTURE NOTE --");
        try {
            Vote vote = voteDAO.read(new Vote(4));
            System.out.println("Vote lu : " +vote);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Création
        Vote vote = new Vote(6);
        System.out.println("-- CREATION --");
        int r = 0;
        try {
            r = voteDAO.create(vote);
            System.out.println("Le vote a bien été créé !");
            System.out.println("Son id -> "+r);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        vote.setId_vote(r);
        vote.setValeurnote(7);
        System.out.println("-- UPDATE --");
        try {
            voteDAO.update(vote);
            System.out.println("Vote mis à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            voteDAO.delete(vote);
            System.out.println("Vote supprimé !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

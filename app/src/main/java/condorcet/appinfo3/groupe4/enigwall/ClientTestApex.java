package condorcet.appinfo3.groupe4.enigwall;

import com.sun.jersey.api.client.WebResource;
import condorcet.appinfo3.groupe4.enigwall.DAO.ServiceApex;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class ClientTestApex {
    public static void main(String[] args) {
        // Singleton
        WebResource service = ServiceApex.getInstance();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        System.out.println("-- Recherche --");
        try {
            Utilisateur utilisateur = utilisateurDAO.read("1");
            System.out.println("Utilisateur lu : " +utilisateur);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // CREATION d'un nouvel utilisateur si on reçoit son ID c'est ok si on reçoit 0 c'est que l'utilisateur existe déjà
        Utilisateur utilisateur = new Utilisateur("maxime", "maxime@louette.com", "test");

        System.out.println("-- Création --");
        try {
            int r = utilisateurDAO.create(utilisateur);
            System.out.println("L'utilisateur a bien été créé !");
            System.out.println("Son id -> "+r);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        utilisateur = new Utilisateur("maxime7", "maxime@louette.com", "test");

        // CheckPseudo
        System.out.println("-- CHECKPSEUDO --");

        try {
            if(utilisateurDAO.checkPseudo(utilisateur)) {
                System.err.println("Le pseudo est déjà dans la BDD !");
            } else {
                System.out.println("Le pseudo n'est pas dans la BDD !");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // CheckMail
        System.out.println("-- CHECKMAIL --");

        try {
            if(utilisateurDAO.checkMail(utilisateur)) {
                System.err.println("Le mail est déjà dans la BDD !");
            } else {
                System.out.println("Le mail n'est pas dans la BDD !");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        utilisateur = new Utilisateur("maxime", "test");

        // Connexion
        System.out.println("-- Connexion --");
        try {
            utilisateur = utilisateurDAO.connexion(utilisateur);
            System.out.println(utilisateur);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Update
        utilisateur.setId_parcours(1);
        utilisateur.setId_enigme(1);
        System.out.println("-- UPDATE --");
        try {
            utilisateurDAO.update(utilisateur);
            System.out.println("Utilisateur mis à jour !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Delete
        System.out.println("-- DELETE --");
        try {
            utilisateurDAO.delete(utilisateur);
            System.out.println("Utilisateur supprimé !");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
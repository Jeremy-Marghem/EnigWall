package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class UtilisateurDAO extends DAO<Utilisateur> {
    private Gson gson = new Gson();

    // Retourne un utilisateur qui a été cherché via son ID
    @Override
    public Utilisateur read(String id) throws Exception {
        String utl = service.path("info/" + id).get(String.class);
        Utilisateur utilisateur = gson.fromJson(utl, Utilisateur.class);

        return utilisateur;
    }

    // Retourne l'ID de l'utilisateur qui vient d'être créé, si l'utilisateur existe déjà, une exception est levée
    @Override
    public int create(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try {
            json = gson.toJson(utilisateur);
        } catch (Exception e) {
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createUser/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if (status >= 400) {
            throw new Exception("L'utilisateur existe déjà !");
        } else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /* Méthode ajoutée */

    // Retourne false si le pseudo n'existe pas dans la BDD et true s'il existe
    public boolean checkPseudo(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try {
            json = gson.toJson(utilisateur);
        } catch (Exception e) {
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("checkPseudo/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if (status == 500) {
            return false;
        } else {
            return true;
        }
    }

    // Retourne false si le mail n'existe pas dans la BDD et true s'il existe
    public boolean checkMail(Utilisateur obj) throws Exception {
        Utilisateur utilisateur = obj;
        String json = "";

        try {
            json = gson.toJson(utilisateur);
        } catch (Exception e) {
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("checkMail/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if (status == 500) {
            return false;
        } else {
            return true;
        }
    }

    // Retourne l'utilisateur où le pseudo et le mot de passe concorde, sinon une exception est levée
    public Utilisateur connexion(Utilisateur obj) throws Exception {
        Utilisateur utilisateur;

        try {
            String utl = service.path("connexionUser/" + obj.getPseudo() + "-" + obj.getMdp()).get(String.class);
            utilisateur = gson.fromJson(utl, Utilisateur.class);
        } catch (Exception e) {
            throw new Exception("L'utilisateur n'existe pas !");
        }

        return utilisateur;
    }
}

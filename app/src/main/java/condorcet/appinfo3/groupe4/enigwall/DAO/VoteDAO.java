package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Vote;

public class VoteDAO extends DAO<Vote> {

    /**
     * Méthode permettant de récupérer les informations de vote grâce à son id
     * @param id du vote
     * @return le vote lue
     */
    @Override
    public Vote read(String id) throws Exception {
        String v = service.path("infoVote/"+id).get(String.class);
        Vote vote = gson.fromJson(v, Vote.class);

        return vote;
    }

    /**
     * Méthode permettant de créer un vote dans la base de données
     * @param le vote à créer
     * @return id du vote créé
     * @throws si le vote existe déjà, une exception est levée
     */
    @Override
    public int create(Vote obj) throws Exception {
        Vote vote = obj;
        String json = "";

        try{
            json = gson.toJson(vote);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createVote/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le vote existe déjà !");
        }
        else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /**
     * Méthode permettant de mettre à jour un vote
     * @param le vote à mettre à jour
     * @throws si le vote n'existe pas, une exception est levée
     */
    @Override
    public void update(Vote obj) throws Exception {
        Vote vote = obj;
        String json = "";

        try{
            json = gson.toJson(vote);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateVote/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le vote n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer un vote
     * @param le vote à supprimer
     * @throws si le vote n'existe pas, une exception est levée
     */
    @Override
    public void delete(Vote obj) throws Exception {
        Vote vote = obj;
        String json = "";

        try{
            json = gson.toJson(vote);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteVote/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le vote n'existe pas !");
        }
    }

    /**
     * Méthode permettant de récupérer les informations de vote grâce à sa valeur
     * @param le vote à rechercher
     * @return le vote lue
     */
    public Vote read(Vote obj) throws Exception {
        String v = service.path("readVote/"+obj.getValeurnote()).get(String.class);
        Vote vote = gson.fromJson(v, Vote.class);

        return vote;
    }
}

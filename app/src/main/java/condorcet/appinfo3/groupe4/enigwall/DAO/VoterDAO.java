package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import condorcet.appinfo3.groupe4.enigwall.Metier.Voter;

public class VoterDAO extends DAO<Voter> {

    @Override
    public Voter read(String id) throws Exception {
        return null;
    }

    /**
     * Méthode permettant de créer une ligne dans la table voter
     * @param le vote à créer
     * @return 1 si la ligne a été créée
     * @throws si la ligne existe déjà, une exception est levée
     */
    @Override
    public int create(Voter obj) throws Exception {
        Voter voter = obj;
        String json = "";

        try {
            json = gson.toJson(voter);
        } catch (Exception e) {
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createVoter/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();

        if (status >= 400) {
            throw new Exception("La ligne existe déjà !");
        } else {
            return 1;
        }
    }

    /**
     * Méthode permettant de mettre à jour une ligne de la table voter
     * @param la ligne à mettre à jour
     * @throws si la ligne n'existe pas, une exception est levée
     */
    @Override
    public void update(Voter obj) throws Exception {
        Voter voter = obj;
        String json = "";

        try{
            json = gson.toJson(voter);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateVoter/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("La ligne n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer une ligne dans la table voter
     * @param la ligne à supprimer
     * @throws si la ligne n'existe pas, une exception est levée
     */
    @Override
    public void delete(Voter obj) throws Exception {
        Voter voter = obj;
        String json = "";

        try{
            json = gson.toJson(voter);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteVoter/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("La ligne n'existe pas !");
        }
    }

    /**
     * Méthode permettant de récupérer les informations de la table voter
     * @param id du vote et id du parcours
     * @return information du vote
     */
    public Voter read(String idvote, String idparcours) throws Exception {
        String v = service.path("infoVoter/"+idvote+"-"+idparcours).get(String.class);
        Voter voter = gson.fromJson(v, Voter.class);

        return voter;
    }
}

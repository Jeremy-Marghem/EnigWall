package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;

public class ParcoursDAO extends DAO<Parcours> {

    /**
     * Méthode permettant de récupérer les informations d'un parcours grâce à son id
     * @param id de la ville
     * @return le parcours lu
     */
    public Parcours read(String id) throws Exception {
        String par = service.path("readParcours/"+id).get(String.class);
        Parcours parcours = gson.fromJson(par, Parcours.class);

        return parcours;
    }

    /**
     * Méthode permettant de créer un parcours dans la base de données
     * @param le parcours à créer
     * @return id du parcours créé
     * @throws si le parcours existe déjà, une exception est levée
     */
    @Override
    public int create(Parcours obj) throws Exception {
        Parcours parcours = obj;
        String json = "";

        try{
            json = gson.toJson(parcours);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createParcours/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le parcours existe déjà !");
        }
        else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /**
     * Méthode permettant de mettre à jour un parcours
     * @param le parcours à mettre à jour
     * @throws si le parcours n'existe pas, une exception est levée
     */
    @Override
    public void update(Parcours obj) throws Exception {
        Parcours parcours = obj;
        String json = "";

        try{
            json = gson.toJson(parcours);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateParcours/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le parcours n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer un parcours
     * @param le parcours à supprimer
     * @throws si le parcours n'existe pas, une exception est levée
     */
    @Override
    public void delete(Parcours obj) throws Exception {
        Parcours parcours = obj;
        String json = "";

        try{
            json = gson.toJson(parcours);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteParcours/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("Le parcours n'existe pas !");
        }
    }
}

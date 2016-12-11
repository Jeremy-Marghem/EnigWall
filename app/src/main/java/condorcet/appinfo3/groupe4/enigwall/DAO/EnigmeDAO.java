package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;
import condorcet.appinfo3.groupe4.enigwall.Metier.Liste.ListeEnigme;

public class EnigmeDAO extends DAO<Enigme> {

    /**
     * Méthode permettant de récupérer les informations d'une énigme grâce à son id
     * @param id de l'énigme
     * @return l'énigme lue
     */
    @Override
    public Enigme read(String id) throws Exception {
        String eni = service.path("infoEnigme/"+id).get(String.class);
        System.out.println(eni);
        Enigme enigme = gson.fromJson(eni, Enigme.class);

        return enigme;
    }

    /**
     * Méthode permettant de créer un énigme dans la base de données
     * @param l'énigme à créer
     * @return id de l'énigme créée
     * @throws si l'énigme existe déjà, une exception est levée
     */
    @Override
    public int create(Enigme obj) throws Exception {
        Enigme enigme = obj;
        String json = "";

        try{
            json = gson.toJson(enigme);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createEnigme/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("L'énigme existe déjà !");
        }
        else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /**
     * Méthode permettant de mettre à jour une énigme
     * @param l'énigme à mettre à jour
     * @throws si l'énigme n'existe pas, une exception est levée
     */
    @Override
    public void update(Enigme obj) throws Exception {
        Enigme enigme = obj;
        String json = "";

        try{
            json = gson.toJson(enigme);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateEnigme/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("L'énigme n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer une énigme
     * @param l'énigme à supprimer
     * @throws si l'énigme n'existe pas, une exception est levée
     */
    @Override
    public void delete(Enigme obj) throws Exception {
        Enigme enigme = obj;
        String json = "";

        try{
            json = gson.toJson(enigme);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteEnigme/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("L'énigme n'existe pas !");
        }
    }

    /**
     * Méthode permettant de récupérer une liste de toutes les énigmes suivant un parcours donné
     * @param l'id du parcours
     * @return la liste des énigmes
     */
    public ArrayList<Enigme> readAll(String id) throws Exception {
        ArrayList<Enigme> enigmes = new ArrayList<>();

        String liste = service.path("readAllEnigme/" + id).get(String.class);
        ListeEnigme listeEnigme = gson.fromJson(liste, ListeEnigme.class);
        if (listeEnigme.getItems().size() == 0) throw new Exception("ID inconnu !");
        return listeEnigme.getItems();
    }

    /**
     * Méthode permettant de récupérer une liste de toutes les énigmes suivant un parcours donné et les énigmes suivantes à celle-ci
     * @param l'id du parcours, l'id de l'énigme suivante
     * @return la liste des énigmes
     */
    public ArrayList<Enigme> readSpec(String id, String idsuite) throws Exception {
        ArrayList<Enigme> enigmes = new ArrayList<>();

        String liste = service.path("readSpecEnigme/" + id +"-"+ idsuite).get(String.class);
        ListeEnigme listeEnigme = gson.fromJson(liste, ListeEnigme.class);
        if (listeEnigme.getItems().size() == 0) throw new Exception("ID inconnu !");
        return listeEnigme.getItems();
    }
}

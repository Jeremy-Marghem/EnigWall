package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import javax.ws.rs.core.MultivaluedMap;
import condorcet.appinfo3.groupe4.enigwall.Metier.Liste.ListeVille;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class VilleDAO extends DAO<Ville>{

    /**
     * Méthode permettant de récupérer les informations de ville grâce à son id
     * @param id de la ville
     * @return la ville lue
     */
    @Override
    public Ville read(String id) throws Exception {
        String vl = service.path("infoVille/"+id).get(String.class);
        Ville ville = gson.fromJson(vl, Ville.class);

        return ville;
    }

    /**
     * Méthode permettant de créer une ville dans la base de données
     * @param la ville à créer
     * @return id de la ville créée
     * @throws si la ville existe déjà, une exception est levée
     */
    @Override
    public int create(Ville obj) throws Exception {
        Ville ville = obj;
        String json = "";

        try{
            json = gson.toJson(ville);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("createVille/").type("application/json").post(ClientResponse.class, json);
        int status = response.getStatus();
        MultivaluedMap h = response.getHeaders();

        if(status >=400){
            throw new Exception("La ville existe déjà !");
        }
        else {
            return Integer.valueOf((String) h.getFirst("id"));
        }
    }

    /**
     * Méthode permettant de mettre à jour une ville
     * @param la ville à mettre à jour
     * @throws si la ville n'existe pas, une exception est levée
     */
    @Override
    public void update(Ville obj) throws Exception {
        Ville ville = obj;
        String json = "";

        try{
            json = gson.toJson(ville);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("updateVille/").type("application/json").put(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("La ville n'existe pas !");
        }
    }

    /**
     * Méthode permettant de supprimer une ville
     * @param la ville à supprimer
     * @throws si la ville n'existe pas, une exception est levée
     */
    @Override
    public void delete(Ville obj) throws Exception {
        Ville ville = obj;
        String json = "";

        try{
            json = gson.toJson(ville);
        }
        catch(Exception e){
            throw new Exception("Erreur de conversion JSON");
        }

        ClientResponse response = service.path("deleteVille/").type("application/json").delete(ClientResponse.class, json);
        int status = response.getStatus();

        if(status >=400){
            throw new Exception("La ville n'existe pas !");
        }
    }

    /**
     * Méthode permettant de récupérer les informations de ville grâce à son nom
     * @param la ville à rechercher
     * @return la ville lue
     */
    public Ville read(Ville obj) throws Exception {
        String vl = service.path("readVille/"+obj.getNomville()).get(String.class);
        Ville ville = gson.fromJson(vl, Ville.class);

        return ville;
    }

    /**
     * Méthode permettant de récupérer une liste de toutes les villes
     * @return la liste des villes
     */
    public ArrayList<Ville> readAll() throws Exception {
        ArrayList<Ville> villes = new ArrayList<>();

        String liste = service.path("readAllVille/").get(String.class);
        ListeVille listeVille = gson.fromJson(liste, ListeVille.class);
        if (listeVille.getItems().size() == 0) throw new Exception("Aucune ville !");
        return listeVille.getItems();
    }
}

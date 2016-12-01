package condorcet.appinfo3.groupe4.enigwall.DAO;

import java.util.ArrayList;
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;
import condorcet.appinfo3.groupe4.enigwall.Metier.Liste.ListeEnigme;

public class EnigmeDAO extends DAO<Enigme> {

    @Override
    public Enigme read(String id) throws Exception {
        return null;
    }

    @Override
    public int create(Enigme obj) throws Exception {
        return 0;
    }

    @Override
    public void update(Enigme obj) throws Exception {
    }

    @Override
    public void delete(Enigme obj) throws Exception {
    }

    public ArrayList<Enigme> readAll(String id) throws Exception {
        ArrayList<Enigme> enigmes = new ArrayList<>();

        String liste = service.path("readAllEnigme/" + id).get(String.class);
        ListeEnigme listeEnigme = gson.fromJson(liste, ListeEnigme.class);
        if (listeEnigme.getItems().size() == 0) throw new Exception("ID inconnu !");
        return listeEnigme.getItems();
    }
}

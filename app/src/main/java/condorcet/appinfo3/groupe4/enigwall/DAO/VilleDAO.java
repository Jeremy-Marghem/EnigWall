package condorcet.appinfo3.groupe4.enigwall.DAO;

import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class VilleDAO extends DAO<Ville>{

    @Override
    public Ville read(String id) throws Exception {
        String vil = service.path("info/"+id).get(String.class);
        Ville ville = gson.fromJson(vil, Ville.class);

        return ville;
    }

    @Override
    public int create(Ville obj) throws Exception {
        return 0;
    }

    @Override
    public void update(Ville obj) throws Exception {

    }

    @Override
    public void delete(Ville obj) throws Exception {

    }
}

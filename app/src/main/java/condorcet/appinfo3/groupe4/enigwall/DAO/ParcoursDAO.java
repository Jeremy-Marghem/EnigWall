package condorcet.appinfo3.groupe4.enigwall.DAO;

import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;

public class ParcoursDAO extends DAO<Parcours> {

    public Parcours read(String id) throws Exception {
        String par = service.path("info/"+id).get(String.class);
        Parcours parcours = gson.fromJson(par, Parcours.class);

        return parcours;
    }

    @Override
    public int create(Parcours obj) throws Exception {
        return 0;
    }

    @Override
    public void update(Parcours obj) throws Exception {

    }

    @Override
    public void delete(Parcours obj) throws Exception {

    }
}

package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Parcours {
    int id_parcours, id_ville;
    String nomparcours;

    public Parcours(int id_parcours, int id_ville, String nomparcours) {
        this.id_parcours = id_parcours;
        this.id_ville = id_ville;
        this.nomparcours = nomparcours;
    }

    public Parcours(int id_ville, String nomparcours) {
        this.id_ville = id_ville;
        this.nomparcours = nomparcours;
    }

    public int getId_parcours() {
        return id_parcours;
    }

    public void setId_parcours(int id_parcours) {
        this.id_parcours = id_parcours;
    }

    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public String getNomparcours() {
        return nomparcours;
    }

    public void setNomparcours(String nomparcours) {
        this.nomparcours = nomparcours;
    }

    @Override
    public String toString() {
        return "Parcours{" +
                "id_parcours=" + id_parcours +
                ", id_ville=" + id_ville +
                ", nomparcours='" + nomparcours + '\'' +
                '}';
    }
}

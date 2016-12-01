package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Parcours {
    int id_parcours, id_ville;
    String nom_parcours;

    public Parcours(int id_parcours, int id_ville, String nom_parcours) {
        this.id_parcours = id_parcours;
        this.id_ville = id_ville;
        this.nom_parcours = nom_parcours;
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

    public String getNom_parcours() {
        return nom_parcours;
    }

    public void setNom_parcours(String nom_parcours) {
        this.nom_parcours = nom_parcours;
    }

    @Override
    public String toString() {
        return "Parcours{" +
                "id_parcours=" + id_parcours +
                ", id_ville=" + id_ville +
                ", nom_parcours='" + nom_parcours + '\'' +
                '}';
    }
}

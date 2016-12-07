package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Ville {
    int id_ville;
    String nomville;

    public Ville(String nomville) {
        this.nomville = nomville;
    }

    public Ville(int id_ville, String nomville) {
        this.id_ville = id_ville;
        this.nomville = nomville;
    }

    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public String getNomville() {
        return nomville;
    }

    public void setNomville(String nomville) {
        this.nomville = nomville;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "id_ville=" + id_ville +
                ", nomville='" + nomville + '\'' +
                '}';
    }
}

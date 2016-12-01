package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Ville {
    int id_ville;
    String nom_ville;
    double latitude, longitude;

    public Ville(int id_ville, String nom_ville, double latitude, double longitude) {
        this.id_ville = id_ville;
        this.nom_ville = nom_ville;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public String getNom_ville() {
        return nom_ville;
    }

    public void setNom_ville(String nom_ville) {
        this.nom_ville = nom_ville;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

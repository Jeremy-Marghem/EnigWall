package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Enigme {
    int id_egnime, id_parcours;
    String nom_monument;
    double longitude, latitude;
    String texteFR,texteEn,texteNL,texteGR;
    String image; //récupération par le nom avec l'URL

    public Enigme(int id_egnime, int id_parcours, String nom_monument, double longitude, double latitude, String texteFR, String texteEn, String texteNL, String texteGR, String image) {
        this.id_egnime = id_egnime;
        this.id_parcours = id_parcours;
        this.nom_monument = nom_monument;
        this.longitude = longitude;
        this.latitude = latitude;
        this.texteFR = texteFR;
        this.texteEn = texteEn;
        this.texteNL = texteNL;
        this.texteGR = texteGR;
        this.image = image;
    }

    public int getId_egnime() {
        return id_egnime;
    }

    public void setId_egnime(int id_egnime) {
        this.id_egnime = id_egnime;
    }

    public int getId_parcours() {
        return id_parcours;
    }

    public void setId_parcours(int id_parcours) {
        this.id_parcours = id_parcours;
    }

    public String getNom_monument() {
        return nom_monument;
    }

    public void setNom_monument(String nom_monument) {
        this.nom_monument = nom_monument;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTexteFR() {
        return texteFR;
    }

    public void setTexteFR(String texteFR) {
        this.texteFR = texteFR;
    }

    public String getTexteEn() {
        return texteEn;
    }

    public void setTexteEn(String texteEn) {
        this.texteEn = texteEn;
    }

    public String getTexteNL() {
        return texteNL;
    }

    public void setTexteNL(String texteNL) {
        this.texteNL = texteNL;
    }

    public String getTexteGR() {
        return texteGR;
    }

    public void setTexteGR(String texteGR) {
        this.texteGR = texteGR;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Enigme{" +
                "id_egnime=" + id_egnime +
                ", id_parcours=" + id_parcours +
                ", nom_monument='" + nom_monument + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", texteFR='" + texteFR + '\'' +
                ", texteEn='" + texteEn + '\'' +
                ", texteNL='" + texteNL + '\'' +
                ", texteGR='" + texteGR + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

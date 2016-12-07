package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Enigme {
    int id_enigme, id_parcours, id_enigme_suite;
    String nommonument;
    String coordlatitude, coordlongitude;
    String texteenigmefr, texteenigmenl, texteenigmeger, texteenigmeen;
    String nomimage;

    public Enigme(int id_enigme, int id_parcours, int id_enigme_suite, String nommonument, String coordlatitude, String coordlongitude, String texteenigmefr, String texteenigmenl, String texteenigmeger, String texteenigmeen, String nomimage) {
        this.id_enigme = id_enigme;
        this.id_parcours = id_parcours;
        this.id_enigme_suite = id_enigme_suite;
        this.nommonument = nommonument;
        this.coordlatitude = coordlatitude;
        this.coordlongitude = coordlongitude;
        this.texteenigmefr = texteenigmefr;
        this.texteenigmenl = texteenigmenl;
        this.texteenigmeger = texteenigmeger;
        this.texteenigmeen = texteenigmeen;
        this.nomimage = nomimage;
    }

    public Enigme(int id_parcours, String nommonument, String coordlatitude, String coordlongitude, String texteenigmefr, String texteenigmenl, String texteenigmeger, String texteenigmeen, String nomimage) {
        this.id_parcours = id_parcours;
        this.nommonument = nommonument;
        this.coordlatitude = coordlatitude;
        this.coordlongitude = coordlongitude;
        this.texteenigmefr = texteenigmefr;
        this.texteenigmenl = texteenigmenl;
        this.texteenigmeger = texteenigmeger;
        this.texteenigmeen = texteenigmeen;
        this.nomimage = nomimage;
    }

    public int getId_enigme() {
        return id_enigme;
    }

    public void setId_enigme(int id_enigme) {
        this.id_enigme = id_enigme;
    }

    public int getId_parcours() {
        return id_parcours;
    }

    public void setId_parcours(int id_parcours) {
        this.id_parcours = id_parcours;
    }

    public int getId_enigme_suite() {
        return id_enigme_suite;
    }

    public void setId_enigme_suite(int id_enigme_suite) {
        this.id_enigme_suite = id_enigme_suite;
    }

    public String getNommonument() {
        return nommonument;
    }

    public void setNommonument(String nommonument) {
        this.nommonument = nommonument;
    }

    public String getCoordlatitude() {
        return coordlatitude;
    }

    public void setCoordlatitude(String coordlatitude) {
        this.coordlatitude = coordlatitude;
    }

    public String getCoordlongitude() {
        return coordlongitude;
    }

    public void setCoordlongitude(String coordlongitude) {
        this.coordlongitude = coordlongitude;
    }

    public String getTexteenigmefr() {
        return texteenigmefr;
    }

    public void setTexteenigmefr(String texteenigmefr) {
        this.texteenigmefr = texteenigmefr;
    }

    public String getTexteenigmenl() {
        return texteenigmenl;
    }

    public void setTexteenigmenl(String texteenigmenl) {
        this.texteenigmenl = texteenigmenl;
    }

    public String getTexteenigmeger() {
        return texteenigmeger;
    }

    public void setTexteenigmeger(String texteenigmeger) {
        this.texteenigmeger = texteenigmeger;
    }

    public String getTexteenigmeen() {
        return texteenigmeen;
    }

    public void setTexteenigmeen(String texteenigmeen) {
        this.texteenigmeen = texteenigmeen;
    }

    public String getNomimage() {
        return nomimage;
    }

    public void setNomimage(String nomimage) {
        this.nomimage = nomimage;
    }

    @Override
    public String toString() {
        return "Enigme{" +
                "id_enigme=" + id_enigme +
                ", id_parcours=" + id_parcours +
                ", id_enigme_suite=" + id_enigme_suite +
                ", nommonument='" + nommonument + '\'' +
                ", coordlatitude=" + coordlatitude +
                ", coordlongitude=" + coordlongitude +
                ", texteenigmefr='" + texteenigmefr + '\'' +
                ", texteenigmenl='" + texteenigmenl + '\'' +
                ", texteenigmeger='" + texteenigmeger + '\'' +
                ", texteenigmeen='" + texteenigmeen + '\'' +
                ", nomimage='" + nomimage + '\'' +
                '}';
    }
}

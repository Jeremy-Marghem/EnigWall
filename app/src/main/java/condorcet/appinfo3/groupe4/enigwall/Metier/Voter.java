package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Voter {
    int id_vote, id_parcours, nbrvote;

    public Voter(){}
    public Voter(int id_vote, int id_parcours, int nbrvote) {
        this.id_vote = id_vote;
        this.id_parcours = id_parcours;
        this.nbrvote = nbrvote;
    }

    public int getId_vote() {
        return id_vote;
    }

    public void setId_vote(int id_vote) {
        this.id_vote = id_vote;
    }

    public int getId_parcours() {
        return id_parcours;
    }

    public void setId_parcours(int id_parcours) {
        this.id_parcours = id_parcours;
    }

    public int getNbrvote() {
        return nbrvote;
    }

    public void setNbrvote(int nbrvote) {
        this.nbrvote = nbrvote;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "id_vote=" + id_vote +
                ", id_parcours=" + id_parcours +
                ", nbrvote=" + nbrvote +
                '}';
    }
}

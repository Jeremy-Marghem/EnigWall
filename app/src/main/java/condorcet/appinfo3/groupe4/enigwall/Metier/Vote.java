package condorcet.appinfo3.groupe4.enigwall.Metier;

public class Vote {
    int id_vote, valeurnote;

    public Vote(int id_vote, int valeurnote) {
        this.id_vote = id_vote;
        this.valeurnote = valeurnote;
    }

    public Vote(int valeurnote) {
        this.valeurnote = valeurnote;
    }

    public int getId_vote() {
        return id_vote;
    }

    public void setId_vote(int id_vote) {
        this.id_vote = id_vote;
    }

    public int getValeurnote() {
        return valeurnote;
    }

    public void setValeurnote(int valeurnote) {
        this.valeurnote = valeurnote;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id_vote=" + id_vote +
                ", valeurnote=" + valeurnote +
                '}';
    }
}

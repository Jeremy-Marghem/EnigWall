package condorcet.appinfo3.groupe4.enigwall.Metier.Liste;

import java.util.ArrayList;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class ListeVille {
    private ArrayList<Ville> items= new ArrayList<>();
    private Deplacement next;
    private Deplacement previous;
    private Deplacement first;

    public ArrayList<Ville> getItems() {
        return items;
    }

    public void setItems(ArrayList<Ville> items) {
        this.items = items;
    }

    public Deplacement getNext() {
        return next;
    }

    public void setNext(Deplacement next) {
        this.next = next;
    }

    public Deplacement getPrevious() {
        return previous;
    }

    public void setPrevious(Deplacement previous) {
        this.previous = previous;
    }

    public Deplacement getFirst() {
        return first;
    }

    public void setFirst(Deplacement first) {
        this.first = first;
    }

    public ListeVille() {
    }
}

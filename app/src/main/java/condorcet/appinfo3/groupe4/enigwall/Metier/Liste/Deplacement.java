package condorcet.appinfo3.groupe4.enigwall.Metier.Liste;

public class Deplacement {
    String $ref;

    public Deplacement() {

    }
    public Deplacement(String $ref) {
        this.$ref = $ref;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    @Override
    public String toString() {
        return $ref;
    }
}

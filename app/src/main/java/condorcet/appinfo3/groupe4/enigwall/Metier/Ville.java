package condorcet.appinfo3.groupe4.enigwall.Metier;

import android.os.Parcel;
import android.os.Parcelable;

public class Ville implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_ville);
        dest.writeString(nomville);
    }

    public static final Parcelable.Creator<Ville> CREATOR = new Parcelable.Creator<Ville>(){
        @Override
        public Ville createFromParcel(Parcel source) {
            return new Ville(source);
        }
        @Override
        public Ville[] newArray(int size) {
            return new Ville[size];
        }
    };

    public Ville(Parcel in){
        id_ville = in.readInt();
        nomville = in.readString();
    }
}

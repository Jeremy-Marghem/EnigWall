package condorcet.appinfo3.groupe4.enigwall.Metier;

import android.os.Parcel;
import android.os.Parcelable;

public class Utilisateur implements Parcelable {
    int id_utilisateur, id_enigme, id_parcours;
    String pseudo, mail, mdp;

    public Utilisateur(int id_utilisateur, int id_enigme, int id_parcours, String pseudo, String mail, String mdp) {
        this.id_utilisateur = id_utilisateur;
        this.id_enigme = id_enigme;
        this.id_parcours = id_parcours;
        this.pseudo = pseudo;
        this.mail = mail;
        this.mdp = mdp;
    }

    public Utilisateur(String pseudo, String mail, String mdp) {
        this.pseudo = pseudo;
        this.mail = mail;
        this.mdp = mdp;
    }

    public Utilisateur(String pseudo, String mdp) {
        this.pseudo = pseudo;
        this.mdp = mdp;
    }

    public Utilisateur() {
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id_utilisateur=" + id_utilisateur +
                ", id_enigme=" + id_enigme +
                ", id_parcours=" + id_parcours +
                ", pseudo='" + pseudo + '\'' +
                ", mail='" + mail + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_utilisateur);
        dest.writeInt(id_enigme);
        dest.writeInt(id_parcours);
        dest.writeString(pseudo);
        dest.writeString(mail);
        dest.writeString(mdp);
    }

    public static final Parcelable.Creator<Utilisateur> CREATOR = new Parcelable.Creator<Utilisateur>(){
        @Override
        public Utilisateur createFromParcel(Parcel source) {
            return new Utilisateur(source);
        }
        @Override
        public Utilisateur[] newArray(int size) {
            return new Utilisateur[size];
        }
    };

    public Utilisateur(Parcel in){
        id_utilisateur = in.readInt();
        id_enigme = in.readInt();
        id_parcours = in.readInt();
        pseudo = in.readString();
        mail = in.readString();
        mdp = in.readString();
    }
}

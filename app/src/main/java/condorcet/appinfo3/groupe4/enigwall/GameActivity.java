package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import condorcet.appinfo3.groupe4.enigwall.DAO.EnigmeDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.ParcoursDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;
import condorcet.appinfo3.groupe4.enigwall.R;
import layout.Game_up;

public class GameActivity extends AppCompatActivity {

    TextView enigmeTv;

    public final static String IDUSER = "user";
    public final static String IDVILLE = "ville";
    public final static String IDSTATE = "state";
    public final static String IDENIGME = "enigme";

    public Utilisateur utilisateur;
    public Ville ville;
    public int id_ville;
    public String state;

    public int id_enigme;
    public Parcours parcours;
    public ArrayList<Enigme> liste_enigme;
    public Enigme current_enigme;
    public int cptEnigme;
    public int nbEnigme;

    public Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        i = getIntent();
        state = i.getStringExtra(HubActivity.IDSTATE);
        liste_enigme = new ArrayList<Enigme>();
        cptEnigme = 0;
        if(state.equals("commencer")){
            utilisateur = (Utilisateur) i.getParcelableExtra(HubActivity.IDUSER);
            System.out.println("USER RECU = "+utilisateur);
            ville = (Ville) i.getParcelableExtra(HubActivity.IDVILLE);
            System.out.println("VIlle recu = "+ville);
            id_ville = ville.getId_ville();
            System.out.println("IDVILLE = "+id_ville);
            Begin begin = new Begin(this);
            begin.execute();
        }

        if(state.equals("reprendre")){
            id_enigme = i.getIntExtra(IDENIGME, -1);
        }
        enigmeTv = (TextView) findViewById(R.id.enigmeTv);
    }

    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE
    class Begin extends AsyncTask<String, Integer, Boolean> {
        private ParcoursDAO daoParcours;
        private EnigmeDAO daoEnigme;
        private ProgressDialog pd;

        public Begin(GameActivity pActivity){
            link(pActivity);
        }

        private void link(GameActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            // Désactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(GameActivity.this);
            pd.setMessage("Chargement du jeu...");
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            //RECUPERATION DU PARCOURS EN FONCTION DE LA VILLE
            daoParcours = new ParcoursDAO();
            try {
                parcours = daoParcours.read(String.valueOf(id_ville));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //RECUPERATION DE LA LISTE D'ENIGMES CORRESPONDANT AU PARCOURS
            daoEnigme = new EnigmeDAO();
            try {
                liste_enigme = daoEnigme.readAll(String.valueOf(parcours.getId_parcours()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            current_enigme = liste_enigme.get(nbEnigme); //Enigme en cours
            nbEnigme = liste_enigme.size(); //Nombre d'enigme dans le parcours

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            //------------> INSERER ICI UN SWITCH POUR CHOISIR LE TEXTE EN FONCTION DE LA LANGUE DU GSM
            switch (Locale.getDefault().getLanguage()){
                case "fr":
                    enigmeTv.setText(current_enigme.getTexteenigmefr());
                    break;
                case "en":
                    enigmeTv.setText(current_enigme.getTexteenigmeen());
                    break;
            }
            pd.dismiss();
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(GameActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.VoterDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Voter;

import static java.lang.Math.round;

public class RateActivity extends Activity{

    public final static String IDPARCOURS = "parcours";

    RatingBar ratingBar;
    int id_parcours;
    Voter voter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        Intent i = getIntent();
        id_parcours = i.getIntExtra(GameActivity.IDPARCOURS, -1);
    }

    public void voter(View v){
        int rate = round(ratingBar.getRating());
        voter = new Voter();
        voter.setId_parcours(id_parcours);
        voter.setId_vote(rate);
        voter.setNbrvote(1);
        EnvoiVote envoiVote = new EnvoiVote(RateActivity.this);
        envoiVote.execute();
    }
    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE
    class EnvoiVote extends AsyncTask<String, Integer, Boolean> {
        private VoterDAO voterDAO;
        private ProgressDialog pd;
        private String msgError = "";

        public EnvoiVote(RateActivity pActivity){
            link(pActivity);
        }

        private void link(RateActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            // Désactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(RateActivity.this);
            pd.setMessage(RateActivity.this.getResources().getString(R.string.signinInProgress));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean reussi = true;
            voterDAO = new VoterDAO();

            try {
                voterDAO.update(voter);
            } catch (Exception e) {
                reussi= false;
                msgError = "VOTE ERREUR";
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            if(aBoolean){
                Intent i = new Intent(RateActivity.this, HubActivity.class);
                startActivity(i); //ON REVIENT A L'ACTIVITE HUB
                finish(); //ON DETRUIT L'ACTIVITE RATE
            } else {
                pd.dismiss(); //ARRET DU PROGRESSDIALOG
                Toast toast = Toast.makeText(RateActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(RateActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

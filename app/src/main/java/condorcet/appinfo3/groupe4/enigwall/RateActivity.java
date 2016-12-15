package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;
import condorcet.appinfo3.groupe4.enigwall.DAO.VoterDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Voter;
import static java.lang.Math.round;

public class RateActivity extends Activity{
    RatingBar ratingBar;
    int id_parcours, rate;
    Voter voter;
    Utilisateur utilisateur;

    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        Intent i = getIntent();
        utilisateur = (Utilisateur) i.getParcelableExtra(GameActivity.IDUSER);
        id_parcours = i.getIntExtra(GameActivity.IDPARCOURS, -1);
    }

    public void voter(View v){
        rate = round(ratingBar.getRating());
        voter = new Voter();
        voter.setId_parcours(id_parcours);
        EnvoiVote envoiVote = new EnvoiVote(this);
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
                voterDAO.createOrupdate(voter, String.valueOf(rate));
            } catch (Exception e) {
                reussi= false;
                msgError = getResources().getString(R.string.ratingError);
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
                i.putExtra(IDUSER, utilisateur);
                startActivity(i); //ON REVIENT A L'ACTIVITE HUB
                finish(); //ON DETRUIT L'ACTIVITE RATE
            } else {
                Toast toast = Toast.makeText(RateActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
            }

            pd.dismiss(); //ARRET DU PROGRESSDIALOG
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(RateActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

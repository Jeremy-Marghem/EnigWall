package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import condorcet.appinfo3.groupe4.enigwall.DAO.VoterDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Voter;
import static java.lang.Math.round;

public class RateActivity extends Activity {
    Button voting;
    RatingBar ratingBar;
    int id_parcours, rate;
    Voter voter;
    Utilisateur utilisateur;
    private NetworkReceiver receiver;

    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        voting = (Button) findViewById(R.id.RatingValidButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent i = getIntent();
        utilisateur = (Utilisateur) i.getParcelableExtra(GameActivity.IDUSER);
        id_parcours = i.getIntExtra(GameActivity.IDPARCOURS, -1);

        // Vérification en temps réel de la connexion
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        // L'utilisateur peut appuyer sur le bouton BACK pour ne pas voter et retourner sur le HUB
        Intent i = new Intent(RateActivity.this, HubActivity.class);
        i.putExtra(IDUSER, utilisateur);
        startActivity(i); //ON REVIENT A L'ACTIVITE HUB
        finish(); //ON DETRUIT L'ACTIVITE RATE
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

    // Classe interne pour détection d'internet
    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // Si aucune connexion n'est trouvée, on affiche un toast
            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                Toast toast = Toast.makeText(RateActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive le bouton
                voting.setEnabled(false);
            } else {
                voting.setEnabled(true);
            }
        }
    }
}

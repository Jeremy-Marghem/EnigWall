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
import android.widget.EditText;
import android.widget.Toast;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class LoginActivity extends Activity {
    Button connexion;
    Utilisateur utilisateurConnect;
    EditText pseudoEd, passwordEd;
    private NetworkReceiver receiver;

    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pseudoEd = (EditText)findViewById(R.id.pseudoEdLogin);
        passwordEd = (EditText)findViewById(R.id.passwordEdLogin);
        connexion = (Button)findViewById(R.id.loginButton);

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

    public void login(View v){
        // Méthide trim pour retirer les espaces
        utilisateurConnect = new Utilisateur(pseudoEd.getText().toString().trim().toLowerCase(),passwordEd.getText().toString());
        Connect connect = new Connect(LoginActivity.this);
        connect.execute();
    }

    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE
    class Connect extends AsyncTask<String, Integer, Boolean> {
        private UtilisateurDAO utilisateurDAO;
        private ProgressDialog pd;
        private String msgError = "";

        public Connect(LoginActivity pActivity){
            link(pActivity);
        }

        private void link(LoginActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            // Désactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage(LoginActivity.this.getResources().getString(R.string.signinInProgress));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean reussi = true;
            utilisateurDAO = new UtilisateurDAO();

            try {
                utilisateurConnect = utilisateurDAO.connexion(utilisateurConnect);
            } catch (Exception e) {
                reussi= false;
                msgError = getResources().getString(R.string.userError);
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            if(aBoolean){
                Intent i = new Intent(LoginActivity.this, HubActivity.class);
                i.putExtra(IDUSER, utilisateurConnect);
                startActivity(i); //ON PASSE A L'ACTIVITE HUB
                finish(); //ON DETRUIT L'ACTIVITE LOGIN
            } else {
                Toast toast = Toast.makeText(LoginActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
            }

            pd.dismiss(); //ARRET DU PROGRESSDIALOG
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(LoginActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
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
                Toast toast = Toast.makeText(LoginActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive les boutons
                connexion.setEnabled(false);
            } else {
                // On active les boutons
                connexion.setEnabled(true);
            }
        }
    }
}

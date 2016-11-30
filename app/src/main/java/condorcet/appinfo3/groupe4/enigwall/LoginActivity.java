package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class LoginActivity extends Activity {

    Utilisateur utilisateurConnect;
    EditText pseudoEd, passwordEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pseudoEd = (EditText)findViewById(R.id.pseudoEdLogin);
        passwordEd = (EditText)findViewById(R.id.passwordEdLogin);
    }
    public void login(View v){
        utilisateurConnect = new Utilisateur(pseudoEd.getText().toString(),passwordEd.getText().toString());
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
                Log.i("ERROR",e.getMessage());
                reussi= false;
                msgError = getResources().getString(R.string.userError);
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Intent i = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(i); //ON PASSE A L'ACTIVITE HUB
                finish(); //ON DETRUIT L'ACTIVITE LOGIN
            } else {
                pd.dismiss(); //ARRET DU PROGRESSDIALOG
                Toast toast = Toast.makeText(LoginActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(LoginActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

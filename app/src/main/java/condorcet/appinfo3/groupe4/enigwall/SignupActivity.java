package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class SignupActivity extends AppCompatActivity {
    Button reset, signup;
    EditText pseudoEd, mailEd, passwordEd, password2Ed;
    Utilisateur utilisateurEntre;
    private NetworkReceiver receiver;
    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pseudoEd = (EditText)findViewById(R.id.pseudoEd);
        mailEd = (EditText)findViewById(R.id.mailEd);
        passwordEd = (EditText)findViewById(R.id.passwordEd);
        password2Ed = (EditText)findViewById(R.id.passwordEd2);

        reset = (Button)findViewById(R.id.resetButton);
        signup = (Button)findViewById(R.id.validateButton);

        // Vérification en temps réel de la connexion
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    /////RESET DES CHAMPS
    public void reset(View v){
        pseudoEd.setText("");
        pseudoEd.requestFocus();
        mailEd.setText("");
        passwordEd.setText("");
        password2Ed.setText("");
    }
    public void resetPseudo(){
        pseudoEd.setText("");
        pseudoEd.requestFocus();
    }
    public void resetMail(){
        mailEd.setText("");
        mailEd.requestFocus();
    }
    ///////////////////

    /////METHODE DE VERIFICATION
    public boolean verifPassword(){
        // True si les 2 mots de passes correspondent, false si pas
        return passwordEd.getText().toString().equals(password2Ed.getText().toString());
    }

    public boolean verifMail() {
        // True si c'est un email valide, false si pas
        return Patterns.EMAIL_ADDRESS.matcher(mailEd.getText().toString()).matches();
    }

    public boolean verifPseudo() {
        // True si le pseudo ne contient pas de chiffres, false s'il en contient
        return Pattern.matches("[a-zA-Z]+", pseudoEd.getText().toString());
    }


    public void signin(View v){
        boolean verifOk = true;

        if(!verifPassword()){
            passwordEd.setText("");
            password2Ed.setText("");
            passwordEd.requestFocus(); //replace le curseur sur le premier champ

            Toast toast = Toast.makeText(SignupActivity.this,getResources().getText(R.string.passwordMessage),Toast.LENGTH_SHORT);
            toast.show();
            verifOk = false;
        } else if(passwordEd.getText().toString().length()<4) {
            passwordEd.setText("");
            password2Ed.setText("");
            passwordEd.requestFocus();

            Toast toast = Toast.makeText(SignupActivity.this,getResources().getText(R.string.passwordLenght),Toast.LENGTH_SHORT);
            toast.show();
            verifOk = false;
        }

        if(!verifMail()){
            mailEd.setText("");
            mailEd.requestFocus();

            Toast toast = Toast.makeText(SignupActivity.this,getResources().getText(R.string.mailMessage),Toast.LENGTH_SHORT);
            toast.show();
            verifOk = false;
        }

        if(pseudoEd.getText().toString().length()<5){
            pseudoEd.setText("");
            pseudoEd.requestFocus();

            Toast toast = Toast.makeText(SignupActivity.this, getResources().getString(R.string.toast_pseudoLength), Toast.LENGTH_SHORT);
            toast.show();
            verifOk = false;
        } else if(!verifPseudo()) {
            pseudoEd.setText("");
            pseudoEd.requestFocus();

            Toast toast = Toast.makeText(SignupActivity.this, getResources().getString(R.string.toast_pseudoChar), Toast.LENGTH_SHORT);
            toast.show();
            verifOk = false;
        }

        if(verifOk){
            utilisateurEntre = new Utilisateur(pseudoEd.getText().toString(),mailEd.getText().toString(),passwordEd.getText().toString());
            Verification verification = new Verification(SignupActivity.this);
            verification.execute();
        }
    }
    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE
    class Verification extends AsyncTask<String, Integer, Boolean> {
        private UtilisateurDAO utilisateurDAO;
        private ProgressDialog pd;
        private String msgError = "";

        public Verification(SignupActivity pActivity){
            link(pActivity);
        }

        private void link(SignupActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage(SignupActivity.this.getResources().getString(R.string.signupInProgress));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean reussi = true;
            utilisateurDAO = new UtilisateurDAO();

            //ON VERIFIE SI L'E-MAIL EST DEJA UTILISE DANS LA DB
            boolean mailDejaPresent = false;
            try {
                mailDejaPresent = utilisateurDAO.checkMail(utilisateurEntre);
                if(mailDejaPresent){
                    msgError += getResources().getString(R.string.toast_mailUsed)+ " ";
                    reussi = false;
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

            //ON VERIFIE SI LE PSEUDO EST DEJA UTILISE DANS LA DB
            boolean pseudoDejaPresent = false;
            try {
                pseudoDejaPresent = utilisateurDAO.checkPseudo(utilisateurEntre);
                if(pseudoDejaPresent){
                    msgError += getResources().getString(R.string.toast_pseudoUsed);
                    reussi = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //Si tout est ok, on créé l'utilisateur dans la base de données
            if(reussi) {
                try {
                    int id = utilisateurDAO.create(utilisateurEntre);
                } catch (Exception e) {
                    reussi = false;
                }
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Intent i = new Intent(SignupActivity.this, HubActivity.class);
                i.putExtra(IDUSER,utilisateurEntre);
                startActivity(i); //ON PASSE A L'ACTIVITE HUB
                finish(); //ON DETRUIT L'ACTIVITE SIGNUP
            } else {
                pd.dismiss(); //ARRET DU PROGRESSDIALOG
                Toast toast = Toast.makeText(SignupActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
                if(msgError.contains(getResources().getString(R.string.toast_mailUsed))) {
                    resetMail();
                }
                if(msgError.contains(getResources().getString(R.string.toast_pseudoUsed))) {
                    resetPseudo();
                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(SignupActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // Si aucune connexion n'est trouvée, on affiche un toast
            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                Toast toast = Toast.makeText(SignupActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive les boutons
                signup.setEnabled(false);
                reset.setEnabled(false);
            } else {
                // On active les boutons
                signup.setEnabled(true);
                reset.setEnabled(true);
            }
        }
    }

}

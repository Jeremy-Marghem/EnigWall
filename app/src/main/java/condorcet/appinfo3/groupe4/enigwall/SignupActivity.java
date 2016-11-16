package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;

import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class SignupActivity extends AppCompatActivity {

    EditText pseudoEd, mailEd, passwordEd, password2Ed;
    Utilisateur utilisateurEntre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pseudoEd = (EditText)findViewById(R.id.pseudoEd);
        mailEd = (EditText)findViewById(R.id.mailEd);
        passwordEd = (EditText)findViewById(R.id.passwordEd);
        password2Ed = (EditText)findViewById(R.id.passwordEd2);
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
        boolean ok = false;//SI LES MDP CONCORDENT
        if(passwordEd.getText().toString().equals(password2Ed.getText().toString())){
                ok = true;
        }
        return ok;
    }
    public boolean verifMail() {
        boolean ok = false; //SI LE MAIL RESPECTE LA REGEX
        if (Patterns.EMAIL_ADDRESS.matcher(mailEd.getText().toString()).matches()) {
            ok = true;
        }
        return ok;
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
            pseudoEd.setText("");
            pseudoEd.requestFocus(); //replace le curseur sur le premier champ

            Toast toast = Toast.makeText(SignupActivity.this,"Le pseudo doit contenir 5 caracteres minimum",Toast.LENGTH_SHORT);
            toast.show();
        }

        if(verifOk){
            utilisateurEntre = new Utilisateur(pseudoEd.getText().toString(),mailEd.getText().toString(),passwordEd.getText().toString());
            Verification verification = new Verification(SignupActivity.this);
            verification.execute();
        }
    }
    ///////////////////


    ////CLASSE INTERNE ASYNCHRONE
    class Verification extends AsyncTask<String,Integer,Boolean>{

        public Verification(SignupActivity pActivity){
            link(pActivity);
        }

        private void link(SignupActivity pActivity){

        }

        @Override
        protected void onPreExecute() {
            ////CREATION D'UNE BOITE DE DIALOGUE
            ProgressDialog pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage(SignupActivity.this.getResources().getString(R.string.signupInProgress));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean reussi = true;

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

            //ON VERIFIE SI L'E-MAIL EST DEJA UTILISE DANS LA DB
            boolean mailDejaPresent = false;
            try {
                mailDejaPresent = utilisateurDAO.checkMail(utilisateurEntre);
                if(mailDejaPresent){
                    Toast toast = Toast.makeText(SignupActivity.this,getResources().getString(R.string.toast_mailUsed),Toast.LENGTH_SHORT);
                    toast.show();
                    resetMail();
                    reussi = false;
                }
            }catch(Exception e){
                e.printStackTrace();
            }


            //ON VERIFIE SI LE PSEUDO EST DEJA UTILISE DANS LA DB
            boolean pseudoDejaPresent = false;
            try {
                pseudoDejaPresent = utilisateurDAO.checkPseudo(utilisateurEntre);
                if(pseudoDejaPresent){
                    Toast toast = Toast.makeText(SignupActivity.this,getResources().getString(R.string.toast_pseudoUsed),Toast.LENGTH_SHORT);
                    toast.show();
                    resetPseudo();
                    reussi = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                try {
                    int id = utilisateurDAO.create(utilisateurEntre);
                    Intent i = new Intent(SignupActivity.this,HubActivity.class);
                    startActivity(i); //ON PASSE A L'ACTIVITE HUB
                    finish(); //ON DETRUIT L'ACTIVITE SIGNUP
                } catch (Exception e) {
                    Log.i("ERR CREATION UTIL",e.getMessage());
                    Toast toast = Toast.makeText(SignupActivity.this,"Erreur lors de la creation",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(SignupActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

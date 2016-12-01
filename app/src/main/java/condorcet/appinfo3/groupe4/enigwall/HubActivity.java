package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;


public class HubActivity extends AppCompatActivity{

    Utilisateur utilisateur;
    TextView welcome;
    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i = getIntent();
        utilisateur = (Utilisateur)i.getParcelableExtra(LoginActivity.IDUSER);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        welcome = (TextView)findViewById(R.id.welcomeTv);
        welcome.setText("Bienvenue"+" "+utilisateur.getPseudo()+" !");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.menu_hub_del:

                AlertDialog.Builder builder = new AlertDialog.Builder(HubActivity.this);

                builder.setTitle("ATTENTION! Ceci supprimera votre compte ainsi que votre progression dans le jeu!")
                .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("SUPPRIMER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteUser delete = new DeleteUser(HubActivity.this);
                        delete.execute();
                        Intent in = new Intent(HubActivity.this,MainActivity.class);
                        startActivity(in);
                    }
                });

                builder.show();

                return true;
            case R.id.menu_hub_deco:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(HubActivity.this);

                builder2.setTitle("Voulez-vous vraiment vous deconnecter?")
                 .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                    }
                })
                 .setPositiveButton("DECONNECTION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        utilisateur = null;
                        Intent in = new Intent(HubActivity.this,MainActivity.class);
                        startActivity(in);
                    }
                });

                builder2.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE

    class DeleteUser extends AsyncTask<String, Integer, Boolean> {
        private UtilisateurDAO utilisateurDAO;
        private ProgressDialog pd;
        private String msgError = "";

    public DeleteUser(HubActivity pActivity){
        link(pActivity);
    }

    private void link(HubActivity pActivity){
    }

    @Override
    protected void onPreExecute() {
        ////CREATION D'UNE BOITE DE DIALOGUE
        pd = new ProgressDialog(HubActivity.this);
        pd.setMessage("SUPRESSION EN COURS...");
        pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
        pd.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean reussi = true;
        utilisateurDAO = new UtilisateurDAO();

        try {
            utilisateurDAO.delete(utilisateur);
        } catch (Exception e) {
            Log.i("ERROR",e.getMessage());
            reussi= false;
            msgError = "Erreur, nous n'avons pas pu supprimer votre compte";
        }

        return reussi;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean){
            Intent i = new Intent(HubActivity.this,MainActivity.class);
            startActivity(i); //ON PASSE A L'ACTIVITE MAIN
            finish(); //ON DETRUIT L'ACTIVITE HUB
        } else {
            pd.dismiss(); //ARRET DU PROGRESSDIALOG
            Toast toast = Toast.makeText(HubActivity.this, msgError, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onCancelled() {
        Toast toast = Toast.makeText(HubActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
        toast.show();
    }
}
}

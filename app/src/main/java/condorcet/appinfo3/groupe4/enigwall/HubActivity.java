package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;

public class HubActivity extends AppCompatActivity {
    Button commencer, reprendre;
    Utilisateur utilisateur;
    TextView welcome;
    private NetworkReceiver receiver;
    public final static String IDUSER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        utilisateur = (Utilisateur) i.getParcelableExtra(LoginActivity.IDUSER);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        welcome = (TextView) findViewById(R.id.welcomeTv);
        welcome.setText(getResources().getString(R.string.hub_connectMsg)+ " " + utilisateur.getPseudo().substring(0,1).toUpperCase() + utilisateur.getPseudo().substring(1) + " !");
        commencer = (Button)findViewById(R.id.beginButton);
        reprendre = (Button)findViewById(R.id.continueButton);

        // Vérification en temps réel de la connexion
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hub_del:

                AlertDialog.Builder builder = new AlertDialog.Builder(HubActivity.this);

                TextView mytitle = new TextView(this);
                mytitle.setText(getResources().getString(R.string.hub_del_cpt));
                mytitle.setTextSize(20);
                mytitle.setPadding(35, 15, 15, 15);
                mytitle.setTypeface(Typeface.DEFAULT_BOLD);

                builder.setNegativeButton(getResources().getString(R.string.hub_but_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.hub_but_del), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeleteUser delete = new DeleteUser(HubActivity.this);
                                delete.execute();
                                Intent in = new Intent(HubActivity.this, MainActivity.class);
                                startActivity(in);
                            }
                        });
                builder.setCustomTitle(mytitle);
                builder.show();

                return true;
            case R.id.menu_hub_deco:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(HubActivity.this);

                TextView mytitle2 = new TextView(this);
                mytitle2.setText(getResources().getString(R.string.hub_signoff));
                mytitle2.setTextSize(20);
                mytitle2.setPadding(35, 15, 15, 15);
                mytitle2.setTypeface(Typeface.DEFAULT_BOLD);

                builder2.setNegativeButton(getResources().getString(R.string.hub_signoff_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.hub_signoff_del), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                utilisateur = null;
                                Intent in = new Intent(HubActivity.this, MainActivity.class);
                                startActivity(in);
                            }
                        });
                builder2.setCustomTitle(mytitle2);
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

        public DeleteUser(HubActivity pActivity) {
            link(pActivity);
        }

        private void link(HubActivity pActivity) {
        }

        @Override
        protected void onPreExecute() {
            // Désactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(HubActivity.this);
            pd.setMessage(getResources().getString(R.string.hub_del_advert));
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
                reussi = false;
                msgError = getResources().getString(R.string.hub_del_error);
            }

            return reussi;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            if (aBoolean) {
                Intent i = new Intent(HubActivity.this, MainActivity.class);
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
            Toast toast = Toast.makeText(HubActivity.this, getResources().getString(R.string.toastCancel), Toast.LENGTH_SHORT);
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
                Toast toast = Toast.makeText(HubActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive les boutons
                commencer.setEnabled(false);
                reprendre.setEnabled(false);
            } else {
                // On active les boutons
                commencer.setEnabled(true);
                reprendre.setEnabled(true);
            }
        }
    }

    public void play (View v){
        Intent i = new Intent(HubActivity.this,GameActivity.class);
        startActivity(i);
    }

    public void rate(View v){
        Intent i = new Intent(HubActivity.this,RateActivity.class);
        startActivity(i);
    }
}

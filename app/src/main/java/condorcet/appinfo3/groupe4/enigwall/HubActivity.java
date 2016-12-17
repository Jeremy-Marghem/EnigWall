package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import condorcet.appinfo3.groupe4.enigwall.DAO.ParcoursDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.VilleDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class HubActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Button commencer, reprendre;
    Utilisateur utilisateur;
    TextView welcome, localisation, information;
    private NetworkReceiver receiver;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    ArrayList<Ville> listeVilles;
    private Ville villeLoc;

    public final static String IDUSER = "user";
    public final static String IDVILLE = "ville";
    public final static String IDSTATE = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        utilisateur = (Utilisateur) i.getParcelableExtra(LoginActivity.IDUSER);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        welcome = (TextView) findViewById(R.id.welcomeTv);
        welcome.setText(getResources().getString(R.string.hub_connectMsg)+ " " + utilisateur.getPseudo().substring(0,1).toUpperCase() + utilisateur.getPseudo().substring(1) + " !");
        localisation = (TextView) findViewById(R.id.localisationTv);
        information = (TextView) findViewById(R.id.informationTv);
        commencer = (Button)findViewById(R.id.beginButton);
        reprendre = (Button)findViewById(R.id.continueButton);

        // Vérification en temps réel de la connexion
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);

        // Activation instance GOOGLEAPI
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // On bloque les boutons avant les tests
        commencer.setEnabled(false);
        reprendre.setEnabled(false);

        createLocationRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
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
                                finish();
                            }
                        });
                builder2.setCustomTitle(mytitle2);
                builder2.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.INTERNET }, 10);
            }
            return;
        }

        if (mLastLocation != null) {
            String cityName = (String) getResources().getText(R.string.msgLocalisationRecherche);
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // On met la ville récupérée grâce au GPS dans la textview
            localisation.setText(getResources().getText(R.string.msgLocalisation)+" : "+cityName);

            // On teste la localisation trouvée avec la liste des villes
            // On créé un boolean pour dire si oui ou non on a trouvé la ville dans la liste
            boolean findCity = false;
            for(Ville ville : listeVilles) {
                if(ville.getNomville().equals(cityName)) {
                    // Comme on a trouvé une ville, on réactive le bouton
                    commencer.setEnabled(true);
                    // On met la ville localisée pour un futur envoie à l'activité GameActivity
                    villeLoc = ville;
                    findCity = true;
                    information.setText(getResources().getText(R.string.enigmeOK));
                }
            }

            if(!findCity) {
                information.setText(getResources().getText(R.string.enigmeNOK));
            }

            // Si l'utilisateur a déjà une partie en cours et que l'on a trouvé sa ville dans la liste,
            // on teste si la ville de l'énigme est la ville localisée,
            // si oui on active le bouton reprendre dans l'asynctask
            String test = String.valueOf(utilisateur.getId_enigme());
            if(!test.equals("0") && findCity) {
                GetParcours getParcours = new GetParcours(this);
                getParcours.execute(String.valueOf(villeLoc.getId_ville()));;
            }
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.INTERNET }, 10);
            }
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        listeVilles = new ArrayList<Ville>();
        // On lance la tâche pour récupérer les villes
        GetAllVille allVille = new GetAllVille(HubActivity.this);
        allVille.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    public void continuer(View v){
        Intent i = new Intent(HubActivity.this, GameActivity.class);
        i.putExtra(IDUSER, utilisateur);
        i.putExtra(IDVILLE, villeLoc);
        i.putExtra(IDSTATE, "reprendre");
        startActivity(i);
        finish();
    }

    public void commencer(View v) {
        if(utilisateur.getId_enigme() == 0) {
            // L'utilisateur n'a aucune énigme en cours, on passe à l'activité suivante
            Intent i = new Intent(HubActivity.this, GameActivity.class);
            i.putExtra(IDUSER, utilisateur);
            i.putExtra(IDVILLE, villeLoc);
            i.putExtra(IDSTATE, "commencer");
            startActivity(i);
            finish();
        } else {
            // L'utilisateur a une énigme en cours, on affiche une boîte de dialogue pour le prévenir
            alertDial();
        }

    }

    private void alertDial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HubActivity.this);

        TextView mytitle = new TextView(this);
        mytitle.setText(getResources().getText(R.string.confirmation));
        mytitle.setTextSize(20);
        mytitle.setPadding(35, 15, 15, 15);
        mytitle.setTypeface(Typeface.DEFAULT_BOLD);

        builder.setNegativeButton(getResources().getText(R.string.hub_but_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        })
                .setPositiveButton(getResources().getText(R.string.begin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(HubActivity.this, GameActivity.class);
                        in.putExtra(IDUSER, utilisateur);
                        in.putExtra(IDVILLE, villeLoc);
                        in.putExtra(IDSTATE, "commencer");
                        startActivity(in);
                        finish();
                    }
                });
        builder.setCustomTitle(mytitle);
        builder.show();
    }

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
            // CREATION D'UNE BOITE DE DIALOGUE
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
                startActivity(i); // ON PASSE A L'ACTIVITE MAIN
                finish(); // ON DETRUIT L'ACTIVITE HUB
            } else {
                Toast toast = Toast.makeText(HubActivity.this, msgError, Toast.LENGTH_SHORT);
                toast.show();
            }

            pd.dismiss(); // ARRET DU PROGRESSDIALOG
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(HubActivity.this, getResources().getString(R.string.toastCancel), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Classe asynchrone pour chercher la liste des villes dispos
    private class GetAllVille extends AsyncTask<String, Integer, Boolean> {
        private VilleDAO villeDAO;

        public GetAllVille(HubActivity pActivity) {
            link(pActivity);
        }

        private void link(HubActivity pActivity) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            villeDAO = new VilleDAO();

            try {
                listeVilles = villeDAO.readAll();
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onCancelled() {
        }
    }
    // Fin classe asynchrone villes

    // Classe aynchrone pour récupérer la parcours en fonction de la ville
    private class GetParcours extends AsyncTask<String, Integer, Boolean> {
        private ParcoursDAO parcoursDAO;
        private Parcours parcours;

        public GetParcours(HubActivity pActivity) {
            link(pActivity);
        }

        private void link(HubActivity pActivity) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String id = strings[0];
            parcoursDAO = new ParcoursDAO();

            try {
                parcours = parcoursDAO.read(id);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean) {
                if(utilisateur.getId_parcours() == parcours.getId_parcours()) {
                    reprendre.setEnabled(true);
                }
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
    // Fin classe asynchrone parcours

    // Classe interne pour détection d'internet
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
            }
        }
    }
}

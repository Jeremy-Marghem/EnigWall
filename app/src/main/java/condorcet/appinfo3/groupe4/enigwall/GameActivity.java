package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;
import condorcet.appinfo3.groupe4.enigwall.DAO.EnigmeDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.ParcoursDAO;
import condorcet.appinfo3.groupe4.enigwall.DAO.UtilisateurDAO;
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class GameActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    public TextView enigmeTv, indicationTv;
    public ImageView enigmePicture;
    public Button nextEnigme;
    public MapView mapView;
    public GoogleMap gMap;
    public URL url;
    public Bitmap bitmap, blur;
    public Utilisateur utilisateur;
    public Ville ville;
    public int id_ville;
    public String state;
    public int id_enigme;
    public Parcours parcours;
    public ArrayList<Enigme> listeEnigme;
    public Enigme currentEnigme;
    public Intent i;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation, objectifLocation;
    private NetworkReceiver receiver;
    private Vibrator vibreur;
    private int etape = 0;

    public final static String IDUSER = "user";
    public final static String IDVILLE = "ville";
    public final static String IDSTATE = "state";
    public final static String LISTE = "liste";
    public final static String IDPARCOURS = "parcours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        i = getIntent();
        state = i.getStringExtra(HubActivity.IDSTATE);
        listeEnigme = new ArrayList<Enigme>();

        enigmeTv = (TextView) findViewById(R.id.enigmeTv);
        indicationTv = (TextView) findViewById(R.id.indicationTv);
        indicationTv.setText(getResources().getText(R.string.msgLocalisationRecherche));
        enigmePicture = (ImageView) findViewById(R.id.enigmePicture);
        nextEnigme = (Button) findViewById(R.id.nextenigme);

        if(state.equals("commencer")) {
            utilisateur = (Utilisateur) i.getParcelableExtra(HubActivity.IDUSER);
            ville = (Ville) i.getParcelableExtra(HubActivity.IDVILLE);
            id_ville = ville.getId_ville();
        }

        if(state.equals("reprendre")) {
            utilisateur = (Utilisateur) i.getParcelableExtra(HubActivity.IDUSER);
            ville = (Ville) i.getParcelableExtra(HubActivity.IDVILLE);
            id_ville = ville.getId_ville();
            id_enigme = utilisateur.getId_enigme();
        }

        if(state.equals("suivant")) {
            utilisateur = (Utilisateur) i.getParcelableExtra(GameActivity.IDUSER);
            id_ville = i.getIntExtra(GameActivity.IDVILLE, -1);
            listeEnigme = i.getParcelableArrayListExtra(GameActivity.LISTE);
        }

        // On lance l'asynctask
        GoApplication goApplication = new GoApplication(this);
        goApplication.execute();

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

        // Gestion de la map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        // On met la carte invisible pendant le temps que les coordonnées ne sont pas chargées
        mapView.setVisibility(View.INVISIBLE);

        createLocationRequest();
        objectifLocation = new Location("obj");
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
            double distance = 15;//mLastLocation.distanceTo(objectifLocation);

            // Gestion des distances pour affichage d'un message différent
            // Si on est entre 0 et 25 M, le changement d'énigme est possible
            if(distance > 1000 && distance <= 4000) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi1)+" - "+Math.round(distance)+ "M");
                if(etape != 1){
                    vibreur.vibrate(1000);
                    etape = 1;
                }
            } else if(distance <= 1000 && distance > 400) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi2)+" - "+Math.round(distance)+ "M");
                if(etape != 2){
                    vibreur.vibrate(1000);
                    etape = 2;
                }
            } else if(distance <= 400 && distance > 100) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi3)+" - "+Math.round(distance)+ "M");
                if(etape != 3){
                    vibreur.vibrate(1000);
                    etape = 3;
                }
            } else if(distance <= 100 && distance > 25) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi4)+" - "+Math.round(distance)+ "M");
                if(etape != 4){
                    vibreur.vibrate(1000);
                    etape = 4;
                }
            } else if(distance <= 25 && distance >= 0) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi5)+" - "+Math.round(distance)+ "M");
                if(etape != 5){
                    vibreur.vibrate(2000);
                    etape = 5;
                }
                // On remet une image du monument non floutée
                enigmePicture.setImageBitmap(bitmap);
                // On affiche le bouton pour passer à l'énigme suivante
                nextEnigme.setVisibility(View.VISIBLE);
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

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        mapView.onStart();
        super.onStart();
        vibreur = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        mapView.onResume();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        mapView.onStop();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng obj = new LatLng(Double.parseDouble(currentEnigme.getCoordlatitude()),
                Double.parseDouble(currentEnigme.getCoordlongitude()));
        gMap.addCircle(new CircleOptions().center(obj).radius(150).fillColor(Color.rgb(32, 102, 127)).strokeWidth(0));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(obj, 16));
        // On désactive le zoom sur la carte
        gMap.getUiSettings().setZoomGesturesEnabled(false);
        // On désactive le mouvement sur la carte
        gMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    public void enigmeSuivante(View view) {
        // On retire de la liste l'énigme qui a été réussie
        listeEnigme.remove(0);
        // On sauvegarde l'avancement de l'utilisateur et on passe à l'énigme suivante ou au vote
        SaveUser saveUser = new SaveUser(this);
        saveUser.execute();
    }

    ////CLASSE INTERNE ASYNCHRONE
    class GoApplication extends AsyncTask<String, Integer, Boolean> {
        private ParcoursDAO parcoursDAO;
        private EnigmeDAO enigmeDAO;
        private ProgressDialog pd;

        public GoApplication(GameActivity pActivity){
            link(pActivity);
        }

        private void link(GameActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            // Désactivation de l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(GameActivity.this);
            pd.setMessage(getResources().getText(R.string.loadgame));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            // Récupération du parcours en fonction de l'id de la ville
            // On charge les DAO seulement si l' on a commencé une nouvelle partie
            if(state.equals("commencer")) {
                parcoursDAO = new ParcoursDAO();
                try {
                    parcours = parcoursDAO.read(String.valueOf(id_ville));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Récupération de la liste d'énigmes suivant le parcours
                enigmeDAO = new EnigmeDAO();
                try {
                    listeEnigme = enigmeDAO.readAll(String.valueOf(parcours.getId_parcours()));
                } catch (Exception e) {
                    return false;
                }

                // On réinit l'avancement de l'utilisateur
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                try {
                    utilisateurDAO.updateAvancement(utilisateur);
                } catch (Exception e) {
                    return false;
                }
            }

            if(state.equals("reprendre")) {
                // Récupération de la liste d'énigmes suivant le parcours et l'id de l'énigme suivante
                enigmeDAO = new EnigmeDAO();
                try {
                    // On récupére l'id de l'énigme suivante
                    Enigme enigme = enigmeDAO.read(String.valueOf(utilisateur.getId_enigme()));

                    if(enigme.getId_enigme_suite() == 0) {
                        // S'il n'y a pas d'énigme suivante, on sait que l'on est à la dernière énigme
                        listeEnigme.add(enigme);
                    } else {
                        // S'il y une énigme suivante, on la recherche
                        listeEnigme = enigmeDAO.readSpec(String.valueOf(utilisateur.getId_parcours()),
                        String.valueOf(enigme.getId_enigme_suite()));
                    }
                } catch (Exception e) {
                    return false;
                }
            }

            // On récupère l'énigme, et ça sera l'énigme en cours
            currentEnigme = listeEnigme.get(0);

            // Récupération de l'image normale via le site
            try {
                String lien = "http://www.enigwall.esy.es/app/"+id_ville+"/"+currentEnigme.getNomimage();
                url = new URL(lien);

                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (MalformedURLException e) {}
            catch (IOException e) {}

            // Récupération de l'image floutée via le site
            try {
                String lien = "http://www.enigwall.esy.es/app/"+id_ville+"/blur/"+currentEnigme.getNomimage();
                url = new URL(lien);

                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                blur = BitmapFactory.decodeStream(inputStream);
            }
            catch (MalformedURLException e) {}
            catch (IOException e) {}


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactivation de l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            switch (Locale.getDefault().getLanguage()) {
                case "fr":
                    enigmeTv.setText(currentEnigme.getTexteenigmefr());
                    break;
                case "en":
                    enigmeTv.setText(currentEnigme.getTexteenigmeen());
                    break;
            }

            // On met les coordonnées GPS
            objectifLocation.setLatitude(Double.parseDouble(currentEnigme.getCoordlatitude()));
            objectifLocation.setLongitude(Double.parseDouble(currentEnigme.getCoordlongitude()));
            mapView.getMapAsync(GameActivity.this);
            // On active la visibilité de la carte
            mapView.setVisibility(View.VISIBLE);

            // Si c'est la dernière énigme on change le texte du bouton
            if (listeEnigme.size() == 1) {
                nextEnigme.setText(getResources().getText(R.string.voting));
            }

            // On affiche l'image floutée
            enigmePicture.setImageBitmap(blur);

            pd.dismiss();
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(GameActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Classe asynchrone pour la sauvegarde de l'avancement de l'utilisateur
    private class SaveUser extends AsyncTask<String, Integer, Boolean> {
        private UtilisateurDAO utilisateurDAO;

        public SaveUser(GameActivity pActivity){
            link(pActivity);
        }

        private void link(GameActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            utilisateurDAO = new UtilisateurDAO();

            try {
                if(listeEnigme.size() == 0) {
                    utilisateurDAO.updateAvancement(utilisateur);
                } else {
                    utilisateur.setId_enigme(currentEnigme.getId_enigme());
                    utilisateur.setId_parcours(currentEnigme.getId_parcours());
                    utilisateurDAO.update(utilisateur);
                }
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean) {
                if(listeEnigme.size() == 0) {
                    // On passe au vote puisqu'il n'y a plus d'énigmes
                    Intent intent = new Intent(GameActivity.this, RateActivity.class);
                    intent.putExtra(IDUSER, utilisateur);
                    intent.putExtra(IDPARCOURS, currentEnigme.getId_parcours());
                    startActivity(intent);
                } else {
                    // On recharge l'activité en cours avec les nouvelles valeurs
                    Intent intent = new Intent(GameActivity.this, GameActivity.class);
                    intent.putExtra(IDUSER, utilisateur);
                    intent.putExtra(IDVILLE, id_ville);
                    intent.putExtra(IDSTATE, "suivant");
                    intent.putParcelableArrayListExtra(LISTE, listeEnigme);
                    startActivity(intent);
                }
            }
        }
    }

    // Classe interne pour détection de l'internet
    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // Si aucune connexion n'est trouvée, on affiche un toast
            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                Toast toast = Toast.makeText(GameActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive les boutons
                nextEnigme.setEnabled(false);
            }
        }
    }
}
package condorcet.appinfo3.groupe4.enigwall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import condorcet.appinfo3.groupe4.enigwall.Metier.Enigme;
import condorcet.appinfo3.groupe4.enigwall.Metier.Parcours;
import condorcet.appinfo3.groupe4.enigwall.Metier.Utilisateur;
import condorcet.appinfo3.groupe4.enigwall.Metier.Ville;

public class GameActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    TextView enigmeTv, indicationTv;
    ImageView enigmePicture;
    MapView mapView;
    GoogleMap gMap;
    URL url;

    public final static String IDUSER = "user";
    public final static String IDVILLE = "ville";
    public final static String IDSTATE = "state";
    public final static String IDENIGME = "enigme";

    public Utilisateur utilisateur;
    public Ville ville;
    public int id_ville;
    public String state;

    public int id_enigme;
    public Parcours parcours;
    public ArrayList<Enigme> liste_enigme;
    public Enigme current_enigme;
    public int cptEnigme;
    public int nbEnigme;

    public Intent i;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation, objectifLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        i = getIntent();
        state = i.getStringExtra(HubActivity.IDSTATE);
        liste_enigme = new ArrayList<Enigme>();
        cptEnigme = 0;

        if(state.equals("commencer")){
            utilisateur = (Utilisateur) i.getParcelableExtra(HubActivity.IDUSER);
            ville = (Ville) i.getParcelableExtra(HubActivity.IDVILLE);
            id_ville = ville.getId_ville();
            Begin begin = new Begin(this);
            begin.execute();
        }

        if(state.equals("reprendre")){
            id_enigme = i.getIntExtra(IDENIGME, -1);
        }

        enigmeTv = (TextView) findViewById(R.id.enigmeTv);
        indicationTv = (TextView) findViewById(R.id.indicationTv);
        enigmePicture = (ImageView) findViewById(R.id.enigmePicture);

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
            double distance = mLastLocation.distanceTo(objectifLocation);

            if(distance > 1000) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi1)+" - "+Math.round(distance)+ "M");
            } else if(distance <= 1000 && distance > 400) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi2)+" - "+Math.round(distance)+ "M");
            } else if(distance <= 400 && distance > 100) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi3)+" - "+Math.round(distance)+ "M");
            } else if(distance <= 100 && distance > 30) {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi4)+" - "+Math.round(distance)+ "M");
            } else {
                indicationTv.setText(getResources().getText(R.string.msgIndicationProxi5)+" - "+Math.round(distance)+ "M");
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
        LatLng obj = new LatLng(Double.parseDouble(current_enigme.getCoordlatitude()),
                Double.parseDouble(current_enigme.getCoordlongitude()));
        gMap.addCircle(new CircleOptions().center(obj).radius(250).fillColor(Color.rgb(4, 204, 20)).strokeWidth(0));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(obj, 15));
    }

    ///////////////////

    ////CLASSE INTERNE ASYNCHRONE
    class Begin extends AsyncTask<String, Integer, Boolean> {
        private ParcoursDAO parcoursDAO;
        private EnigmeDAO enigmeDAO;
        private ProgressDialog pd;
        private Bitmap bitmap;

        public Begin(GameActivity pActivity){
            link(pActivity);
        }

        private void link(GameActivity pActivity){
        }

        @Override
        protected void onPreExecute() {
            // Désactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            ////CREATION D'UNE BOITE DE DIALOGUE
            pd = new ProgressDialog(GameActivity.this);
            pd.setMessage(getResources().getText(R.string.loadgame));
            pd.setCancelable(false); //ON EMPECHE L'UTILISATION DU BOUTON BACK
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            // RECUPERATION DU PARCOURS EN FONCTION DE LA VILLE
            parcoursDAO = new ParcoursDAO();
            try {
                parcours = parcoursDAO.read(String.valueOf(id_ville));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //RECUPERATION DE LA LISTE D'ENIGMES CORRESPONDANT AU PARCOURS
            enigmeDAO = new EnigmeDAO();
            try {
                liste_enigme = enigmeDAO.readAll(String.valueOf(parcours.getId_parcours()));
            } catch (Exception e) {
                return false;
            }

            current_enigme = liste_enigme.get(nbEnigme); //Enigme en cours
            nbEnigme = liste_enigme.size(); //Nombre d'enigme dans le parcours

            // Récupération de l'image via le site
            try {
                String lien = "http://www.enigwall.esy.es/app/"+current_enigme.getNomimage();
                url = new URL(lien);

                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (MalformedURLException e) {}
            catch (IOException e) {}


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            // Réactive l'orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            //------------> INSERER ICI UN SWITCH POUR CHOISIR LE TEXTE EN FONCTION DE LA LANGUE DU GSM
            switch (Locale.getDefault().getLanguage()){
                case "fr":
                    enigmeTv.setText(current_enigme.getTexteenigmefr());
                    break;
                case "en":
                    enigmeTv.setText(current_enigme.getTexteenigmeen());
                    break;
            }

            // On met les coordonnées
            objectifLocation.setLatitude(Double.parseDouble(current_enigme.getCoordlatitude()));
            objectifLocation.setLongitude(Double.parseDouble(current_enigme.getCoordlongitude()));
            mapView.getMapAsync(GameActivity.this);

            // On met l'image
            enigmePicture.setImageBitmap(bitmap);

            pd.dismiss();
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(GameActivity.this,getResources().getString(R.string.toastCancel),Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
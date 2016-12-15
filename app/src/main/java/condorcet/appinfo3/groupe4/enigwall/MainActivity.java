package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button signup, signin;
    private NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (Button)findViewById(R.id.signupButton);
        signin = (Button)findViewById(R.id.signinButton);

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

    public void signup(View v){
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(i); //ON PASSE A L'ACTIVITE HUB
    }

    public void login(View v) {
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i); //ON PASSE A L'ACTIVITE LOGIN
    }

    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // Si aucune connexion n'est trouvée, on affiche un toast
            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                Toast toast = Toast.makeText(MainActivity.this, getResources().getText(R.string.connexionError), Toast.LENGTH_SHORT);
                toast.show();
                // On désactive les boutons
                signup.setEnabled(false);
                signin.setEnabled(false);
            } else {
                // On active les boutons
                signup.setEnabled(true);
                signin.setEnabled(true);
            }
        }
    }
}
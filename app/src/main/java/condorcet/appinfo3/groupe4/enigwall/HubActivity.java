package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
        welcome.setText("Bienvenue "+utilisateur.getPseudo()+" !");

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
                Log.i("Options menu ","Supression compte");
                return true;
            case R.id.menu_hub_deco:
                Log.i("Options menu ","Deconnexion");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

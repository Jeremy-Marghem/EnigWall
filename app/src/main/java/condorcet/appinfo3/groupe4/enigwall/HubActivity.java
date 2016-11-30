package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class HubActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
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

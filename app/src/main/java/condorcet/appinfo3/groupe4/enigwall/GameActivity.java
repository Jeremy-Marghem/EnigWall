package condorcet.appinfo3.groupe4.enigwall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import condorcet.appinfo3.groupe4.enigwall.R;
import layout.Game_up;

public class GameActivity extends AppCompatActivity {

    //Game_up fragmentUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //fragmentUp = (Game_up) getSupportFragmentManager().findFragmentById(R.id.fragment);

    }
}
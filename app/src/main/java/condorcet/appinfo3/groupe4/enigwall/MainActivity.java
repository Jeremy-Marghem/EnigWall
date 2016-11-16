package condorcet.appinfo3.groupe4.enigwall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signup(View v){
        Intent i = new Intent(MainActivity.this,SignupActivity.class);
        startActivity(i);
    }
}
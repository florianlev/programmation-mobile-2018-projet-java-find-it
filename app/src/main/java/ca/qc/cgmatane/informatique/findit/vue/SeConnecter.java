package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ca.qc.cgmatane.informatique.findit.R;

public class SeConnecter extends AppCompatActivity {

    static final public int ACTIVITE_JOUER = 1;

    protected Intent intentionNaviguerJouer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_se_connecter);

        intentionNaviguerJouer = new Intent(this, Jouer.class);
        Button actionNaviguerJouer = (Button) findViewById(R.id.action_se_connecter_pour_jouer);
        actionNaviguerJouer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerJouer, ACTIVITE_JOUER);
            }
        });
    }




}

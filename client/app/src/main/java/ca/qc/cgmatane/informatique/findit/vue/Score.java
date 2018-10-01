package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.qc.cgmatane.informatique.findit.FindIt;
import ca.qc.cgmatane.informatique.findit.R;

public class Score extends AppCompatActivity {

    static final public int ACTIVITE_RETOUR_ACCUEIL = 1;

    protected Intent intentionNaviguerAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_score);

        intentionNaviguerAccueil = new Intent(this, FindIt.class);
        Button actionNaviguerAccueil = (Button) findViewById(R.id.action_naviguer_accueil);
        actionNaviguerAccueil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerAccueil, ACTIVITE_RETOUR_ACCUEIL);
            }
        });
    }
}

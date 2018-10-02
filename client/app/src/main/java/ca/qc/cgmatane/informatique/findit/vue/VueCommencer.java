package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ca.qc.cgmatane.informatique.findit.R;

public class VueCommencer extends AppCompatActivity {

    static final public int ACTIVITE_SE_CONNECTER = 1;
    static final public int ACTIVITE_CREER_COMPTE = 2;

    protected Intent intentionNaviguerSeConnecter;
    protected Intent intentionNaviguerCreerCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_commencer);

        intentionNaviguerSeConnecter = new Intent(VueCommencer.this, VueSeConnecter.class);
        Button actionNaviguerSeConnecter = (Button) findViewById(R.id.action_naviguer_se_connecter);
        actionNaviguerSeConnecter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerSeConnecter, ACTIVITE_SE_CONNECTER);
            }
        });

        intentionNaviguerCreerCompte = new Intent(VueCommencer.this, CreerCompte.class);
        Button actionNaviguerCreerCompte = (Button) findViewById(R.id.action_naviguer_creer_compte);
        actionNaviguerCreerCompte.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerCreerCompte, ACTIVITE_CREER_COMPTE);
            }
        });

    }


    public void naviguerRetourAccueil(){
        this.finish();
    }


}

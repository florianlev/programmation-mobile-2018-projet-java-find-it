package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.qc.cgmatane.informatique.findit.R;

public class Commencer extends AppCompatActivity {

    static final public int ACTIVITE_SE_CONNECTER = 1;
    static final public int ACTIVITE_CREER_COMPTE = 2;

    protected Intent intentionNaviguerSeConnecter;
    protected Intent intentionNaviguerCreerCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_commencer);

    }



    public void naviguerRetourAccueil(){
        this.finish();
    }


}

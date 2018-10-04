package ca.qc.cgmatane.informatique.findit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.qc.cgmatane.informatique.findit.accesseur.BaseDeDonnees;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;
import ca.qc.cgmatane.informatique.findit.vue.VueCommencer;
import ca.qc.cgmatane.informatique.findit.vue.VueScore;

public class FindIt extends AppCompatActivity {

    static final public int ACTIVITE_COMMENCER = 1;
    static final public int ACTIVITE_SCORE = 2;

    protected Intent intentionNaviguerCommencer;
    protected Intent intentionNaviguerScore;
    protected UtilisateurDAO utilisateurDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vue_accueil);

        BaseDeDonnees.getInstance(getApplicationContext());
        utilisateurDAO = utilisateurDAO.getInstance();
        utilisateurDAO.listerUtilisateur();

        //Utilisateur utilisateur = new Utilisateur("test","testtoto", "test");
        //utilisateurDAO.ajouterUtilisateurSQL(utilisateur);

        intentionNaviguerCommencer = new Intent(this, VueCommencer.class);
        Button actionNaviguerCommencer = (Button) findViewById(R.id.action_naviguer_commencer);
        actionNaviguerCommencer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerCommencer, ACTIVITE_COMMENCER);
            }
        });

        intentionNaviguerScore = new Intent(this, VueScore.class);
        Button actionNaviguerScore = (Button) findViewById(R.id.action_naviguer_score);
        actionNaviguerScore.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerScore, ACTIVITE_SCORE);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}


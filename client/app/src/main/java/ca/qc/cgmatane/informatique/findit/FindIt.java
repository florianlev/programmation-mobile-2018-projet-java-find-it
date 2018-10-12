package ca.qc.cgmatane.informatique.findit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ca.qc.cgmatane.informatique.findit.accesseur.BaseDeDonnees;
import ca.qc.cgmatane.informatique.findit.accesseur.ScoreDAO;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.modele.Score;
import ca.qc.cgmatane.informatique.findit.vue.VueCommencer;
import ca.qc.cgmatane.informatique.findit.vue.VueJeu;
import ca.qc.cgmatane.informatique.findit.vue.VueScore;

public class FindIt extends AppCompatActivity {

    static final public int ACTIVITE_COMMENCER = 1;
    static final public int ACTIVITE_SCORE = 2;
    static final public int ACTIVITE_JEU = 3;


    protected Intent intentionNaviguerCommencer;
    protected Intent intentionNaviguerVueJeu;

    protected Intent intentionNaviguerScore;
    protected UtilisateurDAO utilisateurDAO;
    protected ScoreDAO scoreDAO;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vue_accueil);


        BaseDeDonnees.getInstance(getApplicationContext());
        utilisateurDAO = utilisateurDAO.getInstance();

        scoreDAO = scoreDAO.getInstance();

        //utilisateurDAO.listerUtilisateur();

        preferences = getSharedPreferences("detail_utilisateur",MODE_PRIVATE);

        intentionNaviguerCommencer = new Intent(this, VueCommencer.class);
        intentionNaviguerScore = new Intent(this, VueScore.class);
        intentionNaviguerVueJeu = new Intent(this, VueJeu.class);
        Button actionNaviguerCommencer = (Button) findViewById(R.id.action_naviguer_commencer);
        TextView textUtilisateur = (TextView) findViewById(R.id.textView_utilisateur_vue_accueil);

        if(preferences.getBoolean("estConnecter",false)){
            textUtilisateur.setText("Welcome : " + preferences.getString("pseudo", null));
            System.out.println("ID : " +  preferences.getInt("id",0));
            Score score = new Score(40000, preferences.getInt("id",0));
            //scoreDAO.ajouterScore(score);
        }else{
            //Vidage des session si l'utilisateur n'a pas cocher rester connecter
            SharedPreferences.Editor editeur = preferences.edit();
            editeur.clear();
            editeur.commit();
        }

        actionNaviguerCommencer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                System.out.println(preferences.getString("pseudo",null));
                if (preferences.getBoolean("estConnecter", false)){
                    startActivityForResult(intentionNaviguerVueJeu,ACTIVITE_JEU);
                }
                else{
                    startActivityForResult(intentionNaviguerCommencer, ACTIVITE_COMMENCER);
                }
            }
        });

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


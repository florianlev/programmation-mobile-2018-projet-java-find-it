package ca.qc.cgmatane.informatique.findit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;
import ca.qc.cgmatane.informatique.findit.vue.Commencer;
import ca.qc.cgmatane.informatique.findit.vue.Score;

public class FindIt extends AppCompatActivity {

    static final public int ACTIVITE_COMMENCER = 1;
    static final public int ACTIVITE_SCORE = 2;
    static final public int ACTIVITE_CARTE = 3;


    protected Intent intentionNaviguerCommencer;
    protected Intent intentionNaviguerScore;

    protected UtilisateurDAO utilisateurDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        utilisateurDAO = new UtilisateurDAO();
        setContentView(R.layout.vue_accueil);

        intentionNaviguerCommencer = new Intent(this, Commencer.class);
        Button actionNaviguerCommencer = (Button) findViewById(R.id.action_naviguer_commencer);
        actionNaviguerCommencer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerCommencer, ACTIVITE_COMMENCER);
            }
        });

        intentionNaviguerScore = new Intent(this, Score.class);
        Button actionNaviguerScore = (Button) findViewById(R.id.action_naviguer_score);
        actionNaviguerScore.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                startActivityForResult(intentionNaviguerScore, ACTIVITE_SCORE);
            }
        });
    }

    public void ajouterUtilisateur(Utilisateur utilisateur){
        utilisateurDAO.ajouterUtilisateur(utilisateur);
    }



    protected void onActivityResult(int activite, int resultat, Intent donnes){
        switch (activite){
            case ACTIVITE_COMMENCER:
                //afficherTousLesLivres();
                break;

            case ACTIVITE_SCORE:
                //afficherTousLesLivres();
                break;
        }
    }
}

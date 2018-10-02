package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;

public class VueSeConnecter extends AppCompatActivity {

    static final public int ACTIVITE_JOUER = 1;

    protected Intent intentionNaviguerJouer;
    protected EditText champPseudo;
    protected EditText champMdp;

    protected UtilisateurDAO accesseurUtilisateur = UtilisateurDAO.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_se_connecter);

        champPseudo = (EditText) findViewById(R.id.vue_se_connecter_champ_pseudo);
        champMdp = (EditText) findViewById(R.id.vue_se_connecter_champ_mot_de_passe);
        intentionNaviguerJouer = new Intent(this, VueJeu.class);
        Button actionNaviguerJouer = (Button) findViewById(R.id.action_se_connecter_pour_jouer);
        actionNaviguerJouer.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        verifierConnection();
                    }
                });
    }

    private void verifierConnection() {
        String pseudo =champPseudo.getText().toString();
        String mdp =champMdp.getText().toString();
        int verif=accesseurUtilisateur.verifConnecction(pseudo,mdp);
        if(verif>= 1){
            startActivityForResult(intentionNaviguerJouer, ACTIVITE_JOUER);

        }
        else{
            System.out.println("ERREUR");

        }
    }
}
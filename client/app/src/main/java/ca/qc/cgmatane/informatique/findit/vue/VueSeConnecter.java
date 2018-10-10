package ca.qc.cgmatane.informatique.findit.vue;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;

public class VueSeConnecter extends AppCompatActivity {

    static final public int ACTIVITE_JOUER = 1;

    protected Intent intentionNaviguerJouer;
    protected EditText champPseudo;
    protected EditText champMdp;
    protected Utilisateur utilisateur;

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
        utilisateur = new Utilisateur(champPseudo.getText().toString(), champMdp.getText().toString());
        Toast toast = Toast.makeText(getApplicationContext(), "Mail ou mot de passe incorrect", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);


        Dialog dialog = new Dialog(this);

        if (accesseurUtilisateur.verifierConnection(utilisateur)) {
            System.out.println("GAGNER");
            startActivityForResult(intentionNaviguerJouer, ACTIVITE_JOUER);
        }
        else{
            toast.show();
        }
    }
}
package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.modele.Utilisateur;

public class VueCreerCompte extends AppCompatActivity {

    static final public int ACTIVITE_JOUER = 1;

    protected Intent intentionNaviguerJouer;

    protected EditText champPseudo;
    protected EditText champMail;
    protected EditText champMdp;
    protected EditText champMdpConfirme;
    protected Utilisateur utilisateur;

    protected UtilisateurDAO accesseurUtilisateur = UtilisateurDAO.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_creer_compte);

        champPseudo = (EditText)findViewById(R.id.vue_creer_compte_champ_nom);
        champMail = (EditText)findViewById(R.id.vue_creer_compte_champ_mail);
        champMdp = (EditText)findViewById(R.id.vue_creer_compte_champ_mot_de_passe);
        champMdpConfirme = (EditText)findViewById(R.id.vue_creer_compte_champ_verification_mot_de_passe);

        intentionNaviguerJouer = new Intent(this, VueCommencer.class);
        Button actionNaviguerJouer =
                (Button) findViewById(R.id.action_sinscrire);

        actionNaviguerJouer.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View arg0){
                        enregistrerUtilisateur();
                        startActivityForResult(intentionNaviguerJouer, ACTIVITE_JOUER);
                    }
                });
    }

    private void enregistrerUtilisateur(){
        utilisateur = new Utilisateur(champPseudo.getText().toString(),champMail.getText().toString(), champMdp.getText().toString());
        accesseurUtilisateur.ajouterUtilisateur(utilisateur);

    }
}

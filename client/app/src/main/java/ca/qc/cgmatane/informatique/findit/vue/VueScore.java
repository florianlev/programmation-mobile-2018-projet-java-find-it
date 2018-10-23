package ca.qc.cgmatane.informatique.findit.vue;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.BaseDeDonnees;
import ca.qc.cgmatane.informatique.findit.accesseur.ScoreDAO;

public class VueScore extends AppCompatActivity {

    protected List<HashMap<String, String>> listeScorePourAdapteur;
    protected ScoreDAO accesseurScore;
    protected ListView vuelisteScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_score);

        BaseDeDonnees.getInstance(getApplicationContext());
        accesseurScore = ScoreDAO.getInstance();
        vuelisteScore = (ListView) findViewById(R.id.vue_liste_score);

        afficherTousLesEvenements();

        Button actionNaviguerAccueil = (Button) findViewById(R.id.action_naviguer_accueil);
        actionNaviguerAccueil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                retourAncienneActivite();
            }
        });

    }

    public void retourAncienneActivite(){
        this.finish();
    }

    protected void afficherTousLesEvenements(){
        listeScorePourAdapteur = accesseurScore.recupererListePourAdapteur();
        SimpleAdapter adapteur = new SimpleAdapter(this, listeScorePourAdapteur,
                android.R.layout.two_line_list_item,
                new String[] {"valeur", "joueur"},
                new int[] {android.R.id.text1, android.R.id.text2});
        vuelisteScore.setAdapter(adapteur);
    }

}

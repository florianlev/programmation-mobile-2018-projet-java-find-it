package ca.qc.cgmatane.informatique.findit.vue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.GalerieDAO;

public class VueAfficherImage extends AppCompatActivity {
    private GalerieDAO accesseurGalerie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_afficher_image);
        Bundle parametres = this.getIntent().getExtras();
        int parametre_possition_photo = (int) parametres.get("position");

        accesseurGalerie= GalerieDAO.getInstance();
        String urlImage=accesseurGalerie.recuperereListeScorePourImageAdapteur()[parametre_possition_photo];
        ImageView imageView=(ImageView) findViewById(R.id.imageView_apercu);
        Picasso.get().load(urlImage).into(imageView);
    }

    //todo implementer gesture swap , qui decale a la valeur de possition a l'ellement suivant de la liste
}

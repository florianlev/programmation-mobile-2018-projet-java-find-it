package ca.qc.cgmatane.informatique.findit.vue;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.GalerieDAO;

public class VueAfficherImage extends AppCompatActivity {

    private GalerieDAO accesseurGalerie;

    private GestureDetectorCompat mDetector;

    int parametre_position_photo;

    private static final String DEBUG_TAG = "Taille tableau";

    int compteur = 0;
    float initialX, initialY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_afficher_image);
        Bundle parametres = this.getIntent().getExtras();
        parametre_position_photo = (int) parametres.get("position");

        accesseurGalerie= GalerieDAO.getInstance();
        afficherImage(parametre_position_photo);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                // swipe de droite à gauche
                if (initialX < finalX) {
                    this.mDetector.onTouchEvent(event);
                    parametre_position_photo++;

                    if (parametre_position_photo >= accesseurGalerie.recuperereListePourImageAdapteur().length){
                        parametre_position_photo = 0;
                    }
                    afficherImage(parametre_position_photo);
                    return super.onTouchEvent(event);
                }

                break;
        }
        return super.onTouchEvent(event);
    }


    public void afficherImage(int position){
        String urlImage = accesseurGalerie.recuperereListePourImageAdapteur()[position];
        ImageView imageView = (ImageView) findViewById(R.id.imageView_apercu);
        Picasso.get().load(urlImage).into(imageView);
    }
}

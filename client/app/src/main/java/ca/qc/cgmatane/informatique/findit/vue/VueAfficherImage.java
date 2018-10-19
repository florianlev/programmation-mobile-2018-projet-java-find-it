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

    int parametre_possition_photo;

    private static final String DEBUG_TAG = "Taille tableau";

    int compteur = 0;
    float initialX, initialY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_afficher_image);
        Bundle parametres = this.getIntent().getExtras();
        parametre_possition_photo = (int) parametres.get("position");

        accesseurGalerie= GalerieDAO.getInstance();
        afficherImage(parametre_possition_photo);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                Log.d(DEBUG_TAG, "Action was UP");

                if (initialX < finalX) {
                    Log.d(DEBUG_TAG, "Right to Left swipe performed");
                    this.mDetector.onTouchEvent(event);
                    parametre_possition_photo++;
                    if (parametre_possition_photo >= accesseurGalerie.recuperereListeScorePourImageAdapteur().length){
                        parametre_possition_photo = 0;
                    }
                    afficherImage(parametre_possition_photo);
                    return super.onTouchEvent(event);
                }

                break;
        }
        return super.onTouchEvent(event);

    }

        /*
        this.mDetector.onTouchEvent(event);
        Log.d(DEBUG_TAG, "" + accesseurGalerie.recuperereListeScorePourImageAdapteur().length + "  param position : " + parametre_possition_photo);
        parametre_possition_photo++;
        afficherImage(parametre_possition_photo);
        compteur++;
        if (compteur % 3 == 0) parametre_possition_photo -= 3;
        return super.onTouchEvent(event);*/


    public void afficherImage(int position){
        Log.d(DEBUG_TAG, "======DEBUT=======");
        String urlImage = accesseurGalerie.recuperereListeScorePourImageAdapteur()[position];
        Log.d(DEBUG_TAG, "  variable classe position : " + parametre_possition_photo);
        Log.d(DEBUG_TAG, "parametre fonction : " + position);
        ImageView imageView = (ImageView) findViewById(R.id.imageView_apercu);
        Picasso.get().load(urlImage).into(imageView);
        Log.d(DEBUG_TAG, "======FIN=======");

    }
}

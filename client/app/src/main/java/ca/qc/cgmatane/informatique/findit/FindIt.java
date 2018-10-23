package ca.qc.cgmatane.informatique.findit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import ca.qc.cgmatane.informatique.findit.accesseur.BaseDeDonnees;
import ca.qc.cgmatane.informatique.findit.accesseur.UtilisateurDAO;
import ca.qc.cgmatane.informatique.findit.vue.MyGestureListener;
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

    SharedPreferences preferences;

    private GestureDetectorCompat mDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vue_accueil);

        BaseDeDonnees.getInstance(getApplicationContext());
        utilisateurDAO = utilisateurDAO.getInstance();

        preferences = getSharedPreferences("detail_utilisateur",MODE_PRIVATE);

        intentionNaviguerCommencer = new Intent(this, VueCommencer.class);
        intentionNaviguerScore = new Intent(this, VueScore.class);
        intentionNaviguerVueJeu = new Intent(this, VueJeu.class);
        Button actionNaviguerCommencer = (Button) findViewById(R.id.action_naviguer_commencer);
        TextView texteUtilisateur = (TextView) findViewById(R.id.textView_utilisateur_vue_accueil);

        if (preferences.getBoolean("estConnecter",false)){
            texteUtilisateur.setText("Bienvenue " + preferences.getString("pseudo", null));

        } else{
            //Vidage des sessions si l'utilisateur n'a pas coché rester connecté
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
                != PackageManager.PERMISSION_GRANTED | ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            /*ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 2);*/
        }

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        ImageView imageView = (ImageView) findViewById(R.id.imageView_apercu);
        Picasso.get().load(R.drawable.map).into(imageView);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}


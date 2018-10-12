package ca.qc.cgmatane.informatique.findit.vue;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ca.qc.cgmatane.informatique.findit.R;

public class VueGalerie extends AppCompatActivity {

    protected GridView vueGridGalerie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_galerie);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intentionNaviguerModiferEvenement = new Intent(VueGalerie.this, VueAfficherImage.class);
                intentionNaviguerModiferEvenement.putExtra("position",position);
                startActivityForResult(intentionNaviguerModiferEvenement,1);

            }
        });
        Button actionNaviguerretour = (Button) findViewById(R.id.action_naviguer_retour_jeu);
        actionNaviguerretour.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                retourAncienneActivite();
            }
        });



    /*
        java.io.FileInputStream in;

        ImageView imageView = findViewById(R.id.imageView);
        try {

            File monImage = new File("sdcard/exemple.jpg");
            in = new FileInputStream(monImage);

            imageView.setImageBitmap(BitmapFactory.decodeStream(in));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public void retourAncienneActivite(){
        this.finish();
    }

    //https://stackoverflow.com/questions/2688169/how-to-load-an-imageview-from-a-png-file
}

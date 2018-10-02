package ca.qc.cgmatane.informatique.findit.vue;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ca.qc.cgmatane.informatique.findit.R;

public class VueGalerie extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_galerie);

        java.io.FileInputStream in;

        ImageView imageView = findViewById(R.id.imageView);
        try {

            File monImage = new File("sdcard/exemple.jpg");
            in = new FileInputStream(monImage);

            imageView.setImageBitmap(BitmapFactory.decodeStream(in));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/2688169/how-to-load-an-imageview-from-a-png-file
}

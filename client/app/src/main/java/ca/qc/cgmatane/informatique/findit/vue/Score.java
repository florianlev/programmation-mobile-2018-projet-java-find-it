package ca.qc.cgmatane.informatique.findit.vue;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ca.qc.cgmatane.informatique.findit.R;

public class Score extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_score);

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
}

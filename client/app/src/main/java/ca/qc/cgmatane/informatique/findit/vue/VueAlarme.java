package ca.qc.cgmatane.informatique.findit.vue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.qc.cgmatane.informatique.findit.R;

public class VueAlarme extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vue_alarme);

        Button stopAlarm = (Button) findViewById(R.id.action_naviguer_ancienne_activite);
        stopAlarm.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //mMediaPlayer.stop();
                naviguerAncienneActivite();
                return false;
            }
        });

        Button prendrePhoto = (Button) findViewById(R.id.action_prendre_photo);
        prendrePhoto.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
                return false;
            }
        });

        //playSound(this, getAlarmUri());
    }



    public void naviguerAncienneActivite(){
        this.finish();
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    private File creerFichierImage() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nomImage = "JPEG_" + timeStamp + "_";
        File dossierDestination = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                nomImage,
                ".jpg",
                dossierDestination
        );

        String cheminPhoto = image.getAbsolutePath();
        //Toast.makeText(getApplicationContext(),cheminPhoto, Toast.LENGTH_SHORT).show();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            try {
                creerFichierImage();
                //Todo inplementer fonction d'envoie vers serveur

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (resultCode == Activity.RESULT_CANCELED) {


            Toast.makeText(getApplicationContext(), "Vous avez annuler la capture", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(),"Désolé! echec de capture photo", Toast.LENGTH_SHORT).show();
        }
    }
}
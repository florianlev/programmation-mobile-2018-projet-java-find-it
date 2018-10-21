package ca.qc.cgmatane.informatique.findit.vue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.apache.http.HttpConnection;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.GalerieDAO;
import ca.qc.cgmatane.informatique.findit.accesseur.HttpPostRequete;

public class VueAlarme extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int APPAREIL_PHOTO = 2;
    private MediaPlayer mMediaPlayer;
    protected GalerieDAO accesseurGalerieDAO = GalerieDAO.getInstance();
    ImageView imageAEnvoyer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vue_alarme);
        imageAEnvoyer = (ImageView) findViewById(R.id.imageAEnvoyer);
        Button stopAlarm = (Button) findViewById(R.id.action_naviguer_ancienne_activite);
        Button choisirPhoto = (Button) findViewById(R.id.action_choisir_photo);
        Button envoyerPhoto = (Button) findViewById(R.id.action_envoyer_photo);

        envoyerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click");
                int time = (int) (System.currentTimeMillis());
                Timestamp tsTemp = new Timestamp(time);
                Bitmap image = ((BitmapDrawable) imageAEnvoyer.getDrawable()).getBitmap();

                new TransfererImage(image,tsTemp.toString()).execute();
                naviguerAncienneActivite();

            }
        });

        choisirPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galerieIntention = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galerieIntention, RESULT_LOAD_IMAGE);
            }
        });


        Button prendrePhoto = (Button) findViewById(R.id.action_prendre_photo);
        prendrePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, APPAREIL_PHOTO);
                }

            }
        });


        stopAlarm.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //mMediaPlayer.stop();
                naviguerAncienneActivite();
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
        Bitmap bm;
        bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()), 100,100, true);
        /*Uri uri = Uri.fromFile(image);
        System.out.println(uri);*/

        imageAEnvoyer.setIma;
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            imageAEnvoyer.setImageURI(selectedImage);
        }
        else if (requestCode == APPAREIL_PHOTO & resultCode == RESULT_OK){
            System.out.println("APPAREIL PHOTO");
            try{
                creerFichierImage();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    private class TransfererImage extends AsyncTask<Void,Void,Void>{

        Bitmap image;
        String nom;
        String URLServer = "http://158.69.113.110/findItServeur/galerie/ajouter/index.php";
        public TransfererImage(Bitmap image, String nom){
            this.image = image;
            this.nom = nom;
        }

        protected Void doInBackground(Void... params){
            System.out.println("doInBackground");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage  = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> fichierAEnvoyer = new ArrayList<>();
            fichierAEnvoyer.add(new BasicNameValuePair("image", encodedImage));
            fichierAEnvoyer.add (new BasicNameValuePair("nom", nom));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(URLServer);

            try {
                post.setEntity(new UrlEncodedFormEntity(fichierAEnvoyer));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image telecharger", Toast.LENGTH_SHORT).show();
        }



    }
}

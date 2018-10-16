package ca.qc.cgmatane.informatique.findit.vue;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;

import ca.qc.cgmatane.informatique.findit.FindIt;
import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.ScoreDAO;
import ca.qc.cgmatane.informatique.findit.modele.Score;

public class VueJeu extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Marker marqueurJoueur = null;
    LocationRequest mLocationRequest = new LocationRequest();
    private Marker marqueurDestination = null;

    protected double latitudeJoueur, longitudeJoueur;
    protected double latitudeDestination, longitudeDestination;


    static final public int ACTIVITE_SCORE = 1;
    static final public int ACTIVITE_GALERIE = 2;
    static final public int ACTIVITE_ALARME = 3;
    static final public int ACTIVITE_VUE_ACCUEIL = 4;

    protected Intent intentionNaviguerScore;
    protected Intent intentionNaviguerGalerie;
    protected Intent intentionNaviguerAlarme;
    protected Intent intentionNaviguerCommencer;

    protected ScoreDAO scoreDAO;

    int initialisation = 0;

    SharedPreferences preferences;

    UiSettings uiSettings;
    private GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_jeu);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        scoreDAO = scoreDAO.getInstance();

        preferences = getSharedPreferences("detail_utilisateur",MODE_PRIVATE);
        System.out.println("YOOO " + preferences.getString("pseudo",null));

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    latitudeJoueur = location.getLatitude();
                    longitudeJoueur = location.getLongitude();
                    LatLng positionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);
                    //Toast.makeText(VueJeu.this, "latitude" + latitudeJoueur + " longitude" + longitudeJoueur, Toast.LENGTH_LONG).show();

                    if (marqueurJoueur == null) {
                        MarkerOptions options = new MarkerOptions().position(positionJoueur).title("Position joueur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marqueurJoueur = mMap.addMarker(options);
                    } else {
                        marqueurJoueur.setPosition(positionJoueur);
                    }

                    if (initialisation == 0){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionJoueur));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
                        initialisation++;
                        ouvrirDialogue();
                    }
                }

                if (cestGagne()){
                    scoreDAO.ajouterScore(new Score(1000,preferences.getInt("id",0)));
                    stopLocationUpdates();
                    activeAlarme();
                }
            }
        };

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }





    public void activeAlarme(){
        intentionNaviguerAlarme = new Intent(this, VueAlarme.class);
        startActivityForResult(intentionNaviguerAlarme, ACTIVITE_ALARME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_score) {
            intentionNaviguerScore = new Intent(this, VueScore.class);
            startActivityForResult(intentionNaviguerScore, ACTIVITE_SCORE);
            return true;
        }
        if (id == R.id.nav_galerie) {
            intentionNaviguerGalerie = new Intent(this, VueGalerie.class);
            startActivityForResult(intentionNaviguerGalerie, ACTIVITE_GALERIE);
            return true;
        }

        if(id == R.id.nav_deconnection){
            intentionNaviguerCommencer = new Intent(this, FindIt.class);
            SharedPreferences.Editor editeur = preferences.edit();
            editeur.clear();
            editeur.commit();
            startActivityForResult(intentionNaviguerCommencer, ACTIVITE_VUE_ACCUEIL);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        recupererPossitionJoueur();
        recupererPossitionDestination();
        createLocationRequest();
        startLocationUpdates();
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    public void recupererPossitionJoueur() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        double latitudeJoueur = location.getLatitude();
                        double longitudeJoueur = location.getLongitude();
                        LatLng possitionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);
                        //Toast.makeText(VueJeu.this, "latitude" + latitudeJoueur + " longitude" + longitudeJoueur, Toast.LENGTH_LONG).show();

                        if (marqueurJoueur == null) {
                            MarkerOptions options = new MarkerOptions().position(possitionJoueur).title("Position joueur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            marqueurJoueur = mMap.addMarker(options);
                        } else {
                            marqueurJoueur.setPosition(possitionJoueur);
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(possitionJoueur));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13f));

                    }
                });
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(2500);
        mLocationRequest.setFastestInterval(1250);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    
    public void recupererPossitionDestination() {

        this.latitudeDestination = genererLatitudeNouvelleDestination();
        this.longitudeDestination = genererLongitudeNouvelleDestination();
        LatLng positionDestination = new LatLng(latitudeDestination, longitudeDestination);
        //Toast.makeText(VueJeu.this, "latitude" + latitudeDestination + " longitude" + longitudeDestination, Toast.LENGTH_LONG).show();

        if (marqueurDestination == null) {
            MarkerOptions options = new MarkerOptions().position(positionDestination).title("Destination");
            marqueurDestination = mMap.addMarker(options);
            } else {
            marqueurDestination.setPosition(positionDestination);
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(possitionDestination));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(14f));
    }

    public double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        int earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }

    public boolean cestGagne(){
        if (distanceInKmBetweenEarthCoordinates(latitudeDestination, longitudeDestination, latitudeJoueur, longitudeJoueur) <= 0.1){
            System.out.println("Gagné");
            return true;
        }else{
            System.out.println("Marche encore");
            return false;
        }
    }

    public double genererLatitudeNouvelleDestination(){
        double latitudeDestinationMax = 48.850020;
        double latitudeDestinationMin = 48.830022;

        double latitudeNouvelleDestination = latitudeDestinationMin + (Math.random() * ((latitudeDestinationMax - latitudeDestinationMin)));
        latitudeDestination = latitudeNouvelleDestination;
        System.out.println("Nouvelle destination " + latitudeNouvelleDestination);

        return latitudeNouvelleDestination;
    }

    public double genererLongitudeNouvelleDestination(){
        double longitudeDestinationMax = -67.535786;
        double longitudeDestinationMin = -67.491784;

        double longitudeNouvelleDestination = longitudeDestinationMin + (Math.random() * ((longitudeDestinationMax - longitudeDestinationMin)));
        longitudeDestination = longitudeNouvelleDestination;
        System.out.println("Nouvelle destination " + longitudeNouvelleDestination);

        return longitudeNouvelleDestination;
    }



    protected void onActivityResult(int activite, int resultat, Intent donnees){
        switch (activite){
            case ACTIVITE_ALARME:
                recreate();
                break;
        }
    }

    public void ouvrirDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Explications");
        alertDialog.setMessage("Votre position est le pin bleu. Il faut se rendre vers le pin rouge (destination) pour gagner des points.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Compris!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }
}
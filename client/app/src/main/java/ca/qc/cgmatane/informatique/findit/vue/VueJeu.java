package ca.qc.cgmatane.informatique.findit.vue;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import ca.qc.cgmatane.informatique.findit.FindIt;
import ca.qc.cgmatane.informatique.findit.R;
import ca.qc.cgmatane.informatique.findit.accesseur.ScoreDAO;
import ca.qc.cgmatane.informatique.findit.modele.Score;

public class VueJeu extends AppCompatActivity implements    OnMapReadyCallback ,
                                                            GoogleMap.OnMapClickListener,
                                                            GoogleMap.OnMapLongClickListener {

    static final public int ACTIVITE_SCORE = 1;
    static final public int ACTIVITE_GALERIE = 2;
    static final public int ACTIVITE_ALARME = 3;
    static final public int ACTIVITE_VUE_ACCUEIL = 4;

    protected Intent intentionNaviguerScore;
    protected Intent intentionNaviguerGalerie;
    protected Intent intentionNaviguerAlarme;
    protected Intent intentionNaviguerCommencer;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    LocationRequest mLocationRequest = new LocationRequest();

    private Marker marqueurJoueur = null;
    private Marker marqueurDestination = null;

    protected double latitudeJoueur, longitudeJoueur;
    protected double latitudeDestination, longitudeDestination;

    protected ScoreDAO scoreDAO;

    SharedPreferences preferences;

    UiSettings uiSettings;
    private GestureDetectorCompat mDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventManager mShakeDetector;
    private long tempsDebutPartie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tempsDebutPartie = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_jeu);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        scoreDAO = scoreDAO.getInstance();

        preferences = getSharedPreferences("detail_utilisateur",MODE_PRIVATE);

        verifierPremiereUtilisation();

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

                    if (marqueurJoueur == null) {
                        MarkerOptions options = new MarkerOptions().position(positionJoueur).title("Votre position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marqueurJoueur = mMap.addMarker(options);
                    } else {
                        marqueurJoueur.setPosition(positionJoueur);
                    }
                }

                if (cestGagne()){
                    int valeurScore= genererAugmentationScore(500,50);
                    scoreDAO.modifierScore(new Score(valeurScore, preferences.getInt("id",0)));
                    stopLocationUpdates();
                    activeAlarme();
                }
            }
        };

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        // Initialisation ShakeDetector
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeEventManager();
        mShakeDetector.setOnShakeListener(new ShakeEventManager.OnShakeListener() {

            @Override
            public void onShake(int count) {
                Toast.makeText(VueJeu.this, "Nouvelle destination générée ", Toast.LENGTH_LONG).show();
                recreate();
            }
        });
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        recupererPositionJoueur();
        recupererPositionDestination();
        createLocationRequest();
        startLocationUpdates();
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }


    @SuppressLint("MissingPermission")
    public void recupererPositionJoueur() {

        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        double latitudeJoueur = location.getLatitude();
                        double longitudeJoueur = location.getLongitude();
                        LatLng positionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);

                        if (marqueurJoueur == null) {
                            MarkerOptions options = new MarkerOptions().position(positionJoueur).title("Votre position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            marqueurJoueur = mMap.addMarker(options);
                        } else {
                            marqueurJoueur.setPosition(positionJoueur);
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionJoueur));
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
    
    @SuppressLint("MissingPermission")
    public void recupererPositionDestination() {

        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        double latitudeJoueur = location.getLatitude();
                        double longitudeJoueur = location.getLongitude();
                        LatLng positionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);
                        LatLng positionDestination= getRandomLocation(positionJoueur,500);

                        if (marqueurDestination == null) {
                            MarkerOptions options = new MarkerOptions().position(positionDestination).title("Destination");
                            marqueurDestination = mMap.addMarker(options);
                        } else {
                            marqueurDestination.setPosition(positionDestination);
                        }
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(possitionDestination));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(14f));

                    }
                });
    }

    public double degresEnRadians(double degres) {
        return degres * Math.PI / 180;
    }

    public double distanceEnKmEntreCoordonnees(double lat1, double lon1, double lat2, double lon2) {
        int rayonTerreKm = 6371;

        double dLat = degresEnRadians(lat2-lat1);
        double dLon = degresEnRadians(lon2-lon1);

        lat1 = degresEnRadians(lat1);
        lat2 = degresEnRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return rayonTerreKm * c;
    }


    public boolean cestGagne(){
        if (distanceEnKmEntreCoordonnees(latitudeDestination, longitudeDestination, latitudeJoueur, longitudeJoueur) <= 1){
            return true;
        }else{
            return false;
        }
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


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void verifierPremiereUtilisation() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            ouvrirDialogue();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        recupererPositionJoueur();
    }


    public LatLng getRandomLocation(LatLng point, int rayon) {

        List<LatLng> pointsAleatoires = new ArrayList<>();
        List<Float> distancesAleatoires = new ArrayList<>();
        Location maLocalisation = new Location("");
        maLocalisation.setLatitude(point.latitude);
        maLocalisation.setLongitude(point.longitude);

        // Génère 10 points aléatoires
        for(int i = 0; i<10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convertit le rayon des mètres en degres
            double rayonEnDegres = rayon / 111000f;
            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = rayonEnDegres * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            pointsAleatoires.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            distancesAleatoires.add(l1.distanceTo(maLocalisation));
        }

        // Sélectionne le point le plus proche du centre
        int indexOfNearestPointToCentre = distancesAleatoires.indexOf(Collections.min(distancesAleatoires));
        LatLng positionDestination= pointsAleatoires.get(indexOfNearestPointToCentre);
        this.latitudeDestination = positionDestination.latitude;
        this.longitudeDestination = positionDestination.longitude;

        return positionDestination;
    }


    public int genererAugmentationScore(int valeurDepart, int valeurMin){
        int valeurAugmentation = 0;
        long intervalle = System.currentTimeMillis() - tempsDebutPartie;
        long intervaleEnMinute = TimeUnit.MILLISECONDS.toMinutes(intervalle);

        if(intervaleEnMinute >= 5){
            int retraitAuScore = (int) intervaleEnMinute / 5 * 50;
            valeurAugmentation = valeurDepart - retraitAuScore;
        }
        if(valeurAugmentation < valeurMin){
            valeurAugmentation = valeurMin;
        }

        return valeurAugmentation;
    }
}
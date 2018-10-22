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
                        MarkerOptions options = new MarkerOptions().position(positionJoueur).title("Position joueur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marqueurJoueur = mMap.addMarker(options);
                    } else {
                        marqueurJoueur.setPosition(positionJoueur);
                    }
                }

                if (cestGagne()){
                    int valeurScore=genereraugmentationScore(500,50);
                    scoreDAO.modifierScore(new Score(valeurScore,preferences.getInt("id",0)));
                    stopLocationUpdates();
                    activeAlarme();
                }
            }
        };

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeEventManager();
        mShakeDetector.setOnShakeListener(new ShakeEventManager.OnShakeListener() {

            @Override
            public void onShake(int count) {
                Toast.makeText(VueJeu.this, "Nouvelle destination generer ", Toast.LENGTH_LONG).show();
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
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }
    @SuppressLint("MissingPermission")
    public void recupererPossitionJoueur() {



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
    
    @SuppressLint("MissingPermission")
    public void recupererPossitionDestination() {

        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        double latitudeJoueur = location.getLatitude();
                        double longitudeJoueur = location.getLongitude();
                        LatLng possitionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);
                        //Toast.makeText(VueJeu.this, "latitude" + latitudeJoueur + " longitude" + longitudeJoueur, Toast.LENGTH_LONG).show();

                        LatLng positionDestination= getRandomLocation(possitionJoueur,500);

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
        System.out.println("latitude "+latitudeDestination+" longitude "+ longitudeDestination);
        if (distanceInKmBetweenEarthCoordinates(latitudeDestination, longitudeDestination, latitudeJoueur, longitudeJoueur) <= 0.1){
            System.out.println("Gagné");
            return true;
        }else{
            System.out.println("Marche encore");
            return false;
        }
    }

    /*public double genererLatitudeNouvelleDestination(){
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
    }*/



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
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
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

        recupererPossitionJoueur();
    }

    public LatLng getRandomLocation(LatLng point, int radius) {

        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        LatLng positionDestination= randomPoints.get(indexOfNearestPointToCentre);
        this.latitudeDestination = positionDestination.latitude;
        this.longitudeDestination = positionDestination.longitude;
        return positionDestination;
    }
    public int genereraugmentationScore(int valeurDepart,int valeurMin){
        int valeurAugmentation=0;
        System.out.println("heure de depart de la partie "+ tempsDebutPartie);
        System.out.println(System.currentTimeMillis());
        long intervalle=System.currentTimeMillis()-tempsDebutPartie;
        long intervaleEnMinute=TimeUnit.MILLISECONDS.toMinutes(intervalle);
        System.out.println("temps ecouler en minute "+ intervaleEnMinute);
        if(intervaleEnMinute>=5){
            int retraitAuScore=(int)intervaleEnMinute/5*50;
            valeurAugmentation=valeurDepart-retraitAuScore;
        }
        if(valeurAugmentation<valeurMin){
            valeurAugmentation=valeurMin;
        }
        return valeurAugmentation;
    }
}
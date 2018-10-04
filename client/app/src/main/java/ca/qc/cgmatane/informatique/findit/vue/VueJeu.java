package ca.qc.cgmatane.informatique.findit.vue;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

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
    protected Intent intentionNaviguerScore;
    protected Intent intentionNaviguerGalerie;
    protected Intent intentionNaviguerAlarme;

    protected ScoreDAO scoreDAO;

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

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    latitudeJoueur = location.getLatitude();
                    longitudeJoueur = location.getLongitude();
                    LatLng possitionJoueur = new LatLng(latitudeJoueur, longitudeJoueur);
                    Toast.makeText(VueJeu.this, "latitude" + latitudeJoueur + " longitude" + longitudeJoueur, Toast.LENGTH_LONG).show();

                    if (marqueurJoueur == null) {
                        MarkerOptions options = new MarkerOptions().position(possitionJoueur).title("position joueur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marqueurJoueur = mMap.addMarker(options);
                    } else {
                        marqueurJoueur.setPosition(possitionJoueur);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(possitionJoueur));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
                }

                if (cestGagne()){
                    scoreDAO.ajouterScore(new Score(1, 1000));
                    stopLocationUpdates();
                    activeAlarme();
                }
            }

            ;
        };
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
        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
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
                        Toast.makeText(VueJeu.this, "latitude" + latitudeJoueur + " longitude" + longitudeJoueur, Toast.LENGTH_LONG).show();

                        if (marqueurJoueur == null) {
                            MarkerOptions options = new MarkerOptions().position(possitionJoueur).title("position joueur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
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

        this.latitudeDestination = 48.840218;
        this.longitudeDestination = -67.498787;
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
        if (distanceInKmBetweenEarthCoordinates(latitudeDestination, longitudeDestination, latitudeJoueur, longitudeJoueur) <= 1){
            System.out.println("Gagné");
            return true;
        }else{
            System.out.println("Marche encore");
            return false;
        }
    }
}
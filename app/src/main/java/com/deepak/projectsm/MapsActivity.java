package com.deepak.projectsm;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.location.LocationListener;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private LocationManager locationManager;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.stylemap));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        this.mMap = googleMap;
        if(this.mMap != null){
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
            addMarker();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // Toast.makeText(MainActivity.this, "Lat" + location.getLatitude() + " Lon" + location.getLongitude(), Toast.LENGTH_SHORT).show();
        this.location = location;
        Log.e(TAG,"Lat" + location.getLatitude() + " Lon" + location.getLongitude());
        if(this.mMap != null && this.location !=null){
            addMarker();
        }else{
            Log.e(TAG,"mMap and location are null");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            Log.e("onResume","ifWorked");
            StartLocationAlert startLocationAlert = new StartLocationAlert(this);
            requestLocationUpdates();

        }else{

            requestLocationUpdates();
        }


    }

    public void requestLocationUpdates() {
        // Log.e(TAG,"requestLocationUpdates");
        if (locationManager != null) {
            // LocationListener mLocationListener = new MainActivity();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                //                                         );
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public void addMarker(){
        if(this.location != null){

            LatLng current = new LatLng(this.location.getLatitude(), this.location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(current).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        }else{
            Toast.makeText(MapsActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.deepak.projectsm;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class StartLocationAlert implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private Activity context;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG = "StartLocationAlert";
    private GoogleApiClient googleApiClient;


    public StartLocationAlert(Activity context) {
        this.context = context;
        googleApiClient = getInstance();
        if(googleApiClient != null){
            googleApiClient.connect();

            if(googleApiClient.isConnected()){
                if(googleApiClient.hasConnectedApi(LocationServices.API)){
                    Toast.makeText(context , "hasConnectedApi True", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context , "googleApiClient.hasConnectedApi(LocationServices.API) => False", Toast.LENGTH_SHORT).show();
                }
            }else  if(googleApiClient.isConnecting()){
                Toast.makeText(context , "Please wait", Toast.LENGTH_SHORT).show();
            }
            settingsRequest();
        }
    }

    private GoogleApiClient getInstance(){
        return new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private void settingsRequest()
    {
        Log.e("settingsRequest","Comes");

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // Log.e("Application","Button Clicked");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        // Log.e("Application","Button Clicked1");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            Log.e("Applicationsett",e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //Log.e("Application","Button Clicked2");
                        Toast.makeText(context, "Location is Enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        MapsActivity mapsActivity = new MapsActivity();
        mapsActivity.requestLocationUpdates();
        Toast.makeText(context , "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
        Log.e(TAG,""+connectionResult.toString()+" Error Message "+ connectionResult.hashCode());
    }

}
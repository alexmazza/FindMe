package com.prismio.findme;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    @Inject
    LocationClientFactory mLocationClientFactory;

    @Inject
    LocationRequestFactory mLocationRequestFactory;

    @Inject
    Bus mBus;

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (serviceConnected()) {
            mLocationClient = mLocationClientFactory.create(this, this);
            mLocationRequest = mLocationRequestFactory.create();

            FindMeMapFragment mapFragment = (FindMeMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment == null) {
                mapFragment = new FindMeMapFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.map, mapFragment);
                ft.commit();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBus.unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocationClient.connect();
    }

    @Override
    protected void onStop() {

        if (mLocationClient.isConnected()) {
            stopLocationUpdates();
        }
        mLocationClient.disconnect();

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {


        Location location = mLocationClient.getLastLocation();
        if (location != null) {
            mBus.post(new LocationChangedEvent(new LatLng(location.getLatitude(), location.getLongitude()), "You are here!", true));
        }

        startLocationUpdates();

    }

    @Subscribe
    public void OnMapClicked(MapClickedEvent event){
        stopLocationUpdates();
    }

    @Override
    public void onDisconnected() {
        //Do something on disconnect
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Do something if the connection to Google Play Services fails
    }

    @Override
    public void onLocationChanged(Location location) {
        mBus.post(new LocationChangedEvent(new LatLng(location.getLatitude(), location.getLongitude()), "You are here!", true));
    }

    private void startLocationUpdates() {
        Log.d("FindMe", "Starting location updates...");
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        Log.d("FindMe", "Stopping location updates");
        mLocationClient.removeLocationUpdates(this);
    }
}

package com.prismio.findme;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class LocationRequestFactory {

    private static final int UPDATE_INTERVAL = 30000;
    private static final int FAST_INTERVAL_CEILING = 5000;

    @Inject
    public LocationRequestFactory(){}

    public LocationRequest create(){

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING);

        return locationRequest;

    }
}

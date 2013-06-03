package com.prismio.findme;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class LocationClientFactory {

    @Inject @ForApplication Context mContext;

    public LocationClient create(GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks,
                                 GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener){
        return new LocationClient(mContext, connectionCallbacks, onConnectionFailedListener);
    }

}

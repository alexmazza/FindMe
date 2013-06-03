package com.prismio.findme;

import com.google.android.gms.maps.model.LatLng;


public class LocationChangedEvent {

    public final LatLng latLng;
    public final String title;
    public final boolean shouldZoom;

    public LocationChangedEvent(LatLng latLng, String title, boolean shouldZoom){
        this.latLng = latLng;
        this.title = title;
        this.shouldZoom = shouldZoom;
    }
}

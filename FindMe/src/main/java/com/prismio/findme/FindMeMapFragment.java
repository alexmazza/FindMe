package com.prismio.findme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prismio.findme.networking.FindMeApi;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;


public class FindMeMapFragment extends SupportMapFragment implements GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;

    @Inject
    Bus mBus;

    @Inject
    FindMeApi mApi;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((FindMeApp) getActivity().getApplication()).inject(this);

        mMap = getMap();
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public void onResume() {
        super.onResume();

        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mBus.unregister(this);
    }

    @Subscribe
    public void onLocationChanged(LocationChangedEvent event){
        initMap(event.latLng, event.shouldZoom);

        addMarker(event.latLng, event.title);
    }

    private void addMarker(LatLng latLng, String title){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(title)).showInfoWindow();
    }

    private void initMap(LatLng latLng, boolean shouldZoom){

        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(cu);
        if(shouldZoom)
            mMap.animateCamera(zoom);
    }



    @Override
    public void onMapClick(LatLng latLng) {
        //If the map is clicked, notifies the activity to stop location updates and
        //notifies itself to add a new marker
        mBus.post(new MapClickedEvent());
        mBus.post(new LocationChangedEvent(latLng, "You will be here, eventually!", false));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String longUrl = "https://maps.google.it/?q=" + marker.getPosition().latitude + "," +
                marker.getPosition().longitude;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    sendIntent(jsonObject.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Do something in case of response error
            }
        };

        JSONObject requestObject = createJSONObject(longUrl);
        mApi.getShortUrl(requestObject, listener, errorListener);
        return false;
    }

    private JSONObject createJSONObject(String url){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
            return jsonObject;
        } catch (JSONException e) {
           Log.d("FindMe", e.toString());
        }
        return null;
    }

    private void sendIntent(String message){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "FindMe - Hey I'm here!");
        intent.putExtra(Intent.EXTRA_TEXT, "Meet me here! " + message);
        startActivity(Intent.createChooser(intent, "Share your location"));
    }
}

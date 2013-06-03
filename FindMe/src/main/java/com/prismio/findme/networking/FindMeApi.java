package com.prismio.findme.networking;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alex on 03/06/2013.
 */
@Singleton
public class FindMeApi {

    private static final String API_URL = "http://s.prismio.com";

    private final RequestQueue mQueue;

    @Inject
    public FindMeApi(RequestQueue queue){
        mQueue = queue;
    }

    public Request<?> getShortUrl(JSONObject requestObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        return mQueue.add(new JsonObjectRequest(API_URL, requestObject, listener, errorListener));
    }


}

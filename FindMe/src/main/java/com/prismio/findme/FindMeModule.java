package com.prismio.findme;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                FindMeMapFragment.class
        },
        complete = false
)
public class FindMeModule {


    private final Bus mBus = new Bus();


    @Provides @Singleton Bus provideBus(){
        return mBus;
    }

    @Provides @Singleton RequestQueue provideRequestQueue(@ForApplication Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        return queue;
    }



}

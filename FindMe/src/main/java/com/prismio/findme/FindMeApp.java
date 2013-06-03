package com.prismio.findme;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;


public class FindMeApp extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = ObjectGraph.create(getModules().toArray());
    }

    public ObjectGraph getObjectGraph(){
        return mObjectGraph;
    }

    protected List<Object> getModules(){
        return Arrays.<Object>asList(
          new AndroidModule(this),
          new FindMeModule()
        );
    }

    public void inject(Object object){
        mObjectGraph.inject(object);
    }
}

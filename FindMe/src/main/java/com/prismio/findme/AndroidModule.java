package com.prismio.findme;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class AndroidModule {

    private final FindMeApp mFindMeApp;



    public AndroidModule(FindMeApp findMeApp){
        mFindMeApp = findMeApp;
    }

    @Provides @Singleton @ForApplication Context provideApplicationContext(){
        return mFindMeApp;
    }

}

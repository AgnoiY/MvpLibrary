package com.cjy.mvplibrary;

import android.content.Context;

public class  MvpLibrary {

    private static Context mAppContext;

    public static void init (Context context){
        if (context != null){
           mAppContext = context;
        }
    }
}

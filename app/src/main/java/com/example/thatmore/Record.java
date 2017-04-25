package com.example.thatmore;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by в on 17.12.2016.
 */

public class Record {
    public static final String STORAGE_NAME = "Record";

    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    public static void init(Context cntxt){
        context = cntxt;
    }

    private static void init(){
        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static void setRecord( String name, int value ){
        if( settings == null ){
            init();
        }
        editor.putInt( name, value );
        editor.commit();
    }

    public static int getRecord ( String name ){
        if ( settings == null ){
            init();
        }
        return settings.getInt( name, 0 );
    }
}
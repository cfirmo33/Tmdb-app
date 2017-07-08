package com.cfirmo33.moviesapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cfirmo33.moviesapp.model.Genre;

import java.util.List;

/**
 * Created by carlos on 05/07/17.
 */

public class MainApplication extends Application {
    private static MainApplication singleton;

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_IMAGE_URL = "https://image.tmdb.org/t/p/w154/";
    public static final String API_IMAGE_POSTER_URL = "https://image.tmdb.org/t/p/w500/";

    public static MainApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public void setPreference(String key, String content) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public String getPreference(String key, String defValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getString(key, defValue);
    }

    public void salveGenre(List<Genre> genres) {
        if(genres != null){
            for(Genre genre : genres){
                setPreference(genre.getId().toString(), genre.getName());
            }
        }

    }
}

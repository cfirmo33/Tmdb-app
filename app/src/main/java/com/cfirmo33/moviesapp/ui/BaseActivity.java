package com.cfirmo33.moviesapp.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by carlos on 06/07/17.
 */

public class BaseActivity extends AppCompatActivity {
    public Context getContext() {
        return this;
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

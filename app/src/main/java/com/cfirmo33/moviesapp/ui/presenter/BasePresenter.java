package com.cfirmo33.moviesapp.ui.presenter;

import android.content.Context;

public class BasePresenter {
    private Context context;

    public BasePresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
package com.cfirmo33.moviesapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cfirmo33.moviesapp.MainApplication;

import java.util.List;

/**
 * Created by carlos on 07/07/17.
 */

public abstract class Utils {

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    public static String getGenres(List<Integer> genres) {
        String sGenre = "";
        if (genres!= null) {
            for (Integer idGenre : genres) {
                if (sGenre.length() > 1) {
                    sGenre = sGenre.concat(", ").concat(MainApplication.getInstance()
                            .getPreference(idGenre.toString(), ""));
                } else {
                    sGenre = sGenre.concat(MainApplication.getInstance()
                            .getPreference(idGenre.toString(), ""));
                }
            }
        }
        return sGenre;
    }
}

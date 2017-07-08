package com.cfirmo33.moviesapp.ui.presenter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.cfirmo33.moviesapp.MainApplication;
import com.cfirmo33.moviesapp.R;
import com.cfirmo33.moviesapp.api.MovieService;
import com.cfirmo33.moviesapp.api.RetrofitUtils;
import com.cfirmo33.moviesapp.model.Movie;
import com.cfirmo33.moviesapp.model.MovieResultsPage;
import com.cfirmo33.moviesapp.ui.contract.MainContract;
import com.cfirmo33.moviesapp.ui.contract.MovieDetailContract;
import com.cfirmo33.moviesapp.utils.Constant;
import com.cfirmo33.moviesapp.utils.PaginationScrollListener;
import com.cfirmo33.moviesapp.utils.Utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailPresenter extends BasePresenter implements MovieDetailContract.IPresenter {
    private final MovieDetailContract.IView pcView;
    private Movie mMovie;

    public MovieDetailPresenter(Context context, MovieDetailContract.IView pcView) {
        super(context);
        this.pcView = pcView;
        this.pcView.setPresenter(this);
    }

    @Override
    public void onStart() {
        pcView.loadTitle(mMovie.getTitle());
        pcView.loadOverview(mMovie.getOverview());
        pcView.loadGenre(Utils.getGenres(mMovie.getGenres()));
        pcView.loadReleaseDate(mMovie.getReleaseDate());

        pcView.loadImagePoster(MainApplication.API_IMAGE_POSTER_URL + mMovie.getPosterPath());
        pcView.loadImageBackdrop(MainApplication.API_IMAGE_POSTER_URL + mMovie.getBackdropPath());
    }

    @Override
    public void onDestroy() {
    }


    @Override
    public void loadModel(Movie model) {
        this.mMovie = model;
    }
}
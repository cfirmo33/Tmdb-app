package com.cfirmo33.moviesapp.ui.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfirmo33.moviesapp.MainApplication;
import com.cfirmo33.moviesapp.R;
import com.cfirmo33.moviesapp.ui.BaseActivity;
import com.cfirmo33.moviesapp.ui.contract.MovieDetailContract;
import com.cfirmo33.moviesapp.ui.presenter.MovieDetailPresenter;
import com.cfirmo33.moviesapp.utils.Constant;
import com.cfirmo33.moviesapp.utils.Utils;

public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.IView {

    ImageView backdrop;
    ImageView poster;
    TextView overview, title, genre, releaseDate;
    CollapsingToolbarLayout toolbarLayout;

    private MovieDetailContract.IPresenter pcPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        new MovieDetailPresenter(getContext(), this);

        if (getIntent().hasExtra(Constant.EXTRA_MOVIE)) {
            getPresenter().loadModel(getIntent().getParcelableExtra(Constant.EXTRA_MOVIE));
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        backdrop = (ImageView) findViewById(R.id.backdrop);
        title = (TextView) findViewById(R.id.movie_title);
        overview = (TextView) findViewById(R.id.movie_description);
        genre = (TextView) findViewById(R.id.movie_genre);
        releaseDate = (TextView) findViewById(R.id.movie_release);
        poster = (ImageView) findViewById(R.id.movie_poster);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    public void loadTitle(String mMovieTitle) {
        toolbarLayout.setTitle(mMovieTitle);
        title.setText(mMovieTitle);
    }

    public void loadOverview(String mMovieOverview){
        overview.setText(mMovieOverview);
    }

    public void loadGenre(String mMovieGenre){
        genre.setText(mMovieGenre);
    }

    public void loadReleaseDate(String mMovieReleaseDate){
        releaseDate.setText(mMovieReleaseDate);
    }

    public void loadImagePoster(String imagePosterUrl){
        Utils.loadImage(getContext(), imagePosterUrl, poster);
    }

    public void loadImageBackdrop(String imageBackdropUrl){
        Utils.loadImage(getContext(), imageBackdropUrl, backdrop);
    }

    @Override
    public void setPresenter(MovieDetailContract.IPresenter presenter) {
        pcPresenter = presenter;
    }

    @Override
    public MovieDetailContract.IPresenter getPresenter() {
        return pcPresenter;
    }
}
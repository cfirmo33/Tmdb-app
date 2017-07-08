package com.cfirmo33.moviesapp.ui.contract;

import com.cfirmo33.moviesapp.model.Movie;
import com.cfirmo33.moviesapp.ui.presenter.IBasePresenter;

public class MovieDetailContract {

    public interface IView extends IBaseView<IPresenter> {
        void loadTitle(String mMovieTitle);
        void loadOverview(String mMovieOverview);
        void loadGenre(String mMovieGenre);
        void loadReleaseDate(String mMovieReleaseDate);

        void loadImagePoster(String imagePosterUrl);
        void loadImageBackdrop(String imageBackdropUrl);
    }

    public interface IPresenter extends IBasePresenter {
        void loadModel(Movie model);
    }
}
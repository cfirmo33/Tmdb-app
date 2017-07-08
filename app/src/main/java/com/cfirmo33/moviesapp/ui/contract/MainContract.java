package com.cfirmo33.moviesapp.ui.contract;

import com.cfirmo33.moviesapp.model.MovieResultsPage;
import com.cfirmo33.moviesapp.ui.presenter.IBasePresenter;

public class MainContract {

    public interface IView extends IBaseView<IPresenter> {
        void hideErrorView();

        void showErrorView(Throwable throwable);
        void populateFirstMovieList(MovieResultsPage movieResultsPage);

        void populateNextMovieList(MovieResultsPage movieResultsPage);
        void showNextPageErrorView(Throwable throwable);
    }

    public interface IPresenter extends IBasePresenter {
        void loadGenres();
        void loadFirstMoviesPage(int firstPage);
        void loadNextMoviesPage(int currentPage);
    }
}
package com.cfirmo33.moviesapp.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.cfirmo33.moviesapp.MainApplication;
import com.cfirmo33.moviesapp.R;
import com.cfirmo33.moviesapp.api.MovieService;
import com.cfirmo33.moviesapp.api.RetrofitUtils;
import com.cfirmo33.moviesapp.model.Genre;
import com.cfirmo33.moviesapp.model.GenreResults;
import com.cfirmo33.moviesapp.model.MovieResultsPage;
import com.cfirmo33.moviesapp.ui.contract.MainContract;
import com.cfirmo33.moviesapp.utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter implements MainContract.IPresenter {
    private final String TAG = "MainActivity";
    private final MainContract.IView pcView;
    private MovieService movieService;

    public MainPresenter(Context context, MainContract.IView pcView) {
        super(context);
        this.pcView = pcView;
        this.pcView.setPresenter(this);

        movieService = RetrofitUtils.getRetrofitClient().create(MovieService.class);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     *
     * @param currentPage
     */
    private Observable<MovieResultsPage> callUpcomingMovies(int currentPage) {
        return movieService.upcoming(getContext().getString(R.string.my_api_key), "en_US", currentPage);
    }

    /**
     * Performs a Retrofit call to the genre in API.
     */
    private Observable<GenreResults> callGenres() {
        return movieService.genres(getContext().getString(R.string.my_api_key));
    }

    public void loadGenres() {
        callGenres().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(genres -> MainApplication.getInstance().salveGenre(genres.getGenres()),
                        throwable -> Log.e(TAG, "Erro to load genre"));
    }

    public void loadFirstMoviesPage(int firstPage) {
        Log.d(TAG, "loadFirstPage: ");
        // To ensure list is visible when retry button in error view is clicked
        pcView.hideErrorView();

        callUpcomingMovies(firstPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(pcView::populateFirstMovieList,
                        pcView::showErrorView);
    }

    @Override
    public void loadNextMoviesPage(int currentPage) {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callUpcomingMovies(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(pcView::populateNextMovieList,
                        pcView::showNextPageErrorView);
    }
}
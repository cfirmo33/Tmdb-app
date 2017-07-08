package com.cfirmo33.moviesapp.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cfirmo33.moviesapp.R;
import com.cfirmo33.moviesapp.adapter.PaginationAdapter;
import com.cfirmo33.moviesapp.model.Movie;
import com.cfirmo33.moviesapp.model.MovieResultsPage;
import com.cfirmo33.moviesapp.ui.BaseActivity;
import com.cfirmo33.moviesapp.ui.contract.MainContract;
import com.cfirmo33.moviesapp.ui.presenter.MainPresenter;
import com.cfirmo33.moviesapp.utils.Constant;
import com.cfirmo33.moviesapp.utils.PaginationAdapterCallback;
import com.cfirmo33.moviesapp.utils.PaginationScrollListener;

import java.util.concurrent.TimeoutException;

public class MainActivity extends BaseActivity implements PaginationAdapterCallback, MainContract.IView {
    LinearLayoutManager linearLayoutManager;
    PaginationAdapter adapter;

    RecyclerView rvMovies;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 15;
    private int currentPage = PAGE_START;

    private MainContract.IPresenter pcPresenter;

    /**
     * In next version, make a implementation with a store (NYtimes Strore "https://github.com/NYTimes/Store")
     * to remove the page limitation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MainPresenter(getContext(), this);
        setContentView(R.layout.activity_main);

        rvMovies = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        adapter = new PaginationAdapter(this)
                .setOnClickListener(this::openMovieDetail);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(linearLayoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(adapter);
        rvMovies.addOnScrollListener(getPaginationScrollListener(linearLayoutManager));

        getPresenter().loadGenres();
        getPresenter().loadFirstMoviesPage(PAGE_START);

        btnRetry.setOnClickListener(view -> getPresenter().loadFirstMoviesPage(PAGE_START));
    }

    private void openMovieDetail(int position, Movie movie) {
        if (movie != null) {
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra(Constant.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    public void populateFirstMovieList(MovieResultsPage movieResultsPage) {
        hideErrorView();
        progressBar.setVisibility(View.GONE);
        adapter.addAll(movieResultsPage.getResults());

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    public void populateNextMovieList(MovieResultsPage movieResultsPage) {
        adapter.removeLoadingFooter();
        isLoading = false;
        adapter.addAll(movieResultsPage.getResults());

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    public void showNextPageErrorView(Throwable throwable) {
        throwable.printStackTrace();
        adapter.showRetry(true, fetchErrorMessage(throwable));
    }

    @Override
    public void retryPageLoad() {
        getPresenter().loadNextMoviesPage(currentPage);
    }

    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     */
    public void showErrorView(Throwable throwable) {
        throwable.printStackTrace();
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    public void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private PaginationScrollListener getPaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                getPresenter().loadNextMoviesPage(currentPage);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }

    @Override
    public void setPresenter(MainContract.IPresenter presenter) {
        pcPresenter = presenter;
    }

    @Override
    public MainContract.IPresenter getPresenter() {
        return pcPresenter;
    }
}

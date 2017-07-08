package com.cfirmo33.moviesapp.ui.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.cfirmo33.moviesapp.ui.contract.SearchContract;
import com.cfirmo33.moviesapp.ui.presenter.SearchPresenter;
import com.cfirmo33.moviesapp.utils.Constant;
import com.cfirmo33.moviesapp.utils.PaginationAdapterCallback;
import com.cfirmo33.moviesapp.utils.PaginationScrollListener;

import java.util.concurrent.TimeoutException;

public class SearchActivity extends BaseActivity implements PaginationAdapterCallback, SearchContract.IView {
    Toolbar mToolbar;
    SearchView mSearchView;

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
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;
    private String searchTerm = "";

    private SearchContract.IPresenter pcPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new SearchPresenter(getContext(), this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (SearchView) findViewById(R.id.search_view);

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

        btnRetry.setOnClickListener(view -> getPresenter().findMovies(searchTerm, PAGE_START));
        setupActionBar();
        setupSearchView();
    }

    private void openMovieDetail(int position, Movie movie) {
        if (movie != null) {
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra(Constant.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTerm = newText;
                if (searchTerm.length() >= 2) {
                    getPresenter().findMovies(searchTerm, currentPage);
                    return true;
                }
                return false;
            }
        });

        mSearchView.setOnCloseListener(() -> {
            finish();
            return true;
        });
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private PaginationScrollListener getPaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                getPresenter().findNextMoviesPage(searchTerm, currentPage);
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
    public void setPresenter(SearchContract.IPresenter presenter) {
        pcPresenter = presenter;
    }

    @Override
    public SearchContract.IPresenter getPresenter() {
        return pcPresenter;
    }

    @Override
    public void retryPageLoad() {
        getPresenter().findNextMoviesPage(searchTerm, currentPage);
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

    public void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void populateFirstMovieList(MovieResultsPage movieResultsPage) {
        hideErrorView();
        progressBar.setVisibility(View.GONE);
        adapter.setList(movieResultsPage.getResults());
        adapter.notifyDataSetChanged();

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

    @Override
    public void showNextPageErrorView(Throwable throwable) {
        throwable.printStackTrace();
        adapter.showRetry(true, fetchErrorMessage(throwable));
    }
}
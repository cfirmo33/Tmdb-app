package com.cfirmo33.moviesapp.api;


import com.cfirmo33.moviesapp.model.Genre;
import com.cfirmo33.moviesapp.model.GenreResults;
import com.cfirmo33.moviesapp.model.Movie;
import com.cfirmo33.moviesapp.model.MovieResultsPage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by carlos on 05/07/17.
 */

public interface MovieService {

    /**
     * Get the basic movie information for a specific movie id.
     *
     * @param movieId A Movie TMDb id.
     */
    @GET("movie/{movie_id}")
    Observable<Movie> summary(
            @Query("api_key") String apiKey,
            @Path("movie_id") int movieId
    );

    /**
     * Search for movies by title.
     *
     * @param query CGI escaped string
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     * @param includeAdult <em>Optional.</em> Toggle the inclusion of adult titles. Expected value is: true or false
     * @param year <em>Optional.</em> Filter the results release dates to matches that include this value.
     * @param primaryReleaseYear <em>Optional.</em> Filter the results so that only the primary release dates have this
     * value.
     * @param searchType <em>Optional.</em> By default, the search type is 'phrase'. This is almost guaranteed the
     * option you will want. It's a great all purpose search type and by far the most tuned for every day querying. For
     * those wanting more of an "autocomplete" type search, set this option to 'ngram'.
     */
    @GET("search/movie")
    Observable<MovieResultsPage> movieSearch(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("include_adult") Boolean includeAdult,
            @Query("year") Integer year,
            @Query("primary_release_year") Integer primaryReleaseYear,
            @Query("search_type") String searchType
    );

    @GET("search/movie")
    Observable<MovieResultsPage> movieSearchSimple(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") Integer page
    );

    /**
     * Get the list of genres.
     *
     */
    @GET("genre/movie/list")
    Observable<GenreResults> genres(@Query("api_key") String apiKey);

    /**
     * Get the list of upcoming movies. This list refreshes every day. The maximum number of items this list will
     * include is 100.
     *
     * @param page <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("movie/upcoming")
    Observable<MovieResultsPage> upcoming(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") Integer page);

}

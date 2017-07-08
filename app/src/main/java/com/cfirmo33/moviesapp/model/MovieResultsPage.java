package com.cfirmo33.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieResultsPage extends BaseResultsPage {

    @SerializedName("results")
    @Expose
    public List<Movie> results;

    public List<Movie> getResults() {
        if(results == null){
            results = new ArrayList<>();
        }
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}


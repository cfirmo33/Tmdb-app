package com.cfirmo33.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GenreResults {

    @SerializedName("genres")
    @Expose
    public List<Genre> genres;

    public List<Genre> getGenres() {
        if(genres == null){
            genres = new ArrayList<>();
        }
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}


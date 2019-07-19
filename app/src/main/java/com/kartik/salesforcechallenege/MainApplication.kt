/*
 * Created by Kartik Kumar Gujarati on 7/19/19 7:51 AM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege

import android.app.Application
import com.kartik.salesforcechallenege.model.Movies

class MainApplication : Application() {

    private lateinit var lastSearchedMovie: String
    private lateinit var lastMovieResult: List<Movies.Movie>

    override fun onCreate() {
        super.onCreate()
        lastSearchedMovie = ""
        lastMovieResult = ArrayList()
    }

    fun getLastSearchedMovie() : String {
        return lastSearchedMovie
    }

    fun setLastSearchedMovie(movieName: String) {
        lastSearchedMovie = movieName
    }

    fun getLastMovieResult() : List<Movies.Movie> {
        return lastMovieResult
    }

    fun setLastMovieResult(movies: List<Movies.Movie>) {
        lastMovieResult = movies
    }
}
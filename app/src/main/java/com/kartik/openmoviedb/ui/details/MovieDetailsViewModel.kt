/*
 * Created by Kartik Kumar Gujarati on 7/19/19 1:58 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel()  {
    private val _movieDetails = MutableLiveData<Resource<Movies.MovieDetails>>()
    val movieDetails : LiveData<Resource<Movies.MovieDetails>>
        get() = _movieDetails

    fun getMovieDetails(movieId: String) {
        repository.getMovieDetails(movieId, _movieDetails)
    }
}

class MovieDetailsViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(repository) as T
    }
}
/*
 * Created by Kartik Kumar Gujarati on 7/19/19 1:58 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.details

import androidx.lifecycle.*
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel()  {
    private val _movieDetails = MutableLiveData<Resource<Movies.MovieDetails>>()
    val movieDetails : LiveData<Resource<Movies.MovieDetails>>
        get() = _movieDetails

    fun getMovieDetails(movieId: String) {
        _movieDetails.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetails.postValue(repository.getMovieDetails(movieId))
        }
    }
}

class MovieDetailsViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(repository) as T
    }
}
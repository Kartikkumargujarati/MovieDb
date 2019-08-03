/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _favMovieList = MutableLiveData<Resource<List<Movies.Movie>>>()
    val favMovieList : LiveData<Resource<List<Movies.Movie>>>
        get() = _favMovieList
    init {
        repository.getFavoriteMovies(_favMovieList)
    }

    fun unFavoriteAMovie(movie: Movies.Movie) {
        repository.unFavoriteAMovieFromFavorite(movie, _favMovieList)
    }
}

class FavoritesViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}
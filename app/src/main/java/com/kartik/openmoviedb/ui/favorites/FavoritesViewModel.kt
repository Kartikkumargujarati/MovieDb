/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.favorites

import androidx.lifecycle.*
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    val favMovieList : LiveData<Resource<List<Movies.Movie>>> = repository.getFavoriteMovies()

    private val _unFavMovie = MutableLiveData<Movies.Movie>()
    val unFavMovie: LiveData<Resource<Movies.Movie>> = _unFavMovie.switchMap { movie ->
        repository.unFavoriteAMovieFromFavorite(movie)
    }

    fun unFavoriteAMovie(movie: Movies.Movie) {
        _unFavMovie.value = movie
    }
}

class FavoritesViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}
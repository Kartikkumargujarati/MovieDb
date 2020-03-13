/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.favorites

import androidx.lifecycle.*
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _favMovieList = MutableLiveData<Resource<List<Movies.Movie>>>()
    val favMovieList : LiveData<Resource<List<Movies.Movie>>>
        get() = _favMovieList
    init {
        _favMovieList.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _favMovieList.postValue(repository.getFavoriteMovies())
        }
    }

    private val _unFavMovie = MutableLiveData<Resource<Movies.Movie>>()
    val unFavMovie: LiveData<Resource<Movies.Movie>>
        get() = _unFavMovie

    fun unFavoriteAMovie(movie: Movies.Movie) {
        _unFavMovie.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _unFavMovie.postValue(repository.unFavoriteAMovieFromFavorite(movie))
        }
    }
}

class FavoritesViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}
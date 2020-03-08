/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.search

import androidx.lifecycle.*
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieList = MutableLiveData<Resource<List<Movies.Movie>>>()
    val movieList : LiveData<Resource<List<Movies.Movie>>>
        get() = _movieList

    private val _favMovie = MutableLiveData<Resource<Movies.Movie>>()
    val favMovie : LiveData<Resource<Movies.Movie>>
        get() = _favMovie

    fun searchMovie(searchKey: String, page: Int = 1) {
        _movieList.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _movieList.postValue(repository.getMoviesFromSearch(searchKey, page))
        }
    }

    fun favoriteAMovie(movie: Movies.Movie) {
        _favMovie.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            _favMovie.postValue(repository.favoriteAMovie(movie))
        }
    }

}

class SearchViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}
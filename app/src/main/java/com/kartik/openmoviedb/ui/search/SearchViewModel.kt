/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.search

import androidx.lifecycle.*
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.model.Movies

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private var pageNumber = 1
    private val searchKey = MutableLiveData<String>()

    val movieList: LiveData<Resource<List<Movies.Movie>>>  = searchKey.switchMap { key ->
        repository.getMoviesFromSearch(key, pageNumber)
    }

    private val _favMovie = MutableLiveData<Movies.Movie>()
    val favMovie : LiveData<Resource<Movies.Movie>> = _favMovie.switchMap { movie ->
        repository.favoriteAMovie(movie)
    }

    fun favoriteAMovie(movie: Movies.Movie) {
        _favMovie.value = movie
    }

    fun searchMovie(searchKey: String, page: Int = 1) {
        this.searchKey.value = searchKey
        pageNumber = page
    }
}

class SearchViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}
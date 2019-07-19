/*
 * Created by Kartik Kumar Gujarati on 7/18/19 6:33 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartik.salesforcechallenege.data.MovieRepository
import com.kartik.salesforcechallenege.data.Resource
import com.kartik.salesforcechallenege.model.Movies

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieList = MutableLiveData<Resource<List<Movies.Movie>>>()
    val movieList : LiveData<Resource<List<Movies.Movie>>>
        get() = _movieList

    fun searchMovie(searchKey: String, page: Int = 1) {
        repository.getMoviesFromSearch(searchKey, page, _movieList)
    }
}

class SearchViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}
/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:48 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.data

import androidx.lifecycle.MutableLiveData
import com.kartik.salesforcechallenege.BuildConfig
import com.kartik.salesforcechallenege.data.local.MovieDao
import com.kartik.salesforcechallenege.data.remote.MovieRemoteServiceImpl
import com.kartik.salesforcechallenege.model.Movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MovieRepository(private val movieDao: MovieDao, private val remoteService: MovieRemoteServiceImpl) {

    fun getMoviesFromSearch(searchKey: String, pageNumber: Int, result: MutableLiveData<Resource<List<Movies.Movie>>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = remoteService.getRemoteService().searchMovies(searchKey, pageNumber.toString(), BuildConfig.OMDB_API_KEY)
                if (response.isSuccessful && response.code() == 200) {
                    returnData(response.body()!!, result, Status.SUCCESS)
                } else {
                    // handle error
                    returnData(result = result, status = Status.ERROR)
                }
            } catch (exception: Exception) {
                // handle error
                returnData(result = result, status = Status.ERROR)
            }
        }
    }

    fun getFavoriteMovies(result: MutableLiveData<Resource<List<Movies.Movie>>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movies = movieDao.getAllFavoriteMovies()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(movies)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { result.value = Resource.error("Could not pull favorite movies.", null) }
            }
        }
    }

    fun favoriteAMovie(movie: Movies.Movie, result: MutableLiveData<Resource<Movies.Movie>>) {
        result.value = Resource.loading(movie)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                movie.isFavoriteLoading = false
                //if already favorited, un-favorite. If not already favorited, favorite it.
                if (!movie.isFavorite) {
                    movie.isFavorite = true
                    movieDao.addMovieToFavorites(movie)
                } else {
                    movie.isFavorite = false
                    movieDao.removeMovieFromFavorites(movie)
                }
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(movie)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { result.value = Resource.error("Could not favorite a Movie", movie) }
            }
        }
    }

    fun unFavoriteAMovieFromFavorite(movie: Movies.Movie, result: MutableLiveData<Resource<List<Movies.Movie>>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // un-favorite a favorite movie
                movieDao.removeMovieFromFavorites(movie)
                val movies = movieDao.getAllFavoriteMovies()
                withContext(Dispatchers.Main) {
                    movie.isFavorite = !movie.isFavorite
                    result.value = Resource.success(movies)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { result.value = Resource.error("Could not un-favorite a Movie", null) }
            }
        }
    }

    private suspend fun returnData(movieList: Movies.MovieList? = null, result: MutableLiveData<Resource<List<Movies.Movie>>>, status: Status) {
        val favMovieList = movieDao.getAllFavoriteMovies()
        if (movieList?.movies != null) {
            for (movie in movieList.movies) {
                for (favMov in favMovieList) {
                    if (movie.imdbID == favMov.imdbID) {
                        movie.isFavorite = true
                    }
                }
            }
        }

        when(status) {
            Status.SUCCESS -> {
                withContext(Dispatchers.Main) { result.value = Resource.success(movieList?.movies) }
            }
            Status.ERROR -> {
                withContext(Dispatchers.Main) { result.value = Resource.error("Unable to load data") }
            }
        }
    }

    fun getMovieDetails(movieId: String, result: MutableLiveData<Resource<Movies.MovieDetails>>) {
        result.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = remoteService.getRemoteService().getMovieDetails(movieId, BuildConfig.OMDB_API_KEY)
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        result.value = Resource.success(response.body()!!)
                    }
                } else {
                    // handle error
                    withContext(Dispatchers.Main) { result.value = Resource.error("Could not load Movie Details", null) }
                }
            } catch (exception: Exception) {
                // handle error
                withContext(Dispatchers.Main) { result.value = Resource.error("Could not load Movie Details", null) }
            }
        }
    }
}
/*
 * Created by Kartik Kumar Gujarati on 7/18/19 7:48 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kartik.openmoviedb.BuildConfig
import com.kartik.openmoviedb.data.local.MovieDao
import com.kartik.openmoviedb.data.remote.MovieRemoteServiceImpl
import com.kartik.openmoviedb.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/*
* The MovieRepository class acts as the data layer for the entire app. This class knows the source of data and how to fetch it.
* */
class MovieRepository(private val movieDao: MovieDao, private val remoteService: MovieRemoteServiceImpl) {

    // Get Movies from remote server given a search keyword.
    fun getMoviesFromSearch(searchKey: String, pageNumber: Int): LiveData<Resource<List<Movies.Movie>>> = liveData {
        emit(Resource.loading(null))
        try {
            val response = remoteService.getRemoteService().searchMovies(searchKey, pageNumber.toString(), BuildConfig.OMDB_API_KEY)
            if (response.isSuccessful && response.code() == 200) {
                emitSource(returnData(response.body()!!))
            } else {
                // handle error
                emit(Resource.error("Unable to load data"))
            }
        } catch (exception: Exception) {
            // handle error
            emit(Resource.error("Unable to load data"))
        }
    }

    // Pull  favorited movies from room db.
    fun getFavoriteMovies(): LiveData<Resource<List<Movies.Movie>>> = liveData  {
        emit(Resource.loading(null))
        withContext(Dispatchers.IO) {
            try {
                val movies = movieDao.getAllFavoriteMovies()
                emit(Resource.success(movies))
            } catch (exception: Exception) {
                emit(Resource.error("Could not pull favorite movies.", null))
            }
        }
    }

    // Favorite or un-favorite a movie and update the object appropriately. Used from Search List
    fun favoriteAMovie(movie: Movies.Movie): LiveData<Resource<Movies.Movie>> = liveData {
        emit(Resource.loading(null))
        withContext(Dispatchers.IO) {
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
                emit(Resource.success(movie))
            } catch (exception: Exception) {
                emit(Resource.error("Could not favorite a Movie", movie))
            }
        }
    }

    // Unfavorite a previously Favorited Movie. Used from the Favorites list.
    fun unFavoriteAMovieFromFavorite(movie: Movies.Movie): LiveData<Resource<Movies.Movie>> = liveData {
        emit(Resource.loading(null))
        // un-favorite a favorite movie
        withContext(Dispatchers.IO) {
            try {
                movieDao.removeMovieFromFavorites(movie)
                movie.isFavorite = !movie.isFavorite
                emit(Resource.success(movie))
            } catch (exception: Exception) {
                emit(Resource.error("Could not un-favorite a Movie", null))
            }
        }
    }

    // Get Movie Details from remote server from an imdbID.
    suspend fun getMovieDetails(movieId: String): Resource<Movies.MovieDetails> {
        return try {
            val response = remoteService.getRemoteService().getMovieDetails(movieId, BuildConfig.OMDB_API_KEY)
            if (response.isSuccessful && response.code() == 200) {
                Resource.success(response.body()!!)
            } else {
                // handle error
                Resource.error("Could not load Movie Details", null)
            }
        } catch (exception: Exception) {
            // handle error
            Resource.error("Could not load Movie Details", null)
        }
    }

    // Helper function
    private fun returnData(movieList: Movies.MovieList?): LiveData<Resource<List<Movies.Movie>>> = liveData {
        withContext(Dispatchers.IO) {
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
            emit(Resource.success(movieList?.movies))
        }
    }
}
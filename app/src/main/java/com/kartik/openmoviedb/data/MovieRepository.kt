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
                emit(returnData(response.body()!!))
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
    fun getFavoriteMovies(): Resource<List<Movies.Movie>> {
        return try {
            val movies = movieDao.getAllFavoriteMovies().value
            Resource.success(movies)
        } catch (exception: Exception) {
            Resource.error("Could not pull favorite movies.", null)
        }
    }

    // Favorite or un-favorite a movie and update the object appropriately. Used from Search List
    fun favoriteAMovie(movie: Movies.Movie): Resource<Movies.Movie> {
        return try {
            movie.isFavoriteLoading = false
            //if already favorited, un-favorite. If not already favorited, favorite it.
            if (!movie.isFavorite) {
                movie.isFavorite = true
                movieDao.addMovieToFavorites(movie)
            } else {
                movie.isFavorite = false
                movieDao.removeMovieFromFavorites(movie)
            }
            Resource.success(movie)
        } catch (exception: Exception) {
            Resource.error("Could not favorite a Movie", movie)
        }
    }

    // Unfavorite a previously Favorited Movie. Used from the Favorites list.
    fun unFavoriteAMovieFromFavorite(movie: Movies.Movie): Resource<List<Movies.Movie>> {
        return try {
            // un-favorite a favorite movie
            movieDao.removeMovieFromFavorites(movie)
            val movies = movieDao.getAllFavoriteMovies().value
            movie.isFavorite = !movie.isFavorite
            Resource.success(movies)
        } catch (exception: Exception) {
            Resource.error("Could not un-favorite a Movie", null)
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
    private fun returnData(movieList: Movies.MovieList?): Resource<List<Movies.Movie>> {
        val favMovieList = movieDao.getAllFavoriteMovies().value
        if (movieList?.movies != null) {
            for (movie in movieList.movies) {
                if (favMovieList != null) {
                    for (favMov in favMovieList) {
                        if (movie.imdbID == favMov.imdbID) {
                            movie.isFavorite = true
                        }
                    }
                }
            }
        }
        return Resource.success(movieList?.movies)
    }
}
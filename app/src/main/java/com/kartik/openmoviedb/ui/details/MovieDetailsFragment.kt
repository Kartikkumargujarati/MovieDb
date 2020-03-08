/*
 * Created by Kartik Kumar Gujarati on 7/19/19 12:58 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.openmoviedb.ui.details

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.kartik.openmoviedb.R
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.data.Status
import com.kartik.openmoviedb.data.local.MovieRoomDb
import com.kartik.openmoviedb.data.remote.MovieRemoteServiceImpl
import com.kartik.openmoviedb.model.Movies
import kotlinx.android.synthetic.main.fragment_movie_details.*

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is contained in a [MovieDetailsActivity]
 */
class MovieDetailsFragment : Fragment() {

    private var movie: Movies.Movie? = null
    private var rootView: View? = null
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_MOVIE)) {
                movie = it.getParcelable(ARG_MOVIE)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie_details, container, false)
        rootView = root
        val movieDao = MovieRoomDb.getDatabase(activity?.applicationContext!!).movieDao()
        val repository = MovieRepository(movieDao, MovieRemoteServiceImpl())
        movieDetailsViewModel = ViewModelProviders.of(this, MovieDetailsViewModelFactory(repository))[MovieDetailsViewModel::class.java]
        movieDetailsViewModel.movieDetails.observe(::getLifecycle, ::updateView)
        movieDetailsViewModel.getMovieDetails(movie?.imdbID.toString())
        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.finish()
            return true
        }
        return false
    }

    private fun updateView(resource: Resource<Movies.MovieDetails>?) {
        progress.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> resource.data?.let {
                movie_name_tv.text = it.title
                movie_release_date_tv.text = String.format(resources.getString(R.string.released_label_txt), it.released)
                movie_imdb_rating_tv.text = String.format(resources.getString(R.string.imdb_label_txt), it.imdbRating)
                movie_rated_tv.text = String.format(resources.getString(R.string.rated_label_txt), it.rated)
                movie_genre_tv.text = it.genre
                movie_plot_tv.text = it.plot
                movie_staring_tv.text = String.format(resources.getString(R.string.staring_label_txt), it.actors)
                movie_director_tv.text = String.format(resources.getString(R.string.director_label_txt), it.director)
                movie_writer_tv.text = String.format(resources.getString(R.string.writer_label_txt), it.writer)
            }!!
            Status.ERROR -> {
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * The fragment argument representing the movie ID that this fragment
         * represents.
         */
        const val ARG_MOVIE = "omdb_movie"
    }
}

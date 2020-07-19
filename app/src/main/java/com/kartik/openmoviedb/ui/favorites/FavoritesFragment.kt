package com.kartik.openmoviedb.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kartik.openmoviedb.MainApplication
import com.kartik.openmoviedb.R
import com.kartik.openmoviedb.data.MovieRepository
import com.kartik.openmoviedb.data.Resource
import com.kartik.openmoviedb.data.Status
import com.kartik.openmoviedb.data.local.MovieRoomDb
import com.kartik.openmoviedb.data.remote.MovieRemoteServiceImpl
import com.kartik.openmoviedb.model.Movies
import com.kartik.openmoviedb.ui.details.MovieDetailsActivity
import com.kartik.openmoviedb.ui.details.MovieDetailsFragment
import com.kartik.openmoviedb.ui.search.MovieListAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_search.progress
import kotlinx.android.synthetic.main.movie_list.view.*

/**
 * A fragment representing a a list of favorited movies/
 * This fragment is contained in a [MainActivity]
 */
class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        val movieDao = MovieRoomDb.getDatabase(activity?.applicationContext!!).movieDao()
        val repository = MovieRepository(movieDao, MovieRemoteServiceImpl())
        favoritesViewModel = ViewModelProvider(this, FavoritesViewModelFactory(repository))[FavoritesViewModel::class.java]
        favoritesViewModel.favMovieList.observe(::getLifecycle, ::updateList)
        favoritesViewModel.unFavMovie.observe(::getLifecycle, ::updateFavMovie)
        setupRecyclerView(root.movie_list)
        return root
    }

    /********************       Helper methods        *****************/

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager  = GridLayoutManager(activity, resources.getInteger(R.integer.columns_in_list))
        recyclerView.layoutManager = layoutManager

        adapter = MovieListAdapter(ArrayList(), object : MovieListAdapter.OnClickListener {
            override fun onMovieFav(movie: Movies.Movie) {
                // un-fav a movie
                favoritesViewModel.unFavoriteAMovie(movie)
                //update the movie in the local list (if present)
                val movies = (activity?.application as MainApplication?)?.getLastMovieResult()
                if (movies != null) {
                    for (currentMovie in movies)
                        if (currentMovie.imdbID == movie.imdbID) {
                            currentMovie.isFavorite = false
                            break
                        }
                }
            }

            override fun onMovieClick(movie: Movies.Movie) {
                // navigate to details
                startDetailsActivity(movie)
            }
        })
        recyclerView.adapter = adapter
    }

    private fun updateList(resource: Resource<List<Movies.Movie>>?) {
        progress.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> resource.data?.let {
                if (it.isNotEmpty()) {
                    empty_fav_tv.visibility = View.GONE
                } else {
                    empty_fav_tv.visibility = View.VISIBLE
                }
                adapter.setMovies(it)
            }!!
            Status.ERROR -> {
                adapter.setMovies(ArrayList())
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    private fun updateFavMovie(resource: Resource<Movies.Movie>?) {
        progress.visibility = View.GONE
        when(resource?.status) {
            Status.SUCCESS -> resource.data?.let {
                adapter.removeMovie(it)
            }!!
            Status.ERROR -> {
                adapter.setMovies(ArrayList())
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    private fun startDetailsActivity(movie: Movies.Movie) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsFragment.ARG_MOVIE, movie)
        startActivity(intent)
    }
}
package com.kartik.openmoviedb.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import com.kartik.openmoviedb.util.InfiniteScrollListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.movie_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A fragment representing a a list of movies from search results
 * This fragment is contained in a [MainActivity]
 */
class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var rootView: View
    private lateinit var adapter: MovieListAdapter
    private var isLoading: Boolean = false
    private var searchKey: String? = null
    private var pageNumber: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        rootView = root
        val movieDao = MovieRoomDb.getDatabase(activity?.applicationContext!!).movieDao()
        val repository = MovieRepository(movieDao, MovieRemoteServiceImpl())
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(repository))[SearchViewModel::class.java]
        searchViewModel.movieList.observe(::getLifecycle, ::updateList)
        searchViewModel.favMovie.observe(::getLifecycle, ::updateFavoriteMovie)
        setupSearchView(root.movie_sv)
        setupRecyclerView(root.movie_list)
        return root
    }

    override fun onResume() {
        super.onResume()
        val application = (activity?.application as MainApplication?)
        if(application?.getLastMovieResult()?.isEmpty() == false) {
            (activity?.application as MainApplication?)?.getLastMovieResult()?.let { adapter.setMovies(it) }
            empty_search_tv.visibility = View.GONE
        }
        if(!application?.getLastSearchedMovie().isNullOrBlank()) {
            rootView.movie_sv.onActionViewExpanded()
            rootView.movie_sv.clearFocus()
            rootView.movie_sv.setQuery(application?.getLastSearchedMovie(), false)
            searchKey = application?.getLastSearchedMovie()
        }
    }

    /********************       Helper methods        *****************/

    private fun setupSearchView(movie_sv: SearchView?) {
        movie_sv?.isIconified = true
        movie_sv?.isActivated = true
        movie_sv?.onActionViewExpanded()
        movie_sv?.clearFocus()
        movie_sv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.setMovies(ArrayList())
                (activity?.application as MainApplication?)?.setLastMovieResult(ArrayList())
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)  //debounce timeOut
                    searchKey = query.toString()
                    pageNumber = 1
                    query?.let {
                        searchViewModel.searchMovie(it)
                        (activity?.application as MainApplication?)?.setLastSearchedMovie(it)
                    }
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean { return false }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager  = GridLayoutManager(activity, resources.getInteger(R.integer.columns_in_list))
        recyclerView.layoutManager = layoutManager

        adapter = MovieListAdapter(ArrayList(), object : MovieListAdapter.OnClickListener {
            override fun onMovieFav(movie: Movies.Movie) {
                // fav a movie
                searchViewModel.favoriteAMovie(movie)
            }

            override fun onMovieClick(movie: Movies.Movie) {
                // navigate to details
                startDetailsActivity(movie)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {
            override fun doLoadMoreData(firstVisibleItemPosition: Int) {
                setDataLoading(isLoading)
                recyclerView.scrollToPosition(firstVisibleItemPosition)
                pageNumber++
                if (searchKey != null) {
                    searchViewModel.searchMovie(searchKey.toString(), pageNumber)
                    isLoading = true
                }
            }
        })
    }

    private fun updateList(resource: Resource<List<Movies.Movie>>?) {
        progress.visibility = View.GONE
        isLoading = false
        when(resource?.status) {
            Status.SUCCESS -> {
                if (resource.data == null) {
                    empty_search_tv.visibility = View.VISIBLE
                    empty_search_tv.text = resources.getText(R.string.no_search_movies_found)
                } else {
                    empty_search_tv.visibility = View.GONE
                    adapter.updateMovies(resource.data)
                    (activity?.application as MainApplication?)?.addLastMovieResult(resource.data)
                }
            }
            Status.ERROR -> {
                adapter.setMovies(ArrayList())
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }

    private fun updateFavoriteMovie(resource: Resource<Movies.Movie>?) {
        when(resource?.status) {
            Status.SUCCESS -> {
                resource.data?.isFavoriteLoading = false
            }
            Status.ERROR -> {
                resource.data?.isFavoriteLoading = false
            }
            Status.LOADING -> resource.data?.isFavoriteLoading = true
        }
        adapter.notifyDataSetChanged()
    }

    private fun startDetailsActivity(movie: Movies.Movie) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsFragment.ARG_MOVIE, movie)
        startActivity(intent)
    }

}
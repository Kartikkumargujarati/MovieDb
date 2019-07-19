package com.kartik.salesforcechallenege.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kartik.salesforcechallenege.MainApplication
import com.kartik.salesforcechallenege.R
import com.kartik.salesforcechallenege.data.MovieRepository
import com.kartik.salesforcechallenege.data.Resource
import com.kartik.salesforcechallenege.data.Status
import com.kartik.salesforcechallenege.data.local.MovieRoomDb
import com.kartik.salesforcechallenege.data.remote.MovieRemoteServiceImpl
import com.kartik.salesforcechallenege.model.Movies
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.movie_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var rootView: View
    private lateinit var adapter: MovieListAdapter
    private var isLoading: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        rootView = root
        val movieDao = MovieRoomDb.getDatabase(activity?.applicationContext!!).movieDao()
        val repository = MovieRepository(movieDao, MovieRemoteServiceImpl())
        searchViewModel = ViewModelProviders.of(this, SearchViewModelFactory(repository))[SearchViewModel::class.java]
        searchViewModel.movieList.observe(::getLifecycle, ::updateView)

        setupSearchView(root.movie_sv)
        setupRecyclerView(root.movie_list)
        return root
    }

    override fun onResume() {
        super.onResume()
        val application = (activity?.application as MainApplication?)
        if(application?.getLastMovieResult()?.isEmpty() == false) {
            (activity?.application as MainApplication?)?.getLastMovieResult()?.let { adapter.setMovies(it) }
        }
        if(!application?.getLastSearchedMovie().isNullOrBlank()) {
            rootView.movie_sv.onActionViewExpanded()
            rootView.movie_sv.clearFocus()
            rootView.movie_sv.setQuery(application?.getLastSearchedMovie(), false)
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
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)  //debounce timeOut
                    searchViewModel.searchMovie(query.toString())
                    (activity?.application as MainApplication?)?.setLastSearchedMovie(query.toString())
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean { return false }
        })
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager  = GridLayoutManager(activity, 1)
        recyclerView.layoutManager = layoutManager

        adapter = MovieListAdapter(ArrayList(), object : MovieListAdapter.OnClickListener {
            override fun onMovieFav(movie: Movies.Movie) {
                // fav a movie
            }

            override fun onMovieClick(movie: Movies.Movie) {
                // navigate to details
            }
        })
        recyclerView.adapter = adapter
    }

    private fun updateView(resource: Resource<List<Movies.Movie>>?) {
        progress.visibility = View.GONE
        isLoading = false
        when(resource?.status) {
            Status.SUCCESS -> resource.data?.let {
                adapter.setMovies(it)
                (activity?.application as MainApplication?)?.setLastMovieResult(it)
            }!!
            Status.ERROR -> {
                adapter.setMovies(ArrayList())
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> progress.visibility = View.VISIBLE
        }
    }


}
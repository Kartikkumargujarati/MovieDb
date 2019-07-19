/*
 * Created by Kartik Kumar Gujarati on 7/18/19 8:47 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kartik.salesforcechallenege.R
import com.kartik.salesforcechallenege.model.Movies
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private var movies: ArrayList<Movies.Movie>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    internal fun setMovies(movies: List<Movies.Movie>) {
        this.movies = movies as ArrayList<Movies.Movie>
        notifyDataSetChanged()
    }

    fun updateMovies(movies: List<Movies.Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bindData(movies[position])
    }

    internal inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(movie: Movies.Movie) {
            Glide.with(itemView.context).load(movie.poster).into(itemView.movie_poster_iv)
            itemView.movie_title_tv.text = movie.title
            itemView.movie_year_tv.text = movie.year
            itemView.movie_type_tv.text = movie.type?.capitalize()
            if (movie.isFavorite) {
                itemView.movie_fav_iv.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_favorite_fill, null))
            } else {
                itemView.movie_fav_iv.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_favorite_empty, null))
            }
            itemView.setOnClickListener { onClickListener.onMovieClick(movie) }
            itemView.movie_fav_iv.setOnClickListener { onClickListener.onMovieFav(movie) }
            if (movie.isFavoriteLoading) {
                itemView.movie_fav_pb.visibility = View.VISIBLE
            } else {
                itemView.movie_fav_pb.visibility = View.GONE
            }
        }
    }

    interface OnClickListener {
        fun onMovieClick(movie: Movies.Movie)
        fun onMovieFav(movie: Movies.Movie)
    }
}
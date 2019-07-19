/*
 * Created by Kartik Kumar Gujarati on 7/18/19 8:47 PM
 * Copyright (c) 2019 . All rights reserved.
 */

package com.kartik.salesforcechallenege.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartik.salesforcechallenege.R
import com.kartik.salesforcechallenege.model.Movies
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private var movies: List<Movies.Movie>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    internal fun setMovies(movies: List<Movies.Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bindData(movies[position])
    }

    internal inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(movie: Movies.Movie) {
            itemView.movie_title_tv.text = movie.title
            itemView.setOnClickListener { onClickListener.onItemClick(movie) }
        }
    }

    interface OnClickListener {
        fun onItemClick(movie: Movies.Movie)
    }
}
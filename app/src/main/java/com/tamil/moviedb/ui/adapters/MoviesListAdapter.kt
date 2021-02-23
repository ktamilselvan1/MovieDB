package com.tamil.moviedb.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tamil.moviedb.BuildConfig
import com.tamil.moviedb.data.remote.responses.ResultsItem
import com.tamil.moviedb.databinding.ListItemMovieBinding

class MoviesListAdapter : RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private var onItemClickListener: ((ResultsItem, View) -> Unit)? = null

    fun setOnItemClickListener(listener: (ResultsItem, View) -> Unit) {
        onItemClickListener = listener
    }

    /**
     * Diff Util to update the items only updated while assinging values
     */
    private val diffCallback = object : DiffUtil.ItemCallback<ResultsItem>() {
        override fun areItemsTheSame(
            oldItem: ResultsItem,
            newItem: ResultsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ResultsItem,
            newItem: ResultsItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var movies: List<ResultsItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class MoviesListViewHolder(val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val binding = ListItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoviesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        holder.binding.apply {
            title.text = movies[position].title
            desc.text = movies[position].overview
            date.text = movies[position].releaseDate
            Glide.with(holder.itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + movies[position].posterPath).into(imageView)
            movies[position].voteAverage?.let {
                rating.rating = (it / 2).toFloat()
            }
            parent.tag = movies[position]
            parent.setOnClickListener { parentView ->
                onItemClickListener?.let {
                    it(parentView.tag as ResultsItem, parentView)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
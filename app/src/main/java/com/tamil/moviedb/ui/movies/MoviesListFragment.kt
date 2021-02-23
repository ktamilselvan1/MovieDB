package com.tamil.moviedb.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamil.moviedb.R
import com.tamil.moviedb.databinding.FragmentMoviesListBinding
import com.tamil.moviedb.ext.showAToast
import com.tamil.moviedb.ui.adapters.MoviesListAdapter
import com.tamil.moviedb.util.Status

class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    private lateinit var binding: FragmentMoviesListBinding
    private var viewModel: MoviesViewModel? = null
    private val moviesListAdapter by lazy {
        MoviesListAdapter()
    }

    private var loading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesListBinding.bind(view)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!loading) {
                            loading = true
                            viewModel?.getMovieList()
                        }
                    }
                }
            })
        }
        viewModel =
            viewModel ?: ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
        registerForObservers()
    }

    private fun registerForObservers() {
        viewModel?.moviesListResponse?.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    loading = true
                    if (moviesListAdapter.movies.isEmpty())
                        binding.progress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    loading = false
                    binding.progress.visibility = View.GONE
                    resource.data?.let { data ->
                        moviesListAdapter.movies = data
                        moviesListAdapter.notifyDataSetChanged()
                    } ?: binding.root.showAToast("No data available")
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    loading = false
                    binding.root.showAToast(resource.message ?: "Something went wrong")
                }
            }
        })

        moviesListAdapter.setOnItemClickListener { resultsItem, view ->
            val action =
                MoviesListFragmentDirections.actionMoviesListFragmentToMoviesDetailFragment(
                    resultsItem
                )
            findNavController().navigate(action)
        }
    }

}
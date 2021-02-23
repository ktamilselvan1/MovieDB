package com.tamil.moviedb.ui.movies

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamil.moviedb.R
import com.tamil.moviedb.databinding.FragmentMoviesListBinding
import com.tamil.moviedb.ext.performDelayedSearch
import com.tamil.moviedb.ext.showAToast
import com.tamil.moviedb.ui.adapters.MoviesListAdapter
import com.tamil.moviedb.util.Status
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    private lateinit var binding: FragmentMoviesListBinding
    private var viewModel: MoviesViewModel? = null
    private val moviesListAdapter by lazy {
        MoviesListAdapter()
    }
    private var searchView: SearchView? = null
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
                            if (searchView?.isIconified == false)
                                viewModel?.searchMovies(searchView?.query.toString())
                            else
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_list_menu, menu)

        /**
         * Setup [SearchView]
         */
        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        /**
         * Search view debounce implementation
         */
        searchView?.let {
            it.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            lifecycleScope.launch {
                it.performDelayedSearch().collect { query ->
                    viewModel?.searchMovies(query)
                }
            }
            it.setOnCloseListener {
                viewModel?.getMovieList()
                false
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * listen the Livedata output from [MoviesViewModel]
     */
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

        /**
         * Listener callback from [MoviesListAdapter]
         */
        moviesListAdapter.setOnItemClickListener { resultsItem, view ->
            val action =
                MoviesListFragmentDirections.actionMoviesListFragmentToMoviesDetailFragment(
                    resultsItem
                )
            findNavController().navigate(action)
        }
    }

}
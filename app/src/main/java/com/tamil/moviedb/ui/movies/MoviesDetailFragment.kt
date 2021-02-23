package com.tamil.moviedb.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tamil.moviedb.BuildConfig
import com.tamil.moviedb.R
import com.tamil.moviedb.data.remote.responses.ResultsItem
import com.tamil.moviedb.databinding.FragmentsMovieDetailBinding
import com.tamil.moviedb.ext.*
import com.tamil.moviedb.util.Status

class MoviesDetailFragment : Fragment(R.layout.fragments_movie_detail) {

    private lateinit var binding: FragmentsMovieDetailBinding
    private var viewModel: MoviesViewModel? = null
    private val args: MoviesDetailFragmentArgs by navArgs()

    private var movieData: ResultsItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentsMovieDetailBinding.bind(view)
        viewModel =
            viewModel ?: ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
        movieData = args.movieItem
        registerForObservers()
    }

    private fun registerForObservers() {
        viewModel?.movieDetailResponse?.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        binding.apply {
                            duration.text = String.format(
                                getString(R.string.duration),
                                data.runtime?.getDuration()
                            ).toBold()
                            companies.text = String.format(
                                getString(R.string.companies),
                                data.productionCompanies?.getCommaSeparatedCompanies()
                            ).toBold()
                            genres.text = String.format(
                                getString(R.string.genres),
                                data.genres?.getCommaSeparatedGenres()
                            ).toBold()
                        }
                    } ?: binding.root.showAToast("No data available")
                }
                Status.ERROR -> {
                    binding.root.showAToast(resource.message ?: "Something went wrong")
                }
            }
        })
        movieData?.let { data ->
            data.id?.let { viewModel?.getMovieDetail(it) }
            binding.apply {
                requireActivity().title = data.title
                detailTitle.text = data.title
                detailDesc.text = data.overview
                date.text = String.format(
                    getString(R.string.released),
                    data.releaseDate
                ).toBold()
                Glide.with(requireActivity())
                    .load(BuildConfig.IMAGE_BASE_URL + data.posterPath).into(productImage)
                data.voteAverage?.let {
                    rating.rating = (it / 2).toFloat()
                }
            }
        }
    }

}
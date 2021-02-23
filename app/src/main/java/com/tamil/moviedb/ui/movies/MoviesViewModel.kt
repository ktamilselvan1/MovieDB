package com.tamil.moviedb.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamil.moviedb.data.remote.responses.MovieDetailResponse
import com.tamil.moviedb.data.remote.responses.MovieListResponse
import com.tamil.moviedb.data.remote.responses.ResultsItem
import com.tamil.moviedb.repo.MoviesRepository
import com.tamil.moviedb.util.NetworkResource
import com.tamil.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {
    private var pageNo = 1

    private var moviesListItems = arrayListOf<ResultsItem>()
    private var movieResponse: MovieListResponse? = null

    private val _moviesListResponse = MutableLiveData<Resource<List<ResultsItem>>>()
    val moviesListResponse: LiveData<Resource<List<ResultsItem>>> = _moviesListResponse

    private val _movieDetailResponse = MutableLiveData<Resource<MovieDetailResponse>>()
    val movieDetailResponse: LiveData<Resource<MovieDetailResponse>> = _movieDetailResponse

    init {
        getMovieList()
    }

    fun getMovieList() {
        if (pageNo != 1 && pageNo == movieResponse?.totalPages)
            return
        viewModelScope.launch {
            when (val response = moviesRepository.getLatestMovies(pageNo)) {
                is NetworkResource.NetworkError -> {
                    _moviesListResponse.postValue(
                        Resource.error(
                            "Check your internet connection",
                            null
                        )
                    )
                }
                is NetworkResource.ResponseError -> {
                    _moviesListResponse.postValue(
                        Resource.error(
                            response.message ?: "Something went wrong",
                            null
                        )
                    )

                }
                is NetworkResource.Success -> {
                    movieResponse = response.value.body()
                    response.value.body()?.results?.let {
                        pageNo++
                        moviesListItems.addAll(it)
                        _moviesListResponse.postValue(Resource.success(moviesListItems))
                    }
                }
            }
        }

    }

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            when (val response = moviesRepository.getMovieDetail(movieId)) {
                is NetworkResource.NetworkError -> {
                    _movieDetailResponse.postValue(
                        Resource.error(
                            "Check your internet connection",
                            null
                        )
                    )
                }
                is NetworkResource.ResponseError -> {
                    _movieDetailResponse.postValue(
                        Resource.error(
                            response.message ?: "Something went wrong",
                            null
                        )
                    )

                }
                is NetworkResource.Success -> {
                    response.value.body()?.let {
                        _movieDetailResponse.postValue(Resource.success(it))
                    }
                }
            }
        }
    }
}
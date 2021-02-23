package com.tamil.moviedb.repo

import com.tamil.moviedb.data.remote.MoviesApi
import com.tamil.moviedb.data.remote.responses.MovieDetailResponse
import com.tamil.moviedb.data.remote.responses.MovieListResponse
import com.tamil.moviedb.util.NetworkResource
import com.tamil.moviedb.util.apiWrapperCall
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

/**
 * Implementation for [MoviesRepository]
 */
class MoviesRepositoryImpl(private val moviesApi: MoviesApi) : MoviesRepository {

    override suspend fun getLatestMovies(pageNo: Int): NetworkResource<Response<MovieListResponse>> {
        return apiWrapperCall(dispatcher = Dispatchers.IO) {
            moviesApi.getLatestMoviesCall(pageNo)
        }
    }

    override suspend fun searchMovies(
        query: String,
        pageNo: Int
    ): NetworkResource<Response<MovieListResponse>> {
        return apiWrapperCall(dispatcher = Dispatchers.IO) {
            moviesApi.searchMovie(query, pageNo)
        }

    }

    override suspend fun getMovieDetail(movieId: Int): NetworkResource<Response<MovieDetailResponse>> {
        return apiWrapperCall(dispatcher = Dispatchers.IO) {
            moviesApi.getMovieDetailCall(movieId)
        }
    }
}
package com.tamil.moviedb.repo

import com.tamil.moviedb.data.remote.responses.MovieDetailResponse
import com.tamil.moviedb.data.remote.responses.MovieListResponse
import com.tamil.moviedb.util.NetworkResource
import retrofit2.Response

class FakeMoviesRepositoryImpl : MoviesRepository {
    var shouldReturnNetworkError = false
    override suspend fun getLatestMovies(pageNo: Int): NetworkResource<Response<MovieListResponse>> {
        return if (shouldReturnNetworkError) {
            NetworkResource.ResponseError(null, "Something went wrong")
        } else {
            NetworkResource.Success(Response.success(MovieListResponse(results = arrayListOf())))
        }
    }

    override suspend fun searchMovies(
        query: String,
        pageNo: Int
    ): NetworkResource<Response<MovieListResponse>> {
        return if (shouldReturnNetworkError) {
            NetworkResource.ResponseError(null, "Something went wrong")
        } else {
            NetworkResource.Success(Response.success(MovieListResponse(results = arrayListOf())))
        }
    }

    override suspend fun getMovieDetail(movieId: Int): NetworkResource<Response<MovieDetailResponse>> {
        return if (shouldReturnNetworkError) {
            NetworkResource.ResponseError(null, "Something went wrong")
        } else {
            NetworkResource.Success(Response.success(MovieDetailResponse()))
        }
    }
}
package com.tamil.moviedb.repo

import com.tamil.moviedb.data.remote.responses.MovieDetailResponse
import com.tamil.moviedb.data.remote.responses.MovieListResponse
import com.tamil.moviedb.util.NetworkResource
import retrofit2.Response

interface MoviesRepository {
    suspend fun getLatestMovies(pageNo: Int): NetworkResource<Response<MovieListResponse>>

    suspend fun getMovieDetail(movieId: Int): NetworkResource<Response<MovieDetailResponse>>
}
package com.tamil.moviedb.data.remote

import com.tamil.moviedb.data.remote.responses.MovieDetailResponse
import com.tamil.moviedb.data.remote.responses.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getLatestMoviesCall(@Query("page") page: Int): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailCall(@Path("movie_id") movieId: Int): Response<MovieDetailResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MovieListResponse>
}
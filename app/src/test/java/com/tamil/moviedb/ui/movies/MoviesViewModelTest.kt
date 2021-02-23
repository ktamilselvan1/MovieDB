package com.tamil.moviedb.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.tamil.moviedb.MainCoroutineRule
import com.tamil.moviedb.getOrAwaitValueTest
import com.tamil.moviedb.repo.FakeMoviesRepositoryImpl
import com.tamil.moviedb.repo.MoviesRepository
import com.tamil.moviedb.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MoviesViewModel
    private lateinit var repo: MoviesRepository


    @Before
    fun setup() {
        repo = FakeMoviesRepositoryImpl()
        viewModel = MoviesViewModel(repo)
    }

    @Test
    fun should_return_success_for_success_response_movies_list() = runBlockingTest {
        viewModel.getMovieList()
        val value = viewModel.moviesListResponse.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun should_return_error_for_error_response_movies_list() = runBlockingTest {
        (repo as FakeMoviesRepositoryImpl).shouldReturnNetworkError = true
        viewModel.getMovieList()
        val value = viewModel.moviesListResponse.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun should_return_success_for_success_response_movies_detail() = runBlockingTest {
        viewModel.getMovieDetail(464052)
        val value = viewModel.movieDetailResponse.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun should_return_error_for_error_response_movies_detail() = runBlockingTest {
        (repo as FakeMoviesRepositoryImpl).shouldReturnNetworkError = true
        viewModel.getMovieDetail(464052)
        val value = viewModel.movieDetailResponse.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

}
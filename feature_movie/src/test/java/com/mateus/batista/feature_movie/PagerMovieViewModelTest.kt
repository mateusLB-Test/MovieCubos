package com.mateus.batista.feature_movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mateus.batista.base_feature.util.FlowState
import com.mateus.batista.domain.interactor.MovieByGenreIdUseCase
import com.mateus.batista.domain.model.Movies
import com.mateus.batista.feature_movie.list.viewModel.PagerMovieViewModel
import com.mateus.batista.feature_movie.mapper.MovieMapper
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class PagerMovieViewModelTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PagerMovieViewModel

    private val useCase = mockk<MovieByGenreIdUseCase>()
    private val testModule = module { single { useCase } }

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        viewModel = PagerMovieViewModel()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `getMovieGenreId must postLoading when called`() {
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.getMovieGenreId(1)

        assertEquals(FlowState.Status.LOADING, viewModel.getListMovieGenreStatus().value?.status)
    }

    @Test
    fun `getMovieGenreId with success response must postSuccess with data mapped`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieGenreId(1)

        assertEquals(FlowState.Status.SUCCESS, viewModel.getListMovieGenreStatus().value?.status)
        assertEquals(viewModel.getListMovieGenreStatus().value?.data, MovieMapper.toFeature(dummyData.movies))

    }

    @Test
    fun `getMovieName with success response must increment incrementationPage`() {
        val incrementTest = viewModel.incrementationPage + 1
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieGenreId(1)

        assertEquals(viewModel.incrementationPage, incrementTest)
    }

    @Test
    fun `getMovieName with success response must set totalOfPage`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieGenreId(1)

        assertEquals(viewModel.totalOfPage, 10)
    }

    @Test
    fun `getMovieGenreId with failure response must postError with throwable`() {
        val dummyError = Throwable()
        stubError(dummyError)

        viewModel.getMovieGenreId(1)

        assertEquals(FlowState.Status.ERROR, viewModel.getListMovieGenreStatus().value?.status)
        assertEquals(viewModel.getListMovieGenreStatus().value?.error, dummyError)
    }

    @Test
    fun `loadMoreGenre must postLoading when called`() {
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.loadMoreGenre(1)

        assertEquals(FlowState.Status.LOADING, viewModel.getLoadMoreGenreState().value?.status)
    }

    @Test
    fun `loadMoreGenre when incrementationPage is smaller then totalOfPage must postSuccess with data mapped`() {
        viewModel.totalOfPage = 2
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.loadMoreGenre(1)
        verify {
            useCase.execute(params = any(), onSuccess = any(), onError = any())
        }
    }

    @Test
    fun `loadMoreGenre when incrementationPage is bigger then totalOfPage must not call API`() {
        viewModel.totalOfPage = 0

        viewModel.loadMoreGenre(1)
        verify {
            useCase wasNot Called
        }
    }

    @Test
    fun `loadMoreGenre with success response must postSuccess with data mapped`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.loadMoreGenre(1)

        assertEquals(FlowState.Status.SUCCESS, viewModel.getLoadMoreGenreState().value?.status)
        assertEquals(viewModel.getLoadMoreGenreState().value?.data, MovieMapper.toFeature(dummyData.movies))

    }

    @Test
    fun `loadMoreGenre with success response must increment incrementationPage`() {
        val incrementTest = viewModel.incrementationPage + 1
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.loadMoreGenre(1)

        assertEquals(viewModel.incrementationPage, incrementTest)
    }

    @Test
    fun `loadMoreGenre with failure response must postError with throwable`() {
        val dummyError = Throwable()
        stubError(dummyError)

        viewModel.loadMoreGenre(1)

        assertEquals(FlowState.Status.ERROR, viewModel.getLoadMoreGenreState().value?.status)
        assertEquals(viewModel.getLoadMoreGenreState().value?.error, dummyError)
    }

    private fun stubSuccess(movies: Movies) {
        every {
            useCase.execute(
                params = any(),
                onError = any(),
                onSuccess = captureLambda()
            )
        } answers {
            lambda<(Movies) -> Unit>().invoke(movies)
        }
    }

    private fun stubError(error: Throwable) {
        every {
            useCase.execute(
                params = any(),
                onError = captureLambda(),
                onSuccess = any()
            )
        } answers {
            lambda<(Throwable) -> Unit>().invoke(error)
        }
    }
}
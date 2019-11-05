package com.mateus.batista.feature_movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mateus.batista.base_feature.util.FlowState
import com.mateus.batista.domain.interactor.MovieByGenreIdUseCase
import com.mateus.batista.domain.interactor.MovieByNameUseCase
import com.mateus.batista.domain.model.Movies
import com.mateus.batista.feature_movie.list.viewModel.ListMovieViewModel
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

class ListMovieViewModelTest : KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListMovieViewModel

    private val useCase = mockk<MovieByNameUseCase>()
    private val testModule = module { single { useCase } }

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        viewModel = ListMovieViewModel()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `getMovieName must postLoading when called`() {
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.getMovieName("")

        assertEquals(FlowState.Status.LOADING, viewModel.getListMovieNameStatus().value?.status)
    }

    @Test
    fun `getMovieName with success response must postSuccess with data mapped`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieName("")

        assertEquals(FlowState.Status.SUCCESS, viewModel.getListMovieNameStatus().value?.status)
        assertEquals(viewModel.getListMovieNameStatus().value?.data, MovieMapper.toFeature(dummyData.movies))

    }

    @Test
    fun `getMovieName with success response must increment incrementationPage`() {
        val incrementTest = viewModel.incrementationPage + 1
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieName("")

        assertEquals(viewModel.incrementationPage, incrementTest)
    }

    @Test
    fun `getMovieName with success response must set totalOfPage`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.getMovieName("")

        assertEquals(viewModel.totalOfPage, 10)
    }

    @Test
    fun `getMovieName with failure response must postError with throwable`() {
        val dummyError = Throwable()
        stubError(dummyError)

        viewModel.getMovieName("")

        assertEquals(FlowState.Status.ERROR, viewModel.getListMovieNameStatus().value?.status)
        assertEquals(viewModel.getListMovieNameStatus().value?.error, dummyError)
    }

    @Test
    fun `loadMoreName must postLoading when called`() {
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.loadMoreName("")

        assertEquals(FlowState.Status.LOADING, viewModel.getLoadMoreNameState().value?.status)
    }

    @Test
    fun `loadMoreName when incrementationPage is smaller then totalOfPage must postSuccess with data mapped`() {
        viewModel.totalOfPage = 2
        every { useCase.execute(params = any(), onSuccess = any(), onError = any()) } just Runs

        viewModel.loadMoreName("")
        verify {
            useCase.execute(params = any(), onSuccess = any(), onError = any())
        }
    }

    @Test
    fun `loadMoreName when incrementationPage is bigger then totalOfPage must not call API`() {
        viewModel.totalOfPage = 0

        viewModel.loadMoreName("")
        verify {
            useCase wasNot Called
        }
    }

    @Test
    fun `loadMoreName with success response must postSuccess with data mapped`() {
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.loadMoreName("")

        assertEquals(FlowState.Status.SUCCESS, viewModel.getLoadMoreNameState().value?.status)
        assertEquals(viewModel.getLoadMoreNameState().value?.data, MovieMapper.toFeature(dummyData.movies))

    }

    @Test
    fun `loadMoreName with success response must increment incrementationPage`() {
        val incrementTest = viewModel.incrementationPage + 1
        val dummyData = Movies(10, listOf())
        stubSuccess(dummyData)

        viewModel.loadMoreName("")

        assertEquals(viewModel.incrementationPage, incrementTest)
    }

    @Test
    fun `loadMoreName with failure response must postError with throwable`() {
        val dummyError = Throwable()
        stubError(dummyError)

        viewModel.loadMoreName("")

        assertEquals(FlowState.Status.ERROR, viewModel.getLoadMoreNameState().value?.status)
        assertEquals(viewModel.getLoadMoreNameState().value?.error, dummyError)
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
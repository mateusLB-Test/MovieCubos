package com.mateus.batista.domain

import com.mateus.batista.domain.interactor.MovieByGenreIdUseCase
import com.mateus.batista.domain.model.Movies
import com.mateus.batista.domain.repository.MovieRepository
import com.mateus.batista.domain.util.Response
import com.mateus.batista.domain.util.testModule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class DetailMoviesUseCaseTest : KoinTest {

    private val repository = mock<MovieRepository>()
    private lateinit var useCase: MovieByGenreIdUseCase

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        useCase = MovieByGenreIdUseCase(repository, CoroutineScope(Dispatchers.Unconfined))
    }

    @After
    fun after(){
        stopKoin()
    }

    @Test
    fun `DetailMoviesUseCase when pass valid params must call repository`() = runBlocking<Unit> {
        val dummyData = mock<Movies>()
        stubRepositorySuccess(dummyData)

        useCase.run(MovieByGenreIdUseCase.Params(0, 28))
        verify(repository).getMovieByGenreId(0, 28)
    }

    @Test
    fun `DetailMoviesUseCase when missing params must call throws exception`() = runBlocking {
        val error = assertFailsWith<IllegalArgumentException> { useCase.run() }
        assertEquals("\"Params\" must not be null", error.message)
    }

    @Test
    fun `DetailMoviesUseCase return a Success response`() = runBlocking {
        val dummyData = mock<Movies>()
        stubRepositorySuccess(dummyData)

        val response = useCase.run(MovieByGenreIdUseCase.Params(0, 28))
        assertTrue(response is Response.Success && response.data == dummyData)
    }

    @Test
    fun `DetailMoviesUseCase return a Error response`() = runBlocking {
        val dummyError = RuntimeException()
        stubRepositoryError(dummyError)

        val response = useCase.run(MovieByGenreIdUseCase.Params(0, 28))
        assertTrue(response is Response.Error && response.exception == dummyError)
    }

    private suspend fun stubRepositorySuccess(movies: Movies) {
        whenever(repository.getMovieByGenreId(any(), any())).thenReturn(Response.Success(movies))
    }

    private suspend fun stubRepositoryError(error: Throwable) {
        whenever(repository.getMovieByGenreId(any(), any())).thenReturn(Response.Error(error))
    }
}
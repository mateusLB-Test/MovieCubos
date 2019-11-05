package com.mateus.batista.domain

import com.mateus.batista.domain.interactor.MovieByNameUseCase
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
class ListMoviesUseCaseTest : KoinTest {

    private val repository = mock<MovieRepository>()
    private lateinit var useCase: MovieByNameUseCase

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        useCase = MovieByNameUseCase(repository, CoroutineScope(Dispatchers.Unconfined))
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `ListMoviesUseCase when pass valid params must call repository`() = runBlocking<Unit> {
        val dummyData = mock<Movies>()
        stubRepositorySuccess(dummyData)

        useCase.run(MovieByNameUseCase.Params(0, ""))
        verify(repository).getMovieByName(0, "")
    }

    @Test
    fun `ListMoviesUseCase when missing params must call throws exception`() = runBlocking {
        val error = assertFailsWith<IllegalArgumentException> { useCase.run() }
        assertEquals("\"Params\" must not be null", error.message)
    }

    @Test
    fun `ListMoviesUseCase return a Success response`() = runBlocking {
        val dummyData = mock<Movies>()
        stubRepositorySuccess(dummyData)

        val response = useCase.run(MovieByNameUseCase.Params(0, ""))
        assertTrue(response is Response.Success && response.data == dummyData)
    }

    @Test
    fun `ListPostUseCase return a Error response`() = runBlocking {
        val dummyError = RuntimeException()
        stubRepositoryError(dummyError)

        val response = useCase.run(MovieByNameUseCase.Params(0, ""))
        assertTrue(response is Response.Error && response.exception == dummyError)
    }

    private suspend fun stubRepositorySuccess(movies: Movies) {
        whenever(repository.getMovieByName(any(), any())).thenReturn(Response.Success(movies))
    }

    private suspend fun stubRepositoryError(error: Throwable) {
        whenever(repository.getMovieByName(any(), any())).thenReturn(Response.Error(error))
    }
}
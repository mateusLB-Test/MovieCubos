package com.mateus.batista.data_remote

import com.mateus.batista.data.source.MovieRemoteDataSource
import com.mateus.batista.data_remote.core.MovieRemoteDataSourceImp
import com.mateus.batista.data_remote.mapper.MovieMapper
import com.mateus.batista.data_remote.model.MovieResponse
import com.mateus.batista.data_remote.service.MovieService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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

@RunWith(JUnit4::class)
class MovieRemoteDataSourceTest : KoinTest {

    private val service = mock<MovieService>()
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Before
    fun setUp() {
        startKoin { }
        movieRemoteDataSource = MovieRemoteDataSourceImp(service)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `getMovieByGenreId must return right mapped value`() = runBlocking {
        val dummyMovie = MovieResponse()
        stubMovieByGenreId(0, 28, dummyMovie)

        val response = movieRemoteDataSource.getMovieByGenreId(0, 28)
        assertEquals(response, MovieMapper.toData(dummyMovie))
    }

    @Test
    fun `getMovieByName must return right mapped value`() = runBlocking {
        val dummyMovie = MovieResponse()
        stubMovieByName(0, "", dummyMovie)

        val response = movieRemoteDataSource.getMovieByName(0, "")
        assertEquals(response, MovieMapper.toData(dummyMovie))
    }

    private suspend fun stubMovieByGenreId(page: Int, genreId: Int, movieResponse: MovieResponse) {
        whenever(service.getMovieByGenreId(page, genreId)).thenReturn(movieResponse)
    }

    private suspend fun stubMovieByName(page: Int, name: String, movieResponse: MovieResponse) {
        whenever(service.getMovieByName(page, name)).thenReturn(movieResponse)
    }
}
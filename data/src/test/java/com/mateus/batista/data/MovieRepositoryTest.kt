package com.mateus.batista.data

import com.mateus.batista.data.mapper.MovieMapper
import com.mateus.batista.data.model.MovieListModel
import com.mateus.batista.data.repository.MovieRepositoryImp
import com.mateus.batista.data.source.MovieRemoteDataSource
import com.mateus.batista.data.util.NetworkStatus
import com.mateus.batista.domain.repository.MovieRepository
import com.mateus.batista.domain.util.Response
import com.nhaarman.mockitokotlin2.given
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
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(JUnit4::class)
class MovieRepositoryTest : KoinTest {

    private val remote = mock<MovieRemoteDataSource>()

    private lateinit var repository: MovieRepository

    private val networkStatus = mock<NetworkStatus>()
    private val testModule = module { single { networkStatus } }

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        repository = MovieRepositoryImp(remote)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `getMovieByGenreId When return Exception MUST return a Error response`() =
        runBlocking {
            stubNetworkStatus(true)
            val dummyError = Throwable()
            stubMovieByGenreIdError(0, 28, dummyError)

            val response = repository.getMovieByGenreId(0, 28)
            assert(response is Response.Error)
        }

    @Test
    fun `getMovieByGenreId When API return success MUST return a Success response`() = runBlocking {
        stubNetworkStatus(true)
        val dummyData = mock<MovieListModel>()

        stubMovieByGenreId(0, 28, dummyData)
        val response = repository.getMovieByGenreId(0, 28)

        assert(response is Response.Success && response.data == MovieMapper.toDomain(dummyData))
    }

    @Test
    fun `getMovieByName When return Exception MUST return a Error response`() =
        runBlocking {
            stubNetworkStatus(true)
            val dummyError = Throwable()
            stubMovieByNameError(0, "", dummyError)

            val response = repository.getMovieByGenreId(0, 28)
            assert(response is Response.Error)
        }

    @Test
    fun `getMovieByName When API return success MUST return a Success response`() = runBlocking {
        stubNetworkStatus(true)
        val dummyData = mock<MovieListModel>()

        stubMovieByName(0, "", dummyData)
        val response = repository.getMovieByName(0, "")

        assert(response is Response.Success && response.data == MovieMapper.toDomain(dummyData))
    }

    private fun stubNetworkStatus(isOnline: Boolean) {
        whenever(networkStatus.isOnline()).thenReturn(isOnline)
    }

    private suspend fun stubMovieByGenreId(
        page: Int,
        genreId: Int,
        movieListModel: MovieListModel
    ) {
        whenever(remote.getMovieByGenreId(page, genreId)).thenReturn(movieListModel)
    }

    private suspend fun stubMovieByGenreIdError(page: Int, genreId: Int, error: Throwable) {
        given(remote.getMovieByGenreId(page, genreId)).willAnswer { error }
    }

    private suspend fun stubMovieByName(page: Int, name: String, movieListModel: MovieListModel) {
        whenever(remote.getMovieByName(page, name)).thenReturn(movieListModel)
    }

    private suspend fun stubMovieByNameError(page: Int, name: String, error: Throwable) {
        given(remote.getMovieByName(page, name)).willAnswer { error }
    }
}
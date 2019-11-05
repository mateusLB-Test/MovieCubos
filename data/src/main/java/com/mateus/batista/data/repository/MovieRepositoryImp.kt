package com.mateus.batista.data.repository

import com.mateus.batista.data.mapper.MovieMapper
import com.mateus.batista.data.source.MovieRemoteDataSource
import com.mateus.batista.data.util.NetworkStatus
import com.mateus.batista.data.util.safeApiCall
import com.mateus.batista.domain.model.Movies
import com.mateus.batista.domain.repository.MovieRepository
import com.mateus.batista.domain.util.Response
import org.koin.core.KoinComponent
import org.koin.core.inject


class MovieRepositoryImp(
    private val remote: MovieRemoteDataSource
) : KoinComponent, MovieRepository {
    private val networkStatus: NetworkStatus by inject()

    override suspend fun getMovieByGenreId(page: Int, genreId: Int): Response<Movies> {
        return safeApiCall({ networkStatus.isOnline() },
            { MovieMapper.toDomain(remote.getMovieByGenreId(page, genreId)) })
    }

    override suspend fun getMovieByName(page: Int, name: String): Response<Movies> {
        return safeApiCall({ networkStatus.isOnline() },
            { MovieMapper.toDomain(remote.getMovieByName(page, name)) })
    }
}
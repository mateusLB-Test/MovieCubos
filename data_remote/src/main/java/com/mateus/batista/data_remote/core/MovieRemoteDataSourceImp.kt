package com.mateus.batista.data_remote.core

import com.mateus.batista.data.model.MovieListModel
import com.mateus.batista.data.source.MovieRemoteDataSource
import com.mateus.batista.data_remote.mapper.MovieMapper
import com.mateus.batista.data_remote.service.MovieService
import org.koin.core.KoinComponent

class MovieRemoteDataSourceImp(
    private val service: MovieService
) : KoinComponent, MovieRemoteDataSource {

    override suspend fun getMovieByGenreId(page: Int, genreId: Int): MovieListModel {
        return MovieMapper.toData(service.getMovieByGenreId(page, genreId))
    }

    override suspend fun getMovieByName(page: Int, name: String): MovieListModel {
        return MovieMapper.toData(service.getMovieByName(page, name))
    }
}
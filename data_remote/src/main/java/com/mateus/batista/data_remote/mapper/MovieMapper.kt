package com.mateus.batista.data_remote.mapper

import com.mateus.batista.data.model.MovieListModel
import com.mateus.batista.data.model.MovieModel
import com.mateus.batista.data_remote.model.MovieApi
import com.mateus.batista.data_remote.model.MovieResponse

object MovieMapper : RemoteMapper<MovieResponse, MovieListModel> {

    override fun toData(model: MovieResponse): MovieListModel {
        return MovieListModel(
            totalPages = model.totalPages ?: 0,
            results = parseMovieList(model.results)
        )
    }

    private fun parseMovieList(list: List<MovieApi>?): List<MovieModel> {
        return list?.map { parseMovie(it) } ?: listOf()
    }

    private fun parseMovie(movieApi: MovieApi): MovieModel {
        return MovieModel(
            posterPath = movieApi.posterPath ?: "",
            id = movieApi.id ?: -1,
            title = movieApi.title ?: "",
            overview = movieApi.overview ?: ""
        )
    }
}
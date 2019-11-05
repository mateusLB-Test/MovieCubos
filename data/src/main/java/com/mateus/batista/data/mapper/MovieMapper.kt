package com.mateus.batista.data.mapper

import com.mateus.batista.data.model.MovieListModel
import com.mateus.batista.data.model.MovieModel
import com.mateus.batista.domain.model.Movie
import com.mateus.batista.domain.model.Movies

object MovieMapper {
    fun toDomain(listModel: MovieListModel): Movies {
        return Movies(
            totalPages = listModel.totalPages,
            movies = parseMovieList(listModel.results)
        )
    }

    private fun parseMovieList(list: List<MovieModel>): List<Movie> {
        return list.map { parseMovie(it) }
    }

    private fun parseMovie(movieModel: MovieModel): Movie {
        return Movie(
            posterPath = "http://image.tmdb.org/t/p/w342${movieModel.posterPath}",
            id = movieModel.id,
            title = movieModel.title,
            overview = movieModel.overview
        )
    }
}
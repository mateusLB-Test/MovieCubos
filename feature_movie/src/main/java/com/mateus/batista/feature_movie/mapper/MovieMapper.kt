package com.mateus.batista.feature_movie.mapper

import com.mateus.batista.domain.model.Movie
import com.mateus.batista.feature_movie.model.MovieFeature

object MovieMapper {

    fun toFeature(model: List<Movie>): List<MovieFeature> {
        return model.map { parseMovie(it) }
    }

    private fun parseMovie(movie: Movie): MovieFeature {
        return MovieFeature(
            posterPath = movie.posterPath,
            title = movie.title,
            overview = movie.overview
        )
    }
}
package com.mateus.batista.domain.interactor

import com.mateus.batista.domain.model.Movies
import com.mateus.batista.domain.repository.MovieRepository
import com.mateus.batista.domain.util.Response
import com.mateus.batista.domain.util.UseCase
import kotlinx.coroutines.CoroutineScope

class MovieByGenreIdUseCase(
    private val repository: MovieRepository,
    scope: CoroutineScope
) : UseCase<Movies, MovieByGenreIdUseCase.Params>(scope) {
    override suspend fun run(params: Params?): Response<Movies> {
        requireNotNull(params) { "\"Params\" must not be null" }

        return repository.getMovieByGenreId(params.page, params.genreId)
    }

    data class Params(val page: Int, val genreId: Int)
}
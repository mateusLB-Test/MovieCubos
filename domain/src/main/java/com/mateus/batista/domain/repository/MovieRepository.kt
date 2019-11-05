package com.mateus.batista.domain.repository

import com.mateus.batista.domain.util.Response
import com.mateus.batista.domain.model.Movies

interface MovieRepository {
    suspend fun getMovieByGenreId(page: Int, genreId: Int): Response<Movies>

    suspend fun getMovieByName(page: Int, name: String): Response<Movies>
}
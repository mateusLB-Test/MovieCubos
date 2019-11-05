package com.mateus.batista.data.source

import com.mateus.batista.data.model.MovieListModel

interface MovieRemoteDataSource {
    suspend fun getMovieByGenreId(page: Int, genreId: Int): MovieListModel
    suspend fun getMovieByName(page: Int, name: String): MovieListModel
}
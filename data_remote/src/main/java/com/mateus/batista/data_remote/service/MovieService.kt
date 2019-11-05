package com.mateus.batista.data_remote.service

import com.mateus.batista.data_remote.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("discover/movie")
    suspend fun getMovieByGenreId(
        @Query("page") page: Int,
        @Query("with_genres") genreId: Int,
        @Query("api_key") api_key: String = "bc01982083ce2003024d825b3829cd98"
    ): MovieResponse

    @GET("search/movie")
    suspend fun getMovieByName(
        @Query("page") page: Int,
        @Query("query") name: String,
        @Query("api_key") api_key: String = "bc01982083ce2003024d825b3829cd98"
    ): MovieResponse
}
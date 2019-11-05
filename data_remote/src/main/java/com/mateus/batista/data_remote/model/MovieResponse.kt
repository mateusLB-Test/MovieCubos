package com.mateus.batista.data_remote.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("total_results")
    var totalResults: Int? = null,
    @SerializedName("total_pages")
    var totalPages: Int? = null,
    @SerializedName("results")
    var results: List<MovieApi>? = null
)

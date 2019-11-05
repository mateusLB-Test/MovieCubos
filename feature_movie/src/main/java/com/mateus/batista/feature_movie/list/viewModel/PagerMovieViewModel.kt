package com.mateus.batista.feature_movie.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mateus.batista.base_feature.BaseViewModel
import com.mateus.batista.base_feature.util.*
import com.mateus.batista.domain.interactor.MovieByGenreIdUseCase
import com.mateus.batista.feature_movie.mapper.MovieMapper
import com.mateus.batista.feature_movie.model.MovieFeature

class PagerMovieViewModel : BaseViewModel() {

    private val listMovieGenreStatus by lazy { MutableLiveData<FlowState<List<MovieFeature>>>() }
    private val loadMoreGenreState by lazy { MutableLiveData<FlowState<List<MovieFeature>>>() }


    fun getListMovieGenreStatus(): LiveData<FlowState<List<MovieFeature>>> = listMovieGenreStatus
    fun getLoadMoreGenreState(): LiveData<FlowState<List<MovieFeature>>> = loadMoreGenreState

    var incrementationPage = 1
    var totalOfPage = 1

    private val genreUseCase: MovieByGenreIdUseCase by useCase()

    fun getMovieGenreId(genreId: Int) {
        listMovieGenreStatus.postLoading()
        genreUseCase.execute(
            params = MovieByGenreIdUseCase.Params(page = 1, genreId = genreId),
            onSuccess = {
                listMovieGenreStatus.postSuccess(MovieMapper.toFeature(it.movies))
                incrementationPage++
                totalOfPage = it.totalPages
            },
            onError = { listMovieGenreStatus.postError(it) }
        )
    }

    fun loadMoreGenre(genreId: Int) {
        if (incrementationPage <= totalOfPage) {
            loadMoreGenreState.postLoading()
            genreUseCase.execute(
                params = MovieByGenreIdUseCase.Params(page = incrementationPage, genreId = genreId),
                onSuccess = {
                    loadMoreGenreState.postSuccess(MovieMapper.toFeature(it.movies))
                    incrementationPage++
                },
                onError = { loadMoreGenreState.postError(it) }
            )
        }
    }
}
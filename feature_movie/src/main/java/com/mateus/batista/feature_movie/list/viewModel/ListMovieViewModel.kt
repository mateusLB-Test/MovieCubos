package com.mateus.batista.feature_movie.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mateus.batista.base_feature.BaseViewModel
import com.mateus.batista.base_feature.util.*
import com.mateus.batista.domain.interactor.MovieByNameUseCase
import com.mateus.batista.feature_movie.mapper.MovieMapper
import com.mateus.batista.feature_movie.model.MovieFeature

class ListMovieViewModel : BaseViewModel() {

    private val listMovieNameStatus by lazy { MutableLiveData<FlowState<List<MovieFeature>>>() }
    private val loadMoreNameState by lazy { MutableLiveData<FlowState<List<MovieFeature>>>() }

    fun getListMovieNameStatus(): LiveData<FlowState<List<MovieFeature>>> = listMovieNameStatus
    fun getLoadMoreNameState(): LiveData<FlowState<List<MovieFeature>>> = loadMoreNameState

    var incrementationPage = 1
    var totalOfPage = 1

    private val nameUseCase: MovieByNameUseCase by useCase()

    fun getMovieName(name: String) {
        listMovieNameStatus.postLoading()
        nameUseCase.execute(
            params = MovieByNameUseCase.Params(page = 1, name = name),
            onSuccess = {
                listMovieNameStatus.postSuccess(MovieMapper.toFeature(it.movies))
                incrementationPage++
                totalOfPage = it.totalPages
            },
            onError = { listMovieNameStatus.postError(it) }
        )
    }

    fun loadMoreName(name: String) {
        if (incrementationPage <= totalOfPage) {
            loadMoreNameState.postLoading()
            nameUseCase.execute(
                params = MovieByNameUseCase.Params(page = incrementationPage, name = name),
                onSuccess = {
                    loadMoreNameState.postSuccess(MovieMapper.toFeature(it.movies))
                    incrementationPage++
                },
                onError = { loadMoreNameState.postError(it) }
            )
        }
    }
}
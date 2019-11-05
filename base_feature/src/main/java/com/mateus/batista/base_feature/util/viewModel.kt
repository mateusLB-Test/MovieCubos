package com.mateus.batista.base_feature.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateus.batista.domain.util.UseCase
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

fun <D> MutableLiveData<FlowState<D>>.postLoading() {
    value = FlowState(FlowState.Status.LOADING)
}

fun <D> MutableLiveData<FlowState<D>>.postSuccess(data: D?) {
    value = FlowState(FlowState.Status.SUCCESS, data = data)
}

fun <D> MutableLiveData<FlowState<D>>.postError(error: Throwable?) {
    value = FlowState(FlowState.Status.ERROR, error = error)
}

inline fun <V, reified U> V.useCase() where U : UseCase<*, *>, V : ViewModel, V : KoinComponent = inject<U> {
    parametersOf(viewModelScope)
}
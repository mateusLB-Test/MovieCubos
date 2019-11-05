package com.mateus.batista.base_feature.util

class FlowState<D>(
    val status: Status,
    val data: D? = null,
    val error: Throwable? = null
) {
    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}
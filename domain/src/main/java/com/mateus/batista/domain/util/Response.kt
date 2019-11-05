package com.mateus.batista.domain.util

sealed class Response<out D> {
    class Success<D>(val data: D) : Response<D>()
    class Error(val exception: Throwable) : Response<Nothing>()
}

fun <D> Response<D>.handleResponse(
    success: (D) -> Unit,
    error: (Throwable) -> Unit
) {
    when (this) {
        is Response.Success -> {
            success(data)
        }
        is Response.Error -> {
            error(exception)
        }
    }
}
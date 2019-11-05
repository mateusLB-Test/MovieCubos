package com.mateus.batista.data.util

import com.mateus.batista.domain.util.Response

open class DataSourceException(message: String? = null, code: Int? = null) : Exception(message)

class NoInternetException(message: String? = "No Internet Connection", code: Int? = 503) :
    DataSourceException(message, code)

class ServerException(message: String?) : Exception(message)

suspend fun <T : Any> safeApiCall(
    isOnline: () -> Boolean,
    call: suspend () -> T
)
        : Response<T> {
    return when {
        isOnline() -> {
            try {
                val dataFromRemote = call()
                Response.Success(dataFromRemote)
            } catch (exception: Throwable) {
                Response.Error(ServerException(exception.message))
            }
        }
        else -> Response.Error(NoInternetException())
    }
}
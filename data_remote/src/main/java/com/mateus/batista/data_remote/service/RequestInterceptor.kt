package com.mateus.batista.data_remote.service

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        builder.header("Content-Type", "application/json")
        builder.method(original.method, original.body)

        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}
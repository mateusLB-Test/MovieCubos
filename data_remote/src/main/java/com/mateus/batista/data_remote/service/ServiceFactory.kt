package com.mateus.batista.data_remote.service

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object ServiceFactory {

    fun createService(url: String, okHttpClient: OkHttpClient) : MovieService {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(UnitConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MovieService::class.java)
    }


    fun createOkHttpClient(
        requestInterceptor: RequestInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        with(clientBuilder) {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(requestInterceptor)
            connectTimeout(120, TimeUnit.SECONDS)
            readTimeout(120, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
        }
        return clientBuilder.build()
    }

    fun createHttpLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        with(interceptor) {
            if (isDebug) {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            } else {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        return interceptor
    }

    object UnitConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type, annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            return if (type == Unit::class.java) UnitConverter else null
        }

        private object UnitConverter : Converter<ResponseBody, Unit> {
            override fun convert(value: ResponseBody) {
                value.close()
            }
        }
    }
}
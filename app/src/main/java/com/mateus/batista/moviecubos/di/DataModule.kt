package com.mateus.batista.moviecubos.di

import com.mateus.batista.data.repository.MovieRepositoryImp
import com.mateus.batista.data.source.MovieRemoteDataSource
import com.mateus.batista.data.util.NetworkStatus
import com.mateus.batista.data_remote.core.MovieRemoteDataSourceImp
import com.mateus.batista.data_remote.service.RequestInterceptor
import com.mateus.batista.data_remote.service.ServiceFactory
import com.mateus.batista.domain.repository.MovieRepository
import com.mateus.batista.moviecubos.di.Constants.BASE_RUL
import com.mateus.batista.moviecubos.util.NetworkStatusImp
import org.koin.android.BuildConfig
import org.koin.dsl.module

object Constants {
    const val BASE_RUL = "https://api.themoviedb.org/3/"
}

val dataModule = module {
    factory { MovieRepositoryImp(get()) as MovieRepository}
    factory { NetworkStatusImp(get()) as NetworkStatus }
}

val remoteModule = module {
    factory {MovieRemoteDataSourceImp(get()) as MovieRemoteDataSource }
}

val serviceModule = module {
    single { ServiceFactory.createService(BASE_RUL, get()) }
    single { ServiceFactory.createOkHttpClient(get(), get()) }
    single { ServiceFactory.createHttpLoggingInterceptor(BuildConfig.DEBUG) }
    single { RequestInterceptor() }
}
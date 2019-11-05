package com.mateus.batista.moviecubos.di

import com.mateus.batista.domain.interactor.MovieByGenreIdUseCase
import com.mateus.batista.domain.interactor.MovieByNameUseCase
import com.mateus.batista.domain.util.ThreadContextProvider
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val domainModule = module {
    factory { (scope: CoroutineScope) -> MovieByGenreIdUseCase(get(), scope) }
    factory { (scope: CoroutineScope) -> MovieByNameUseCase(get(), scope) }
    factory { ThreadContextProvider() }
}
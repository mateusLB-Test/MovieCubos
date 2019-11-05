package com.mateus.batista.domain.util
import org.koin.dsl.module

val testModule = module {
    single { TestContextProvider() as ThreadContextProvider }
}
package com.mateus.batista.moviecubos.di

import com.mateus.batista.feature_movie.list.viewModel.ListMovieViewModel
import com.mateus.batista.feature_movie.list.viewModel.PagerMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListMovieViewModel() }
    viewModel { PagerMovieViewModel() }
}
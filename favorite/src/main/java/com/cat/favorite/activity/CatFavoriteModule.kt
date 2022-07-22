package com.cat.favorite.activity

import com.cat.viewmodel.CatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CatViewModel(get()) }
}
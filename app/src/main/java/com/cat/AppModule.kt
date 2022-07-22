package com.cat

import com.cat.viewmodel.CatViewModel
import com.cat.core.usecase.CatInteractor
import com.cat.core.usecase.CatUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<CatUseCase> { CatInteractor(get()) }
}

val viewModelModule = module {
    viewModel { CatViewModel(get()) }
}
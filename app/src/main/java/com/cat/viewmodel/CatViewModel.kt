package com.cat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cat.core.domain.Cat
import com.cat.core.usecase.CatUseCase
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class CatViewModel(private val catUseCase: CatUseCase) : ViewModel() {
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            catUseCase.getAllCat(it).asLiveData()
        }
        .asLiveData()

    val catList = catUseCase.getAllCat().asLiveData()

    fun getCat(id: String) = catUseCase.getCat(id).asLiveData()

    val favoriteCat = catUseCase.getFavoriteCat().asLiveData()

    fun setFavoriteCat(cat: Cat, newStatus: Boolean) =
        catUseCase.setFavoriteCat(cat, newStatus)
}
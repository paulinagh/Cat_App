package com.cat.core.usecase

import com.cat.core.domain.Cat
import com.cat.core.repository.ICatRepository

class CatInteractor(private val catRepository: ICatRepository) : CatUseCase {
    override fun getAllCat() = catRepository.getAllCat()

    override fun getAllCat(id: String) = catRepository.getAllCat(id)

    override fun getCat(id: String) = catRepository.getCat(id)

    override fun getFavoriteCat() = catRepository.getFavoriteCat()

    override fun setFavoriteCat(cat: Cat, state: Boolean) = catRepository.setFavoriteCat(cat, state)
}
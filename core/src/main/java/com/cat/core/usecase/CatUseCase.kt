package com.cat.core.usecase

import com.cat.core.Resource
import com.cat.core.domain.Cat
import com.cat.core.domain.CatDetail
import kotlinx.coroutines.flow.Flow

interface CatUseCase {
    fun getAllCat(): Flow<Resource<List<Cat>>>
    fun getAllCat(id: String): Flow<List<Cat>>
    fun getCat(id: String): Flow<Resource<CatDetail>>
    fun getFavoriteCat(): Flow<List<Cat>>
    fun setFavoriteCat(cat: Cat, state: Boolean)
}
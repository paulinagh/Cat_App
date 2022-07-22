package com.cat.core.local

import com.cat.core.local.entity.CatEntity
import com.cat.core.local.room.CatDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val tourismDao: CatDao) {
    fun getAllCat(): Flow<List<CatEntity>> = tourismDao.getAllCat()

    fun getAllCat(id: String): Flow<List<CatEntity>> = tourismDao.getAllCat(id)

    fun getFavoriteCat(): Flow<List<CatEntity>> = tourismDao.getFavoriteCat()

    suspend fun insertCat(tourismList: List<CatEntity>) = tourismDao.insertCat(tourismList)

    fun setFavoriteCat(tourism: CatEntity, newState: Boolean) {
        tourism.isFavorite = newState
        tourismDao.updateFavoriteCat(tourism)
    }
}
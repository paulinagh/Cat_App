package com.cat.core.local.room

import androidx.room.*
import com.cat.core.local.entity.CatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Query("SELECT * FROM cat")
    fun getAllCat(): Flow<List<CatEntity>>

    @Query("SELECT * FROM cat WHERE id like '%' || :id || '%'")
    fun getAllCat(id: String): Flow<List<CatEntity>>

    @Query("SELECT * FROM cat where isFavorite = 1")
    fun getFavoriteCat(): Flow<List<CatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: List<CatEntity>)

    @Update
    fun updateFavoriteCat(cat: CatEntity)
}
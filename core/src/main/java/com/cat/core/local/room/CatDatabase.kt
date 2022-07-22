package com.cat.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cat.core.local.entity.CatEntity

@Database(entities = [CatEntity::class], version = 3, exportSchema = false)
abstract class CatDatabase: RoomDatabase() {
    abstract fun catDao(): CatDao
}
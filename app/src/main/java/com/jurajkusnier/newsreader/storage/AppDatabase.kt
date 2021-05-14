package com.jurajkusnier.newsreader.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jurajkusnier.newsreader.storage.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): ArticlesDao
}

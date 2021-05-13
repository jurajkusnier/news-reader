package com.jurajkusnier.newsreader.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM articles")
    fun getAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: Int): Flow<ArticleEntity>

    @Insert
    fun insert(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    fun clear()
}

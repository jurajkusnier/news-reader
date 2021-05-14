package com.jurajkusnier.newsreader.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jurajkusnier.newsreader.storage.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles")
    fun getAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: Int): Flow<ArticleEntity>

    @Insert
    fun insert(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    fun clear()
}

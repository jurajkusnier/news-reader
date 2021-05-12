package com.jurajkusnier.newsreader.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jurajkusnier.newsreader.api.ArticleDto

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String?,
    val description: String?
) {
    companion object {
        fun from(articleDto: ArticleDto) = ArticleEntity(
            title = articleDto.title,
            author = articleDto.author,
            description = articleDto.description
        )
    }
}

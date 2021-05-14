package com.jurajkusnier.newsreader.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jurajkusnier.newsreader.network.ArticleDto
import java.util.Date

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String?,
    val description: String?,
    val sourceName: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String?
) {
    companion object {
        fun from(articleDto: ArticleDto) = ArticleEntity(
            title = articleDto.title,
            author = articleDto.author,
            description = articleDto.description,
            sourceName = articleDto.source.name,
            url = articleDto.url,
            urlToImage = articleDto.urlToImage,
            publishedAt = articleDto.publishedAt,
            content = articleDto.content
        )
    }
}

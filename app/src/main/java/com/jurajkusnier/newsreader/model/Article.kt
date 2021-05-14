package com.jurajkusnier.newsreader.model

import android.content.Context
import com.jurajkusnier.newsreader.storage.entity.ArticleEntity
import com.jurajkusnier.newsreader.util.toUIFormat
import java.util.Date

data class Article(
    val id: Int,
    val sourceName: String,
    val title: String?,
    private val publishedAt: Date
) {

    fun getPublishedDate(context: Context) = publishedAt.toUIFormat(context)

    companion object {
        fun from(articleEntity: ArticleEntity): Article {
            return Article(
                id = articleEntity.id,
                title = articleEntity.title,
                sourceName = articleEntity.sourceName,
                publishedAt = articleEntity.publishedAt
            )
        }
    }
}

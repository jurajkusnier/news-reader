package com.jurajkusnier.newsreader.model

import android.content.Context
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.storage.entity.ArticleEntity
import com.jurajkusnier.newsreader.util.toMediumUIFormat
import java.util.Date

data class ArticleDetail(
    val id: Int = 0,
    val title: String,
    val author: String?,
    val description: String?,
    val sourceName: String,
    val url: String,
    val urlToImage: String?,
    private val publishedAt: Date,
    val content: String?
) {

    fun getPublishedDate(context: Context) = publishedAt.toMediumUIFormat(context)

    fun getShortTitle(context: Context) = context.getString(R.string.article_from, sourceName)

    companion object {
        fun from(articleEntity: ArticleEntity): ArticleDetail {
            return ArticleDetail(
                id = articleEntity.id,
                title = articleEntity.title,
                author = articleEntity.author,
                description = articleEntity.description,
                sourceName = articleEntity.sourceName,
                url = articleEntity.url,
                urlToImage = articleEntity.urlToImage,
                publishedAt = articleEntity.publishedAt,
                content = articleEntity.content
            )
        }
    }
}

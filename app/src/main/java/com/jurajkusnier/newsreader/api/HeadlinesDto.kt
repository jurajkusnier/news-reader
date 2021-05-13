package com.jurajkusnier.newsreader.api

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class HeadlinesDto(val status: String, val articles: List<ArticleDto>)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val source: SourceDto,
    val title: String,
    val author: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String?
)

@JsonClass(generateAdapter = true)
data class SourceDto(val name: String)

package com.jurajkusnier.newsreader.news

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeadlinesDto(val status: String, val articles: List<ArticleDto>)

@JsonClass(generateAdapter = true)
data class ArticleDto(val title: String, val author: String?, val description: String?)

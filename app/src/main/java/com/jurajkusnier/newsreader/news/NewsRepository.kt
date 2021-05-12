package com.jurajkusnier.newsreader.news

import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {
    suspend fun getTopHeadlines(country: String) = newsService.topHeadlines(country)
}

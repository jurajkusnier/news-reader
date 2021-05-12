package com.jurajkusnier.newsreader.news

import android.util.Log
import com.jurajkusnier.newsreader.api.ArticleDto
import com.jurajkusnier.newsreader.api.NewsService
import com.jurajkusnier.newsreader.db.ArticleEntity
import com.jurajkusnier.newsreader.db.NewsDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao
) {

    private val mutableNetworkStateFlow = MutableStateFlow(NetworkState.IDLE)

    val news = mutableNetworkStateFlow.combine(newsDao.getAll()) { networkState, cachedData ->
        Log.d("UPDATE","network = $networkState cached items = ${cachedData.size}")
        News(cachedData.map { Article.from(it) }, networkState)
    }

    suspend fun updateNews(country: String) {
        mutableNetworkStateFlow.emit(NetworkState.LOADING)
        try {
            val response = newsService.topHeadlines(country)
            if (response.isSuccessful) {
                saveArticlesToDb(response.body()?.articles ?: listOf())
                mutableNetworkStateFlow.emit(NetworkState.IDLE)
            } else {
                mutableNetworkStateFlow.emit(NetworkState.ERROR)
            }
        } catch (exception: Exception) {
            mutableNetworkStateFlow.emit(NetworkState.ERROR)
        }
    }

    private fun saveArticlesToDb(articles: List<ArticleDto>) {
        newsDao.clear()
        newsDao.insert(articles.map { ArticleEntity.from(it) })
    }

    enum class NetworkState {
        IDLE, LOADING, ERROR
    }

    data class News(val articles: List<Article>, val networkState: NetworkState)

    data class Article(val title: String, val author: String?, val description: String?) {
        companion object {
            fun from(articleEntity: ArticleEntity): Article {
                return Article(
                    title = articleEntity.title,
                    author = articleEntity.author,
                    description = articleEntity.description
                )
            }
        }
    }

}

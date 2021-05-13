package com.jurajkusnier.newsreader.news

import android.content.Context
import android.util.Log
import com.jurajkusnier.newsreader.api.ArticleDto
import com.jurajkusnier.newsreader.api.NewsService
import com.jurajkusnier.newsreader.db.ArticleEntity
import com.jurajkusnier.newsreader.db.NewsDao
import com.jurajkusnier.newsreader.util.toUIFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val newsDao: NewsDao
) {

    private val mutableNetworkStateFlow = MutableStateFlow(NetworkState.IDLE)

    val news = mutableNetworkStateFlow.combine(newsDao.getAll()) { networkState, cachedData ->
        Log.d("UPDATE", "network = $networkState cached items = ${cachedData.size}")
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
            Log.e("NetworkRepository", exception.toString())
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

    data class Article(
        val id: Int,
        val source: String,
        val title: String?,
        private val publishedAt: Date
    ) {

        fun getPublishedDate(context: Context) = publishedAt.toUIFormat(context)

        companion object {
            fun from(articleEntity: ArticleEntity): Article {
                return Article(
                    id = articleEntity.id,
                    title = articleEntity.title,
                    source = articleEntity.sourceName,
                    publishedAt = articleEntity.publishedAt
                )
            }
        }
    }
}

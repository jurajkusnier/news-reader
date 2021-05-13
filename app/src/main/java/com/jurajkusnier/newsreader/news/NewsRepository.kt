package com.jurajkusnier.newsreader.news

import android.content.Context
import android.util.Log
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.api.ArticleDto
import com.jurajkusnier.newsreader.api.NewsService
import com.jurajkusnier.newsreader.db.ArticleEntity
import com.jurajkusnier.newsreader.db.NewsDao
import com.jurajkusnier.newsreader.util.toMediumUIFormat
import com.jurajkusnier.newsreader.util.toUIFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.Date
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
}

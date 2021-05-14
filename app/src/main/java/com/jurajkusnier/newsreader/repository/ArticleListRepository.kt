package com.jurajkusnier.newsreader.repository

import com.jurajkusnier.newsreader.model.Article
import com.jurajkusnier.newsreader.model.ArticleList
import com.jurajkusnier.newsreader.model.LoadingState
import com.jurajkusnier.newsreader.network.ArticleDto
import com.jurajkusnier.newsreader.network.NewsService
import com.jurajkusnier.newsreader.storage.ArticlesDao
import com.jurajkusnier.newsreader.storage.entity.ArticleEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

class ArticleListRepository @Inject constructor(
    private val newsService: NewsService,
    private val articlesDao: ArticlesDao
) {

    private val mutableNetworkStateFlow = MutableStateFlow(LoadingState.DONE)

    val articleList =
        mutableNetworkStateFlow.combine(articlesDao.getAll()) { networkState, cachedData ->
            ArticleList(cachedData.map { Article.from(it) }, networkState)
        }

    suspend fun updateNews(country: String): UpdateResult {
        Timber.d("START updateNews($country)")
        mutableNetworkStateFlow.emit(LoadingState.LOADING)
        try {
            val response = newsService.topHeadlines(country)
            if (response.isSuccessful) {
                saveArticlesToDb(response.body()?.articles ?: listOf())
            } else {
                Timber.e("API response was not successful, code = ${response.code()}")
                return UpdateResult.Error
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            return if (exception is CancellationException) {
                UpdateResult.Success
            } else {
                mutableNetworkStateFlow.emit(LoadingState.DONE)
                UpdateResult.Error
            }
        } finally {
            Timber.d("END updateNews($country)")
        }
        mutableNetworkStateFlow.emit(LoadingState.DONE)
        return UpdateResult.Success
    }

    private fun saveArticlesToDb(articles: List<ArticleDto>) {
        articlesDao.clear()
        articlesDao.insert(articles.map { ArticleEntity.from(it) })
        Timber.d("${articles.size} articles saved to database")
    }

    enum class UpdateResult {
        Success, Error
    }
}

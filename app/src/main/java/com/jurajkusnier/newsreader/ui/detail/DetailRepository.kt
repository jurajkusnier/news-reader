package com.jurajkusnier.newsreader.ui.detail

import com.jurajkusnier.newsreader.db.NewsDao
import com.jurajkusnier.newsreader.news.NewsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DetailRepository @Inject constructor(private val newsDao: NewsDao) {

    fun getArticle(articleId: Int) =
        newsDao.getArticle(articleId).map { NewsRepository.ArticleDetail.from(it) }
}

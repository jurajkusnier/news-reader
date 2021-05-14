package com.jurajkusnier.newsreader.repository

import com.jurajkusnier.newsreader.model.ArticleDetail
import com.jurajkusnier.newsreader.storage.ArticlesDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleDetailRepository @Inject constructor(private val articlesDao: ArticlesDao) {

    fun getArticle(articleId: Int) =
        articlesDao.getArticle(articleId).map { ArticleDetail.from(it) }
}

package com.jurajkusnier.newsreader.ui.detail

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.jurajkusnier.newsreader.repository.ArticleDetailRepository
import com.jurajkusnier.newsreader.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted private val articleId: Int,
    articleDetailRepository: ArticleDetailRepository
) :
    ViewModel() {

    val article = articleDetailRepository.getArticle(articleId).asLiveData()

    val action = SingleLiveEvent<ArticleDetailActions>()

    fun openUrl() {
        article.value?.url?.let { url -> action.value = ArticleDetailActions.OpenUrl(url.toUri()) }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(articleId: Int): ArticleDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            articleId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(articleId) as T
            }
        }
    }
}

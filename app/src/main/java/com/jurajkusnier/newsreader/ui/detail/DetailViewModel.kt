package com.jurajkusnier.newsreader.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


class DetailViewModel @AssistedInject constructor(
    @Assisted private val articleId: Int,
    detailRepository: DetailRepository
) :
    ViewModel() {

    val article = detailRepository.getArticle(articleId).asLiveData()

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(articleId: Int): DetailViewModel
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

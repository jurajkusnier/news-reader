package com.jurajkusnier.newsreader.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jurajkusnier.newsreader.repository.ArticleListRepository
import com.jurajkusnier.newsreader.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(private val articleListRepository: ArticleListRepository) :
    ViewModel() {

    val articleList = articleListRepository.articleList.asLiveData()
    val error = SingleLiveEvent<Unit>()
    var updateJob: Job? = null

    init {
        update()
    }

    fun update() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch(Dispatchers.IO) {
            if (articleListRepository.updateNews("us") == ArticleListRepository.UpdateResult.Error) {
                error.postValue(Unit)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }
}

package com.jurajkusnier.newsreader.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jurajkusnier.newsreader.news.ArticleDto
import com.jurajkusnier.newsreader.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<NewsState>()
    val news: LiveData<NewsState>
        get() = _news

    init {
        load()
    }

    private fun load() {
        _news.value = NewsState.Loading
        viewModelScope.launch {
            try {
                newsRepository.getTopHeadlines("us").let { response ->
                    if (response.isSuccessful) {
                        Log.d("SUCCESS", response.body()?.toString() ?: "")
                        _news.postValue(NewsState.Done(response.body()!!.articles))
                    } else {
                        _news.postValue(NewsState.Error)
                        Log.d("FAIL", response.toString())
                    }
                }
            } catch (exception: Exception) {
                Log.e("Exception", exception.toString())
                _news.postValue(NewsState.Error)
            }
        }
    }

    sealed class NewsState {
        object Loading : NewsState()
        object Error : NewsState()
        data class Done(val list: List<ArticleDto>) : NewsState()
    }
}
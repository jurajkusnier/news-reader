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

    private val _news = MutableLiveData<List<ArticleDto>>()
    val news:LiveData<List<ArticleDto>>
        get() = _news

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                newsRepository.getTopHeadlines("us").let { response ->
                    if (response.isSuccessful) {
                        Log.d("SUCCESS", response.body()?.toString() ?: "")
                        _news.postValue(response.body()?.articles)
                    } else {
                        Log.d("FAIL", response.toString())
                    }
                }
            } catch (exception: Exception) {
                Log.e("Exception", exception.toString())
            }
        }
    }
}
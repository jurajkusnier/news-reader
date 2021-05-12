package com.jurajkusnier.newsreader.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jurajkusnier.newsreader.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    val news = newsRepository.news.asLiveData()

    init {
        update()
    }

    private fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.updateNews("us")
        }
    }
}

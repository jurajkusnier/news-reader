package com.jurajkusnier.newsreader.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jurajkusnier.newsreader.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    fun test() {
        viewModelScope.launch {
            try {
                newsRepository.getTopHeadlines("us").let { response ->
                    if (response.isSuccessful) {
                        Log.d("SUCCESS", response.body()?.toString() ?: "")
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
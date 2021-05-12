package com.jurajkusnier.newsreader.ui.main

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("showOnLoading")
fun ProgressBar.showOnLoading(newsState: MainViewModel.NewsState) {
    visibility = if (newsState is MainViewModel.NewsState.Loading)
        View.GONE
    else
        View.VISIBLE
}

@BindingAdapter("showOnError")
fun View.showOnError(newsState: MainViewModel.NewsState) {
    visibility = if (newsState is MainViewModel.NewsState.Error)
        View.VISIBLE
    else
        View.GONE
}

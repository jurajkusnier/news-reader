package com.jurajkusnier.newsreader.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.databinding.MainFragmentBinding
import com.jurajkusnier.newsreader.news.NewsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(MainFragmentBinding.bind(view)) {
            recyclerView.adapter = newsAdapter
            viewModel.news.observe(viewLifecycleOwner) {
                newsAdapter.submitList(it.articles)
                when (it.networkState) {
                    NewsRepository.NetworkState.IDLE -> renderIdleNetworkState()
                    NewsRepository.NetworkState.LOADING -> renderLoadingNetworkState()
                    NewsRepository.NetworkState.ERROR -> renderErrorNetworkState()
                }
            }
        }
    }

    private fun MainFragmentBinding.renderIdleNetworkState() {
        loadingProgressBar.isGone = true
        errorText.isGone = true
    }

    private fun MainFragmentBinding.renderErrorNetworkState() {
        loadingProgressBar.isGone = true
        errorText.isVisible = true
    }

    private fun MainFragmentBinding.renderLoadingNetworkState() {
        loadingProgressBar.isVisible = true
        errorText.isGone = true
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

package com.jurajkusnier.newsreader.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        setHasOptionsMenu(true)

        with(MainFragmentBinding.bind(view)) {
            (activity as AppCompatActivity).setSupportActionBar(topAppBar)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.update()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

package com.jurajkusnier.newsreader.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)

        val newsAdapter = NewsAdapter()
        with(binding) {
            recyclerView.adapter = newsAdapter

            viewModel.news.observe(viewLifecycleOwner) {
                when (it) {
                    is MainViewModel.NewsState.Done -> {
                        newsAdapter.submitList(it.list)
                        loadingProgressBar.visibility = View.GONE
                        errorText.visibility = View.GONE
                    }
                    MainViewModel.NewsState.Error -> {
                        loadingProgressBar.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                    }
                    MainViewModel.NewsState.Loading -> {
                        loadingProgressBar.visibility = View.VISIBLE
                        errorText.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
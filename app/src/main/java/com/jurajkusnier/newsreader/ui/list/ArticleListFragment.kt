package com.jurajkusnier.newsreader.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.databinding.ArticleListFragmentBinding
import com.jurajkusnier.newsreader.model.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleListFragment : Fragment(R.layout.article_list_fragment) {

    private val viewModel: ArticleListViewModel by viewModels()

    @Inject
    lateinit var articleListAdapter: ArticleListAdapter

    private var errorSnackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(ArticleListFragmentBinding.bind(view)) {
            topAppBar.apply {
                setupWithNavController(findNavController())
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.refresh -> {
                            viewModel.update()
                            true
                        }
                        else -> false
                    }
                }
            }

            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            recyclerView.adapter = articleListAdapter.apply {
                clickListener = ::openDetail
            }

            viewModel.articleList.observe(viewLifecycleOwner) {
                articleListAdapter.submitList(it.articles)
                when (it.loadingState) {
                    LoadingState.DONE -> renderIdleNetworkState()
                    LoadingState.LOADING -> renderLoadingNetworkState()
                }
            }

            viewModel.error.observe(viewLifecycleOwner) {
                showErrorMessage()
            }
        }
    }

    private fun openDetail(articleId: Int) {
        findNavController().navigate(
            ArticleListFragmentDirections.actionListFragmentToDetailFragment(
                articleId
            )
        )
    }

    private fun ArticleListFragmentBinding.renderIdleNetworkState() {
        loadingProgressBar.isGone = true
    }

    private fun showErrorMessage() {
        errorSnackbar = Snackbar.make(requireView(), R.string.loading_error, LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                viewModel.update()
            }.addCallback(
                object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        errorSnackbar = null
                    }
                }
            ).apply { show() }
    }

    private fun ArticleListFragmentBinding.renderLoadingNetworkState() {
        loadingProgressBar.isVisible = true
        errorSnackbar?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ERROR_SNACKBAR_VISIBLE, errorSnackbar != null)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState?.getBoolean(ERROR_SNACKBAR_VISIBLE) == true) {
            showErrorMessage()
        }
    }

    companion object {
        private const val ERROR_SNACKBAR_VISIBLE = "snackbar"
    }
}

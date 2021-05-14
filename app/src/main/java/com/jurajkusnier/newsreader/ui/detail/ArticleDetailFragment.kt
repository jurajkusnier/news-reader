package com.jurajkusnier.newsreader.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.databinding.ArticleDetailFragmentBinding
import com.jurajkusnier.newsreader.model.ArticleDetail
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(R.layout.article_detail_fragment) {

    private val navArgs by navArgs<ArticleDetailFragmentArgs>()

    @Inject
    lateinit var viewModelFactoryArticle: ArticleDetailViewModel.AssistedFactory

    private val binding by viewBinding(ArticleDetailFragmentBinding::bind)

    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(viewModelFactoryArticle, navArgs.articleId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setupWithNavController(findNavController())

        viewModel.article.observe(viewLifecycleOwner) { article ->
            render(article)
        }

        viewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is ArticleDetailActions.OpenUrl -> {
                    startActivity(Intent(Intent.ACTION_VIEW, action.url))
                }
            }
        }
    }

    private fun render(article: ArticleDetail) {
        binding.topAppBar.title = article.getShortTitle(binding.root.context)
        with(binding.body) {
            articleTitle.text = article.title
            articleAuthor.text = article.author
            articlePublishedDate.text = article.getPublishedDate(root.context)
            articleDescription.text = article.description
            articleImage.load(article.urlToImage) {
                placeholder(R.drawable.image_placeholder)
                error(R.drawable.image_placeholder)
            }
            buttonReadMore.setOnClickListener {
                viewModel.openUrl()
            }
        }
    }
}

package com.jurajkusnier.newsreader.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.jurajkusnier.newsreader.R
import com.jurajkusnier.newsreader.databinding.DetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val navArgs by navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: DetailViewModel.AssistedFactory

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(viewModelFactory, navArgs.articleId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DetailFragmentBinding.bind(view).apply {
            topAppBar.setupWithNavController(findNavController())
        }

        viewModel.article.observe(viewLifecycleOwner) { article ->
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
                    val browserIntent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                    startActivity(browserIntent)
                }
            }
        }
    }
}
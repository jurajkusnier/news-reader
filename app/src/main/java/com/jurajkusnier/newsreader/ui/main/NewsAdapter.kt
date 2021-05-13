package com.jurajkusnier.newsreader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jurajkusnier.newsreader.databinding.ArticleListItemBinding
import com.jurajkusnier.newsreader.news.NewsRepository
import javax.inject.Inject

typealias NewsClickListener = () -> Unit

class NewsAdapter @Inject constructor() :
    ListAdapter<NewsRepository.Article, NewsAdapter.ViewHolder>(ITEM_COMPARATOR) {

    var clickListener: NewsClickListener? = null

    class ViewHolder(private val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsRepository.Article, clickListener: NewsClickListener?) {
            with(binding) {
                articlePublished.text = article.getPublishedDate(binding.root.context)
                articleSource.text = article.source
                articleTitle.text = article.title
                articleLayout.setOnClickListener {
                    clickListener?.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ArticleListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<NewsRepository.Article>() {
            override fun areItemsTheSame(
                oldItem: NewsRepository.Article,
                newItem: NewsRepository.Article
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NewsRepository.Article,
                newItem: NewsRepository.Article
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

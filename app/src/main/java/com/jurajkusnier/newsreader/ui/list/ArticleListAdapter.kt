package com.jurajkusnier.newsreader.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jurajkusnier.newsreader.databinding.ArticleListItemBinding
import com.jurajkusnier.newsreader.model.Article
import javax.inject.Inject

typealias ArticleClickListener = (articleId: Int) -> Unit

class ArticleListAdapter @Inject constructor() :
    ListAdapter<Article, ArticleListAdapter.ViewHolder>(ITEM_COMPARATOR) {

    var clickListener: ArticleClickListener? = null

    class ViewHolder(private val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, clickListener: ArticleClickListener?) {
            with(binding) {
                articlePublished.text = article.getPublishedDate(root.context)
                articleSource.text = article.sourceName
                articleTitle.text = article.title
                articleLayout.setOnClickListener {
                    clickListener?.invoke(article.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ArticleListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

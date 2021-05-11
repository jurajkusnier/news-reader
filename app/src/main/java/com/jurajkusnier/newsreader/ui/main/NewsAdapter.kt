package com.jurajkusnier.newsreader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jurajkusnier.newsreader.databinding.ArticleListItemBinding
import com.jurajkusnier.newsreader.news.ArticleDto

class NewsAdapter() : ListAdapter<ArticleDto, NewsAdapter.ViewHolder>(ITEM_COMPARATOR) {

    class ViewHolder(private val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleDto) {
            with(binding) {
                articleAuthor.text = article.author
                articleTitle.text = article.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ArticleListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ArticleDto>() {
            override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
                return oldItem == newItem
            }
        }
    }
}
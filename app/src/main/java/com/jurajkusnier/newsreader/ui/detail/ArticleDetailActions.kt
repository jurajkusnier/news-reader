package com.jurajkusnier.newsreader.ui.detail

import android.net.Uri

sealed class ArticleDetailActions {
    data class OpenUrl(val url: Uri) : ArticleDetailActions()
}

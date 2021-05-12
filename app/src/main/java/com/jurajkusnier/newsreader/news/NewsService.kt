package com.jurajkusnier.newsreader.news

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/v2/top-headlines")
    suspend fun topHeadlines(@Query("country") country: String): Response<HeadlinesDto>
}

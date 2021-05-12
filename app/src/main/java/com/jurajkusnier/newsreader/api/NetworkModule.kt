package com.jurajkusnier.newsreader.api

import com.jurajkusnier.newsreader.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideHttpClient() = OkHttpClient.Builder().addInterceptor { chain ->
        chain.proceed(
            chain.request().newBuilder().addHeader("X-Api-Key", BuildConfig.API_KEY).build()
        )
    }.build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideNewsService(retrofit: Retrofit) = retrofit.create(NewsService::class.java)
}

package com.jurajkusnier.newsreader.api

import com.jurajkusnier.newsreader.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.Date

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()

    @Provides
    fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().addHeader("X-Api-Key", BuildConfig.API_KEY).build()
            )
        }
        .addInterceptor(
            HttpLoggingInterceptor { message -> Timber.tag("OkHttp").v(message) }
                .apply { if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BASIC) }
        )
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideNewsService(retrofit: Retrofit): NewsService =
        retrofit.create(NewsService::class.java)
}

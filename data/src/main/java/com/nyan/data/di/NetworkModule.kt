package com.nyan.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nyan.data.service.NetworkService
import com.nyan.data.service.NetworkService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {

    protected open fun baseUrl() = BASE_URL.toHttpUrl()

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient, gson: Gson) = Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun providesNetworkService(retrofit: Retrofit) = retrofit.create(NetworkService::class.java)

//    val networkModule = module {
//        single { provideHttpClient() }
//        single { provideGson() }
//        single { providesRetrofit(get(), get()) }
//        single { providesNetworkService(get()) }
//    }

}
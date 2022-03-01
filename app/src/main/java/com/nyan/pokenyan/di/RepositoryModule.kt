package com.nyan.pokenyan.di

import com.nyan.data.repository.RemoteRepositoryImpl
import com.nyan.data.service.NetworkService
import com.nyan.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(networkService: NetworkService): RemoteRepository {
        return RemoteRepositoryImpl(networkService)
    }

//    val repositoryModule = module {
//        single<RemoteRepository> {
//            RemoteRepositoryImpl(get())
//        }
//    }

}
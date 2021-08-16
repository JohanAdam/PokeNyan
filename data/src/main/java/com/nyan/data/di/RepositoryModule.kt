package com.nyan.data.di

import com.nyan.data.repository.RemoteRepositoryImpl
import com.nyan.domain.repository.RemoteRepository
import org.koin.dsl.module

object RepositoryModule {

    val repositoryModule = module {
        single<RemoteRepository> {
            RemoteRepositoryImpl(get())
        }
    }

}
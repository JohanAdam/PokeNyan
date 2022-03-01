package com.nyan.pokenyan.di

import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.usecases.PokemonDetailUseCase
import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.domain.usecases.PokemonSearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providePokemonListUseCase(remoteRepository: RemoteRepository): PokemonListUseCase {
        return PokemonListUseCase(remoteRepository)
    }

    @Provides
    @Singleton
    fun providePokemonDetailUseCase(remoteRepository: RemoteRepository): PokemonDetailUseCase {
        return PokemonDetailUseCase(remoteRepository)
    }

    @Provides
    @Singleton
    fun providePokemonSearchUseCase(remoteRepository: RemoteRepository): PokemonSearchUseCase {
        return PokemonSearchUseCase(remoteRepository)
    }

//    val domainModule = module {
//        factory { PokemonListUseCase(remoteRepository = get()) }
//        factory { PokemonDetailUseCase(remoteRepository = get()) }
//        factory { PokemonSearchUseCase(remoteRepository = get())}
//    }

}